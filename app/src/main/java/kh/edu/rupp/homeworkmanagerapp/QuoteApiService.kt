package kh.edu.rupp.homeworkmanagerapp

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

/**
 * This interface defines the API endpoints for Retrofit.
 * It uses the QuoteResponse data class defined in QuoteResponse.kt.
 */
interface QuoteApiService {
    
    // Define a GET request to the "random" endpoint to fetch a single quote
    @GET("random")
    fun getRandomQuote(): Call<QuoteResponse>

    companion object {
        // The base URL for the Quotable API
        private const val BASE_URL = "https://api.quotable.io/"

        /**
         * A helper function to create and return the Retrofit service instance.
         */
        fun create(): QuoteApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                // Use Gson to automatically convert the JSON response into our Kotlin object
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(QuoteApiService::class.java)
        }
    }
}
