package com.example.restcountriesapp.home

import com.example.restcountriesapp.domain.model.Country

data class CountriesState(
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
        get() {
            val query = searchQuery.trim()
            if (query.isBlank()) return countries
            return countries.sortedBy { country ->
                when {
                    country.name.startsWith(query, ignoreCase = true) -> 0
                    country.name.contains(query, ignoreCase = true) -> 1
                    else -> 2
                }
            }
        }
}