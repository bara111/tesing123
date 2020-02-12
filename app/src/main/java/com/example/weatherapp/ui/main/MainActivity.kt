@file:Suppress("DEPRECATION")

package com.example.weatherapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.weatherapp.BaseApp
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.ui.details.DetailsActivity
import com.example.weatherapp.ui.weather.WeatherActivity
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainAdapter: MainAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        (application as BaseApp).appComponent.mainComponent().create().inject(this)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityMainBinding>(
            this,
            R.layout.activity_main
        ).apply { lifecycleOwner = this@MainActivity }
        setSupportActionBar(binding.toolbarAll)
        mainAdapter = MainAdapter {
            startActivity(DetailsActivity.newIntent(this@MainActivity, it))
        }
        binding.recycleviewAll.adapter = mainAdapter
        binding.recycleviewAll.hasFixedSize()
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(MainViewModel::class.java)
        viewModel._weatherDailyDataList?.observe(this,
            Observer { list ->
                with(binding) {
                    progressbarMain.visibility = View.GONE
                    textviewMainMaxtemp?.text = list[0].main?.converterTempMax()
                    textviewMainMintemp?.text = list[0].main?.converterTempMin()
                    textviewListitemCondition.text = list[0].weather?.get(0)?.description
                }
                mainAdapter.submitList(list)
            })
        viewModel._errorEvent.observe(this, Observer {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(this, "max temp have changed", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.records -> {
                startActivity(Intent(this, WeatherActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        val TAG: String = MainActivity::javaClass.name
    }
}
