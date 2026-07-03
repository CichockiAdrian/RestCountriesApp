package com.example.restcountriesapp.home

import com.example.restcountriesapp.domain.model.Country

sealed interface HomeEvent {
    data object LoadCountries : HomeEvent
    data object RetryClicked : HomeEvent
    data class SearchChanged(val query: String) : HomeEvent
    data class CountryClicked(val country: Country) : HomeEvent
    data object BackClicked : HomeEvent
    data object LoadNextPage : HomeEvent
}