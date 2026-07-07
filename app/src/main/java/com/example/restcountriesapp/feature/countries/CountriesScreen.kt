package com.example.restcountriesapp.feature.countries

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.restcountriesapp.R
import com.example.restcountriesapp.feature.countries.details.CountryDetailsContent
import com.example.restcountriesapp.feature.countries.list.CountriesList
import com.example.restcountriesapp.feature.countries.list.CountrySearchField

@Composable
fun CountriesScreen(
    state: CountriesState,
    onEvent: (CountriesEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedCountry = state.selectedCountry

    if (selectedCountry != null) {
        CountryDetailsContent(
            country = selectedCountry,
            onBackClick = {
                onEvent(CountriesEvent.BackClicked)
            },
            modifier = modifier
        )
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        CountriesHeader()

        Spacer(modifier = Modifier.height(16.dp))

        CountrySearchField(
            searchQuery = state.searchQuery,
            onSearchQueryChange = { query ->
                onEvent(CountriesEvent.SearchChanged(query))
            },
            onSearchTriggered = {
                onEvent(CountriesEvent.SearchSubmitted)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (state.isLoading) {
            Text(text = stringResource(R.string.loading_countries))
            Spacer(modifier = Modifier.height(16.dp))
        }

        state.errorMessage?.let { message ->
            Text(text = message)

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    onEvent(CountriesEvent.RetryClicked)
                }
            ) {
                Text(text = stringResource(R.string.retry))
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        CountriesList(
            countries = state.filteredCountries,
            isLoadingNextPage = state.isLoadingNextPage,
            hasMoreCountries = state.hasMoreCountries,
            onCountryClick = { country ->
                onEvent(CountriesEvent.CountryClicked(country))
            },
            onLoadNextPage = {
                onEvent(CountriesEvent.LoadNextPage)
            }
        )
    }
}

@Composable
private fun CountriesHeader() {
    Text(
        text = stringResource(R.string.rest_countries_title),
        style = MaterialTheme.typography.headlineMedium
    )
}