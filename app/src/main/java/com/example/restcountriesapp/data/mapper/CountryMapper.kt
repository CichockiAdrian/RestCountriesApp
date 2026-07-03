package com.example.restcountriesapp.data.mapper

import com.example.restcountriesapp.data.remote.dto.CountryDto
import com.example.restcountriesapp.domain.model.Country

fun CountryDto.toCountry(): Country {
    return Country(
        name = names?.common.orEmpty(),
        capital = capitals?.firstOrNull()?.name.orEmpty(),
        region = region.orEmpty(),
        population = population ?: 0L,
        code = codes?.alpha2.orEmpty(),
        flagUrl = flag?.urlPng,
        latitude = coordinates?.lat,
        longitude = coordinates?.lng
    )
}