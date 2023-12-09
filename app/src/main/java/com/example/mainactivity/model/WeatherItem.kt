package com.example.mainactivity.model

data class WeatherItem(
    val city: String,
    val date: String,
    val temperatureIcon: Int,
    val temperature: String,
    val weatherIcon: Int,
    val highTemp: String,
    val lowTemp: String,
    val precipitation: String
)