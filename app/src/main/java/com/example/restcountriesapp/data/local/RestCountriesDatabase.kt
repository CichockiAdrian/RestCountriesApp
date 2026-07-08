package com.example.restcountriesapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.restcountriesapp.data.local.dao.CountryDao
import com.example.restcountriesapp.data.local.entity.CountryEntity

@Database(
    entities = [CountryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class RestCountriesDatabase : RoomDatabase() {
    abstract fun countryDao(): CountryDao
}