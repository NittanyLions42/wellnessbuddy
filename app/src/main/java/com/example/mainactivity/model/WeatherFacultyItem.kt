package com.example.mainactivity.model

data class WeatherFacultyItem (
    val city: String,
    val date: String,
    val temperatureIcon: Int,
    val weatherDescription: String,
    val weatherIcon: Int,
    val highTemp: String,
    val lowTemp: String,
    val percipitation: String,
    val maxUvIndex: String,
    val wind: String,
    val airQuality: String
)