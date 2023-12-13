package com.example.mainactivity.activities

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.example.mainactivity.BuildConfig
import com.example.mainactivity.R
import com.example.mainactivity.adapters.WeatherAdapter
import com.example.mainactivity.api.WeatherService
import com.example.mainactivity.controller.Recommendation
import com.example.mainactivity.controller.RecommendationController
import com.example.mainactivity.controller.WeatherController
import com.example.mainactivity.databinding.ActivityMainBinding
import com.example.mainactivity.model.WeatherItem
import com.example.mainactivity.model.network.RetrofitInstance
import com.example.mainactivity.repository.WeatherRepository
import com.example.mainactivity.utils.WeatherDataConverter
import com.google.android.material.textfield.TextInputEditText


import retrofit2.Retrofit

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var weatherController: WeatherController
    private lateinit var weatherAdapter: WeatherAdapter
    private lateinit var weatherService: WeatherService
    private lateinit var dataConverter: WeatherDataConverter
    private lateinit var retrofit: Retrofit

    private lateinit var recommendationController: RecommendationController
    private lateinit var recommendationTextView: TextView
    private lateinit var recommendationImageView: ImageView
    private lateinit var recommendationdesTextView: TextView
    private var weatherData: List<WeatherItem> = emptyList()

    private lateinit var snapHelper: SnapHelper

    private val defaultZipCode = "16802"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupListeners()
        initializeWeatherController()
        loadDefaultWeatherData()

        dataConverter = WeatherDataConverter
        snapHelper = PagerSnapHelper()

        recommendationController =
            RecommendationController(this, createRecommendationWeatherCallback())

        binding.generateRandActButton.setOnClickListener {
            val weatherData = weatherAdapter?.() // need to incorporate a different function here for weatherdata

            if (!weatherData.isNullOrEmpty()) {
                val firstWeatherItem = weatherData.first()
                val temperature = firstWeatherItem.temperature?.removeSuffix("°F")?.toDoubleOrNull() ?: 0.0
                val weatherCondition = firstWeatherItem.weatherCondition ?: "Unknown"

                recommendationController.displayRecommendation(weatherData, temperature, weatherCondition)
            } else {

                showToast("Weather data not available")
            }
        }
    }

    private fun loadDefaultWeatherData() {
        weatherController.fetchWeatherForecast(defaultZipCode, BuildConfig.API_KEY, "imperial")
    }

    private fun setupToolbar() {
        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun setupListeners() {
        setupZipcodeListener()
        setupLogoutListener()
    }

    private fun setupZipcodeListener() {
        binding.zipcodeEnterButton.setOnClickListener {
            handleZipcodeEntry()
        }
    }

    private fun handleZipcodeEntry() {
        val zipcodeEditText: EditText = findViewById<TextInputEditText>(R.id.zipcode_editTextNumber)
        val enteredPostalCode = zipcodeEditText.text.toString()
        weatherController.fetchWeatherForecast(enteredPostalCode, BuildConfig.API_KEY, "imperial")
    }


    private fun setupLogoutListener() {
        binding.logoutButton.setOnClickListener {
            showLogoutMsg()
        }
    }

    fun setupTabLayout(itemCount: Int) {
        val tabLayout = binding.tabDots
        tabLayout.removeAllTabs()
        for (i in 0 until itemCount) {
            tabLayout.addTab(tabLayout.newTab())
        }
    }

    private fun initializeWeatherController() {
        retrofit = RetrofitInstance.retrofit
        weatherService = retrofit.create(WeatherService::class.java)
        val weatherRepository = WeatherRepository(weatherService)
        weatherController = WeatherController(weatherRepository, createWeatherCallback())
    }


    private fun createWeatherCallback() = object : WeatherController.WeatherCallback {
        override fun onSuccess(weatherData: List<WeatherItem>) {
            runOnUiThread {
                        // Process and round temperatures here
                val processedData = dataConverter.processAndRoundTemperatures(weatherData)

                val dailyData = dataConverter.aggregateWeatherDataByDay(processedData)
                setupRecyclerView(dailyData) // Initialize RecyclerView with API data
                setupTabLayout(dailyData.size)

                val temperature =
                    weatherData.firstOrNull()?.temperature?.removeSuffix("°F")?.toDoubleOrNull() ?: 0.0 //added start
                val weatherCondition = weatherData.firstOrNull()?.weatherCondition ?: "Unknown"

                val randomRecommendation = getRandomRecommendation(processedData, temperature, weatherCondition)
                recommendationController.onRecommendationReady(temperature, weatherCondition, randomRecommendation)

                displayRecommendation(randomRecommendation, weatherCondition)//added end

                    }
                }

                override fun onError(error: String) {
                    runOnUiThread { showErrorDialog(error) }
                }
    }

    fun displayRecommendation(recommendation: Recommendation.Recommendation) {
        val recommendationTextView: TextView = findViewById(R.id.activity_short_desc_textView)
        val recommendationImageView: ImageView = findViewById(R.id.recommend_activity_imageView)
        val recommendationdesTextView: TextView = findViewById(R.id.editTextTextMultiLine)

        recommendationTextView.text = recommendation.title

        val bitmap = recommendationController.decodeBase64Image(recommendation.base64img)
        if (bitmap != null) {
            recommendationImageView.setImageBitmap(bitmap)
        } else {
            recommendationImageView.setImageResource(R.drawable.lay_in_grass)
        }

        recommendationdesTextView.text = recommendation.description
    }

    private fun initializeRecommendationController() {
        recommendationController = RecommendationController(
            this,
            createRecommendationWeatherCallback()
        )
    }


    private fun createRecommendationWeatherCallback() =
        object : RecommendationController.RecommendationWeatherCallback {
            override fun onRecommendationReady(
                temperature: Double,
                weatherCondition: String,
                randomRecommendation: Recommendation.Recommendation?
            ) {
                runOnUiThread {
                    val weatherData: List<WeatherItem> =
                        randomRecommendation?.let {
                            view.displayRecommendation(it) // need to change for fetching weather data 
                        } ?: emptyList()
                }
            }

            override fun onError(error: String) {
                runOnUiThread {
                    showErrorDialog(error)
                }
            }
        }

    private fun createOnScrollListener() = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val position = layoutManager.findFirstCompletelyVisibleItemPosition()
            if (position != RecyclerView.NO_POSITION) {
                binding.tabDots.selectTab(binding.tabDots.getTabAt(position))
            }
        }
    }

    private fun setupRecyclerView(weatherItems: List<WeatherItem>) {
        weatherAdapter = WeatherAdapter(weatherItems)
        binding.horizontalCardRecyclerview.apply {
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = weatherAdapter

            // Attach SnapHelper
            snapHelper.attachToRecyclerView(this)
        }

        binding.horizontalCardRecyclerview.addOnScrollListener(createOnScrollListener())
    }

    private fun showLogoutMsg() {
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Logout")
        builder.setMessage("You have successfully logged out.")

        builder.setPositiveButton("OK") { dialog, _ ->
            val intent = Intent(this, LoginActivity::class.java)
            dialog.dismiss()
            startActivity(intent)
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun showErrorDialog(errorMessage: String) {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Error")
        alertDialogBuilder.setMessage(errorMessage)
        alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = alertDialogBuilder.create()
        dialog.show()
    }




    fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    fun updateTabLayout(size: Int) {
        runOnUiThread {
            val tabLayout = binding.tabDots
            tabLayout.removeAllTabs()

            for (i in 0 until size) {
                tabLayout.addTab(tabLayout.newTab())
            }
        }

    }
}