package com.example.mainactivity.repository

import com.example.mainactivity.api.WeatherService
import com.example.mainactivity.model.network.FiveDayForecast
import com.example.mainactivity.utils.Logger
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherRepository(private val weatherService: WeatherService) {

    interface WeatherCallback {
        fun onSuccess(weatherData: FiveDayForecast?)
        fun onError(error: String)
    }

    interface WeatherFacultyCallback {
        fun onSuccess(weatherFacultyData: FiveDayForecast?)
        fun onError(error: String)
    }

    fun getWeatherForecast(
        zipCode: String,
        apiKey: String,
        units: String = "imperial",
        callback: WeatherCallback
    ) {
        Logger.d("WeatherRepository", "Fetching weather forecast for zipCode:$zipCode")
        val call = weatherService.getWeatherForecast(zipCode, apiKey, units)
        call.enqueue(object : Callback<FiveDayForecast> {
            override fun onResponse(
                call: Call<FiveDayForecast>,
                response: Response<FiveDayForecast>
            ) {
                if (response.isSuccessful) {
                    Logger.d("WeatherRepository", "Weather data retrieved successfully")
                    val weatherData = response.body()
                    callback.onSuccess(weatherData)
                } else {
                    val errorMessage = "Error fetching weather data: ${response.code()}"
                    Logger.e("WeatherRepository", errorMessage)
                    callback.onError(errorMessage)
                }
            }

            override fun onFailure(call: Call<FiveDayForecast>, t: Throwable) {
                val errorMessage = "Network request failed: ${t.message ?: "Unknown error"}"
                Logger.e("WeatherRepository", errorMessage, t)
                callback.onError(errorMessage)
            }
        })
    }

    fun getFacultyWeatherForecast(
        zipCode: String,
        apiKey: String,
        units: String = "imperial",
        callback: WeatherFacultyCallback
    ) {
        Logger.d("WeatherRepository", "Fetching weather forecast for zipCode:$zipCode")
        val call = weatherService.getWeatherForecast(zipCode, apiKey, units)
        call.enqueue(object : Callback<FiveDayForecast> {
            override fun onResponse(
                call: Call<FiveDayForecast>,
                response: Response<FiveDayForecast>
            ) {
                if (response.isSuccessful) {
                    Logger.d("WeatherRepository", "Weather data retrieved successfully")
                    val weatherFacultyData = response.body()
                    callback.onSuccess(weatherFacultyData)
                } else {
                    val errorMessage = "Error fetching weather data: ${response.code()}"
                    Logger.e("WeatherRepository", errorMessage)
                    callback.onError(errorMessage)
                }
            }

            override fun onFailure(call: Call<FiveDayForecast>, t: Throwable) {
                val errorMessage = "Network request failed: ${t.message ?: "Unknown error"}"
                Logger.e("WeatherRepository", errorMessage, t)
                callback.onError(errorMessage)
            }
        })
    }
}
