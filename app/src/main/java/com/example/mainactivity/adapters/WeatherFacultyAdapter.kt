package com.example.mainactivity.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mainactivity.databinding.WeatherCardFacultyBinding
import com.example.mainactivity.model.WeatherFacultyItem
import com.example.mainactivity.utils.Logger
/**
 * Adapter class for RecyclerView to display weather data. Specifically for the faculty activity
 * @param dataset List of WeatherFacultyItem objects to be displayed.
 * **/
class WeatherFacultyAdapter(private var dataset: List<WeatherFacultyItem>) :
    RecyclerView.Adapter<WeatherFacultyAdapter.WeatherViewHolder>() {
    /**
     * ViewHolder class for weather items holds the binding to the layout.
     * @param binding Binding for the individual faculty weather card.
     * **/
    class WeatherViewHolder(val binding: WeatherCardFacultyBinding) : RecyclerView.ViewHolder(binding.root)
    /**
     * Creates new ViewHolder instances for the RecyclerView.
     * @param parent The ViewGroup which the new view will be added.
     * @param viewType The view type of the new view.
     * @return A new WeatherViewHolder that holds the WeatherCardFacultyBinding.
     * **/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val binding = WeatherCardFacultyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherViewHolder(binding)
    }
    /**
     * Binds the faculty-specific data at the specified position to the ViewHolder.
     * @param holder The ViewHolder which should be updated.
     * @param position The position of the item within the adapter's data set.
     * **/
    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        with(holder.binding) {
            val item = dataset[position]
            cityFaculty.text = item.city
            dateFaculty.text = item.date
            temperatureIconFaculty.setImageResource(item.temperatureIcon)
            weatherValueFaculty.text = item.temperature
            weatherIconFaculty.setImageResource(item.weatherIcon)
            highTempValueFaculty.text = item.highTemp
            lowTempValueFaculty.text = item.lowTemp
            precipitationValueFaculty.text = item.precipitation
            cloudCoverValueFaculty.text = item.cloudCover
            windSpeedValueFaculty.text = item.windSpeed
            humidityValueFaculty.text = item.humidity
        }
    }
    /**
     * Returns the total number of items in the data set held by the adapter.
     * @return The total number of items in this adapter.
     * **/
    override fun getItemCount(): Int {
        return dataset.size
    }
    /**
     * Updates the adapter's dataset and refreshes and RecyclerView.
     * @param newDataset The new data set to be displayed.
     * **/
    fun updateData(newDataset: List<WeatherFacultyItem>) {
        Logger.d("WeatherFacultyAdapter", "Updating data with ${newDataset.size} items")

        dataset = newDataset
        notifyDataSetChanged()
    }
}
