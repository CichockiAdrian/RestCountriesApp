package com.example.restcountriesapp.home

import com.example.restcountriesapp.domain.model.Country

sealed interface CountriesEvent {
    data object LoadCountries : CountriesEvent
    data object RetryClicked : CountriesEvent
    data class SearchChanged(val query: String) : CountriesEvent
    data object SearchSubmitted : CountriesEvent
    data class CountryClicked(val country: Country) : CountriesEvent
    data object BackClicked : CountriesEvent
    data object LoadNextPage : CountriesEvent
}