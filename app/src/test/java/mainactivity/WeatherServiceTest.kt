package com.example.mainactivity

import com.example.mainactivity.model.network.ForecastResponse
import com.example.mainactivity.model.network.WeatherService
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer

/**
 * Unit tests for the [WeatherRepository] class.
 *
 * These tests validate the behavior of the WeatherRepository when interacting with the WeatherService.
 */
class WeatherServiceTest {
    private lateinit var service: WeatherService
    private lateinit var mockWebServer: MockWebServer
    private lateinit var moshi: Moshi

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        service = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(WeatherService::class.java)
    }

    @Test
    fun testGetLocationsKey() = runBlocking {
        val mockJsonResponse = TestDataFactory.createLocationResponseJson()
        val expectedResponse = TestDataFactory.createLocationResponse() // Expected object

        val mockResponse = MockResponse()
        mockResponse.setResponseCode(200)
        mockResponse.setBody(mockJsonResponse)
        mockWebServer.enqueue(mockResponse)

        val response = service.getLocationsKey("mockApiKey", "98101", "en-us", false)

        assertEquals(expectedResponse, response.body())

        val request = mockWebServer.takeRequest()
        assertEquals("/locations/v1/postalcodes/search?apikey=mockApiKey&q=98101&language=en-us&details=false", request.path)
    }

    @Test
    fun testGetFiveDayForecast() = runBlocking {
        val expectedForecastResponse: ForecastResponse = TestDataFactory.createForecastResponse()
        val expectedDailyForecasts = expectedForecastResponse.dailyForecasts

        val expectedJsonResponse = TestDataFactory.createForecastResponseJson()

        val mockResponse = MockResponse()
        mockResponse.setResponseCode(200)
        mockResponse.setBody(expectedJsonResponse)
        mockWebServer.enqueue(mockResponse)

        val response = service.getFiveDayForecast("locationKey", "apiKey", "en-us", true, false)

        assertEquals(200, response.code())
        val responseBody = response.body()
        assertEquals(expectedForecastResponse, responseBody)
        assertEquals(expectedDailyForecasts.size, responseBody?.dailyForecasts?.size)

        val request = mockWebServer.takeRequest()
        assertEquals("/forecasts/v1/daily/5day/locationKey?apikey=apiKey&language=en-us&details=true&metric=false", request.path)
    }

    @Test
    fun testGetTwelveHourForecast() = runBlocking {
         val mockHourlyForecast = TestDataFactory.createHourlyForecastResponse()
        val mockJson = TestDataFactory.createHourlyForecastJson()

        val mockResponse = MockResponse()
        mockResponse.setResponseCode(200)
        mockResponse.setBody(mockJson)
        mockWebServer.enqueue(mockResponse)

        val response = service.getTwelveHourForecast("testLocationKey", "testApiKey", "en-us", false)

        assertEquals(200, response.code())
        assertEquals(mockHourlyForecast, response.body())

        val request = mockWebServer.takeRequest()
        assertEquals("/forecasts/v1/hourly/12hour/testLocationKey?apikey=testApiKey&language=en-us&metric=false", request.path)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }
}

