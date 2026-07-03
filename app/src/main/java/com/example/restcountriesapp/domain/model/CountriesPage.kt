package com.example.restcountriesapp.domain.model

data class CountriesPage(
    val countries: List<Country>,
    val nextOffset: Int,
    val hasMore: Boolean
)