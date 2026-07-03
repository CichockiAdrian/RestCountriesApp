package com.example.restcountriesapp.di

import com.example.restcountriesapp.BuildConfig
import com.example.restcountriesapp.data.remote.RestCountriesApi
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {

    single {
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val requestBuilder = chain.request()
                    .newBuilder()

                if (BuildConfig.REST_COUNTRIES_TOKEN.isNotBlank()) {
                    requestBuilder.addHeader(
                        "Authorization",
                        "Bearer ${BuildConfig.REST_COUNTRIES_TOKEN}"
                    )
                }

                chain.proceed(requestBuilder.build())
            }
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl("https://api.restcountries.com/")
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single<RestCountriesApi> {
        get<Retrofit>().create(RestCountriesApi::class.java)
    }
}