package com.example.restcountriesapp.home

import com.example.restcountriesapp.model.Country

sealed interface HomeEvent {
    data class SearchChanged(val query: String) : HomeEvent
    data class CountryClicked(val country: Country) : HomeEvent
    data object BackClicked : HomeEvent
}