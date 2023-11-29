package com.example.mainactivity.repository

import com.example.mainactivity.exception.BadRequestException
import com.example.mainactivity.exception.InternalErrorException
import com.example.mainactivity.exception.NotFoundException
import com.example.mainactivity.model.network.*
import com.example.mainactivity.network.network.LocationResponse
import com.example.mainactivity.utils.Logger

/**
 * Repository class responsible for interacting with the Weather API service
 * and handling weather-related data retrieval and error handling.
 *
 * @param weatherService The Retrofit service interface for making API requests.
 * @param logger The logger for logging errors and messages.
 */
class WeatherRepository(private val weatherService: WeatherService, private val logger: Logger) {

    private val apiKey = "API_KEY"

    /**
     * Retrieve the location key for a given postal code.
     *
     * @param postalCode The postal code for which to retrieve the location key.
     * @return The [LocationResponse] containing location information.
     * @throws BadRequestException if the request is malformed or the postal code is invalid.
     * @throws NotFoundException if the requested resource is not found.
     * @throws InternalErrorException if there is an internal server error.
     * @throws Exception for unknown errors.
     */
    suspend fun getLocationKey(postalCode: String): LocationResponse {
        // TODO: This function will be changed based on API call limit set for UI
        val response = weatherService.getLocationsKey(apiKey, postalCode, "en-us", false)
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("No body in response")
        } else {
            handleErrors(response.code())
        }
    }

    /**
     * Retrieve a five-day weather forecast for a given location key.
     *
     * @param locationKey The location key for which to retrieve the weather forecast.
     * @return The [ForecastResponse] containing the five-day weather forecast.
     * @throws BadRequestException if the request is malformed or the location key is invalid.
     * @throws NotFoundException if the requested resource is not found.
     * @throws InternalErrorException if there is an internal server error.
     * @throws Exception for unknown errors.
     */
    suspend fun getFiveDayForecast(locationKey: String): ForecastResponse {
        // TODO: This function will be changed based on API call limit set for UI
        val response = weatherService.getFiveDayForecast(locationKey, apiKey, "en-us", true, false)
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("No body in response")
        } else {
            handleErrors(response.code())
        }
    }

    /**
     * Retrieve a twelve-hour weather forecast for a given location key.
     *
     * @param locationKey The location key for which to retrieve the weather forecast.
     * @return A list of [HourlyForecastResponse] containing the twelve-hour weather forecast.
     * @throws BadRequestException if the request is malformed or the location key is invalid.
     * @throws NotFoundException if the requested resource is not found.
     * @throws InternalErrorException if there is an internal server error.
     * @throws Exception for unknown errors.
     */
    suspend fun getTwelveHourForecast(locationKey: String): List<HourlyForecastResponse> {
        // TODO: This function will be changed based on API call limit set for UI
        val response = weatherService.getTwelveHourForecast(locationKey, apiKey, "en-us", false)
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("No body in response")
        } else {
            handleErrors(response.code())
        }
    }

    private fun handleErrors(code: Int): Nothing {
        when (code) {
            400 -> throw BadRequestException(400, "Invalid parameter or bad request.")
            404 -> throw NotFoundException(404, "Resource not found.")
            500 -> throw InternalErrorException(500, "Internal server error.")
            else -> {
                logger.e("WeatherRepository", "Unknown error occurred: HTTP $code")
                throw Exception("Unknown error occurred: HTTP $code")
            }
        }
    }
}

