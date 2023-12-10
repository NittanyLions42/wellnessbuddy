package com.example.mainactivity.controller

import android.annotation.SuppressLint
import android.util.Log
import com.example.mainactivity.activities.MainActivity
import org.json.JSONArray
import org.json.JSONException
import java.io.IOException
import java.util.Random

import com.example.mainactivity.model.network.Temperature
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayInputStream

class RecommendationController(private val view: MainActivity) {

    @SuppressLint("AuthLeak")
    var connectionURL =
        "jdbc:jtds:sqlserver://wellnessbuddy.database.windows.net:1433;DatabaseName=wellnessbuddyDB;user=java@wellnessbuddy;password=WellnessBuddy23;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;ssl=request"
    private val recommendationsMap: MutableList<Pair<IntRange, List<Recommendation>>> =
        mutableListOf()




    //query database for values for outdoor or indoor activities
    @Throws(IOException::class)
    fun loadRecommendationFromJson() {
        try {
            // Load JDBC driver
            Class.forName("net.sourceforge.jtds.jdbc.Driver")

            // Establish a connection
            val connection: Connection = DriverManager.getConnection(connectionURL)

            val query = "SELECT Outdoor_Title, Outdoor_Img, Outdoor_Description, Indoor_Title, Indoor_Img, Indoor_Description FROM dbo.activities_data"
            val preparedStatement: PreparedStatement = connection.prepareStatement(query)

            val resultSet: ResultSet = preparedStatement.executeQuery()

            // Process the result set
            while (resultSet.next()) {
                val outdoortitle = resultSet.getString("Outdoor_Title")
                val outdoorimg = resultSet.getString("Outdoor_Img")
                val outdoordescription = resultSet.getString("Outdoor_Description")

                // Parse the information and add to recommendationsMap
                if(outdoortitle != null && outdoorimg != null && outdoordescription != null) {
                    val outdoorRecommendation =
                        Recommendation(outdoortitle, outdoorimg, outdoordescription)
                    recommendationsMap.add(
                        IntRange(55, 100) to listOf(outdoorRecommendation)
                    )
                }
                Log.d("RecommendationController", "Outdoor Title: $outdoortitle, Outdoor Img: $outdoorimg, Outdoor Description: $outdoordescription")

                val indoorTitle = resultSet.getString("Indoor_Title")
                val indoorImg = resultSet.getString("Indoor_Img")
                val indoorDescription = resultSet.getString("Indoor_Description")

                 //Parse the information and add to recommendationsMap
                if(indoorTitle != null && indoorImg != null && indoorDescription != null) {
                    val indoorRecommendation =
                        Recommendation(indoorTitle, indoorImg, indoorDescription)
                    recommendationsMap.add(
                        IntRange(0, 54) to listOf(indoorRecommendation)
                    )
                }
                Log.d("RecommendationController", "Indoor Title: $indoorTitle, Indoor Img: $indoorImg, Indoor Description: $indoorDescription")
            }

            // Close database
            resultSet.close()
            preparedStatement.close()
            connection.close()

        } catch (e: Exception) {
            throw RuntimeException(e)
        }
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

    //get random activity recommendation
    private fun getRandomRecommendation(
        recommendationsMap: List<Pair<IntRange, List<Recommendation>>>,
        isOutdoor: Boolean
    ): Recommendation? {
        val random = Random()
        val ageRange = random.nextInt(101)
        val makeRecommendations = recommendationsMap

            .find { ageRange in it.first && (isOutdoor && it.first.first >= 55 || !isOutdoor && it.first.first <= 54) }
            ?.second

        return makeRecommendations?.let {
            if (it.isNotEmpty()) {
                val index = random.nextInt(it.size)
                it[index]
            } else {
                null
            }
        }
    }

    //this function needs to be called in MainActivity
        fun displayRecommendation(temperature: Temperature) {
            val temperatureValue = temperature.value.toInt()

            val randomRecommendation: Recommendation? = recommendationsMap
                .firstOrNull { it.first.contains(temperatureValue) }
                ?.let { getRandomRecommendation(listOf(it), temperatureValue >= 55) }

            view.displayRecommendation(randomRecommendation)
        }


}

// need to check with weather data for variable name use
//        private void fetchWeatherData(String cityName) {
//            Moshi moshi = new Moshi.Builder().build();
//            Retrofit retrofit = new Retrofit.Builder()
//                    .baseUrl("http://dataservice.accuweather.com/")
//                    .addConverterFactory(MoshiConverterFactory.create(moshi))
//                    .build();
//
//            WeatherService weatherService = retrofit.create(WeatherService.class);
//            Call<requestTracker> call = weatherService.getTwelveHourForecast(locationKey, apiKey);
//
//            call.enqueue(new Callback<requestTracker>() {
//                @Override
//                public void onResponse(Call<requestTracker> call, Response<requestTracker> response) {
//                    if (response.isSuccessful() && response.body() != null) {
//                        double temperatureValue = response.body().getMain().getTemperatureValue();
//                        displayRecommendation(temperatureValue);
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<requestTracker> call, Throwable t) {
//                    t.printStackTrace();
//                }
//            });
//        }

//      fun Recommendation(string: String): Recommendation {
//
//    }


// may not need this parsing function
//    @Throws(JSONException::class)
//    fun parseRecommendations(jsonArray: JSONArray): List<Recommendation> {
//        val recommendations: MutableList<Recommendation> = ArrayList()
//        for (i in 0 until jsonArray.length()) {
//            val recommendationJson = jsonArray.getJSONObject(i)
//            val recommendation =
//                Recommendation(
//                    title = recommendationJson.getString("Title"),
//                    img = recommendationJson.getString("Img"),
//                    description = recommendationJson.getString("Description")
//
//                )
//            recommendations.add(recommendation)
//        }
//        return recommendations
//    }