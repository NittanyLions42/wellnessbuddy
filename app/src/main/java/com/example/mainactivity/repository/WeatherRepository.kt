package com.example.mainactivity.repository

import com.example.mainactivity.api.WeatherService
import com.example.mainactivity.model.network.FiveDayForecast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherRepository(private val weatherService: WeatherService) {

    interface WeatherCallback {
        fun onSuccess(weatherData: FiveDayForecast?)
        fun onError(error: String)
    }

    fun getWeatherForecast(
        zipCode: String,
        countryCode: String,
        apiKey: String,
        units: String = "imperial",
        callback: WeatherCallback
    ) {
        val call = weatherService.getWeatherForecast(zipCode, countryCode, apiKey, units)
        call.enqueue(object : Callback<FiveDayForecast> {
            override fun onResponse(
                call: Call<FiveDayForecast>,
                response: Response<FiveDayForecast>
            ) {
                if (response.isSuccessful) {
                    val weatherData = response.body()
                    callback.onSuccess(weatherData)
                } else {
                    callback.onError("Error fetching weather data")
                }
            }

            override fun onFailure(call: Call<FiveDayForecast>, t: Throwable) {
                callback.onError(t.message ?: "Network request failed")
            }
        })
    }
}
