package com.example.restcountriesapp.data.remote

import com.example.restcountriesapp.data.remote.dto.CountriesResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface RestCountriesApi {

    @GET("countries/v5")
    suspend fun getCountries(
        @Query("limit") limit: Int = 25,
        @Query("offset") offset: Int = 0
    ): CountriesResponseDto
}