package com.example.weatherapp.data.remote

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.weatherapp.data.models.WeatherDailyData
import com.example.weatherapp.data.models.WeatherResponse
import com.example.weatherapp.ui.main.MainActivity
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

class WeatherRemoteDataSource @Inject constructor(private var retrofit: Retrofit) :
    WeatherDataSource.Remote {
    var weatherDailyDataList = MutableLiveData<List<WeatherDailyData>>()
    var errorEvent:String?=null

    fun requestData() {
        val service = retrofit.create(WeatherService::class.java)
        val call = service.getCurrentWeatherData(
            "32.22",
            "35.22", "829740af696dc0258609d2d0a6a8472a"
        )
        call.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ) {
                if (response.code() == 200 && response.body()?.list != null) {
                    val data: List<WeatherDailyData> = response.body()?.list!!
                    weatherDailyDataList.postValue(data)
                    errorEvent = data[0].main?.converterTempMax()
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Log.d(MainActivity.TAG, t.toString())
            }
        })
    }

    override fun getResponse(): MutableLiveData<List<WeatherDailyData>> {
        return weatherDailyDataList
    }

    override fun liveEventError(): String?{
        return errorEvent
    }
}

