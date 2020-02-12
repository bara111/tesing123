package com.example.weatherapp.ui.main

import android.os.Handler
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.Event
import com.example.weatherapp.data.WeatherRepository
import com.example.weatherapp.data.models.WeatherDailyData
import javax.inject.Inject

class MainViewModel @Inject constructor(weatherRepository: WeatherRepository) : ViewModel() {

    var _weatherDailyDataList: MutableLiveData<List<WeatherDailyData>>? = weatherRepository.getResponse()
    var maxTemp: String?=null
    var _errorEvent: MutableLiveData<Event<String?>> = MutableLiveData()
    init {
        Handler().postDelayed({
            maxTemp=weatherRepository.liveEventError()
            _errorEvent.value = Event(maxTemp)
        }, 500)
    }
}