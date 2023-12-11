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
import com.example.mainactivity.adapters.WeatherAdapter
import com.example.mainactivity.api.WeatherService
import com.example.mainactivity.controller.WeatherController
import com.example.mainactivity.databinding.ActivityMainBinding
import com.example.mainactivity.model.WeatherItem
import com.example.mainactivity.model.network.RetrofitInstance
import com.example.mainactivity.repository.WeatherRepository
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Retrofit
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var weatherController: WeatherController
    private lateinit var weatherAdapter: WeatherAdapter
    private lateinit var weatherService: WeatherService
    private lateinit var retrofit: Retrofit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupListeners()
        initializeWeatherData()
        setupListeners()
        initializeWeatherController()

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

    private fun setupTabLayout(itemCount: Int) {
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
                weatherAdapter.updateData(weatherData)
                setupTabLayout(weatherData.size)
                updateTabLayout(weatherData.size)
            }
        }

        override fun onError(error: String) {
            runOnUiThread { showErrorDialog(error) }
        }
    }

    private fun initializeWeatherData() {
        val currentDateFormatted = LocalDate.now().format(DateTimeFormatter.ofPattern("MMM d, E"))
        val weatherItems = getDefaultWeatherData(currentDateFormatted)
        setupRecyclerView(weatherItems)
        setupTabLayout(weatherItems.size)
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

    private fun updateTabLayout(size: Int) {
        val tabLayout = binding.tabDots
        tabLayout.removeAllTabs()
        for (i in 0 until size) {
            tabLayout.addTab(tabLayout.newTab())
        }
    }

    private fun setupRecyclerView(weatherItems: List<WeatherItem>) {
        weatherAdapter = WeatherAdapter(weatherItems)
        binding.horizontalCardRecyclerview.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = weatherAdapter
            PagerSnapHelper().attachToRecyclerView(this)
        }
        binding.horizontalCardRecyclerview.addOnScrollListener(createOnScrollListener())
        updateTabLayout(weatherItems.size)
    }

    private fun getDefaultWeatherData(currentDateFormatted: String): List<WeatherItem> {
        val currentDate = Date()
        val dateFormat = SimpleDateFormat("MMM dd, yy", Locale.US)
        val weatherItems = mutableListOf<WeatherItem>()

        val calendar = Calendar.getInstance()
        calendar.time = currentDate

        for (i in 0 until 5) {
            val currentDateFormatted = dateFormat.format(calendar.time)
            weatherItems.add(
                WeatherItem(
                    "Erie",
                    currentDateFormatted,
                    R.drawable.therm_icon_transparent,
                    "40°F",
                    R.drawable.sunny,
                    "42°F",
                    "30°F",
                    "0%"
                )
            )
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        return weatherItems
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
}

