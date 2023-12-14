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
/**
 * MainActivity manages the display of weather data and activity suggestion for the student users.
 *
 * **/
class MainActivity : AppCompatActivity(), WeatherController.WeatherCallback {
    // Binding for accessing the views
    private lateinit var binding: ActivityMainBinding
    // Controller for handling weather data logic
    private lateinit var weatherController: WeatherController
    // Adapter for populating the RecyclerView with weather data
    private lateinit var weatherAdapter: WeatherAdapter
    // Service for making weather API calls
    private lateinit var weatherService: WeatherService
    // Utility for converting weather data to 3 hour avg for a series of five days
    private lateinit var dataConverter : WeatherDataConverter
    // Retrofit instance for network requests
    private lateinit var retrofit: Retrofit
    // Helper for page snapping in RecyclerView
    private lateinit var snapHelper: SnapHelper
    // Controller for handling activity suggestion
    private lateinit var activitySuggestionController: ActivitySuggestionController

    // List of current weather data
    private var currentWeatherData: List<WeatherItem> = listOf()
    // Default zipcode of Penn State University which is used to display default weather/activity
    private val defaultZipCode = "16802"

    /**
     * Called when the activity is starting.
     * **/
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

    /**
     * Loads default weather data based on a predefined zipcode
     * **/
    private fun loadDefaultWeatherData() {
        lifecycleScope.launch {
            try {
                weatherController.fetchWeatherForecast(defaultZipCode, BuildConfig.API_KEY, "imperial")
            } catch (e: Exception) {
                showErrorDialog("Failed to load default weather data: ${e.message}")
            }
        }
    }

    /**
     * Sets up custom toolbar display
     * **/
    private fun setupToolbar() {
        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    /**
     * Sets up listeners for various UI components (zipcode, logout, random activity button)
     * **/
    private fun setupListeners() {
        setupZipcodeListener()
        setupLogoutListener()
        setupRandomActivityButton()
    }

    /**
     * Sets up listener for the zipcode entry button.
     * **/
    private fun setupZipcodeListener() {
        binding.zipcodeEnterButton.setOnClickListener {
            handleZipcodeEntry()
        }
    }

    /**
     * Handle the zipcode entry by the user and fetches weather data for the entered zipcode
     * **/
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

    /**
     * Sets up listener for the logout button.
     * **/
    private fun setupLogoutListener() {
        binding.logoutButton.setOnClickListener {
            showLogoutMsg()
        }
    }

    /**
     * Sets up listener for the random activity generation button.
     * **/
    private fun setupRandomActivityButton() {
        binding.generateRandActButton.setOnClickListener {
            if (currentWeatherData.isNotEmpty()) {
                activitySuggestionController.recommendActivityBasedOnWeather(currentWeatherData)
            } else {
                showErrorDialog("Please fetch weather data first.")
            }
        }
    }

    /**
     * Updates UI activity recommendation based on the current weather.
     * **/
    fun displayActivityRecommendation(recommendation: ActivitySuggestionController.ActivityRecommendation) {
        lifecycleScope.launch {
            binding.activityShortDescTextView.text = recommendation.title
            binding.recommendActivityImageView.setImageResource(recommendation.imageResId)
            binding.editTextTextMultiLine.text = recommendation.description
        }
    }

    /**
     * Implementation of WeatherController callback methods.
     * **/
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

    /**
     * Shows error message if failure to retrieve weather data.
     * **/
    override fun onError(error: String) {
        showErrorDialog("Failed to load weather data: $error")
    }

    /**
     * Shows a logout message to the user.
     * **/
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

    /**
     * Shows an error dialog with a specified message.
     * **/
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