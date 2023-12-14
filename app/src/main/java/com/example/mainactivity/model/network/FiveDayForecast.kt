package com.example.mainactivity.model.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Represents the overall structure of a five-day weather forecast response.
 *
 * @property cod A code indicating the status of the API response.
 * @property message Additional information about the API response.
 * @property cnt The count of data points returned.
 * @property list A list of weather data points.
 * @property city Information about the city for which the forecast is provided.
 */
@JsonClass(generateAdapter = true)
data class FiveDayForecast(
    val cod: String,
    val message: Int,
    val cnt: Int,
    val list: List<WeatherData>,
    val city: City
)

/**
 * Represents detailed weather data for a specific time point.
 *
 * @property dt The date and time of the data point in Unix format.
 * @property main The main weather details like temperature and humidity.
 * @property weather A list of weather conditions.
 * @property wind Wind details.
 * @property pop Probability of precipitation.
 * @property clouds Cloudiness information.
 * @property dtTxt The date and time of the data point in text format.
 */
@JsonClass(generateAdapter = true)
data class WeatherData(
    val dt: Long,
    val main: MainDetails,
    val weather: List<WeatherCondition>,
    val wind: Wind,
    val pop: Double,
    val clouds: Clouds,
    @Json(name = "dt_txt") val dtTxt: String
)

/**
 * Represents the main details of a weather forecast.
 *
 * @property temp The current temperature.
 * @property tempMin The minimum temperature.
 * @property tempMax The maximum temperature.
 * @property humidity The humidity percentage.
 */
@JsonClass(generateAdapter = true)
data class MainDetails(
    val temp: Double,
    @Json(name = "temp_min") val tempMin: Double,
    @Json(name = "temp_max") val tempMax: Double,
    val humidity: Int
)

/**
 * Represents a weather condition.
 *
 * @property main The main weather condition (e.g., Rain, Clouds).
 */
@JsonClass(generateAdapter = true)
data class WeatherCondition(
    val main: String
)

/**
 * Represents wind details.
 *
 * @property speed The wind speed.
 */
@JsonClass(generateAdapter = true)
data class Wind(
    val speed: Double
)

/**
 * Represents cloudiness information.
 *
 * @property all The percentage of cloudiness.
 */
@JsonClass(generateAdapter = true)
data class Clouds(
    val all: Int
)

/**
 * Represents information about the city.
 *
 * @property sunrise The sunrise time in Unix format.
 * @property sunset The sunset time in Unix format.
 * @property name The name of the city.
 */
@JsonClass(generateAdapter = true)
data class City(
    val sunrise: Long,
    val sunset: Long,
    val name: String
)
