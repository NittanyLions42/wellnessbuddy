package com.example.mainactivity.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Represents the response structure for a location query based on postal code.
 * This class is used to deserialize the JSON response containing location-related details.
 *
 * @property postalCode The primary postal code associated with the location query.
 * @property administrativeArea The administrative area details for the location.
 * @property parentCity Details of the parent city related to the provided postal code.
 */
@JsonClass(generateAdapter = true)
data class LocationResponse(
    @Json(name = "PrimaryPostalCode") val postalCode: String,
    @Json(name = "AdministrativeArea") val administrativeArea: AdministrativeArea,
    @Json(name = "ParentCity") val parentCity: ParentCity
)

/**
 * Contains information about the administrative area of a location.
 * Typically represents data like the state or province.
 *
 * @property stateId The identifier of the administrative area (e.g., state abbreviation).
 */
@JsonClass(generateAdapter = true)
data class AdministrativeArea(
    @Json(name = "ID") val stateId: String
)

/**
 * Represents the parent city information for a given location.
 * Used in the context of a location response.
 *
 * @property locationKey A unique key identifying the parent city.
 * @property cityName The localized name of the parent city.
 */
@JsonClass(generateAdapter = true)
data class ParentCity(
    @Json(name = "Key") val locationKey: String,
    @Json(name = "LocalizedName") val cityName: String
)
