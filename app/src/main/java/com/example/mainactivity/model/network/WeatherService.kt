package com.example.mainactivity.model.network

import com.example.mainactivity.network.network.LocationResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Interface representing the Weather Service API for retrieving weather-related data.
 */
interface WeatherService {
    @GET("locations/v1/postalcodes/search")
    suspend fun getLocationsKey(
        @Query("apikey") apiKey: String,
        @Query("q") postalCode: String,
        @Query("language") language: String = "en-us",
        @Query("details") details: Boolean = false
    ): Response<LocationResponse>

    @GET("forecasts/v1/daily/5day/{locationKey}")
    suspend fun getFiveDayForecast(
        @Path("locationKey") locationKey: String,
        @Query("apikey") apiKey: String,
        @Query("language") language: String = "en-us",
        @Query("details") details: Boolean = true,
        @Query("metric") metric: Boolean = false
    ): Response<ForecastResponse>

    @GET("forecasts/v1/hourly/12hour/{locationKey}")
    suspend fun getTwelveHourForecast(
        @Path("locationKey") locationKey: String,
        @Query("apikey") apiKey: String,
        @Query("language") language: String = "en-us",
        @Query("metric") metric: Boolean = false
    ): Response<List<HourlyForecastResponse>>
}
