package com.example.restcountriesapp.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.restcountriesapp.domain.model.Country
import com.example.restcountriesapp.domain.repository.CountryRepository
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeRoute(
    modifier: Modifier = Modifier
) {
    val viewModel: HomeViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    HomeScreen(
        state = state,
        onEvent = viewModel::onEvent,
        modifier = modifier
    )
}