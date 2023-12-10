package com.example.mainactivity.activities

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.mainactivity.BuildConfig
import com.example.mainactivity.R
import com.example.mainactivity.adapters.WeatherAdapter
import com.example.mainactivity.adapters.WeatherFacultyAdapter
import com.example.mainactivity.api.WeatherService
import com.example.mainactivity.controller.WeatherController
import com.example.mainactivity.databinding.ActivityFacultyBinding
import com.example.mainactivity.model.WeatherFacultyItem
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

class FacultyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFacultyBinding
    private lateinit var weatherController: WeatherController
    private lateinit var weatherFacultyAdapter: WeatherFacultyAdapter
    private lateinit var weatherService: WeatherService
    private lateinit var retrofit: Retrofit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFacultyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set custom toolbar as the support action bar
        setSupportActionBar(binding.toolbarFaculty)

        // Add logic for the toolbar logout button here:
        binding.logoutButtonFaculty.setOnClickListener {
            // Handle the logout logic here
        }

        // Remove the title text from the action bar
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val currentDate = LocalDate.now()
        val currentDateFormatted = currentDate.format(DateTimeFormatter.ofPattern("MMM d, E"))
        val weatherFacultyItems = getDefaultWeatherFacultyData(currentDateFormatted)

        binding.zipcodeFacultyEnterButton.setOnClickListener {
            val zipcodeEditText: EditText =
                findViewById<TextInputEditText>(R.id.zipcode_faculty)
            val enteredPostalCode = zipcodeEditText.text.toString()
            val apiKey = BuildConfig.API_KEY
            weatherController.fetchWeatherForecast(
                enteredPostalCode,
                apiKey,
                "imperial"
            )
        }

        weatherFacultyAdapter = WeatherFacultyAdapter(weatherFacultyItems)
        // Set up the RecyclerView with a horizontal LinearLayoutManager and an adapter
        binding.horizontalFacultyCardRecyclerview.apply {
            layoutManager = LinearLayoutManager(this@FacultyActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = WeatherFacultyAdapter(weatherFacultyItems)

            // Attach the PagerSnapHelper to enable snapping behavior
            val snapHelper = PagerSnapHelper()
            snapHelper.attachToRecyclerView(this)
        }

        // Initialize the TabLayout and add a tab for each item in the RecyclerView
        val tabLayout = binding.tabDotsFaculty
        for (i in weatherFacultyItems.indices) {
            tabLayout.addTab(tabLayout.newTab())
        }

        // Add an OnScrollListener to the RecyclerView to update the selected tab
        binding.horizontalFacultyCardRecyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val position = layoutManager.findFirstCompletelyVisibleItemPosition()
                if (position != RecyclerView.NO_POSITION) {
                    tabLayout.selectTab(tabLayout.getTabAt(position))
                }
            }
        })

        retrofit = RetrofitInstance.retrofit
        weatherService = retrofit.create(WeatherService::class.java)

        val weatherRepository = WeatherRepository(weatherService)
        weatherController =
            WeatherController(weatherRepository, object : WeatherController.WeatherCallback {
                override fun onSuccess(weatherData: List<WeatherItem>) {
                    runOnUiThread {
                        weatherFacultyAdapter.updateData(weatherFacultyItems)
                    }
                }

                override fun onError(error: String) {
                    runOnUiThread {
                        showErrorDialog(error)
                    }
                }
            })

        binding.logoutButtonFaculty.setOnClickListener {
            showLogoutMsg()
        }
    }

    private fun getDefaultWeatherFacultyData(currentDateFormatted: String): List<WeatherFacultyItem> {
        val currentDate = Date()
        val dateFormat = SimpleDateFormat("MMM dd, yy", Locale.US)
        val weatherFacultyItems = mutableListOf<WeatherFacultyItem>()

        val calendar = Calendar.getInstance()
        calendar.time = currentDate

        for (i in 0 until 5) {
            val currentDateFormatted = dateFormat.format(calendar.time)
            weatherFacultyItems.add(
                WeatherFacultyItem(
                    "Erie",
                    currentDateFormatted,
                    R.drawable.therm_icon_transparent,
                    "35°F",
                    R.drawable.sunny,
                    "42°F",
                    "30°F",
                    "0%",
                    "69",
                    "7.07",
                    "90"
                )
            )
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        return weatherFacultyItems
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
