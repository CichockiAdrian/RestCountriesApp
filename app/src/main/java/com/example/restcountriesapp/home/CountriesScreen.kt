package com.example.restcountriesapp.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun HomeScreen(
    state: CountriesState,
    onEvent: (CountriesEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedCountry = state.selectedCountry

    if (selectedCountry == null) {
        CountriesListScreen(
            searchQuery = state.searchQuery,
            countries = state.filteredCountries,
            isLoading = state.isLoading,
            errorMessage = state.errorMessage,
            isLoadingNextPage = state.isLoadingNextPage,
            hasMoreCountries = state.hasMoreCountries,
            onSearchQueryChange = { query ->
                onEvent(CountriesEvent.SearchChanged(query))
            },
            onSearchTriggered = {
                onEvent(CountriesEvent.SearchSubmitted)
            },
            onCountryClick = { country ->
                onEvent(CountriesEvent.CountryClicked(country))
            },
            onRetryClick = {
                onEvent(CountriesEvent.RetryClicked)
            },
            onLoadNextPage = {
                onEvent(CountriesEvent.LoadNextPage)
            },
            modifier = modifier
        )
    } else {
        CountryDetailsScreen(
            country = selectedCountry,
            onBackClick = {
                onEvent(CountriesEvent.BackClicked)
            },
            modifier = modifier
        )
    }
}