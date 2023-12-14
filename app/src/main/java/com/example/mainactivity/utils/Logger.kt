package com.example.mainactivity.utils

import android.util.Log

/**
 * Utility object for logging messages in the Android application.
 */
object Logger {
    // Flag to determine if the application is in test mode.
    // Logging can be disabled during testing to avoid cluttering the log output.
    var isTestMode: Boolean = false

    /**
     * Logs a debug message.
     *
     * @param tag The tag used to identify the source of a log message.
     * @param message The message to be logged.
     */
    fun d(tag: String, message: String) {
        if (!isTestMode) {
            Log.d(tag, message)
        }
    }
    /**
     * Logs an error message. Optionally includes an exception (Throwable) for detailed error reporting.
     *
     * @param tag The tag used to identify the source of a log message.
     * @param message The message to be logged.
     * @param throwable An optional Throwable for logging exceptions.
     */
    fun e(tag: String, message: String, throwable: Throwable? = null) {
        if (!isTestMode) {
            if (throwable != null) {
                Log.e(tag, message, throwable)
            } else {
                Log.e(tag, message)
            }
        }
    }

    fun i(tag: String, message: String) {
        if (!isTestMode) {
            Log.i(tag, message)
        }
    }

    fun v(tag: String, message: String) {
        if (!isTestMode) {
            Log.v(tag, message)
        }
    }

    fun w(tag: String, message: String, throwable: Throwable? = null) {
        if (!isTestMode) {
            if (throwable != null) {
                Log.w(tag, message, throwable)
            } else {
                Log.w(tag, message)
            }
        }
    }
}
