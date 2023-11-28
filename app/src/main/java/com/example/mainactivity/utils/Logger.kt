package com.example.mainactivity.utils

import android.util.Log

/**
 * Utility object for logging messages in the Android application.
 */
object Logger {
    var isTestMode: Boolean = false

    fun d(tag: String, message: String) {
        if (!isTestMode) {
            Log.d(tag, message)
        }
    }

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
