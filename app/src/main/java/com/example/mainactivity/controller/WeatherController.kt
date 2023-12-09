package com.example.mainactivity.controller

import com.example.mainactivity.R
import com.example.mainactivity.model.WeatherItem
import com.example.mainactivity.model.network.FiveDayForecast
import com.example.mainactivity.repository.WeatherRepository

class WeatherController(
    private val weatherRepository: WeatherRepository,
    private val weatherCallback: WeatherCallback
) {

    interface WeatherCallback {
        fun onSuccess(weatherData: List<WeatherItem>)
        fun onError(error: String)
    }

    fun fetchWeatherForecast(
        zipCode: String,
        countryCode: String,
        apiKey: String,
        units: String = "imperial"
    ) {
        weatherRepository.getWeatherForecast(
            zipCode,
            countryCode,
            apiKey,
            units,
            object : WeatherRepository.WeatherCallback {
                override fun onSuccess(weatherData: FiveDayForecast?) {
                    if (weatherData != null) {
                        val weatherItems = convertDataToWeatherItems(weatherData)
                        weatherCallback.onSuccess(weatherItems)
                    } else {
                        weatherCallback.onError("Weather data is null")
                    }
                }

                override fun onError(error: String) {
                    weatherCallback.onError(error)
                }
            })
    }

    private fun convertDataToWeatherItems(weatherData: FiveDayForecast): List<WeatherItem> {
        val weatherItems = mutableListOf<WeatherItem>()
        val city = weatherData.city.name

        for (forecast in weatherData.list) {
            val date = forecast.dt.toString()
            val temperature = forecast.main.temp.toString() + "°F"
            val weatherCondition = forecast.weather.firstOrNull()?.main ?: "Unknown"
            val weatherIcon = getWeatherIconResourceId(weatherCondition)
            val highTemp = forecast.main.tempMax.toString() + "°F"
            val lowTemp = forecast.main.tempMin.toString() + "°F"
            val precipitation = forecast.pop.toString() + " in"

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

    private fun getWeatherIconResourceId(weatherCondition: String): Int {
        return when (weatherCondition) {
            "Clear" -> R.drawable.sunny
            "Clouds" -> R.drawable.weather_icon
            "Rain" -> R.drawable.rainy

            else -> R.drawable.weather_icon
        }
    }
}
