package com.example.restcountriesapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CountriesResponseDto(
    val data: CountriesDataDto?
)

data class CountriesDataDto(
    val objects: List<CountryDto>?
)

data class CountryDto(
    val names: CountryNamesDto?,
    val capitals: List<CountryCapitalDto>?,
    val flag: CountryFlagDto?,
    val population: Long?,
    val region: String?,
    val codes: CountryCodesDto?,
    val coordinates: CountryCoordinatesDto?
)

data class CountryNamesDto(
    val common: String?
)

data class CountryCapitalDto(
    val name: String?
)

data class CountryFlagDto(
    @SerializedName("url_png")
    val urlPng: String?,

    @SerializedName("url_svg")
    val urlSvg: String?
)

data class CountryCodesDto(
    @SerializedName("alpha_2")
    val alpha2: String?
)

data class CountryCoordinatesDto(
    val lat: Double?,
    val lng: Double?
)