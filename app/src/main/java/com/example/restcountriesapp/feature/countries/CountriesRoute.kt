package com.example.restcountriesapp.feature.countries

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.example.restcountriesapp.R
import com.example.restcountriesapp.core.error.ErrorCode
import com.example.restcountriesapp.core.error.ErrorMessageMapper
import com.example.restcountriesapp.feature.common.UiEffect
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
    
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    val backStack = remember {
        mutableStateListOf<Any>(CountriesListKey)
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is UiEffect.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = context.getString(
                            ErrorMessageMapper.toMessageRes(effect.errorCode)
                        )
                    )
                }
            }
        }
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->
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
                                modifier = Modifier.padding(paddingValues)
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
                                modifier = Modifier.padding(paddingValues)
                            )
                        }
                    }

                    else -> {
                        NavEntry(Unit) {
                            MissingCountryContent(
                                onBackClick = {
                                    backStack.removeLastOrNull()
                                },
                                modifier = Modifier.padding(paddingValues)
                            )
                        }
                    }
                }
            }
        )
    }
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
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(
                    ErrorMessageMapper.toMessageRes(ErrorCode.COUNTRY_NOT_FOUND)
                )
            )

            Button(
                onClick = onBackClick
            ) {
                Text(text = stringResource(R.string.back))
            }
        }
    }
}