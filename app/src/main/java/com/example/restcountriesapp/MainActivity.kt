package com.example.restcountriesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.restcountriesapp.ui.theme.RestCountriesAppTheme
import com.example.restcountriesapp.feature.main.AppRoute
import com.example.restcountriesapp.core.ads.AdsManager

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AdsManager.initialize(this)

        enableEdgeToEdge()
        setContent {
            RestCountriesAppTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    AppRoute(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}