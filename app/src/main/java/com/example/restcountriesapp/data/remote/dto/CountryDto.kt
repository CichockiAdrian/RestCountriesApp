package com.example.restcountriesapp.data.remote.dto

data class CountryDto(
    val names: CountryNamesDto?,
    val capitals: List<CountryCapitalDto>?,
    val flag: CountryFlagDto?,
    val population: Long?,
    val region: String?,
    val codes: CountryCodesDto?,
    val coordinates: CountryCoordinatesDto?
)