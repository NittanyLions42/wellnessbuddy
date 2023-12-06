package com.example.mainactivity.activities

import android.content.Intent
import android.os.Bundle
import com.example.mainactivity.R
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.mainactivity.adapters.WeatherFacultyAdapter
import com.example.mainactivity.databinding.ActivityFacultyBinding
import com.example.mainactivity.model.WeatherFacultyItem

class FacultyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFacultyBinding

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

        // Create a dataset
        val weatherItems = listOf(
            WeatherFacultyItem("Miami, FL", "Dec 23, Tue", R.drawable.therm_icon_transparent, "21°C",
                R.drawable.weather_icon , "27°C", "19°C" , "0.02 in", "10", "5 mph", "4.6"),
            WeatherFacultyItem("Miami, FL", "Dec 23, Tue", R.drawable.therm_icon_transparent, "21°C",
                R.drawable.weather_icon , "27°C", "19°C" , "0.02 in", "10", "5 mph", "4.6"),
            WeatherFacultyItem("Miami, FL", "Dec 23, Tue", R.drawable.therm_icon_transparent, "21°C",
                R.drawable.weather_icon , "27°C", "19°C" , "0.02 in", "10", "5 mph", "4.6"),
            WeatherFacultyItem("Miami, FL", "Dec 23, Tue", R.drawable.therm_icon_transparent, "21°C",
                R.drawable.weather_icon , "27°C", "19°C" , "0.02 in", "10", "5 mph", "4.6"),
            WeatherFacultyItem("Miami, FL", "Dec 23, Tue", R.drawable.therm_icon_transparent, "21°C",
                R.drawable.weather_icon , "27°C", "19°C" , "0.02 in", "10", "5 mph", "4.6")
        )

        // Set up the RecyclerView with a horizontal LinearLayoutManager and an adapter
        binding.horizontalFacultyCardRecyclerview.apply {
            layoutManager = LinearLayoutManager(this@FacultyActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = WeatherFacultyAdapter(weatherItems)

            // Attach the PagerSnapHelper to enable snapping behavior
            val snapHelper = PagerSnapHelper()
            snapHelper.attachToRecyclerView(this)
        }

        // Initialize the TabLayout and add a tab for each item in the RecyclerView
        val tabLayout = binding.tabDotsFaculty
        for (i in weatherItems.indices) {
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

        binding.logoutButtonFaculty.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
