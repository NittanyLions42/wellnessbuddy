import android.content.SharedPreferences
import java.util.*

class RequestTracker(private val sharedPreferences: SharedPreferences) {

    companion object {
        private const val LOCATION_KEY_COUNT = "location_key_count"
        private const val FIVE_DAY_FORECAST_COUNT = "five_day_forecast_count"
        private const val TWELVE_HOUR_FORECAST_COUNT = "twelve_hour_forecast_count"
        private const val TWELVE_HOUR_FORECAST_TIMESTAMP = "twelve_hour_forecast_timestamp"
    }

    fun canCallLocationKey(): Boolean {
        val count = sharedPreferences.getInt(LOCATION_KEY_COUNT, 0)
        return count < 2
    }

    fun updateLocationKeyCall() {
        updateCallCount(LOCATION_KEY_COUNT)
    }

    fun canCallFiveDayForecast(): Boolean {
        val count = sharedPreferences.getInt(FIVE_DAY_FORECAST_COUNT, 0)
        return count < 1
    }

    fun updateFiveDayForecastCall() {
        updateCallCount(FIVE_DAY_FORECAST_COUNT)
    }

    fun canCallTwelveHourForecast(): Boolean {
        val count = sharedPreferences.getInt(TWELVE_HOUR_FORECAST_COUNT, 0)
        val lastTimestamp = sharedPreferences.getLong(TWELVE_HOUR_FORECAST_TIMESTAMP, 0)
        val currentTime = System.currentTimeMillis()

        return count < 2 && (currentTime - lastTimestamp > 12 * 60 * 60 * 1000) // 12 hours in milliseconds
    }

    fun updateTwelveHourForecastCall() {
        val count = sharedPreferences.getInt(TWELVE_HOUR_FORECAST_COUNT, 0)
        if (count == 0) {
            sharedPreferences.edit().putLong(TWELVE_HOUR_FORECAST_TIMESTAMP, System.currentTimeMillis()).apply()
        }
        updateCallCount(TWELVE_HOUR_FORECAST_COUNT)
    }

    private fun updateCallCount(key: String) {
        val count = sharedPreferences.getInt(key, 0)
        sharedPreferences.edit().putInt(key, count + 1).apply()
    }

    fun isNewDay(): Boolean {
        val lastReset = sharedPreferences.getLong("last_reset", 0)
        val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)

        return lastReset < currentDay
    }

    fun resetDailyLimits() {
        sharedPreferences.edit()
            .putInt(LOCATION_KEY_COUNT, 0)
            .putInt(FIVE_DAY_FORECAST_COUNT, 0)
            .putInt(TWELVE_HOUR_FORECAST_COUNT, 0)
            .putLong("last_reset", Calendar.getInstance().get(Calendar.DAY_OF_YEAR).toLong())
            .apply()
    }
}
