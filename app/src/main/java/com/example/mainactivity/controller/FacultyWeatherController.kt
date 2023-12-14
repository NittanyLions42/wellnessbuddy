package com.example.mainactivity.controller

import com.example.mainactivity.R
import com.example.mainactivity.model.WeatherFacultyItem
import com.example.mainactivity.model.network.FiveDayForecast
import com.example.mainactivity.repository.WeatherRepository
import com.example.mainactivity.utils.Logger
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt
/**
 * Controller for fetching and handling weather data specifically for faculty user.
 *
 * @param weatherRepository The repository to fetch weather data.
 * @param facultyCallback The callback interface to communicate with the view.
 * **/
class FacultyWeatherController(
    private val weatherRepository: WeatherRepository,
    private val facultyCallback: FacultyWeatherCallback
) {
    /**
     * Interface for callbacks to handle success or error in weather data fetching.
     * **/
    interface FacultyWeatherCallback {
        fun onSuccess(weatherFacultyData: List<WeatherFacultyItem>)
        fun onError(error: String)
    }

    /**
     * Fetches the weather forecast based on the provided zipcode, API key, and units.
     *
     * @param zipCode The zipcode for which weather data is to be fetched.
     * @param apiKey The API key for weather data access.
     * @param units The measurement units for weather data.
     * **/
    fun fetchFacultyWeatherForecast(
        zipCode: String,
        apiKey: String,
        units: String = "imperial",
    ) {
        Logger.d(
            "FacultyWeatherController",
            "Fetching faculty weather forecast for zipCode: $zipCode"
        )
        weatherRepository.getFacultyWeatherForecast(
            zipCode,
            apiKey,
            units,
            object : WeatherRepository.WeatherFacultyCallback {
                override fun onSuccess(weatherFacultyData: FiveDayForecast?) {
                    if (weatherFacultyData != null) {
                        Logger.d(
                            "FacultyWeatherController",
                            "Weather data retrieved successfully for faculty"
                        )
                        val weatherFacultyItems =
                            convertDataToFacultyWeatherItems(weatherFacultyData)
                        facultyCallback.onSuccess(weatherFacultyItems)
                    } else {
                        Logger.e("FacultyWeatherController", "Weather data is null")
                        facultyCallback.onError("Weather data is null")
                    }
                }

                override fun onError(error: String) {
                    Logger.e("FacultyWeatherController", "Error fetching weather data: $error")
                    facultyCallback.onError(error)
                }
            }
        )
    }
}
/**
 * Converts raw weather data into a list of WeatherFacultyItem objects for UI.
 *
 * @param weatherFacultyData The raw weather data.
 * @return A list of WeatherFacultyItem objects.
 * **/
private fun convertDataToFacultyWeatherItems(weatherFacultyData: FiveDayForecast): List<WeatherFacultyItem> {
    val weatherFacultyItems = mutableListOf<WeatherFacultyItem>()
    val city = weatherFacultyData.city.name

    for (f in weatherFacultyData.list) {
        val date =
            SimpleDateFormat("MMM d, yy", Locale.getDefault()).format(Date(f.dt * 1000L))
        val temperature = f.main.temp.roundToInt().toString() + "°F"
        val weatherCondition = f.weather.firstOrNull()?.main ?: "Unknown"
        val weatherIcon = getWeatherIconResourceId(weatherCondition)
        val highTemp = f.main.tempMax.roundToInt().toString() + "°F"
        val lowTemp = f.main.tempMin.roundToInt().toString() + "°F"
        val precipitation = f.pop.roundToInt().toString() + " in"
        val cloudCover = "${f.clouds.all}%"
        val windSpeed = "${f.wind.speed} mph"
        val humidity = "${f.main.humidity}%"

        val weatherFacultyItem = WeatherFacultyItem(
            city,
            date,
            R.drawable.therm_icon_transparent,
            temperature,
            weatherIcon,
            highTemp,
            lowTemp,
            precipitation,
            cloudCover,
            windSpeed,
            humidity
        )
        weatherFacultyItems.add(weatherFacultyItem)
    }

    return weatherFacultyItems
}

/**
 * Determines the resource ID for the weather icon based on the weather condition.
 *
 * @param weatherCondition The current weather condition.
 * @return The resource ID of the corresponding weather icon.
 * **/
private fun getWeatherIconResourceId(weatherCondition: String): Int {
    return when (weatherCondition) {
        "Clear" -> R.drawable.sunny
        "Clouds" -> R.drawable.weather_icon
        "Rain" -> R.drawable.rainy

        else -> R.drawable.weather_icon
    }
}
