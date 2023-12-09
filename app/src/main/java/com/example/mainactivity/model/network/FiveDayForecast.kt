package com.example.mainactivity.model.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FiveDayForecast(
    val cod: String,
    val message: Int,
    val cnt: Int,
    val list: List<WeatherData>,
    val city: City
)

@JsonClass(generateAdapter = true)
data class WeatherData(
    val dt: Long,
    val main: MainDetails,
    val weather: List<WeatherCondition>,
    val wind: Wind,
    val pop: Double,
    val clouds: Clouds
)

@JsonClass(generateAdapter = true)
data class MainDetails(
    val temp: Double,
    @Json(name = "temp_min") val tempMin: Double,
    @Json(name = "temp_max") val tempMax: Double,
    val humidity: Int
)

@JsonClass(generateAdapter = true)
data class WeatherCondition(
    val main: String
)

@JsonClass(generateAdapter = true)
data class Wind(
    val speed: Double
)

@JsonClass(generateAdapter = true)
data class Clouds(
    val all: Int
)

@JsonClass(generateAdapter = true)
data class City(
    val sunrise: Long,
    val sunset: Long,
    val name: String
)
