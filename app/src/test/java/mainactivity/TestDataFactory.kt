package mainactivity

import com.example.mainactivity.model.network.Wind
import com.example.mainactivity.model.network.City
import com.example.mainactivity.model.network.Clouds
import com.example.mainactivity.model.network.FiveDayForecast
import com.example.mainactivity.model.network.MainDetails
import com.example.mainactivity.model.network.WeatherCondition
import com.example.mainactivity.model.network.WeatherData
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

/**
 * Object containing methods to generate test data for network responses.
 */
object TestDataFactory {
    fun createSampleForecast(): FiveDayForecast {
        return FiveDayForecast(
            cod = "200",
            message = 0,
            cnt = 8, // Updated to include two more days of data (8 entries)
            list = listOf(
                WeatherData(
                    dt = 1702544400,
                    main = MainDetails(
                        temp = 42.44,
                        tempMin = 42.44,
                        tempMax = 42.44,
                        humidity = 94
                    ),
                    weather = listOf(WeatherCondition(main = "Rain")),
                    wind = Wind(speed = 1.39),
                    pop = 1.0,
                    clouds = Clouds(all = 100)
                ),
                WeatherData(
                    dt = 1702555200,
                    main = MainDetails(
                        temp = 42.64,
                        tempMin = 42.64,
                        tempMax = 42.64,
                        humidity = 94
                    ),
                    weather = listOf(WeatherCondition(main = "Rain")),
                    wind = Wind(speed = 2.75),
                    pop = 0.98,
                    clouds = Clouds(all = 100)
                ),
                WeatherData(
                    dt = 1702188000,
                    main = MainDetails(
                        temp = 42.64,
                        tempMin = 42.64,
                        tempMax = 42.64,
                        humidity = 94
                    ),
                    weather = listOf(WeatherCondition(main = "Rain")),
                    wind = Wind(speed = 2.75),
                    pop = 0.98,
                    clouds = Clouds(all = 100)
                ),
                WeatherData(
                    dt = 1702198800,
                    main = MainDetails(
                        temp = 42.64,
                        tempMin = 42.64,
                        tempMax = 42.64,
                        humidity = 94
                    ),
                    weather = listOf(WeatherCondition(main = "Rain")),
                    wind = Wind(speed = 2.75),
                    pop = 0.98,
                    clouds = Clouds(all = 100)
                ),
            ),
            city = City(
                sunrise = 1702136707,
                sunset = 1702167447,
                name = "Bellevue"
            )
        )
    }

    fun createSampleForecastJson(): String {
        val forecast = createSampleForecast()
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val adapter: JsonAdapter<FiveDayForecast> = moshi.adapter(FiveDayForecast::class.java)
        return adapter.toJson(forecast)
    }
}
