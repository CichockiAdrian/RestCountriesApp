package com.example.restcountriesapp.feature.countries

import com.example.restcountriesapp.domain.model.Country

data class CountriesState(
    val countries: List<Country> = emptyList(),
    val searchQuery: String = "",
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

            return countries
                .filter { country ->
                    country.name.contains(query, ignoreCase = true) ||
                            country.code.contains(query, ignoreCase = true)
                }
                .sortedBy { country ->
                    if (country.name.startsWith(query, ignoreCase = true) ||
                        country.code.startsWith(query, ignoreCase = true)
                    ) 0 else 1
                }
        }
}