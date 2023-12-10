package com.example.mainactivity.resources

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.mainactivity.activities.MainActivity
import com.example.mainactivity.model.network.FiveDayForecast
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WeatherIntegrationTest {

//    @Rule
//    @JvmField
//    val activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun testApiCallAndParsing() {

        val mockApiResponse = InstrumentationRegistry.getInstrumentation()
            .context
            .resources
            .assets
            .open("sample_api_response.json")
            .bufferedReader()
            .use { it.readText() }

        // Create a Moshi instance
        val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()

        // Create a JsonAdapter for your data class
        val adapter: JsonAdapter<FiveDayForecast> = moshi.adapter(FiveDayForecast::class.java)

        // Use Moshi to parse the loaded JSON content
        val weatherForecast = adapter.fromJson(mockApiResponse)

//        assertThat(weatherForecast?.city?.name).isEqualTo("Bellevue")
//        assertThat(weatherForecast?.list?.size).isEqualTo(5)

    }
}
