package com.example.mainactivity.exception

/**
 * Custom exception class to handle internal errors.
 *
 * This exception is thrown when an internal error occurs within the application,
 * typically due to unexpected conditions. It extends the standard `Exception` class
 * and provides additional context through a status code.
 *
 * @property statusCode The HTTP status code associated with the internal error.
 * @property message A detailed message describing the error.
 * @constructor Creates an `InternalErrorException` with the specified status code and message.
 */
class InternalErrorException(
    val statusCode: Int,
    override val message: String
) : Exception(message) {
    /**
     * Returns a string representation of the error, including the status code and error message.
     *
     * @return A formatted error string.
     */
    override fun toString(): String {
        return "InternalErrorException (Status Code: $statusCode): $message"
    }
}
