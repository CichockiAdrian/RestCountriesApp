package com.example.restcountriesapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "countries")
data class CountryEntity(
    @PrimaryKey val code: String,
    val name: String,
    val capital: String,
    val region: String,
    val population: Long,
    val flagUrl: String?,
    val latitude: Double?,
    val longitude: Double?,
    val cachedAtMillis: Long
)