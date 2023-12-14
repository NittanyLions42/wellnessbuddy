package com.example.mainactivity.activities

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.EditText
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
import com.example.mainactivity.controller.ActivitySuggestionController
import com.example.mainactivity.controller.WeatherController
import com.example.mainactivity.databinding.ActivityMainBinding
import com.example.mainactivity.model.WeatherItem
import com.example.mainactivity.model.network.RetrofitInstance
import com.example.mainactivity.repository.WeatherRepository
import com.example.mainactivity.utils.WeatherDataConverter
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import androidx.lifecycle.lifecycleScope

class MainActivity : AppCompatActivity(), WeatherController.WeatherCallback {
    private lateinit var binding: ActivityMainBinding
    private lateinit var weatherController: WeatherController
    private lateinit var weatherAdapter: WeatherAdapter
    private lateinit var weatherService: WeatherService
    private lateinit var dataConverter : WeatherDataConverter
    private lateinit var retrofit: Retrofit

    private lateinit var snapHelper: SnapHelper

    private lateinit var activitySuggestionController: ActivitySuggestionController

    private var currentWeatherData: List<WeatherItem> = listOf()

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

        activitySuggestionController = ActivitySuggestionController(this)

    }

    private fun loadDefaultWeatherData() {
        lifecycleScope.launch {
            try {
                weatherController.fetchWeatherForecast(defaultZipCode, BuildConfig.API_KEY, "imperial")
            } catch (e: Exception) {
                showErrorDialog("Failed to load default weather data: ${e.message}")
            }
        }
    }

    private fun setupToolbar() {
        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun setupListeners() {
        setupZipcodeListener()
        setupLogoutListener()
        setupRandomActivityButton()
    }

    private fun setupZipcodeListener() {
        binding.zipcodeEnterButton.setOnClickListener {
            handleZipcodeEntry()
        }
    }

    private fun handleZipcodeEntry() {
        val zipcodeEditText: EditText = findViewById<TextInputEditText>(R.id.zipcode_editTextNumber)
        val enteredPostalCode = zipcodeEditText.text.toString()

        if (enteredPostalCode.isBlank()) {
            showErrorDialog("Please enter a valid ZIP code.")
            return
        }

        lifecycleScope.launch {
            try {
                weatherController.fetchWeatherForecast(enteredPostalCode, BuildConfig.API_KEY, "imperial")
            } catch (e: Exception) {
                showErrorDialog("Failed to load weather data for ZIP code: ${e.message}")
            }
        }
    }

    private fun setupLogoutListener() {
        binding.logoutButton.setOnClickListener {
            showLogoutMsg()
        }
    }

    private fun setupRandomActivityButton() {
        binding.generateRandActButton.setOnClickListener {
            if (currentWeatherData.isNotEmpty()) {
                activitySuggestionController.recommendActivityBasedOnWeather(currentWeatherData)
            } else {
                showErrorDialog("Please fetch weather data first.")
            }
        }
    }

    fun displayActivityRecommendation(recommendation: ActivitySuggestionController.ActivityRecommendation) {
        lifecycleScope.launch {
            binding.activityShortDescTextView.text = recommendation.title
            binding.recommendActivityImageView.setImageResource(recommendation.imageResId)
            binding.editTextTextMultiLine.text = recommendation.description
        }
    }

    // WeatherController callback methods
    override fun onSuccess(weatherData: List<WeatherItem>) {
        currentWeatherData = WeatherDataConverter.processAndRoundTemperatures(weatherData)
        val aggregatedData = WeatherDataConverter.aggregateWeatherDataByDay(currentWeatherData)

        runOnUiThread {
            setupRecyclerView(aggregatedData) // Initialize RecyclerView with processed and aggregated data
            setupTabLayout(aggregatedData.size) // Set up TabLayout based on the aggregated data size
        }

        // Generate activity recommendation based on the new weather data
        if (aggregatedData.isNotEmpty()) {
            activitySuggestionController.recommendActivityBasedOnWeather(aggregatedData)
        }
    }

    override fun onError(error: String) {
        showErrorDialog("Failed to load weather data: $error")
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

    fun showErrorDialog(errorMessage: String) {
        AlertDialog.Builder(this)
            .setTitle("Error")
            .setMessage(errorMessage)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun initializeWeatherController() {
        retrofit = RetrofitInstance.retrofit
        weatherService = retrofit.create(WeatherService::class.java)
        val weatherRepository = WeatherRepository(weatherService)
        weatherController = WeatherController(weatherRepository, this)
    }

    private fun setupRecyclerView(weatherItems: List<WeatherItem>) {
        weatherAdapter = WeatherAdapter(weatherItems)
        binding.horizontalCardRecyclerview.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = weatherAdapter
            snapHelper.attachToRecyclerView(this)
        }
        binding.horizontalCardRecyclerview.addOnScrollListener(createOnScrollListener())
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

    private fun setupTabLayout(itemCount: Int) {
        val tabLayout = binding.tabDots
        tabLayout.removeAllTabs()
        for (i in 0 until itemCount) {
            tabLayout.addTab(tabLayout.newTab())
        }
    }



}