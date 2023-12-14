package com.example.mainactivity.model
/**
 * Data class representing weather information for a specific location and date.
 * This class is tailored to display detailed weather information in a faculty-related context.
 *
 * @property city The name of the city for which the weather data is applicable.
 * @property date The date for which the weather data is relevant.
 * @property temperatureIcon A resource ID (Int) pointing to the drawable resource for the temperature icon.
 * @property temperature The current temperature as a String.
 * @property weatherIcon A resource ID for the general weather condition icon (e.g., sunny, cloudy, rainy).
 * @property highTemp The highest expected temperature for the day.
 * @property lowTemp The lowest expected temperature for the day.
 * @property precipitation The amount of expected precipitation in inches.
 * @property cloudCover The percentage of cloud cover.
 * @property windSpeed The wind speed in miles per hour (mph)
 * @property humidity The humidity percentage.
 */
data class WeatherFacultyItem(
    val city: String,
    val date: String,
    val temperatureIcon: Int,
    var temperature: String,
    val weatherIcon: Int,
    val highTemp: String,
    val lowTemp: String,
    val precipitation: String,
    val cloudCover: String,
    val windSpeed: String,
    val humidity: String
)