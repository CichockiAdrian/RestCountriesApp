package com.example.restcountriesapp

import com.example.restcountriesapp.data.remote.RestCountriesApi
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ExampleUnitTest {
    @Test
    fun testRealApiCall() = runBlocking {
        val token = "rc_live_demo"
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val requestBuilder = chain.request().newBuilder()
                requestBuilder.addHeader("Authorization", "Bearer $token")
                val response = chain.proceed(requestBuilder.build())
                println("HTTP STATUS: ${response.code}")
                println("HTTP MSG: ${response.message}")
                try {
                    val bodyString = response.peekBody(2048).string()
                    println("HTTP BODY: $bodyString")
                } catch (e: Exception) {
                    println("Could not peek body: ${e.message}")
                }
                response
            }
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.restcountries.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(RestCountriesApi::class.java)

        try {
            val response = api.getCountries(limit = 10, offset = 0)
            println("API RESPONSE: Success! Found ${response.data?.objects?.size} countries")
        } catch (e: Exception) {
            println("API ERROR:")
            e.printStackTrace()
        }
    }
}