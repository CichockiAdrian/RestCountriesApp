package com.example.restcountriesapp.feature.countries

import com.example.restcountriesapp.domain.model.Country

data class CountriesState(
    val countries: List<Country> = emptyList(),
    val searchQuery: String = "",
    val selectedRegion: String = "All",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isLoadingNextPage: Boolean = false,
    val hasMoreCountries: Boolean = true,
    val nextOffset: Int = 0
) {
    // Keep filteredCountries for local filtering if needed, 
    // but primary filtering is now in DAO for pagination consistency.
    val filteredCountries: List<Country>
        get() = countries
}
