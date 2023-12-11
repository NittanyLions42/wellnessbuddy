package com.example.mainactivity.model

import java.util.Date

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
