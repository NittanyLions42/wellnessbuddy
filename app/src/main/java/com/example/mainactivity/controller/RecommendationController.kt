package com.example.mainactivity.controller

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.util.Log
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.util.Base64
import com.example.mainactivity.activities.MainActivity
import com.example.mainactivity.adapters.WeatherAdapter
import com.example.mainactivity.model.WeatherItem
import java.io.IOException
import java.sql.DriverManager
import java.sql.SQLException
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.random.Random

class RecommendationController(
    private val view: MainActivity,
    private val recommendationWeatherCallback: RecommendationWeatherCallback
) {

    @SuppressLint("AuthLeak")
    var connectionURL =
        "jdbc:jtds:sqlserver://wellnessbuddy.database.windows.net:1433;DatabaseName=wellnessbuddyDB;user=java@wellnessbuddy;password=WellnessBuddy23;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;ssl=request"

    private val recommendationsMap: CopyOnWriteArrayList<AgeRangeRecommendation> = CopyOnWriteArrayList()

    private val weatherAdapter: WeatherAdapter? = null

    interface RecommendationWeatherCallback {
        fun onRecommendationReady(
            temperature: Double,
            weatherCondition: String,
            randomRecommendation: Recommendation.Recommendation?
        )
        fun onError(error: String)
    }

    data class AgeRangeRecommendation(
        val ageRange: IntRange,
        val recommendations: List<Recommendation.Recommendation>
    )

    fun updateWeatherAdapter(weatherData: List<WeatherItem>) {
        weatherAdapter?.updateData(weatherData)
    }

    // query database for values for outdoor or indoor activities
    @Throws(SQLException::class)
    fun loadRecommendationFromJson() {
        try {
            // Load JDBC driver
            Class.forName("net.sourceforge.jtds.jdbc.Driver")

            // Establish a connection
            DriverManager.getConnection(connectionURL).use { connection ->
                val query =
                    "SELECT Outdoor_Title, Outdoor_Img, Outdoor_Description, Indoor_Title, Indoor_Img, Indoor_Description FROM dbo.activities_data"
                connection.prepareStatement(query).use { preparedStatement ->
                    val resultSet = preparedStatement.executeQuery()

                    // Process the result set
                    while (resultSet.next()) {
                        val outdoortitle = resultSet.getString("Outdoor_Title")
                        val outdoorimg = resultSet.getString("Outdoor_Img")
                        val outdoordescription = resultSet.getString("Outdoor_Description")

                        // Parse the information and add to recommendationsMap
                        if (outdoortitle != null && outdoorimg != null && outdoordescription != null) {
                            val outdoorRecommendation =
                                Recommendation.Recommendation(
                                    outdoortitle,
                                    outdoorimg,
                                    outdoordescription
                                )
                            recommendationsMap.add(
                                AgeRangeRecommendation(
                                    IntRange(55, 100),
                                    listOf(outdoorRecommendation)
                                )
                            )

                            Log.d(
                                "RecommendationController",
                                "Outdoor Title: $outdoortitle, Outdoor Img: $outdoorimg, Outdoor Description: $outdoordescription"
                            )
                        }

                        val indoorTitle = resultSet.getString("Indoor_Title")
                        val indoorImg = resultSet.getString("Indoor_Img")
                        val indoorDescription = resultSet.getString("Indoor_Description")

                        // Parse the information and add to recommendationsMap
                        if (indoorTitle != null && indoorImg != null && indoorDescription != null) {
                            val indoorRecommendation =
                                Recommendation.Recommendation(
                                    indoorTitle,
                                    indoorImg,
                                    indoorDescription
                                )
                            recommendationsMap.add(
                                AgeRangeRecommendation(
                                    IntRange(0, 54),
                                    listOf(indoorRecommendation)
                                )
                            )

                            Log.d(
                                "RecommendationController",
                                "Indoor Title: $indoorTitle, Indoor Img: $indoorImg, Indoor Description: $indoorDescription"
                            )
                        }
                    }
                }
            }
        } catch (e: SQLException) {
            throw RuntimeException("Error executing SQL query", e)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    fun displayRecommendation(
        weatherData: List<WeatherItem>,
        temperature: Double,
        weatherCondition: String
    ) {
        val randomRecommendation = getRandomRecommendation(recommendationsMap, temperature, weatherCondition)
        recommendationWeatherCallback.onRecommendationReady(temperature, weatherCondition, randomRecommendation)

        randomRecommendation?.let {
            view.displayRecommendation(it, weatherCondition)
        }
    }


    private fun createWeatherCallback() = object : WeatherController.WeatherCallback {
        override fun onSuccess(weatherData: List<WeatherItem>) {
            runOnUiThread {

                weatherAdapter?.updateData(weatherData)
                setupTabLayout(weatherData.size)
                updateTabLayout(weatherData.size)

                val temperature =
                    weatherData.firstOrNull()?.temperature?.removeSuffix("°F")?.toDoubleOrNull() ?: 0.0
                val weatherCondition = weatherData.firstOrNull()?.weatherCondition ?: "Unknown" // may need to remove weatherCondition.. not reading from the right dataset

                recommendationWeatherCallback.onRecommendationReady(
                    temperature,
                    weatherCondition,
                    randomRecommendation
                )

                displayRecommendation(weatherData, temperature, weatherCondition)
            }
        }

        override fun onError(error: String) {
            runOnUiThread { showErrorDialog(error) }
            recommendationWeatherCallback.onError(error)
        }
    }

    private fun updateTabLayout(size: Int) {
        view.updateTabLayout(size)

    }

    private fun setupTabLayout(size: Int) {
        view.setupTabLayout(size)
    }

    private fun runOnUiThread(function: () -> Unit) {
        Handler(Looper.getMainLooper()).post { function.invoke() }
    }

    private fun showErrorDialog(error: String) {
        val builder = AlertDialog.Builder(view) // Assuming 'view' is the context from MainActivity
        builder.setTitle("Error")
        builder.setMessage(error)
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    fun decodeBase64Image(base64img: String): Bitmap? {
        return try {
            val imageBytes = Base64.decode(base64img, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            null
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }



    // get random activity recommendation
    fun getRandomRecommendation(
        weatherData: CopyOnWriteArrayList<AgeRangeRecommendation>,
        temperature: Double,
        weatherCondition: String
    ): Recommendation.Recommendation? {
        val random = Random.Default
        val ageRange = random.nextInt(101)

        val makeRecommendations = recommendationsMap
            .find {
                ageRange in it.ageRange && isWeatherConditionSuitable(
                    it.recommendations,
                    temperature,
                    weatherCondition
                )
            }
            ?.recommendations

        return makeRecommendations?.let {
            if (it.isNotEmpty()) {
                val index = random.nextInt(it.size)
                it[index]
            } else {
                null
            }
        }
    }

    private fun isWeatherConditionSuitable(
        recommendations: List<Recommendation.Recommendation>,
        temperature: Double,
        weatherCondition: String
    ): Boolean {
        return temperature > 70 && isClear(weatherCondition)
    }

    private fun isClear(weatherCondition: String): Boolean {
        return weatherCondition.contains(
            "clear",
            ignoreCase = true
        ) || weatherCondition.contains("sun", ignoreCase = true)
    }

    fun onRecommendationReady(temperature: Double, weatherCondition: String, randomRecommendation: Recommendation.Recommendation?) {
        // Handle recommendation ready (if needed)

        if (randomRecommendation != null) {
            // Example: Display the recommendation in the UI
            view.displayRecommendation(randomRecommendation, weatherCondition)

            // Example: Show a toast message with the recommendation details
            view.showToast("Recommended activity: ${randomRecommendation.title}")


        } else {
            // Handle the case when no recommendation is available
            view.showToast("No recommendation available for the current weather")
        }
    }
}

