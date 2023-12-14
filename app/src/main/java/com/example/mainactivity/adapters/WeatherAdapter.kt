package com.example.mainactivity.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mainactivity.model.WeatherFacultyItem
import com.example.mainactivity.databinding.WeatherCardBinding
import com.example.mainactivity.model.WeatherItem
import com.example.mainactivity.utils.Logger
/**
 * Adapter class for RecyclerView to display weather data. Specifically for the user activity.
 * @param dataset List of WeatherItem objects to be displayed.
 * **/
class WeatherAdapter(private var dataset: List<WeatherItem>) :
    RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {

    /**
     * ViewHolder class for weather items holds the binding to the layout.
     * @param binding Binding for the individual student weather card.
     * **/
    class WeatherViewHolder(val binding: WeatherCardBinding) : RecyclerView.ViewHolder(binding.root)

    /**
     * Creates new ViewHolder instances for the RecyclerView.
     * @param parent The ViewGroup which the new view will be added.
     * @param viewType The view type of the new view.
     * @return A new WeatherViewHolder that holds the WeatherCardBinding.
     * **/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val binding = WeatherCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherViewHolder(binding)
    }

    /**
     * Binds the data at the specified position to the ViewHolder.
     * @param holder The ViewHolder which should be updated.
     * @param position The position of the item within the adapter's data set.
     * **/
    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        with(holder.binding) {
            val item = dataset[position]
            textViewCity.text = item.city
            textViewDate.text = item.date
            imageViewTemperatureIcon.setImageResource(item.temperatureIcon)
            textViewWeather.text = item.temperature
            imageViewWeatherIcon.setImageResource(item.weatherIcon)
            textViewHighTempValue.text = item.highTemp
            textViewLowTempValue.text = item.lowTemp
            textViewPrecipitationValue.text = item.precipitation
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
    fun updateData(newDataset: List<WeatherItem>) {
        Logger.d("WeatherAdapter", "Updating data with ${newDataset.size} items")

        dataset = newDataset
        notifyDataSetChanged()
    }
}
