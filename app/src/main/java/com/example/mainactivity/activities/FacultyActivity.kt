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

class FacultyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFacultyBinding
    private lateinit var facultyController: FacultyWeatherController
    private lateinit var weatherFacultyAdapter: WeatherFacultyAdapter
    private lateinit var dataConverter: WeatherDataConverter
    private lateinit var weatherService: WeatherService
    private lateinit var retrofit: Retrofit
    private lateinit var snapHelper: PagerSnapHelper
    private val defaultZipCode = "16802"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFacultyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupFacultyToolbar()
        setupFacultyListeners()
        initializeFacultyWeatherController()

        dataConverter = WeatherDataConverter
        snapHelper = PagerSnapHelper()
        loadDefaultFacultyWeatherData()
    }

    private fun initializeFacultyWeatherController() {
        retrofit = RetrofitInstance.retrofit
        weatherService = retrofit.create(WeatherService::class.java)
        val weatherRepository = WeatherRepository(weatherService)
        facultyController = FacultyWeatherController(weatherRepository, facultyWeatherCallback)
    }

    private fun setupFacultyToolbar() {
        val toolbar: Toolbar = binding.toolbarFaculty
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun setupFacultyListeners() {
        binding.zipcodeFacultyEnterButton.setOnClickListener {
            handleZipcodeEntry()
        }
        binding.logoutButtonFaculty.setOnClickListener {
            showLogoutMsg()
        }
    }

    private fun handleZipcodeEntry() {
        val zipcodeFacultyEditText: EditText = findViewById(R.id.zipcode_faculty)
        val enteredPostalCode = zipcodeFacultyEditText.text.toString()
        facultyController.fetchFacultyWeatherForecast(enteredPostalCode, BuildConfig.API_KEY, "imperial")
    }

    private fun loadDefaultFacultyWeatherData() {
        facultyController.fetchFacultyWeatherForecast(defaultZipCode, BuildConfig.API_KEY, "imperial")
    }

    private val facultyWeatherCallback = object : FacultyWeatherController.FacultyWeatherCallback {
        override fun onSuccess(weatherFacultyData: List<WeatherFacultyItem>) {
            runOnUiThread {
                if (weatherFacultyData.isNotEmpty()) {
                    val processedData = dataConverter.processAndRoundWeatherFacultyData(weatherFacultyData)

                    val dailyData = dataConverter.aggregateWeatherFacultyDataByDay(processedData)

                    setupRecyclerView(dailyData)
                    setupTabLayout(dailyData.size) // update the tabs
                } else {
                    showErrorDialog("No data available")
                }
            }
        }

        override fun onError(error: String) {
            runOnUiThread { showErrorDialog(error) }
        }
    }

    private fun setupRecyclerView(weatherFacultyItems: List<WeatherFacultyItem>) {
        weatherFacultyAdapter = WeatherFacultyAdapter(weatherFacultyItems)
        binding.horizontalFacultyCardRecyclerview.apply {
            layoutManager = LinearLayoutManager(this@FacultyActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = weatherFacultyAdapter
            snapHelper.attachToRecyclerView(this)
        }
    }

    private fun setupTabLayout(itemCount: Int) {
        val tabLayout = binding.tabDotsFaculty
        tabLayout.removeAllTabs()
        for (i in 0 until itemCount) {
            tabLayout.addTab(tabLayout.newTab())
        }
    }

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
