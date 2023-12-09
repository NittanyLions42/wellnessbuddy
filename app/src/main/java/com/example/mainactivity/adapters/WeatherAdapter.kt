package com.example.mainactivity.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mainactivity.model.WeatherItem
import com.example.mainactivity.databinding.WeatherCardBinding

class WeatherAdapter(private var dataset: List<WeatherItem>) :
    RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {

    class WeatherViewHolder(val binding: WeatherCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val binding = WeatherCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherViewHolder(binding)
    }

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

    override fun getItemCount() = dataset.size

    fun updateData(newDataset: List<WeatherItem>) {
        dataset = newDataset
        notifyDataSetChanged()
    }

}
