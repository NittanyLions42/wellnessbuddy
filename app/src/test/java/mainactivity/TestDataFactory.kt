package mainactivity

import com.example.mainactivity.model.network.AirAndPollen
import com.example.mainactivity.model.network.DailyForecast
import com.example.mainactivity.model.network.DayForecast
import com.example.mainactivity.model.network.ForecastResponse
import com.example.mainactivity.model.network.HourlyForecastResponse
import com.example.mainactivity.model.network.NightForecast
import com.example.mainactivity.model.network.Speed
import com.example.mainactivity.model.network.Temperature
import com.example.mainactivity.model.network.TemperatureInfo
import com.example.mainactivity.model.network.TemperatureValue
import com.example.mainactivity.model.network.Wind
import com.example.mainactivity.network.network.AdministrativeArea
import com.example.mainactivity.network.network.LocationResponse
import com.example.mainactivity.network.network.ParentCity
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

/**
 * Object containing methods to generate test data for network responses.
 */
object TestDataFactory {
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    fun createLocationResponseJson(): String {
        val locationResponse = createLocationResponse()
        return moshi.adapter(LocationResponse::class.java).toJson(locationResponse)
    }

    fun createForecastResponseJson(): String {
        val forecastResponse = createForecastResponse()
        return moshi.adapter(ForecastResponse::class.java).toJson(forecastResponse)
    }

    fun createHourlyForecastJson(): String {
        val hourlyForecastResponseList = createHourlyForecastResponse()

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val listType =
            Types.newParameterizedType(List::class.java, HourlyForecastResponse::class.java)
        val adapter = moshi.adapter<List<HourlyForecastResponse>>(listType)

        return adapter.toJson(hourlyForecastResponseList)
    }


    fun createLocationResponse(
        postalCode: String = "98101",
        stateId: String = "WA",
        locationKey: String = "123456",
        cityName: String = "Seattle"
    ): LocationResponse {
        return LocationResponse(
            postalCode = postalCode,
            administrativeArea = createAdministrativeArea(stateId),
            parentCity = createParentCity(locationKey, cityName)
        )
    }

    private fun createAdministrativeArea(stateId: String): AdministrativeArea {
        return AdministrativeArea(stateId = stateId)
    }

    private fun createParentCity(locationKey: String, cityName: String): ParentCity {
        return ParentCity(locationKey = locationKey, cityName = cityName)
    }

    fun createForecastResponse(): ForecastResponse {
        return ForecastResponse(
            dailyForecasts = listOf(
                createSpecificDailyForecast(
                    date = "2023-11-25",
                    minTemp = 32,
                    maxTemp = 45,
                    tempUnit = "F",
                    uvIndexValue = 2,
                    uvIndexCategory = "Low",
                    airQualityValue = 30,
                    airQualityCategory = "Good",
                    dayIconPhrase = "Sunny",
                    dayHasPrecipitation = false,
                    dayPrecipitationProbability = 0,
                    dayWindValue = 10.0,
                    dayWindUnit = "mi/h",
                    nightIconPhrase = "Clear",
                    nightHasPrecipitation = false,
                    nightPrecipitationProbability = 0,
                    nightWindValue = 5.0,
                    nightWindUnit = "mi/h"
                ),
                createSpecificDailyForecast(
                    date = "2023-11-26",
                    minTemp = 32,
                    maxTemp = 45,
                    tempUnit = "F",
                    uvIndexValue = 2,
                    uvIndexCategory = "Low",
                    airQualityValue = 38,
                    airQualityCategory = "Good",
                    dayIconPhrase = "Rainy",
                    dayHasPrecipitation = true,
                    dayPrecipitationProbability = 30,
                    dayWindValue = 10.0,
                    dayWindUnit = "mi/h",
                    nightIconPhrase = "Rainy",
                    nightHasPrecipitation = true,
                    nightPrecipitationProbability = 60,
                    nightWindValue = 5.0,
                    nightWindUnit = "mi/h"
                ),
                createSpecificDailyForecast(
                    date = "2023-11-27",
                    minTemp = 40,
                    maxTemp = 50,
                    tempUnit = "F",
                    uvIndexValue = 2,
                    uvIndexCategory = "Low",
                    airQualityValue = 30,
                    airQualityCategory = "Good",
                    dayIconPhrase = "Sunny",
                    dayHasPrecipitation = false,
                    dayPrecipitationProbability = 0,
                    dayWindValue = 10.0,
                    dayWindUnit = "mi/h",
                    nightIconPhrase = "Clear",
                    nightHasPrecipitation = false,
                    nightPrecipitationProbability = 0,
                    nightWindValue = 5.0,
                    nightWindUnit = "mi/h"
                ),
                createSpecificDailyForecast(
                    date = "2023-11-28",
                    minTemp = 48,
                    maxTemp = 55,
                    tempUnit = "F",
                    uvIndexValue = 2,
                    uvIndexCategory = "Low",
                    airQualityValue = 30,
                    airQualityCategory = "Good",
                    dayIconPhrase = "Cloudy",
                    dayHasPrecipitation = false,
                    dayPrecipitationProbability = 0,
                    dayWindValue = 10.0,
                    dayWindUnit = "mi/h",
                    nightIconPhrase = "Cloudy",
                    nightHasPrecipitation = false,
                    nightPrecipitationProbability = 0,
                    nightWindValue = 5.0,
                    nightWindUnit = "mi/h"
                ),
                createSpecificDailyForecast(
                    date = "2023-11-29",
                    minTemp = 45,
                    maxTemp = 60,
                    tempUnit = "F",
                    uvIndexValue = 2,
                    uvIndexCategory = "Low",
                    airQualityValue = 30,
                    airQualityCategory = "Good",
                    dayIconPhrase = "Sunny",
                    dayHasPrecipitation = false,
                    dayPrecipitationProbability = 0,
                    dayWindValue = 10.0,
                    dayWindUnit = "mi/h",
                    nightIconPhrase = "Clear",
                    nightHasPrecipitation = false,
                    nightPrecipitationProbability = 0,
                    nightWindValue = 5.0,
                    nightWindUnit = "mi/h"
                ),
            )
        )
    }

    private fun createSpecificDailyForecast(
        date: String,
        minTemp: Int,
        maxTemp: Int,
        tempUnit: String,
        uvIndexValue: Int,
        uvIndexCategory: String,
        airQualityValue: Int,
        airQualityCategory: String,
        dayIconPhrase: String,
        dayHasPrecipitation: Boolean,
        dayPrecipitationProbability: Int,
        dayWindValue: Double,
        dayWindUnit: String,
        nightIconPhrase: String,
        nightHasPrecipitation: Boolean,
        nightPrecipitationProbability: Int,
        nightWindValue: Double,
        nightWindUnit: String
    ): DailyForecast {
        return DailyForecast(
            date = date,
            temperature = TemperatureInfo(
                minimum = TemperatureValue(value = minTemp, unit = tempUnit),
                maximum = TemperatureValue(value = maxTemp, unit = tempUnit)
            ),
            airAndPollen = listOf(
                AirAndPollen(name = "UVIndex", value = uvIndexValue, category = uvIndexCategory),
                AirAndPollen(
                    name = "AirQuality",
                    value = airQualityValue,
                    category = airQualityCategory
                )
            ),
            day = DayForecast(
                iconPhrase = dayIconPhrase,
                hasPrecipitation = dayHasPrecipitation,
                dayPrecipitation = dayPrecipitationProbability,
                wind = Wind(Speed(value = dayWindValue, unit = dayWindUnit))
            ),
            night = NightForecast(
                iconPhrase = nightIconPhrase,
                hasPrecipitation = nightHasPrecipitation,
                nightPrecipitation = nightPrecipitationProbability,
                wind = Wind(Speed(value = nightWindValue, unit = nightWindUnit))
            )
        )
    }

    private fun createTemperatureInfo(): TemperatureInfo {
        return TemperatureInfo(
            minimum = TemperatureValue(value = 25, unit = "F"),
            maximum = TemperatureValue(value = 60, unit = "F")
        )
    }

    private fun createAirAndPollen(
        name: String = "UVIndex",
        value: Int = 5,
        category: String? = "Moderate"
    ): AirAndPollen {
        return AirAndPollen(name = name, value = value, category = category)
    }

    private fun createDayForecast(
        iconPhrase: String = "Sunny",
        hasPrecipitation: Boolean = false,
        precipitationProbability: Int = 0,
        wind: Wind = createWind()
    ): DayForecast {
        return DayForecast(
            iconPhrase = iconPhrase,
            hasPrecipitation = hasPrecipitation,
            dayPrecipitation = precipitationProbability,
            wind = wind
        )
    }

    private fun createWind(
        speed: Speed = Speed(value = 10.0, unit = "km/h")
    ): Wind {
        return Wind(speed = speed)
    }

    fun createHourlyForecastResponse(): List<HourlyForecastResponse> {
        return listOf(
            HourlyForecastResponse(
                dateTime = "2023-11-25T06:00:00-08:00",
                temperature = Temperature(30.0, "F")
            ),
            HourlyForecastResponse(
                dateTime = "2023-11-25T07:00:00-08:00",
                temperature = Temperature(31.0, "F")
            ),
            HourlyForecastResponse(
                dateTime = "2023-11-25T08:00:00-08:00",
                temperature = Temperature(31.0, "F")
            ),
            HourlyForecastResponse(
                dateTime = "2023-11-25T09:00:00-08:00",
                temperature = Temperature(31.0, "F")
            ),
            HourlyForecastResponse(
                dateTime = "2023-11-25T10:00:00-08:00",
                temperature = Temperature(30.0, "F")
            ),
            HourlyForecastResponse(
                dateTime = "2023-11-25T11:00:00-08:00",
                temperature = Temperature(33.0, "F")
            ),
            HourlyForecastResponse(
                dateTime = "2023-11-25T12:00:00-08:00",
                temperature = Temperature(30.0, "F")
            ),
            HourlyForecastResponse(
                dateTime = "2023-11-25T13:00:00-08:00",
                temperature = Temperature(33.0, "F")
            ),
            HourlyForecastResponse(
                dateTime = "2023-11-25T14:00:00-08:00",
                temperature = Temperature(35.0, "F")
            ),
            HourlyForecastResponse(
                dateTime = "2023-11-25T15:00:00-08:00",
                temperature = Temperature(35.0, "F")
            ),
            HourlyForecastResponse(
                dateTime = "2023-11-25T16:00:00-08:00",
                temperature = Temperature(40.0, "F")
            ),
            HourlyForecastResponse(
                dateTime = "2023-11-25T17:00:00-08:00",
                temperature = Temperature(31.0, "F")
            )
        )
    }
}
