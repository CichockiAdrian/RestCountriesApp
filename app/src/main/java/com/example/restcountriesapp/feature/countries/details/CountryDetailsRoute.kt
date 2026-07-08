package com.example.restcountriesapp.feature.countries.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.restcountriesapp.R
import org.koin.androidx.compose.koinViewModel

@Composable
fun CountryDetailsRoute(
    countryCode: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: CountryDetailsViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(countryCode) {
        viewModel.loadCountry(countryCode)
    }

    when {
        state.isLoading -> {
            CountryDetailsLoadingContent(
                modifier = modifier
            )
        }

        state.errorMessage != null -> {
            CountryDetailsErrorContent(
                message = state.errorMessage.orEmpty(),
                onRetryClick = {
                    viewModel.loadCountry(countryCode)
                },
                onBackClick = onBackClick,
                modifier = modifier
            )
        }

        state.country != null -> {
            CountryDetailsContent(
                country = state.country!!,
                onBackClick = onBackClick,
                modifier = modifier
            )
        }
    }
}

@Composable
private fun CountryDetailsLoadingContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(R.string.loading_countries))
    }
}

@Composable
private fun CountryDetailsErrorContent(
    message: String,
    onRetryClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = message)

        Button(
            onClick = onRetryClick
        ) {
            Text(text = stringResource(R.string.retry))
        }

        Button(
            onClick = onBackClick
        ) {
            Text(text = stringResource(R.string.back))
        }
    }
}