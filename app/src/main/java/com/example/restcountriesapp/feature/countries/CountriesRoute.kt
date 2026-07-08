package com.example.restcountriesapp.feature.countries

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.example.restcountriesapp.feature.countries.details.CountryDetailsContent
import com.example.restcountriesapp.feature.countries.details.CountryDetailsRoute
import com.example.restcountriesapp.feature.countries.navigation.CountriesListKey
import com.example.restcountriesapp.feature.countries.navigation.CountryDetailsKey
import org.koin.androidx.compose.koinViewModel

@Composable
fun CountriesRoute(
    modifier: Modifier = Modifier
) {
    val viewModel: CountriesViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    val backStack = remember {
        mutableStateListOf<Any>(CountriesListKey)
    }

    NavDisplay(
        backStack = backStack,
        onBack = {
            if (backStack.size > 1) {
                backStack.removeLastOrNull()
            }
        },
        entryProvider = { key ->
            when (key) {
                CountriesListKey -> {
                    NavEntry(key) {
                        CountriesScreen(
                            state = state,
                            onEvent = viewModel::onEvent,
                            onCountryClick = { country ->
                                backStack.add(
                                    CountryDetailsKey(
                                        countryCode = country.code
                                    )
                                )
                            },
                            modifier = modifier
                        )
                    }
                }

                is CountryDetailsKey -> {
                    NavEntry(key) {
                        CountryDetailsRoute(
                            countryCode = key.countryCode,
                            onBackClick = {
                                if (backStack.size > 1) {
                                    backStack.removeAt(backStack.lastIndex)
                                }
                            },
                            modifier = modifier
                        )
                    }
                }

                else -> {
                    NavEntry(Unit) {
                        MissingCountryContent(
                            onBackClick = {
                                backStack.removeLastOrNull()
                            },
                            modifier = modifier
                        )
                    }
                }
            }
        }
    )
}

@Composable
private fun MissingCountryContent(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Country not found. Go back and try again."
        )
    }
}