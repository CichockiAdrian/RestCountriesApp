package com.example.restcountriesapp.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.restcountriesapp.R
import com.example.restcountriesapp.domain.model.Country

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

@Composable
fun CountriesListScreen(
    searchQuery: String,
    countries: List<Country>,
    isLoading: Boolean,
    errorMessage: String?,
    isLoadingNextPage: Boolean,
    hasMoreCountries: Boolean,
    onSearchQueryChange: (String) -> Unit,
    onCountryClick: (Country) -> Unit,
    onRetryClick: () -> Unit,
    onLoadNextPage: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        CountriesHeader()

        Spacer(modifier = Modifier.height(16.dp))

        CountrySearchField(
            searchQuery = searchQuery,
            onSearchQueryChange = onSearchQueryChange
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            Text(text = stringResource(R.string.loading_countries))
            Spacer(modifier = Modifier.height(16.dp))
        }

        errorMessage?.let { message ->
            Text(text = message)

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onRetryClick
            ) {
                Text(stringResource(R.string.retry))
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        CountriesList(
            countries = countries,
            isLoadingNextPage = isLoadingNextPage,
            hasMoreCountries = hasMoreCountries,
            onCountryClick = onCountryClick,
            onLoadNextPage = onLoadNextPage
        )
    }
}

@Composable
fun CountriesHeader() {
    Text(
        text = stringResource(R.string.rest_countries_title),
        style = MaterialTheme.typography.headlineMedium
    )
}

@Composable
fun CountrySearchField(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit
) {
    OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearchQueryChange,
        modifier = Modifier.fillMaxWidth(),
        label = {
            Text(stringResource(R.string.search_country_label))
        }
    )
}

@Composable
fun CountriesList(
    countries: List<Country>,
    isLoadingNextPage: Boolean,
    hasMoreCountries: Boolean,
    onCountryClick: (Country) -> Unit,
    onLoadNextPage: () -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(countries) { country ->
            CountryListItem(
                country = country,
                onClick = {
                    onCountryClick(country)
                }
            )
        }

        item {
            if (isLoadingNextPage) {
                Text(
                    text = stringResource(R.string.loading_more_countries),
                    modifier = Modifier.padding(16.dp)
                )
            } else if (hasMoreCountries) {
                Button(
                    onClick = onLoadNextPage,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                ) {
                    Text(stringResource(R.string.load_more))
                }
            }
        }
    }
}

@Composable
fun CountryListItem(
    country: Country,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Text(
            text = country.name,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
fun CountryDetailsScreen(
    country: Country,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = country.name,
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        CountryDetailsContent(country = country)

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onBackClick
        ) {
            Text(stringResource(R.string.back))
        }
    }
}

@Composable
fun CountryDetailsContent(
    country: Country
) {
    Text(text = stringResource(R.string.country_capital, country.capital))
    Text(text = stringResource(R.string.country_region, country.region))
    Text(text = stringResource(R.string.country_population, country.population.toString()))
}