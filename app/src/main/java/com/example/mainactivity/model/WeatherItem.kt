package com.example.mainactivity.model
/**
 * Data class representing essential weather information.
 * This model is structured to hold weather data for a specific location and date.
 * Specifically used for the student user.
 *
 * @property city The name of the city for which the weather data is applicable.
 * @property date The date for which the weather data is relevant.
 * @property temperatureIcon A resource ID (Int) pointing to the drawable resource for the temperature icon.
 * @property temperature The current temperature as a String.
 * @property weatherIcon A resource ID (Int) for the icon representing the overall weather condition (e.g., sunny, cloudy).
 * @property highTemp The highest predicted temperature for the given date.
 * @property lowTemp The lowest predicted temperature for the given date.
 * @property precipitation A String representing the predicted precipitation amount in inches.
 */
data class WeatherItem(
    val city: String,
    val date: String,
    val temperatureIcon: Int,
    var temperature: String,
    val weatherIcon: Int,
    val highTemp: String,
    val lowTemp: String,
    val precipitation: String
)
