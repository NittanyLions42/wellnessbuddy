package com.example.mainactivity.repository

import RequestTracker
import com.example.mainactivity.exception.BadRequestException
import com.example.mainactivity.exception.InternalErrorException
import com.example.mainactivity.exception.NotFoundException
import com.example.mainactivity.model.network.*
import com.example.mainactivity.model.network.LocationResponse
import com.example.mainactivity.api.WeatherService
import com.example.mainactivity.utils.Logger

/**
 * Repository class responsible for interacting with the Weather API service
 * and handling weather-related data retrieval and error handling.
 *
 * @param weatherService The Retrofit service interface for making API requests.
 * @param logger The logger for logging errors and messages.
 * @param requestTracker The tracker for managing API call limits.
 */
class WeatherRepository(
    private val weatherService: WeatherService,
    private val logger: Logger,
    private val requestTracker: RequestTracker
) {

    private val apiKey = "API_KEY"

    /**
     * Retrieves the location key for a given postal code while adhering to API call limits.
     *
     * @param postalCode The postal code for which to retrieve the location key.
     * @return The [LocationResponse] containing location information.
     * @throws Exception if the API call limit is reached or if no response body is found.
     */
    suspend fun getLocationKey(postalCode: String): LocationResponse {
        if (requestTracker.canCallLocationKey()) {
            val response = weatherService.getLocationsKey(apiKey, postalCode, "en-us", false)
            if (response.isSuccessful) {
                requestTracker.updateLocationKeyCall()
                return response.body() ?: throw Exception("No body in response")
            } else {
                handleErrors(response.code())
            }
        } else {
            throw Exception("API call limit reached for getFiveDayForecast")
        }
    }

    /**
     * Retrieves a five-day weather forecast for a given location key, respecting API call limits.
     *
     * @param locationKey The location key for which to retrieve the weather forecast.
     * @return The [ForecastResponse] containing the five-day weather forecast.
     * @throws Exception if the API call limit is reached or if no response body is found.
     */
    suspend fun getFiveDayForecast(locationKey: String): ForecastResponse {
        if (requestTracker.canCallFiveDayForecast()) {
            val response = weatherService.getFiveDayForecast(locationKey, apiKey, "en-us", true, false)
            if (response.isSuccessful) {
                requestTracker.updateFiveDayForecastCall()
                return response.body() ?: throw Exception("No body in response")
            } else {
                handleErrors(response.code())
            }
        } else {
            throw Exception("API call limit reached for getFiveDayForecast")
        }
    }

    /**
     * Retrieves a twelve-hour weather forecast for a given location key, following API call limits.
     *
     * @param locationKey The location key for which to retrieve the weather forecast.
     * @return A list of [HourlyForecastResponse] containing the twelve-hour weather forecast.
     * @throws Exception if the API call limit is reached or if no response body is found.
     */
    suspend fun getTwelveHourForecast(locationKey: String): List<HourlyForecastResponse> {
        if (requestTracker.canCallTwelveHourForecast()) {
            val response = weatherService.getTwelveHourForecast(locationKey, apiKey, "en-us", false)
            if (response.isSuccessful) {
                requestTracker.updateTwelveHourForecastCall()
                return response.body() ?: throw Exception("No body in response")
            } else {
                handleErrors(response.code())
            }
        } else {
            throw Exception("API call limit reached for getTwelveHourForecast")
        }
    }

    /**
     * Handles various error scenarios based on the HTTP response code.
     *
     * @param code The HTTP response code from the API call.
     * @throws Exception describing the type of error based on the response code.
     */
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
