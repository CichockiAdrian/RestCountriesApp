package com.example.restcountriesapp.home

import com.example.restcountriesapp.domain.model.Country

data class HomeState(
    val countries: List<Country> = emptyList(),
    val searchQuery: String = "",
    val selectedCountry: Country? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isLoadingNextPage: Boolean = false,
    val hasMoreCountries: Boolean = true,
    val nextOffset: Int = 0
) {
    val filteredCountries: List<Country>
        get() = countries.filter { country ->
            country.name.contains(searchQuery, ignoreCase = true)
        }
}