package com.example.mainactivity.exception

/**
 * Exception class for handling not found (404) errors.
 *
 * This exception is thrown when a requested resource is not found on the server.
 * It corresponds to the HTTP status code 404.
 *
 * @param statusCode The HTTP status code, which should be 404 for this exception.
 * @param message A human-readable message describing the error.
 */
class NotFoundException(
    private val statusCode: Int,
    override val message: String
) : Exception(message) {
    override fun toString(): String {
        return "NotFoundException (Status Code: $statusCode): $message"
    }
}
