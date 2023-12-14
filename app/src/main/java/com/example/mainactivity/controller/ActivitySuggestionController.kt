package com.example.mainactivity.controller

import android.annotation.SuppressLint
import com.example.mainactivity.activities.MainActivity
import com.example.mainactivity.model.WeatherItem
import com.example.mainactivity.R
import kotlinx.coroutines.*
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import android.util.Log

class ActivitySuggestionController(private val view: MainActivity) {

    @SuppressLint("AuthLeak")
    private val connectionURL = ""

    data class ActivityRecommendation(
        val title: String,
        val imageResId: Int,
        val description: String
    )

    enum class WeatherCondition {
        RAINY, CLEAR, CLOUDY, DEFAULT
    }

    enum class ActivityType {
        INDOOR, OUTDOOR, DEFAULT
    }

    fun recommendActivityBasedOnWeather(weatherItems: List<WeatherItem>) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val currentWeatherCondition = getCurrentWeatherCondition(weatherItems)
                val activityType = determineActivityType(currentWeatherCondition)
                val recommendation = fetchRandomRecommendation(activityType)
                withContext(Dispatchers.Main) {
                    view.displayActivityRecommendation(recommendation)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    view.showErrorDialog("Failed to recommend activity: ${e.message}")
                }
            }
        }
    }

    fun fetchNewRandomActivity() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val activityType = listOf(ActivityType.INDOOR, ActivityType.OUTDOOR).random()
                val recommendation = fetchRandomRecommendation(activityType)
                withContext(Dispatchers.Main) {
                    view.displayActivityRecommendation(recommendation)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    view.showErrorDialog("Failed to fetch new activity: ${e.message}")
                }
            }
        }
    }

    private fun getCurrentWeatherCondition(weatherItems: List<WeatherItem>): WeatherCondition {
        val firstItem = weatherItems.firstOrNull() ?: return WeatherCondition.DEFAULT

        return when (firstItem.weatherIcon) {
            R.drawable.sunny -> WeatherCondition.CLEAR
            R.drawable.weather_icon -> WeatherCondition.CLOUDY
            R.drawable.rainy -> WeatherCondition.RAINY
            else -> WeatherCondition.DEFAULT
        }
    }

    private fun determineActivityType(weatherCondition: WeatherCondition): ActivityType {
        return when (weatherCondition) {
            WeatherCondition.RAINY, WeatherCondition.CLOUDY -> ActivityType.INDOOR
            WeatherCondition.CLEAR -> ActivityType.OUTDOOR
            else -> ActivityType.DEFAULT
        }
    }

    @SuppressLint("DiscouragedApi")
    private fun fetchRandomRecommendation(activityType: ActivityType): ActivityRecommendation {
        var connection: Connection? = null
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver")
            connection = DriverManager.getConnection(connectionURL)
            val query = when (activityType) {
                ActivityType.OUTDOOR -> "SELECT TOP 1 outdoor_title, outdoor_img, outdoor_description FROM dbo.activities_data WHERE outdoor_img IS NOT NULL ORDER BY NEWID()"
                ActivityType.INDOOR -> "SELECT TOP 1 indoor_title, indoor_img, indoor_description FROM dbo.activities_data WHERE indoor_img IS NOT NULL ORDER BY NEWID()"
                else -> return fetchRandomRecommendation(activityType) // Recursive call for DEFAULT case
            }

            connection.prepareStatement(query).use { preparedStatement ->
                val resultSet = preparedStatement.executeQuery()
                if (resultSet.next()) {
                    val title = resultSet.getString(1)
                    val imageName = resultSet.getString(2)
                    val description = resultSet.getString(3)

                    if (imageName.isNullOrEmpty()) {
                        return fetchRandomRecommendation(activityType) // Recursive call if imageName is null or empty
                    }

                    val imageResId = view.resources.getIdentifier(imageName, "drawable", view.packageName)
                    return ActivityRecommendation(title, imageResId, description)
                }
            }
        } catch (e: SQLException) {
            Log.e("ActivitySuggestion", "SQL Exception: ${e.message}")
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            Log.e("ActivitySuggestion", "Class Not Found Exception: ${e.message}")
            e.printStackTrace()
        } finally {
            connection?.close()
        }

        return fetchRandomRecommendation(activityType) // Recursive call if no data is found
    }

    private fun defaultRecommendation(): ActivityRecommendation {
        return ActivityRecommendation("Default Title", R.drawable.lay_in_grass, "Default Description")
    }
}
