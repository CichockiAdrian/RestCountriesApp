package com.example.restcountriesapp.feature.countries.details

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.restcountriesapp.domain.model.Country
import com.example.restcountriesapp.ui.theme.RestCountriesAppTheme

@Preview(showBackground = true, widthDp = 400, name = "Narrow Layout")
@Composable
fun CountryDetailsPreviewNarrow() {
    RestCountriesAppTheme {
        CountryDetailsContent(
            country = mockCountry,
            wikiInfo = null,
            isWikiLoading = false,
            wikiErrorMessage = null,
            onBackClick = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 800, name = "Wide Layout")
@Composable
fun CountryDetailsPreviewWide() {
    RestCountriesAppTheme {
        CountryDetailsContent(
            country = mockCountry,
            wikiInfo = null,
            isWikiLoading = false,
            wikiErrorMessage = null,
            onBackClick = {}
        )
    }
}

private val mockCountry = Country(
    name = "Poland",
    capital = "Warsaw",
    region = "Europe",
    population = 38000000,
    code = "PL",
    flagUrl = null,
    latitude = 52.0,
    longitude = 20.0
)
