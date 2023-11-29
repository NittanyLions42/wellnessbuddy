package com.example.mainactivity.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mainactivity.databinding.WeatherCardFacultyBinding
import com.example.mainactivity.model.WeatherFacultyItem

class WeatherFacultyAdapter(private val dataset: List<WeatherFacultyItem>) :
    RecyclerView.Adapter<WeatherFacultyAdapter.WeatherViewHolder>() {

    class WeatherViewHolder(val binding: WeatherCardFacultyBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val binding = WeatherCardFacultyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        with(holder.binding) {
            val item = dataset[position]
            cityFaculty.text = item.city
            dateFaculty.text = item.date
            temperatureIconFaculty.setImageResource(item.temperatureIcon)
            weatherValueFaculty.text = item.weatherDescription
            weatherIconFaculty.setImageResource(item.weatherIcon)
            highTempValueFaculty.text = item.highTemp
            lowTempValueFaculty.text = item.lowTemp
            precipitationValueFaculty.text = item.percipitation
            maxUvIndexValueFaculty.text = item.maxUvIndex
            windValueFaculty.text = item.wind
            airQualityValueFaculty.text = item.airQuality
        }
    }

    override fun getItemCount() = dataset.size
}
