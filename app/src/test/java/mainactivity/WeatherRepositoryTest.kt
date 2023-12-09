package mainactivity

import RequestTracker
import com.example.mainactivity.exception.BadRequestException
import com.example.mainactivity.exception.InternalErrorException
import com.example.mainactivity.api.WeatherService
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
//
//    @Mock
//    private lateinit var mockWeatherService: WeatherService
//
//    @Mock
//    private lateinit var mockLogger: Logger
//
//    @Mock
//    private lateinit var mockRequestTracker: RequestTracker
//
//    @InjectMocks
//    private lateinit var weatherRepository: WeatherRepository
//
//    @Before
//    fun setup() {
//        Logger.isTestMode = true
//        `when`(mockRequestTracker.canCallLocationKey()).thenReturn(true)
//        `when`(mockRequestTracker.canCallFiveDayForecast()).thenReturn(true)
//        `when`(mockRequestTracker.canCallTwelveHourForecast()).thenReturn(true)
//    }
//
//    @Test
//    fun getLocationKeyTest_SuccessfulResponse() = runBlocking {
//        // Arrange
//        val mockResponse = TestDataFactory.createLocationResponse()
//        `when`(
//            mockWeatherService.getLocationsKey(
//                anyString(),
//                anyString(),
//                anyString(),
//                anyBoolean()
//            )
//        ).thenReturn(Response.success(mockResponse))
//
//        // Act
//        val response = weatherRepository.getLocationKey("postalCode")
//
//        // Assert
//        verify(mockWeatherService).getLocationsKey("API_KEY", "postalCode", "en-us", false)
//        verify(mockRequestTracker).updateLocationKeyCall()
//        assertEquals(mockResponse, response)
//        verifyNoMoreInteractions(mockWeatherService)
//    }
//
//    @Test
//    fun getLocationKey_BadRequestResponse() = runBlocking {
//        `when`(
//            mockWeatherService.getLocationsKey("API_KEY", "98001", "en-us", false)
//        ).thenReturn(Response.error(400, "".toResponseBody(null)))
//
//        var exception: BadRequestException? = null
//
//        try {
//            weatherRepository.getLocationKey("98001")
//        } catch (e: BadRequestException) {
//            exception = e
//        }
//
//        verify(mockWeatherService).getLocationsKey("API_KEY", "98001", "en-us", false)
//        assertNotNull(exception)
//        assertEquals(400, exception?.statusCode)
//        assertEquals("Invalid parameter or bad request.", exception?.message)
//    }
//
//    @Test
//    fun getFiveDayForecast_SuccessfulResponse() = runBlocking {
//        val mockForecastResponse = TestDataFactory.createForecastResponse()
//        `when`(
//            mockWeatherService.getFiveDayForecast(
//                "locationKey",
//                "API_KEY",
//                "en-us",
//                true,
//                false
//            )
//        ).thenReturn(Response.success(mockForecastResponse))
//
//        `when`(mockRequestTracker.canCallFiveDayForecast()).thenReturn(true)
//
//        val response = weatherRepository.getFiveDayForecast("locationKey")
//
//        verify(mockWeatherService).getFiveDayForecast(
//            "locationKey",
//            "API_KEY",
//            "en-us",
//            true,
//            false
//        )
//        verify(mockRequestTracker).updateFiveDayForecastCall()
//        assertEquals(mockForecastResponse, response)
//    }
//
//
//    @Test
//    fun getFiveDayForecast_ApiLimitReached() = runBlocking {
//        // Arrange
//        `when`(mockRequestTracker.canCallFiveDayForecast()).thenReturn(false)
//
//        // Act
//        var exception: Exception? = null
//        try {
//            weatherRepository.getFiveDayForecast("locationKey")
//        } catch (e: Exception) {
//            exception = e
//        }
//
//        // Assert
//        assertNotNull(exception)
//        assertEquals("API call limit reached for getFiveDayForecast", exception?.message)
//    }
//
//    @Test
//    fun getFiveDayForecast_BadRequestResponse() = runBlocking {
//        `when`(
//            mockWeatherService.getFiveDayForecast("locationKey", "API_KEY", "en-us", true, false)
//        ).thenReturn(Response.error(400, "".toResponseBody(null)))
//
//        var exception: BadRequestException? = null
//
//        try {
//            weatherRepository.getFiveDayForecast("locationKey")
//        } catch (e: BadRequestException) {
//            exception = e
//        }
//
//        verify(mockWeatherService).getFiveDayForecast("locationKey", "API_KEY", "en-us", true, false)
//        assertNotNull(exception)
//        assertEquals(400, exception?.statusCode)
//        assertEquals("Invalid parameter or bad request.", exception?.message)
//    }
//
//
//
//    @Test
//    fun getTwelveHourForecast_SuccessfulResponse() = runBlocking {
//        val mockHourlyForecasts = TestDataFactory.createHourlyForecastResponse()
//        `when`(
//            mockWeatherService.getTwelveHourForecast(
//                "testKey",
//                "API_KEY",
//                "en-us",
//                false
//            )
//        ).thenReturn(Response.success(mockHourlyForecasts))
//
//        `when`(mockRequestTracker.canCallTwelveHourForecast()).thenReturn(true)
//
//        val result = weatherRepository.getTwelveHourForecast("testKey")
//
//        verify(mockWeatherService).getTwelveHourForecast(
//            "testKey",
//            "API_KEY",
//            "en-us",
//            false
//        )
//        verify(mockRequestTracker).updateTwelveHourForecastCall()
//        assertEquals(mockHourlyForecasts, result)
//    }
//
//    @Test
//    fun getTwelveHourForecast_InternalServerError() = runBlocking {
//        `when`(
//            mockWeatherService.getTwelveHourForecast("locationKey", "API_KEY", "en-us", false)
//        ).thenReturn(Response.error(500, "".toResponseBody(null)))
//
//        var exception: InternalErrorException? = null
//
//        try {
//            weatherRepository.getTwelveHourForecast("locationKey")
//        } catch (e: InternalErrorException) {
//            exception = e
//        }
//
//        verify(mockWeatherService).getTwelveHourForecast("locationKey", "API_KEY", "en-us", false)
//        assertNotNull(exception)
//        assertEquals(500, exception?.statusCode)
//        assertEquals("Internal server error.", exception?.message)
//    }
//
//    @Test
//    fun getTwelveHourForecast_BadRequestResponse() = runBlocking {
//        `when`(
//            mockWeatherService.getTwelveHourForecast(
//                "testKey",
//                "API_KEY",
//                "en-us",
//                false
//            )
//        ).thenReturn(Response.error(400, "".toResponseBody(null)))
//
//        var exception: BadRequestException? = null
//
//        try {
//            weatherRepository.getTwelveHourForecast("testKey")
//        } catch (e: BadRequestException) {
//            exception = e
//        }
//
//        verify(mockWeatherService).getTwelveHourForecast("testKey", "API_KEY", "en-us", false)
//        assertNotNull(exception)
//        assertEquals(400, exception?.statusCode)
//        assertEquals("Invalid parameter or bad request.", exception?.message)
//    }
//
//
//    @Test
//    fun getTwelveHourForecast_ApiLimitReached() = runBlocking {
//        `when`(mockRequestTracker.canCallTwelveHourForecast()).thenReturn(false)
//
//        var exception: Exception? = null
//        try {
//            weatherRepository.getTwelveHourForecast("locationKey")
//        } catch (e: Exception) {
//            exception = e
//        }
//
//        assertNotNull(exception)
//        assertEquals("API call limit reached for getTwelveHourForecast", exception?.message)
//    }
}
