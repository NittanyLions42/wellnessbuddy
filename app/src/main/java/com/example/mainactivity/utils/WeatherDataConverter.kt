package com.example.mainactivity.utils

import com.example.mainactivity.R
import com.example.mainactivity.model.WeatherFacultyItem
import com.example.mainactivity.model.WeatherItem
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.roundToInt
/**
 * Utility object for processing and aggregating weather data.
 */
object WeatherDataConverter {
    /**
     * Aggregate weather data for a single day from a list of WeatherItem objects.
     * Specifically used for student user.
     *
     * @param dailyItems List of WeatherItem objects for a particular day.
     * @return An aggregated WeatherItem object representing the day's weather.
     * **/
    @JvmStatic
    fun aggregateWeatherItemDayData(dailyItems: List<WeatherItem>): WeatherItem {
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

    /**
     * Aggregate weather data by day from a list of WeatherItem objects.
     *
     * @param weatherItems List of WeatherItem objects.
     * @return A list of aggregated WeatherItem objects, one for each day.
     * **/
    fun aggregateWeatherDataByDay(weatherItems: List<WeatherItem>): List<WeatherItem> {
        val aggregatedData = mutableListOf<WeatherItem>()

        // Group by the date field instead of dtTxt
        val groupedByDay = weatherItems.groupBy { it.date }

        for ((date, items) in groupedByDay) {
            val dayItem = aggregateWeatherItemDayData(items)
            aggregatedData.add(dayItem)
        }

        return aggregatedData.take(5) // Take data for up to 5 days
    }

    /**
     * Processes and rounds temperatures in a list of WeatherItem objects.
     *
     * @param weatherData List of WeatherItem objects.
     * @return A list of WeatherItem objects with processed temperature values.
     * **/
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

    /**
     * Aggregate weather data by day from a list of WeatherFacultyItem objects.
     *
     * @param weatherFacultyItems List of WeatherFacultyItem objects.
     * @return A list of aggregated WeatherFacultyItem objects, one for each day.
     * **/
    fun aggregateWeatherFacultyDataByDay(weatherFacultyItems: List<WeatherFacultyItem>): List<WeatherFacultyItem> {
        val aggregatedData = mutableListOf<WeatherFacultyItem>()

        // Group by the date field
        val groupedByDay = weatherFacultyItems.groupBy { it.date }

        for ((date, items) in groupedByDay) {
            val dayItem = aggregateWeatherFacultyItemDayData(items) // Adjust this method to work with WeatherFacultyItem
            aggregatedData.add(dayItem)
        }

        return aggregatedData.take(5) // Take data for up to 5 days
    }
    /**
     * Processes and rounds temperatures in a list of WeatherFacultyItem objects.
     *
     * @param weatherFacultyData List of WeatherFacultyItem objects.
     * @return A list of WeatherFacultyItem objects with processed temperature values.
     * **/
    fun processAndRoundWeatherFacultyData(weatherFacultyData: List<WeatherFacultyItem>): List<WeatherFacultyItem> {
        return weatherFacultyData.map { item ->
            item.copy(
                temperature = item.temperature.removeSuffix("°F").toDoubleOrNull()?.roundToInt().toString() + "°F",
                highTemp = item.highTemp.removeSuffix("°F").toDoubleOrNull()?.roundToInt().toString() + "°F",
                lowTemp = item.lowTemp.removeSuffix("°F").toDoubleOrNull()?.roundToInt().toString() + "°F",
                precipitation = item.precipitation.removeSuffix(" in").toDoubleOrNull()?.toString() + " in",
            )
        }
    }
    /**
     * Aggregate weather data for a single day from a list of WeatherFacultyItem objects.
     * Specifically used for faculty user.
     *
     * @param dailyItems List of WeatherFacultyItem objects for a particular day.
     * @return An aggregated WeatherFacultyItem object representing the day's weather.
     * **/
    @JvmStatic
    fun aggregateWeatherFacultyItemDayData(dailyItems: List<WeatherFacultyItem>): WeatherFacultyItem {
        val filteredItems = dailyItems.filter { item ->
            val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.US)
            val date = dateFormat.parse(item.date)
            val calendar = Calendar.getInstance()

            if (date != null) {
                calendar.time = date
                val hour = calendar.get(Calendar.HOUR_OF_DAY)

                val startTime = 0
                val endTime = 3

                hour in startTime..endTime
            } else {
                false
            }
        }

        val maxHighTemp = filteredItems.maxOfOrNull {
            it.highTemp.removeSuffix("°F").toDoubleOrNull()?.roundToInt() ?: Int.MIN_VALUE
        } ?: 0
        val minLowTemp = filteredItems.minOfOrNull {
            it.lowTemp.removeSuffix("°F").toDoubleOrNull()?.roundToInt() ?: Int.MAX_VALUE
        } ?: 0

        val averageTemperature = filteredItems.mapNotNull {
            it.temperature.removeSuffix("°F").toDoubleOrNull()
        }.average()
        val formattedAverageTemperature = String.format("%.0f°F", averageTemperature)

        val firstPrecipitation = filteredItems.firstOrNull()?.precipitation ?: "0.0 in"
        val averageCloudCover = filteredItems.firstOrNull()?.cloudCover ?: "0.0 %"
        val averageWindSpeed = filteredItems.firstOrNull()?.windSpeed ?: "0.0 mph"
        val averageHumidity = filteredItems.firstOrNull()?.humidity ?: "0.0 %"

        val mostFrequentWeatherIcon = filteredItems.groupingBy { it.weatherIcon }.eachCount().maxByOrNull { it.value }?.key ?: R.drawable.partly_suny
        val mostFrequentTempIcon = filteredItems.groupingBy { it.temperatureIcon }.eachCount().maxByOrNull { it.value }?.key ?: R.drawable.therm_icon_transparent

        val date = filteredItems.first().date
        val city = filteredItems.first().city

        return WeatherFacultyItem(
            city = city,
            date = date,
            temperatureIcon = mostFrequentTempIcon,
            temperature = formattedAverageTemperature,
            weatherIcon = mostFrequentWeatherIcon,
            highTemp = "${maxHighTemp}°F",
            lowTemp = "${minLowTemp}°F",
            precipitation = firstPrecipitation,
            cloudCover = averageCloudCover,
            windSpeed = averageWindSpeed,
            humidity = averageHumidity
        )
    }
}