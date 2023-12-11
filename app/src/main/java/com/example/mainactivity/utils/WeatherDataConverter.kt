package com.example.mainactivity.utils

import android.annotation.SuppressLint
import com.example.mainactivity.R
import com.example.mainactivity.model.WeatherItem
import com.example.mainactivity.model.network.FiveDayForecast
import java.text.SimpleDateFormat
import java.util.Locale

object WeatherDataConverter {
    @SuppressLint("ConstantLocale")
    private val dateFormat = SimpleDateFormat("MM dd yy", Locale.getDefault())

    fun convertToWeatherItems(fiveDayForecast: FiveDayForecast): List<WeatherItem> {

        val parsedDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

        return fiveDayForecast.list
            .groupBy {
                // Parse the dt_txt String to a Date object
                val parsedDate = parsedDateFormat.parse(it.dtTxt)
                // Format the Date object to the desired String format
                parsedDate?.let { date ->
                    dateFormat.format(date)
                } ?: ""
            }
            .map { (date, weatherDataList) ->
                val minTemp = weatherDataList.minOf { it.main.tempMin }
                val maxTemp = weatherDataList.maxOf { it.main.tempMax }
                val avgTemp = weatherDataList.map { it.main.temp }.average()

                WeatherItem(
                    city = fiveDayForecast.city.name,
                    date = date,  //
                    temperatureIcon = R.drawable.therm_icon,
                    temperature = String.format(Locale.getDefault(), "%.1f°F", avgTemp),
                    weatherIcon = getWeatherIconResourceId(weatherDataList.first().weather.first().main),
                    highTemp = String.format(Locale.getDefault(), "%.1f°F", maxTemp),
                    lowTemp = String.format(Locale.getDefault(), "%.1f°F", minTemp),
                    precipitation = String.format(Locale.getDefault(), "%.1f%%", weatherDataList.sumOf { it.pop } * 100)
                )
            }
            .sortedBy { dateFormat.parse(it.date) }
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