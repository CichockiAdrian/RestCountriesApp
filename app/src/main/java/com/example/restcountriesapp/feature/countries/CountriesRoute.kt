package com.example.restcountriesapp.feature.countries

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@Composable
fun CountriesRoute(
    modifier: Modifier = Modifier
) {
    val viewModel: CountriesViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    CountriesScreen(
        state = state,
        onEvent = viewModel::onEvent,
        modifier = modifier
    )
}