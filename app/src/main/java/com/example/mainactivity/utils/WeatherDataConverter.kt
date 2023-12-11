package com.example.mainactivity.utils

import android.annotation.SuppressLint
import com.example.mainactivity.R
import com.example.mainactivity.model.WeatherItem
import com.example.mainactivity.model.network.FiveDayForecast
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.roundToInt

object WeatherDataConverter {
    @SuppressLint("ConstantLocale")
    private val outputDateFormat = SimpleDateFormat("MMM dd, yy", Locale.getDefault())
    private val inputDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    fun convertToWeatherItems(fiveDayForecast: FiveDayForecast): List<WeatherItem> {
        val dailyForecasts = fiveDayForecast.list.groupBy {
            val date = inputDateFormat.parse(it.dtTxt)
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date)
        }

        return dailyForecasts.map { (day, forecasts) ->
            val avgTemp = forecasts.map { it.main.temp }.average()
            val minTemp = forecasts.minOf { it.main.tempMin }
            val maxTemp = forecasts.maxOf { it.main.tempMax }

            val formattedDate = outputDateFormat.format(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(day))

            WeatherItem(
                city = fiveDayForecast.city.name,
                date = formattedDate,
                temperatureIcon = R.drawable.therm_icon,
                temperature = String.format(Locale.getDefault(), "%.1f°F", avgTemp),
                weatherIcon = getWeatherIconResourceId(forecasts.first().weather.first().main),
                highTemp = String.format(Locale.getDefault(), "%.1f°F", maxTemp),
                lowTemp = String.format(Locale.getDefault(), "%.1f°F", minTemp),
                precipitation = String.format(Locale.getDefault(), "%.1f%%", forecasts.sumOf { it.pop } * 100 / forecasts.size)
            )
        }
            .sortedBy { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(it.date) }
    }

    private fun getWeatherIconResourceId(weatherCondition: String): Int {
        return when (weatherCondition) {
            "Clear" -> R.drawable.sunny
            "Clouds" -> R.drawable.weather_icon
            "Rain" -> R.drawable.rainy
            else -> R.drawable.weather_icon
        }
    }
}