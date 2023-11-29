package com.example.mainactivity.exception

/**
 * Exception class representing a bad request error.
 *
 * This exception is typically thrown when the application encounters an issue with the
 * request made, such as invalid parameters or malformed data. It inherits from the standard
 * `Exception` class, enriching it with a status code specific to the bad request error.
 *
 * @property statusCode The HTTP status code that represents the specific nature of the bad request error.
 * @property message A descriptive message providing details about the bad request error.
 * @constructor Creates a `BadRequestException` instance with the specified status code and detailed message.
 */
class BadRequestException(
    val statusCode: Int,
    override val message: String
) : Exception(message) {
    /**
     * Generates a string representation of the exception, incorporating the status code and the detailed message.
     *
     * @return A formatted string describing the bad request exception.
     */
    override fun toString(): String {
        return "BadRequestException (Status Code: $statusCode): $message"
    }
}
