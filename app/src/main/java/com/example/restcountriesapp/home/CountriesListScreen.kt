package com.example.restcountriesapp.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.restcountriesapp.R
import com.example.restcountriesapp.domain.model.Country

@Composable
fun CountriesListScreen(
    searchQuery: String,
    countries: List<Country>,
    isLoading: Boolean,
    errorMessage: String?,
    isLoadingNextPage: Boolean,
    hasMoreCountries: Boolean,
    onSearchQueryChange: (String) -> Unit,
    onSearchTriggered: () -> Unit,
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
            onSearchQueryChange = onSearchQueryChange,
            onSearchTriggered = onSearchTriggered
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            Text(text = stringResource(R.string.loading_countries))
            Spacer(modifier = Modifier.height(16.dp))
        }

        errorMessage?.let { message ->
            Text(text = message)

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = onRetryClick) {
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
    onSearchQueryChange: (String) -> Unit,
    onSearchTriggered: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearchQueryChange,
        modifier = Modifier.fillMaxWidth(),
        label = {
            Text(stringResource(R.string.search_country_label))
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                focusManager.clearFocus()
                onSearchTriggered()
            }
        )
    )
}
