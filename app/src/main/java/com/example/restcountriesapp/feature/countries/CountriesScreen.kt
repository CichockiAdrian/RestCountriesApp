package com.example.restcountriesapp.feature.countries

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.restcountriesapp.R
import com.example.restcountriesapp.domain.model.AuthUser
import com.example.restcountriesapp.domain.model.Country
import com.example.restcountriesapp.feature.auth.AuthEvent
import com.example.restcountriesapp.feature.auth.UserProfileCard
import com.example.restcountriesapp.feature.countries.list.CountriesList
import com.example.restcountriesapp.feature.countries.list.CountrySearchField
import com.example.restcountriesapp.ui.theme.AppDimensions
import com.example.restcountriesapp.ui.theme.AppSpacing

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
            .padding(horizontal = AppSpacing.Medium),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = AppDimensions.MaxContentWidth)
                .fillMaxWidth()
        ) {
            user?.let {
                Spacer(modifier = Modifier.height(AppSpacing.Small))
                UserProfileCard(
                    user = it,
                    onSignOutClick = { onAuthEvent(AuthEvent.SignOutClicked) }
                )
                Spacer(modifier = Modifier.height(AppSpacing.Medium))
            }

            CountriesHeader()

            Spacer(modifier = Modifier.height(AppSpacing.Medium))

            CountrySearchField(
                searchQuery = state.searchQuery,
                onSearchQueryChange = { query ->
                    onEvent(CountriesEvent.SearchChanged(query))
                },
                onSearchTriggered = {
                    onEvent(CountriesEvent.SearchSubmitted)
                }
            )

            Spacer(modifier = Modifier.height(AppSpacing.Small))

            RegionFilters(
                selectedRegion = state.selectedRegion,
                onRegionSelected = { region ->
                    onEvent(CountriesEvent.RegionChanged(region))
                }
            )

            Spacer(modifier = Modifier.height(AppSpacing.Medium))

            Box(modifier = Modifier.fillMaxSize()) {
                CountriesContent(
                    state = state,
                    onCountryClick = onCountryClick,
                    onRetryClick = { onEvent(CountriesEvent.RetryClicked) },
                    onLoadNextPage = { onEvent(CountriesEvent.LoadNextPage) }
                )
            }
        }
    }
}

@Composable
private fun CountriesHeader() {
    Column {
        Text(
            text = "EXPLORE THE WORLD",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.tertiary
        )
        Text(
            text = stringResource(R.string.rest_countries_title),
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Discover essential information about nations across the globe.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun RegionFilters(
    selectedRegion: String,
    onRegionSelected: (String) -> Unit
) {
    val regions = listOf("All", "Africa", "Americas", "Asia", "Europe", "Oceania")
    
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(AppSpacing.Small)
    ) {
        items(regions) { region ->
            FilterChip(
                selected = selectedRegion == region,
                onClick = { onRegionSelected(region) },
                label = { Text(text = region) }
            )
        }
    }
}

@Composable
private fun CountriesContent(
    state: CountriesState,
    onCountryClick: (Country) -> Unit,
    onRetryClick: () -> Unit,
    onLoadNextPage: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        when {
            state.errorMessage != null && state.countries.isEmpty() -> {
                ErrorState(
                    message = state.errorMessage,
                    onRetryClick = onRetryClick,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            state.countries.isEmpty() && !state.isLoading -> {
                EmptyState(modifier = Modifier.align(Alignment.Center))
            }

            else -> {
                CountriesList(
                    countries = state.filteredCountries,
                    isLoadingNextPage = state.isLoadingNextPage,
                    hasMoreCountries = state.hasMoreCountries,
                    onCountryClick = onCountryClick,
                    onLoadNextPage = onLoadNextPage
                )
            }
        }

        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun ErrorState(
    message: String,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(AppSpacing.Large),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        Spacer(modifier = Modifier.height(AppSpacing.Medium))
        Button(onClick = onRetryClick) {
            Text(text = stringResource(R.string.retry))
        }
    }
}

@Composable
private fun EmptyState(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(AppSpacing.Large),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "No countries found.",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "Try adjusting your search or filters.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
