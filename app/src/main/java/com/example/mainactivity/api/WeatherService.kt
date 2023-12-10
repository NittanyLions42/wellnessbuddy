package com.example.mainactivity.api

import com.example.mainactivity.model.network.FiveDayForecast
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interface representing the Weather Service API for retrieving weather-related data.
 */

interface WeatherService {
    @GET("data/2.5/forecast")
    fun getWeatherForecast(
        @Query("zip") zipCode: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "imperial"
    ): Call<FiveDayForecast>
}
