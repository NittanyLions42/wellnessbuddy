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
/**
 * Controller for suggesting activities based on current weather conditions.
 * @param view Reference to MainActivity for updating UI.
 * **/
class ActivitySuggestionController(private val view: MainActivity) {

    // Database connection string.
    @SuppressLint("AuthLeak")
    private val connectionURL = "jdbc:jtds:sqlserver://wellnessbuddy.database.windows.net:1433;DatabaseName=wellnessbuddyDB;user=java@wellnessbuddy;password=WellnessBuddy23;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;ssl=request"

    // Data class representing an activity recommendation
    data class ActivityRecommendation(
        val title: String,
        val imageResId: Int,
        val description: String
    )

    // Eum for different weather conditions
    enum class WeatherCondition {
        RAINY, CLEAR, CLOUDY, DEFAULT
    }

    // Eum for types of activities
    enum class ActivityType {
        INDOOR, OUTDOOR, DEFAULT
    }

    /**
     * Recommends an activity based on the current weather conditions.
     * @param weatherItems List of WeatherItem objects representing the weather data
     * **/
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

//    fun fetchNewRandomActivity() {
//        CoroutineScope(Dispatchers.IO).launch {
//            try {
//                val activityType = listOf(ActivityType.INDOOR, ActivityType.OUTDOOR).random()
//                val recommendation = fetchRandomRecommendation(activityType)
//                withContext(Dispatchers.Main) {
//                    view.displayActivityRecommendation(recommendation)
//                }
//            } catch (e: Exception) {
//                withContext(Dispatchers.Main) {
//                    view.showErrorDialog("Failed to fetch new activity: ${e.message}")
//                }
//            }
//        }
//    }

    /**
     * Determines the current weather condition based on the first item in the weather data list.
     *
     * @param weatherItems A list of WeatherItem objects.
     * @return A WeatherCondition enum value representing the current weather condition
     * **/
    private fun getCurrentWeatherCondition(weatherItems: List<WeatherItem>): WeatherCondition {
        val firstItem = weatherItems.firstOrNull() ?: return WeatherCondition.DEFAULT

        return when (firstItem.weatherIcon) {
            R.drawable.sunny -> WeatherCondition.CLEAR
            R.drawable.weather_icon -> WeatherCondition.CLOUDY
            R.drawable.rainy -> WeatherCondition.RAINY
            else -> WeatherCondition.DEFAULT
        }
    }

    /**
     * Determines the type of activity suitable for the current weather.
     *
     * @param weatherCondition A WeatherCondition enum value.
     * @return An ActivityType enum value representing the suitable type of activity.
     * **/
    private fun determineActivityType(weatherCondition: WeatherCondition): ActivityType {
        return when (weatherCondition) {
            WeatherCondition.RAINY, WeatherCondition.CLOUDY -> ActivityType.INDOOR
            WeatherCondition.CLEAR -> ActivityType.OUTDOOR
            else -> ActivityType.DEFAULT
        }
    }

    /**
     * Fetches a random activity recommendation from the database based on activity type.
     *
     * @param activityType An ActivityType enum value.
     * @return An ActivityRecommendation object containing the suggested activity details.
     * **/
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

}
