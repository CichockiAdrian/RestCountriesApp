package com.example.restcountriesapp.data.remote.dto

data class CountriesDataDto(
    val objects: List<CountryDto>?,
    val meta: CountriesMetaDto?
)
