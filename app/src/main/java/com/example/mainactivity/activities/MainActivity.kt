package com.example.mainactivity.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
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
import com.example.mainactivity.controller.WeatherController
import com.example.mainactivity.databinding.ActivityMainBinding
import com.example.mainactivity.model.WeatherItem
import com.example.mainactivity.model.network.RetrofitInstance
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var weatherController: WeatherController
    private lateinit var weatherAdapter: WeatherAdapter
    private lateinit var retrofit: RetrofitInstance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set custom toolbar as the support action bar
        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        // Add logic for the toolbar logout button here:
        val logoutButton: Button = findViewById(R.id.logoutButton)
        logoutButton.setOnClickListener {
            showLogoutMsg()
        }

        // Remove the title text from the action bar
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // Calculate the current date dynamically
        val currentDate = LocalDate.now()
        val currentDateFormatted = currentDate.format(DateTimeFormatter.ofPattern("MMM d, E"))
        val weatherItems = getDefaultWeatherData(currentDateFormatted)

        weatherAdapter = WeatherAdapter(weatherItems)
        // Set up the RecyclerView with a horizontal LinearLayoutManager and an adapter
        binding.horizontalCardRecyclerview.apply {
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = WeatherAdapter(weatherItems)

            // Attach the PagerSnapHelper to enable snapping behavior
            val snapHelper = PagerSnapHelper()
            snapHelper.attachToRecyclerView(this)

            val zipcodeEnterButton: Button = findViewById(R.id.zipcode_enter_button)
            zipcodeEnterButton.setOnClickListener {
                val zipcodeEditText: EditText = findViewById(R.id.zipcode_editTextNumber)
                val enteredPostalCode = zipcodeEditText.text.toString()

                val apiKey = BuildConfig.API_KEY
                // Call the WeatherController to fetch weather data
                weatherController.fetchWeatherForecast(
                    enteredPostalCode,
                    apiKey,
                    "imperial"
                )
            }

            // Initialize the TabLayout and add a tab for each item in the RecyclerView
            val tabLayout = binding.tabDots
            for (i in weatherItems.indices) {
                tabLayout.addTab(tabLayout.newTab())
            }

            binding.logoutButton.setOnClickListener {
                showLogoutMsg()
            }

            // Add an OnScrollListener to the RecyclerView to update the selected tab
            binding.horizontalCardRecyclerview.addOnScrollListener(object :
                RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val position = layoutManager.findFirstCompletelyVisibleItemPosition()
                    if (position != RecyclerView.NO_POSITION) {
                        tabLayout.selectTab(tabLayout.getTabAt(position))
                    }
                }
            })
        }
    }

    private fun getDefaultWeatherData(currentDateFormatted: String): List<WeatherItem> {
        // Create and return a default dataset here
        return listOf(
            WeatherItem(
                "Erie",
                currentDateFormatted,
                R.drawable.therm_icon_transparent,
                "57°F",
                R.drawable.rainy,
                "39°F",
                "33°F",
                "50%"),
            WeatherItem(
                "Erie",
                currentDateFormatted,
                R.drawable.therm_icon_transparent,
                "40°F",
                R.drawable.partly_clear_snow,
                "37°F",
                "29°F",
                "60%"),
            WeatherItem(
                "Erie",
                currentDateFormatted,
                R.drawable.therm_icon_transparent,
                "40°F",
                R.drawable.sunny,
                "42°F",
                "30°F",
                "0%"),
            WeatherItem(
                "Erie",
                currentDateFormatted,
                R.drawable.therm_icon_transparent,
                "38°F",
                R.drawable.sunny,
                "38°F",
                "30°F",
                "0%"),
            WeatherItem(
                "Erie",
                currentDateFormatted,
                R.drawable.therm_icon_transparent,
                "40°F",
                R.drawable.sunny,
                "42°F",
                "34°F",
                "0%")
        )
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
}

