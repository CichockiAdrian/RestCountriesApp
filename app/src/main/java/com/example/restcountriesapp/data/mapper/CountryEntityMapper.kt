package com.example.restcountriesapp.data.mapper

import com.example.restcountriesapp.data.local.entity.CountryEntity
import com.example.restcountriesapp.domain.model.Country

fun CountryEntity.toCountry(): Country {
    return Country(
        name = name,
        capital = capital,
        region = region,
        population = population,
        code = code,
        flagUrl = flagUrl,
        latitude = latitude,
        longitude = longitude
    )
}

fun Country.toEntity(): CountryEntity {
    return CountryEntity(
        code = code,
        name = name,
        capital = capital,
        region = region,
        population = population,
        flagUrl = flagUrl,
        latitude = latitude,
        longitude = longitude,
        cachedAtMillis = System.currentTimeMillis()
    )
}