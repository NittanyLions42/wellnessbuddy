package com.example.mainactivity.controller

/**
 * class Suggestion to be used in ActivitySuggestion for json objects
 */
class Recommendation(
    val title: String, //changed to public for displaying
    private val img: String,
    private val description: String
)
