package com.example.mainactivity.activities

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.mainactivity.BuildConfig
import com.example.mainactivity.R
import com.example.mainactivity.adapters.WeatherFacultyAdapter
import com.example.mainactivity.api.WeatherService
import com.example.mainactivity.controller.FacultyWeatherController
import com.example.mainactivity.databinding.ActivityFacultyBinding
import com.example.mainactivity.model.WeatherFacultyItem
import com.example.mainactivity.model.network.RetrofitInstance
import com.example.mainactivity.repository.WeatherRepository
import com.example.mainactivity.utils.WeatherDataConverter
import retrofit2.Retrofit

/**
 * FacultyActivity manages the display of weather data for the faculty member user. It initializes
 * and manages UI components such as custom toolbar, RecyclerView for weather card display,
 * and handles user interactions.
 * **/
class FacultyActivity : AppCompatActivity() {
    // Binding for accessing the views
    private lateinit var binding: ActivityFacultyBinding
    // Controller for handling weather data logic
    private lateinit var facultyController: FacultyWeatherController
    // Adapter for populating the RecyclerView with weather data
    private lateinit var weatherFacultyAdapter: WeatherFacultyAdapter
    // Utility for converting weather data to 3 hour avg for a series of five days
    private lateinit var dataConverter: WeatherDataConverter
    // Service for making weather API calls
    private lateinit var weatherService: WeatherService
    // Retrofit instance for network requests
    private lateinit var retrofit: Retrofit
    // Helper for page snapping in RecyclerView
    private lateinit var snapHelper: PagerSnapHelper
    // Default zipcode of Penn State University which is used to display default weather/activity
    private val defaultZipCode = "16802"

    /**
     * Called when the activity is starting.
     * **/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFacultyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup methods for UI components
        setupFacultyToolbar()
        setupFacultyListeners()
        initializeFacultyWeatherController()

        dataConverter = WeatherDataConverter
        snapHelper = PagerSnapHelper()
        // Load default weather for when a user first logs in
        loadDefaultFacultyWeatherData()
    }

    /**
     * Initializes the controller for managing weather data.
     * **/
    private fun initializeFacultyWeatherController() {
        retrofit = RetrofitInstance.retrofit
        weatherService = retrofit.create(WeatherService::class.java)
        val weatherRepository = WeatherRepository(weatherService)
        facultyController = FacultyWeatherController(weatherRepository, facultyWeatherCallback)
    }

    /**
     * Sets up the custom toolbar for the activity.
     * **/
    private fun setupFacultyToolbar() {
        val toolbar: Toolbar = binding.toolbarFaculty
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    /**
     * Sets up listeners for user interactions (logout button and zipcode enter button)
     * **/
    private fun setupFacultyListeners() {
        binding.zipcodeFacultyEnterButton.setOnClickListener {
            handleZipcodeEntry()
        }
        binding.logoutButtonFaculty.setOnClickListener {
            showLogoutMsg()
        }
    }
    /**
     * Handles user input for zipcode entry and fetches weather data accordingly
     * **/
    private fun handleZipcodeEntry() {
        val zipcodeFacultyEditText: EditText = findViewById(R.id.zipcode_faculty)
        val enteredPostalCode = zipcodeFacultyEditText.text.toString()
        facultyController.fetchFacultyWeatherForecast(enteredPostalCode, BuildConfig.API_KEY, "imperial")
    }

    /**
     * Loads default weather data based on a predefined zip code
     * **/
    private fun loadDefaultFacultyWeatherData() {
        facultyController.fetchFacultyWeatherForecast(defaultZipCode, BuildConfig.API_KEY, "imperial")
    }

    /**
     * Callback implementation for handling data response
     * **/
    private val facultyWeatherCallback = object : FacultyWeatherController.FacultyWeatherCallback {
        override fun onSuccess(weatherFacultyData: List<WeatherFacultyItem>) {
            runOnUiThread {
                // Check that there is data, otherwise show error dialog
                if (weatherFacultyData.isNotEmpty()) {
                    val processedData = dataConverter.processAndRoundWeatherFacultyData(weatherFacultyData)

                    val dailyData = dataConverter.aggregateWeatherFacultyDataByDay(processedData)

                    setupRecyclerView(dailyData)
                    setupTabLayout(dailyData.size)
                } else {
                    showErrorDialog("No data available")
                }
            }
        }

        override fun onError(error: String) {
            runOnUiThread { showErrorDialog(error) }
        }
    }

    /**
     * Sets up the RecyclerView for displaying weather data to user.
     * **/
    private fun setupRecyclerView(weatherFacultyItems: List<WeatherFacultyItem>) {
        weatherFacultyAdapter = WeatherFacultyAdapter(weatherFacultyItems)
        binding.horizontalFacultyCardRecyclerview.apply {
            layoutManager = LinearLayoutManager(this@FacultyActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = weatherFacultyAdapter
            snapHelper.attachToRecyclerView(this)
        }
        binding.horizontalFacultyCardRecyclerview.addOnScrollListener(createOnScrollListener())
    }

    /**
     * Sets up the TabLayout for pagination in the RecyclerView.
     * **/
    private fun setupTabLayout(itemCount: Int) {
        val tabLayout = binding.tabDotsFaculty
        tabLayout.removeAllTabs()
        for (i in 0 until itemCount) {
            tabLayout.addTab(tabLayout.newTab())
        }
    }

    /**
     * Creates and returns a scroll listener for the RecyclerView.
     * This listener updates the TabLayout based on the current position.
     * **/
    private fun createOnScrollListener() = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val position = layoutManager.findFirstCompletelyVisibleItemPosition()
            if (position != RecyclerView.NO_POSITION) {
                binding.tabDotsFaculty.selectTab(binding.tabDotsFaculty.getTabAt(position))
            }
        }
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
}
