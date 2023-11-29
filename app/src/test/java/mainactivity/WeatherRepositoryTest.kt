package mainactivity

import com.example.mainactivity.exception.BadRequestException
import com.example.mainactivity.exception.InternalErrorException
import com.example.mainactivity.model.network.HourlyForecastResponse
import com.example.mainactivity.model.network.WeatherService
import com.example.mainactivity.repository.WeatherRepository
import com.example.mainactivity.utils.Logger
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyBoolean
import org.mockito.ArgumentMatchers.anyString
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verifyNoMoreInteractions
import retrofit2.Response

/**
 * Unit tests for the [WeatherRepository] class.
 *
 * These tests validate the behavior of the WeatherRepository when interacting with the WeatherService.
 */
@RunWith(MockitoJUnitRunner::class)
class WeatherRepositoryTest {

    @Mock
    private lateinit var mockWeatherService: WeatherService

    @Mock
    private lateinit var mockLogger: Logger


    @InjectMocks
    private lateinit var mockWeatherRepository: WeatherRepository

    @Before
    fun setup() {
        Logger.isTestMode = true
    }

    @Test
    fun getLocationKeyTest_SuccessfulResponse() = runBlocking {
        val mockResponse = TestDataFactory.createLocationResponse()

        `when`(
            mockWeatherService.getLocationsKey(
                anyString(),
                anyString(),
                anyString(),
                anyBoolean()
            )
        ).thenReturn(Response.success(mockResponse))

        val response = mockWeatherRepository.getLocationKey("postalCode")
        val responseLocationKey = response.parentCity.locationKey

        verify(mockWeatherService).getLocationsKey("API_KEY", "postalCode")
        assertEquals(mockResponse, response)
        assertEquals("123456", responseLocationKey)
        verifyNoMoreInteractions(mockWeatherService)
    }

    @Test
    fun getLocationKey_BadRequestResponse() = runBlocking {
        `when`(
            mockWeatherService.getLocationsKey(
                "API_KEY",
                "98001",
                "en-us",
                false
            )
        ).thenReturn(Response.error(400, "".toResponseBody(null)))

        var exception: BadRequestException? = null

        try {
            mockWeatherRepository.getLocationKey("98001")
        } catch (e: BadRequestException) {
            exception = e
        }

        verify(mockWeatherService).getLocationsKey("API_KEY", "98001", "en-us", false)

        assertNotNull(exception)
        assertEquals(400, exception?.statusCode)
        assertEquals("Invalid parameter or bad request.", exception?.message)
    }


    @Test
    fun getFiveDayForecast_SuccessfulResponse() = runBlocking {
        val mockForecastResponse = TestDataFactory.createForecastResponse()
        `when`(
            mockWeatherService.getFiveDayForecast(
                "locationKey",
                "API_KEY",
                "en-us",
                true,
                false
            )
        ).thenReturn(Response.success(mockForecastResponse))

        val response = mockWeatherRepository.getFiveDayForecast("locationKey")

        verify(mockWeatherService).getFiveDayForecast(
            "locationKey",
            "API_KEY",
            "en-us",
            true,
            false
        )
        assertEquals(mockForecastResponse, response)
    }

    @Test
    fun getFiveDayForecast_BadRequestResponse() = runBlocking {
        `when`(
            mockWeatherService.getFiveDayForecast(
                "locationKey",
                "API_KEY",
                "en-us",
                true,
                false
            )
        ).thenReturn(Response.error(400, "".toResponseBody(null)))

        var exception: BadRequestException? = null

        try {
            mockWeatherRepository.getFiveDayForecast("locationKey")
        } catch (e: BadRequestException) {
            exception = e
        }

        verify(mockWeatherService).getFiveDayForecast(
            "locationKey",
            "API_KEY",
            "en-us",
            true,
            false
        )

        assertNotNull(exception)
        assertEquals(400, exception?.statusCode)
        assertEquals("Invalid parameter or bad request.", exception?.message)
    }


    @Test
    fun getTwelveHourForecast_SuccessfulResponse() = runBlocking {
        val mockHourlyForecasts = TestDataFactory.createHourlyForecastResponse()
        val mockResponse = Response.success(mockHourlyForecasts)

        `when`(
            mockWeatherService.getTwelveHourForecast(
                "testKey",
                "API_KEY",
                "en-us",
                false
            )
        ).thenReturn(mockResponse)

        val result = mockWeatherRepository.getTwelveHourForecast("testKey")

        assertNotNull(result)
        assertEquals(mockResponse.body(), result)
    }

    @Test
    fun getTwelveHourForecast_InternalServerError() = runBlocking {
        `when`(
            mockWeatherService.getTwelveHourForecast(
                "locationKey",
                "API_KEY",
                "en-us",
                false
            )
        ).thenReturn(Response.error(500, "".toResponseBody(null)))

        var exception: InternalErrorException? = null

        try {
            mockWeatherRepository.getTwelveHourForecast("locationKey")
        } catch (e: InternalErrorException) {
            exception = e
        }

        verify(mockWeatherService).getTwelveHourForecast("locationKey", "API_KEY", "en-us", false)

        assertNotNull(exception)
        assertEquals(500, exception?.statusCode)
        assertEquals("Internal server error.", exception?.message)
    }


    @Test
    fun getTwelveHourForecast_BadRequestResponse() = runBlocking {
        val mockResponse: Response<List<HourlyForecastResponse>> =
            Response.error(400, "".toResponseBody(null))

        `when`(
            mockWeatherService.getTwelveHourForecast(
                "testKey",
                "API_KEY",
                "en-us",
                false
            )
        ).thenReturn(mockResponse)

        var exception: BadRequestException? = null

        try {
            mockWeatherRepository.getTwelveHourForecast("testKey")
        } catch (e: BadRequestException) {
            exception = e
        }

        assertNotNull(exception)
        assertEquals(400, exception?.statusCode)
        assertEquals("Invalid parameter or bad request.", exception?.message)
    }
}
