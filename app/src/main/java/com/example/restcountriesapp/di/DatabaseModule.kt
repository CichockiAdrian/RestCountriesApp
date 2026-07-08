package com.example.restcountriesapp.di

import androidx.room.Room
import com.example.restcountriesapp.data.local.RestCountriesDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            RestCountriesDatabase::class.java,
            "rest_countries.db"
        ).build()
    }

    single {
        get<RestCountriesDatabase>().countryDao()
    }
}