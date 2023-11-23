package com.example.mainactivity

data class WeatherItem(
    val city: String,
    val date: String,
    val temperatureIcon: Int,
    val weatherDescription: String,
    val weatherIcon: Int,
    val sunrise: String,
    val percipitation: String,
    val sunset: String
)
