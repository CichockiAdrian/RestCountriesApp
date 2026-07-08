package com.example.restcountriesapp.feature.countries.details

import com.example.restcountriesapp.domain.model.Country

data class CountryDetailsState(
    val country: Country? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)