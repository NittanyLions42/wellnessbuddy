package com.example.mainactivity.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mainactivity.databinding.WeatherCardFacultyBinding
import com.example.mainactivity.model.WeatherFacultyItem
import com.example.mainactivity.utils.Logger

class WeatherFacultyAdapter(private var dataset: List<WeatherFacultyItem>) :
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

    override fun getItemCount() = dataset.size

    fun updateData(newDataset: List<WeatherFacultyItem>) {
        Logger.d("WeatherFacultyAdapter", "Updating data with ${newDataset.size} items")

        /*
         Since the api only gives temperature update every 3 hours,
         Preserve the existing temperature values in the new dataset.
         */
        newDataset.forEachIndexed { index, newItem ->
            val existingItem = dataset.getOrNull(index)
            newItem.temperature = existingItem?.temperature ?: newItem.temperature
        }
        dataset = newDataset
        notifyDataSetChanged()
    }
}
