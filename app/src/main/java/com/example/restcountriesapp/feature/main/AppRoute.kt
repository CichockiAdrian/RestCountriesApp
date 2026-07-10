package com.example.restcountriesapp.feature.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.restcountriesapp.feature.auth.AuthRoute
import com.example.restcountriesapp.feature.auth.AuthViewModel
import com.example.restcountriesapp.feature.countries.CountriesRoute
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppRoute(
    modifier: Modifier = Modifier
) {
    val authViewModel: AuthViewModel = koinViewModel()
    val authState by authViewModel.state.collectAsStateWithLifecycle()

    when {
        authState.isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        authState.user == null -> {
            AuthRoute(modifier = modifier)
        }

        else -> {
            CountriesRoute(modifier = modifier)
        }
    }
}
