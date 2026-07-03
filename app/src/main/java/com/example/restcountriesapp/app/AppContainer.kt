package com.example.restcountriesapp.app

import com.example.restcountriesapp.data.remote.RestCountriesApi
import com.example.restcountriesapp.data.repository.CountryRepositoryImpl
import com.example.restcountriesapp.domain.repository.CountryRepository
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.restcountriesapp.BuildConfig

class AppContainer {
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request()
                .newBuilder()
                .addHeader(
                    "Authorization",
                    "Bearer ${BuildConfig.REST_COUNTRIES_TOKEN}"
                )
                .build()

            chain.proceed(request)
        }
        .build()
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.restcountries.com/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val restCountriesApi = retrofit.create(RestCountriesApi::class.java)

    val countryRepository: CountryRepository = CountryRepositoryImpl(
        api = restCountriesApi
    )

}