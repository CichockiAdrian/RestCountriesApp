package com.example.restcountriesapp.feature.countries

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.restcountriesapp.domain.model.Country
import com.example.restcountriesapp.ui.theme.RestCountriesAppTheme

@Preview(showBackground = true, name = "Light Theme")
@Composable
fun CountriesScreenPreviewLight() {
    RestCountriesAppTheme(darkTheme = false) {
        CountriesScreen(
            state = CountriesState(
                countries = mockCountries,
                isLoading = false
            ),
            user = null,
            onEvent = {},
            onAuthEvent = {},
            onCountryClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Dark Theme")
@Composable
fun CountriesScreenPreviewDark() {
    RestCountriesAppTheme(darkTheme = true) {
        CountriesScreen(
            state = CountriesState(
                countries = mockCountries,
                isLoading = false
            ),
            user = null,
            onEvent = {},
            onAuthEvent = {},
            onCountryClick = {}
        )
    }
}

private val mockCountries = listOf(
    Country(
        name = "Poland",
        capital = "Warsaw",
        region = "Europe",
        population = 38000000,
        code = "PL",
        flagUrl = null,
        latitude = 52.0,
        longitude = 20.0
    ),
    Country(
        name = "Germany",
        capital = "Berlin",
        region = "Europe",
        population = 83000000,
        code = "DE",
        flagUrl = null,
        latitude = 51.0,
        longitude = 9.0
    )
)
