package com.example.restcountriesapp.feature.countries

import com.example.restcountriesapp.domain.model.Country

sealed interface CountriesEvent {
    data object LoadCountries : CountriesEvent
    data object RetryClicked : CountriesEvent
    data object LoadNextPage : CountriesEvent

    data class SearchChanged(val query: String) : CountriesEvent
    data class RegionChanged(val region: String) : CountriesEvent
    data object SearchSubmitted : CountriesEvent

}