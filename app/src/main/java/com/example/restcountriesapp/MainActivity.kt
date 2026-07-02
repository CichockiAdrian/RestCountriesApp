package com.example.restcountriesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.restcountriesapp.app.AppContainer
import com.example.restcountriesapp.ui.theme.RestCountriesAppTheme
import com.example.restcountriesapp.home.HomeRoute

class MainActivity : ComponentActivity() {

    private val appContainer = AppContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            RestCountriesAppTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    HomeRoute(
                        countriesRepository = appContainer.countryRepository,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}