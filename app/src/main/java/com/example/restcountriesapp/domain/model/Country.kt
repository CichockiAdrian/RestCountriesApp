package com.example.restcountriesapp.domain.model

data class Country(
    val name: String,
    val capital: String,
    val region: String,
    val population: Long,
    val code: String,
    val flagUrl: String?,
    val latitude: Double?,
    val longitude: Double?

)