package com.example.restcountriesapp.feature.countries

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.restcountriesapp.R
import com.example.restcountriesapp.domain.model.AuthUser
import com.example.restcountriesapp.domain.model.Country
import com.example.restcountriesapp.feature.auth.AuthEvent
import com.example.restcountriesapp.feature.auth.UserProfileCard
import com.example.restcountriesapp.feature.countries.list.CountriesList
import com.example.restcountriesapp.feature.countries.list.CountrySearchField

@Composable
fun CountriesScreen(
    state: CountriesState,
    user: AuthUser?,
    onEvent: (CountriesEvent) -> Unit,
    onAuthEvent: (AuthEvent) -> Unit,
    onCountryClick: (Country) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        user?.let {
            UserProfileCard(
                user = it,
                onSignOutClick = { onAuthEvent(AuthEvent.SignOutClicked) }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

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

        Box(modifier = Modifier.fillMaxSize()) {
            when {
                state.errorMessage != null && state.countries.isEmpty() -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = state.errorMessage)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { onEvent(CountriesEvent.RetryClicked) }) {
                            Text(text = stringResource(R.string.retry))
                        }
                    }
                }

                else -> {
                    CountriesList(
                        countries = state.filteredCountries,
                        isLoadingNextPage = state.isLoadingNextPage,
                        hasMoreCountries = state.hasMoreCountries,
                        onCountryClick = onCountryClick,
                        onLoadNextPage = {
                            onEvent(CountriesEvent.LoadNextPage)
                        }
                    )
                }
            }

            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
private fun CountriesHeader() {
    Text(
        text = stringResource(R.string.rest_countries_title),
        style = MaterialTheme.typography.headlineMedium
    )
}