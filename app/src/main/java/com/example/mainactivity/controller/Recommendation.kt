package com.example.mainactivity.controller

class Recommendation
{

    /**
     * class Suggestion to be used in ActivitySuggestion for json objects
     */
    class Recommendation(
        val title: String, //changed to public for displaying
        val base64img: String,
        val description: String
    )
}
