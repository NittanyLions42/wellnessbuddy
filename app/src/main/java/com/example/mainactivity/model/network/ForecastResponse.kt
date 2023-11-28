package com.example.mainactivity.model.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Represents the complete response for a forecast request.
 * It includes a list of daily forecasts.
 * This class is used to deserialize the JSON response containing 5-day Weather-related details.
 *
 * @property dailyForecasts List of [DailyForecast] detailing the forecast for each day.
 */
@JsonClass(generateAdapter = true)
data class ForecastResponse(
    @Json(name = "DailyForecasts") val dailyForecasts: List<DailyForecast>
)

/**
 * Details the weather forecast for a specific day, including temperature, and conditions for day and night.
 *
 * @property date The date of the forecast.
 * @property temperature Information about the day's temperature range.
 * @property airAndPollen List of [AirAndPollen] detailing air quality and UV index information.
 * @property day Forecast details for the daytime.
 * @property night Forecast details for the nighttime.
 */
@JsonClass(generateAdapter = true)
data class DailyForecast(
    @Json(name = "Date") val date: String,
    @Json(name = "Temperature") val temperature: TemperatureInfo,
    @Json(name = "AirAndPollen") val airAndPollen: List<AirAndPollen>,
    @Json(name = "Day") val day: DayForecast,
    @Json(name = "Night") val night: NightForecast,
)

/**
 * Contains temperature details including minimum and maximum values.
 *
 * @property minimum The minimum temperature for the day.
 * @property maximum The maximum temperature for the day.
 */
@JsonClass(generateAdapter = true)
data class TemperatureInfo(
    @Json(name = "Minimum") val minimum: TemperatureValue,
    @Json(name = "Maximum") val maximum: TemperatureValue
)

/**
 * Represents a temperature value.
 *
 * @property value The numerical value of the temperature.
 * @property unit The unit of measurement for the temperature (e.g., Celsius or Fahrenheit).
 */
@JsonClass(generateAdapter = true)
data class TemperatureValue(
    @Json(name = "Value") val value: Int,
    @Json(name = "Unit")val unit: String
)

/**
 * Details about specific air quality and pollen conditions, such as UV index and air quality index.
 * Only includes 'AirQuality' and 'UVIndex' data.
 *
 * @property name The name of the air or pollen condition (e.g., 'UVIndex' or 'AirQuality').
 * @property value The measured value of the condition.
 * @property category The category of the condition (e.g., Low, Moderate).
 */
@JsonClass(generateAdapter = true)
data class AirAndPollen( //  For AirQuality and UVIndex
    @Json(name = "Name") val name: String,
    @Json(name = "Value") val value: Int,
    @Json(name = "Category") val category: String?
)

/**
 * Contains forecast details for a specific period (day or night), including weather conditions and wind information.
 *
 * @property iconPhrase A phrase describing the weather icon.
 * @property hasPrecipitation Boolean indicating if there is any precipitation expected.
 * @property dayPrecipitation Probability of precipitation during the period.
 */
@JsonClass(generateAdapter = true)
data class DayForecast(
    @Json(name = "IconPhrase") val iconPhrase: String,
    @Json(name = "HasPrecipitation") val hasPrecipitation: Boolean,
    @Json(name = "PrecipitationProbability") val dayPrecipitation: Int,
    @Json(name = "Wind") val wind: Wind
)

@JsonClass(generateAdapter = true)
data class NightForecast(
    @Json(name = "IconPhrase") val iconPhrase: String,
    @Json(name = "HasPrecipitation") val hasPrecipitation: Boolean,
    @Json(name = "PrecipitationProbability") val nightPrecipitation: Int,
    @Json(name = "Wind") val wind: Wind
)

/**
 * Contains wind speed information.
 *
 * @property speed Details of wind speed.
 */
@JsonClass(generateAdapter = true)
data class Wind(
    @Json(name = "Speed") val speed: Speed
)

/**
 * Represents the speed of the wind.
 *
 * @property value The value of the wind speed.
 * @property unit The unit of measurement for wind speed.
 */
@JsonClass(generateAdapter = true)
data class Speed(
    @Json(name = "Value") val value: Double,
    @Json(name = "Unit") val unit: String
)
