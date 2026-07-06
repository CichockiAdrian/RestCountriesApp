package com.example.restcountriesapp.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun HomeScreen(
    state: HomeState,
    onEvent: (HomeEvent) -> Unit,
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
                onEvent(HomeEvent.SearchChanged(query))
            },
            onSearchTriggered = {
                onEvent(HomeEvent.SearchSubmitted)
            },
            onCountryClick = { country ->
                onEvent(HomeEvent.CountryClicked(country))
            },
            onRetryClick = {
                onEvent(HomeEvent.RetryClicked)
            },
            onLoadNextPage = {
                onEvent(HomeEvent.LoadNextPage)
            },
            modifier = modifier
        )
    } else {
        CountryDetailsScreen(
            country = selectedCountry,
            onBackClick = {
                onEvent(HomeEvent.BackClicked)
            },
            modifier = modifier
        )
    }
}