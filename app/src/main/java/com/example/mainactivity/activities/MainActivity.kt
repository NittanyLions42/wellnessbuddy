package com.example.mainactivity.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button

import android.widget.TextView

import androidx.appcompat.app.AlertDialog

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.mainactivity.R
import com.example.mainactivity.adapters.WeatherAdapter
import com.example.mainactivity.controller.Recommendation
import com.example.mainactivity.databinding.ActivityMainBinding
import com.example.mainactivity.model.WeatherItem

import com.example.mainactivity.activities.Credential
import com.example.mainactivity.activities.dbManager

import com.example.mainactivity.controller.RecommendationController
import com.example.mainactivity.model.network.Temperature

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var recommendationController: RecommendationController
    private lateinit var recommendationTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set custom toolbar as the support action bar
        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)



        // Add logic for the toolbar logout button here:
//        val logoutButton: Button = findViewById(R.id.logoutButton)
//        logoutButton.setOnClickListener {
//            // Handle the logout logic here
//
//        }

        recommendationController = RecommendationController(this)
        recommendationController.loadRecommendationFromJson()


        binding.generateRandActButton.setOnClickListener {
            val temperature = Temperature(75, "F")
            recommendationController.displayRecommendation(temperature)
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
            showLogoutMsg()
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



fun displayRecommendation(randomRecommendation: Recommendation?) {
    if(randomRecommendation != null) {
      val  recommendationTextView: TextView = findViewById(R.id.activity_short_desc_textView) //only for title, description not yet added

        recommendationTextView.text = "${randomRecommendation.title}"

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

}

