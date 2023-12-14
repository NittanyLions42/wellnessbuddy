package com.example.mainactivity.controller

import com.example.mainactivity.R
import com.example.mainactivity.model.WeatherItem
import com.example.mainactivity.model.network.FiveDayForecast
import com.example.mainactivity.repository.WeatherRepository
import com.example.mainactivity.utils.Logger
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt
/**
 * Controller for fetching and handling weather data specifically for student user.
 *
 * @param weatherRepository The repository to fetch weather data.
 * @param weatherCallback The callback interface to communicate with the view.
 * **/
class WeatherController(
    private val weatherRepository: WeatherRepository,
    private val weatherCallback: WeatherCallback,
) {
    /**
     * Interface for callbacks to handle success or error in weather data fetching.
     * **/
    interface WeatherCallback {
        fun onSuccess(weatherData: List<WeatherItem>)
        fun onError(error: String)
    }

    /**
     * Fetches the weather forecast based on the provided zipcode, API key, and units.
     *
     * @param zipCode The zipcode for which weather data is to be fetched.
     * @param apiKey The API key for weather data access.
     * @param units The measurement units for weather data.
     * **/
    fun fetchWeatherForecast(
        zipCode: String,
        apiKey: String,
        units: String = "imperial"
    ) {

        Logger.d("WeatherController", "Fetching weather forecast for zipCode:$zipCode")
        weatherRepository.getWeatherForecast(
            zipCode,
            apiKey,
            units,
            object : WeatherRepository.WeatherCallback {
                override fun onSuccess(weatherData: FiveDayForecast?) {
                    if (weatherData != null) {
                        Logger.d("WeatherController", "Weather data retrieved successfully")
                        val weatherItems = convertDataToWeatherItems(weatherData)
                        Logger.d("WeatherController", "Convert Data to weather items successfully")
                        weatherCallback.onSuccess(weatherItems)
                    } else {
                        val errorMessage = "Weather data is null"
                        Logger.e("WeatherController", errorMessage)
                        weatherCallback.onError(errorMessage)
                    }
                }

                override fun onError(error: String) {
                    Logger.e("WeatherController", "Error fetching weather data: $error")
                    weatherCallback.onError(error)
                }
            })
    }
    /**
     * Converts raw weather data into a list of WeatherItem objects for UI.
     *
     * @param weatherData The raw weather data.
     * @return A list of WeatherItem objects.
     * **/
    private fun convertDataToWeatherItems(weatherData: FiveDayForecast): List<WeatherItem> {
        val weatherItems = mutableListOf<WeatherItem>()
        val city = weatherData.city.name

        for (forecast in weatherData.list) {
            val date = SimpleDateFormat("MMM d, yy", Locale.getDefault()).format(Date(forecast.dt * 1000L))
            val temperature = forecast.main.temp.roundToInt().toString() + "°F"
            val weatherCondition = forecast.weather.firstOrNull()?.main ?: "Unknown"
            val weatherIcon = getWeatherIconResourceId(weatherCondition)
            val highTemp = forecast.main.tempMax.roundToInt().toString() + "°F"
            val lowTemp = forecast.main.tempMin.roundToInt().toString() + "°F"
            val precipitation = forecast.pop.roundToInt().toString() + " in"

            val weatherItem = WeatherItem(
                city,
                date,
                R.drawable.therm_icon_transparent,
                temperature,
                weatherIcon,
                highTemp,
                lowTemp,
                precipitation
            )
            weatherItems.add(weatherItem)
        }

        return weatherItems
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
}