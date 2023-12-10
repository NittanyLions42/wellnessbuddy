package com.example.mainactivity.model.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class HourlyForecastResponse(
    @Json(name = "DateTime") val dateTime: String,
    @Json(name = "Temperature") val temperature: Temperature
)

/**
 * Represents the temperature information.
 *
 * @property value The numerical value of the temperature.
 * @property unit The unit of measurement for the temperature (e.g., Celsius or Fahrenheit).
 */
@JsonClass(generateAdapter = true)
data class Temperature(
    @Json(name = "Value") val value: Int,
    @Json(name = "Unit") val unit: String
)
