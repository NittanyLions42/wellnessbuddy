package com.example.mainactivity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mainactivity.databinding.WeatherCardBinding

class WeatherAdapter(private val dataset: List<WeatherItem>) :
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
            textViewWeather.text = item.weatherDescription
            imageViewWeatherIcon.setImageResource(item.weatherIcon)
            textViewSunriseTime.text = item.sunrise
            textViewPrecipitationInches.text = item.percipitation
            textViewSunsetTime.text = item.sunset
        }
    }

    override fun getItemCount() = dataset.size
}
