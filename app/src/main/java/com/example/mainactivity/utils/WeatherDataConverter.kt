package com.example.mainactivity.utils

import android.annotation.SuppressLint
import com.example.mainactivity.R
import com.example.mainactivity.model.WeatherItem
import com.example.mainactivity.model.network.FiveDayForecast
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.roundToInt

object WeatherDataConverter {
    @JvmStatic
    fun aggregateDayData(dailyItems: List<WeatherItem>): WeatherItem {

        // Filter data for the first three hours of the day (or any desired time window)
        val filteredItems = dailyItems.filter { item ->
            val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.US)
            val date = dateFormat.parse(item.date) // Convert the date string to a Date object
            val calendar = Calendar.getInstance()

            if (date != null) {
                calendar.time = date
                val hour = calendar.get(Calendar.HOUR_OF_DAY) // Extract the hour from the date

                // Define the time window (e.g., from midnight to 3 AM)
                val startTime = 0
                val endTime = 3

                hour in startTime..endTime // Check if the hour is within the specified range
            } else {
                false // Handle the case where date parsing fails
            }
        }

        // Find the maximum high and minimum low temperatures
        val maxHighTemp = filteredItems.maxOfOrNull {
            it.highTemp.removeSuffix("°F").toDoubleOrNull()?.roundToInt() ?: Int.MIN_VALUE
        } ?: 0
        val minLowTemp = filteredItems.minOfOrNull {
            it.lowTemp.removeSuffix("°F").toDoubleOrNull()?.roundToInt() ?: Int.MAX_VALUE
        } ?: 0

        // Calculate the average temperature for the time window
        val averageTemperature = filteredItems.mapNotNull {
            it.temperature.removeSuffix("°F").toDoubleOrNull()
        }.average()

        // Format averageTemperature with 0 decimals
        val formattedAverageTemperature = String.format("%.0f°F", averageTemperature)

        // Take the precipitation value from the first item in filteredItems
        val firstPrecipitation = filteredItems.firstOrNull()?.precipitation ?: "0.0 in"

        val precipitationValue = firstPrecipitation.removeSuffix(" in").toDoubleOrNull()
        val formattedTotalPrecipitation = if (precipitationValue != null) {
            String.format("%.2f in", precipitationValue)
        } else {
            "0.00 in" // Provide a default value or handle the case where parsing fails
        }

        // Choose the most frequent weather icon and temperature icon for the day
        val mostFrequentWeatherIcon = filteredItems.groupingBy { it.weatherIcon }.eachCount().maxByOrNull { it.value }?.key ?: R.drawable.sunny
        val mostFrequentTempIcon = filteredItems.groupingBy { it.temperatureIcon }.eachCount().maxByOrNull { it.value }?.key ?: R.drawable.therm_icon_transparent

        // Use the first item's date and city for the aggregated item
        val date = filteredItems.first().date
        val city = filteredItems.first().city

        return WeatherItem(
            city = city,
            date = date,
            temperatureIcon = mostFrequentTempIcon,
            temperature = formattedAverageTemperature, // Average temperature for the day
            weatherIcon = mostFrequentWeatherIcon,
            highTemp = "${maxHighTemp}°F",
            lowTemp = "${minLowTemp}°F",
            precipitation = formattedTotalPrecipitation
        )
    }

    fun aggregateWeatherDataByDay(weatherItems: List<WeatherItem>): List<WeatherItem> {
        val aggregatedData = mutableListOf<WeatherItem>()

        // Group by the date field instead of dtTxt
        val groupedByDay = weatherItems.groupBy { it.date }

        for ((date, items) in groupedByDay) {
            val dayItem = aggregateDayData(items)
            aggregatedData.add(dayItem)
        }

        return aggregatedData.take(5) // Take data for up to 5 days
    }

    fun processAndRoundTemperatures(weatherData: List<WeatherItem>): List<WeatherItem> {
        return weatherData.map { item ->
            item.copy(
                temperature = item.temperature.removeSuffix("°F").toDoubleOrNull()?.roundToInt().toString() + "°F",
                highTemp = item.highTemp.removeSuffix("°F").toDoubleOrNull()?.roundToInt().toString() + "°F",
                lowTemp = item.lowTemp.removeSuffix("°F").toDoubleOrNull()?.roundToInt().toString() + "°F",
                precipitation = item.precipitation.removeSuffix(" in").toDoubleOrNull()?.toString() + " in"
            )
        }
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