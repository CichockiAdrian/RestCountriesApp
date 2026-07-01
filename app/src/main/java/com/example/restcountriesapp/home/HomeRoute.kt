package com.example.restcountriesapp.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.restcountriesapp.data.CountriesRepository

@Composable
fun HomeRoute(
    countriesRepository: CountriesRepository,
    modifier: Modifier = Modifier
) {
    val viewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(countriesRepository)
    )

    val state by viewModel.state.collectAsStateWithLifecycle()

    HomeScreen(
        state = state,
        onEvent = viewModel::onEvent,
        modifier = modifier
    )
}