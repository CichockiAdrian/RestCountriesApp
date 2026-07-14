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
import com.example.restcountriesapp.feature.auth.AuthViewModel
import com.example.restcountriesapp.feature.common.UiEffect
import com.example.restcountriesapp.feature.countries.details.CountryDetailsRoute
import com.example.restcountriesapp.feature.countries.navigation.CountriesListKey
import com.example.restcountriesapp.feature.countries.navigation.CountryDetailsKey
import com.example.restcountriesapp.ui.theme.AppSpacing
import org.koin.androidx.compose.koinViewModel

@Composable
fun CountriesRoute(
    modifier: Modifier = Modifier
) {
    val viewModel: CountriesViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val navStack by viewModel.navigationStack.collectAsStateWithLifecycle()

    val authViewModel: AuthViewModel = koinViewModel()
    val authState by authViewModel.state.collectAsStateWithLifecycle()
    
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

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
        Box(modifier = Modifier.padding(paddingValues)) {
            NavDisplay(
                backStack = navStack,
                onBack = { viewModel.navigateBack() },
                entryProvider = { key ->
                    when (key) {
                        CountriesListKey -> {
                            NavEntry(key) {
                                CountriesScreen(
                                    state = state,
                                    user = authState.user,
                                    onEvent = viewModel::onEvent,
                                    onAuthEvent = authViewModel::onEvent,
                                    onCountryClick = { country ->
                                        viewModel.navigateToDetails(country.code)
                                    }
                                )
                            }
                        }

                        is CountryDetailsKey -> {
                            NavEntry(key) {
                                CountryDetailsRoute(
                                    countryCode = key.countryCode,
                                    onBackClick = { viewModel.navigateBack() }
                                )
                            }
                        }

                        else -> {
                            NavEntry(Unit) {
                                MissingCountryContent(
                                    onBackClick = { viewModel.navigateBack() }
                                )
                            }
                        }
                    }
                }
            )
        }
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
                onClick = onBackClick,
                modifier = Modifier.padding(top = AppSpacing.Medium)
            ) {
                Text(text = stringResource(R.string.back))
            }
        }
    }
}
