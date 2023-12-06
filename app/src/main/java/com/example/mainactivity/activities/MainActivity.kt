package com.example.mainactivity.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.mainactivity.R
import com.example.mainactivity.model.WeatherItem
import com.example.mainactivity.adapters.WeatherAdapter
import com.example.mainactivity.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

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
            // Handle the logout logic here
        }

        // Remove the title text from the action bar
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // Create a dataset
        val weatherItems = listOf(
            WeatherItem("Miami, FL", "Dec 23, Tue", R.drawable.therm_icon_transparent, "21°C",
                R.drawable.weather_icon , "27°C", "19°C" , "0.02 in"),
            WeatherItem("New York, NY", "Dec 24, Fri", R.drawable.therm_icon_transparent, "11°C",
                R.drawable.weather_icon , "17°C", "10°C" ,"0.20 in"),
            WeatherItem("Seattle, WA", "Oct 26, Thurs", R.drawable.therm_icon_transparent, "6°C",
                R.drawable.weather_icon , "11°C", "3°C" , "0.63 in"),
            WeatherItem("LA, CA", "Dec 28, Wed", R.drawable.therm_icon_transparent, "71°C",
                R.drawable.weather_icon , "78°C", "65°C" ,"20 in"),
            WeatherItem("Miami, FL", "April 23, Sat", R.drawable.therm_icon_transparent, "50°C",
                R.drawable.weather_icon , "58°C", "47","6.3 in")
        )

        // Set up the RecyclerView with a horizontal LinearLayoutManager and an adapter
        binding.horizontalCardRecyclerview.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = WeatherAdapter(weatherItems)

            // Attach the PagerSnapHelper to enable snapping behavior
            val snapHelper = PagerSnapHelper()
            snapHelper.attachToRecyclerView(this)
        }

        // Initialize the TabLayout and add a tab for each item in the RecyclerView
        val tabLayout = binding.tabDots
        for (i in weatherItems.indices) {
            tabLayout.addTab(tabLayout.newTab())
        }

        binding.logoutButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // Add an OnScrollListener to the RecyclerView to update the selected tab
        binding.horizontalCardRecyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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

