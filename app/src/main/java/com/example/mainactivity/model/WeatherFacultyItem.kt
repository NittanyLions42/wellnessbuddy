package com.example.mainactivity.model

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