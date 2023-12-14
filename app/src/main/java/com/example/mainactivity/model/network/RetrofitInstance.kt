package com.example.mainactivity.model.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
/**
 * Singleton object for creating and providing a Retrofit instance.
 * This object ensures that only one Retrofit instance is created and reused throughout the app,
 * improving performance and resource management.
 */
object RetrofitInstance {
    /**
     * Lazy initialization of the Retrofit instance.
     * This ensures that the Retrofit object is created only when it's first accessed,
     * and the same instance is reused for subsequent requests.
     */
    val retrofit: Retrofit by lazy {
        // Setting up Moshi with KotlinJsonAdapterFactory to handle Kotlin-specific features
        // such as default parameter values and null safety.
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        // Building the Retrofit instance with the base URL of the API and the Moshi converter factory.
        // MoshiConverterFactory tells Retrofit to use Moshi for JSON serialization and deserialization.
        Retrofit.Builder()
        Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }
}
