package com.example.restcountriesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.HistoricalChange
import androidx.compose.ui.unit.dp
import com.example.restcountriesapp.ui.theme.RestCountriesAppTheme
import java.time.temporal.TemporalQuery

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            RestCountriesAppTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    PrimitiveCountriesApp(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun PrimitiveCountriesApp(
    modifier: Modifier = Modifier
) {
    val countries = listOf(
        "Poland",
        "Germany",
        "France",
        "Spain",
        "Italy",
        "Portugal",
        "Norway",
        "Sweden",
        "Finland",
        "Japan",
        "Brazil",
        "Canada"
    )

    var searchQuery by remember { mutableStateOf("") }
    var selectedCountry by remember { mutableStateOf<String?>(null) }

    val filteredCountries = countries.filter { country ->
        country.contains(searchQuery, ignoreCase = true)
    }
    if (selectedCountry == null) {
        CountriesListScreen(
            searchQuery = searchQuery,
            countries = filteredCountries,
            onSearchQueryChange = { searchQuery = it },
            onCountryClick = { country ->
                selectedCountry = country
            },
            modifier = modifier
        )
    } else {
        CountryDetailsScreen(
            countryName = selectedCountry!!,
            onBackClick = {
                selectedCountry = null
            },
            modifier = modifier
        )
    }
}

@Composable
fun CountriesListScreen(
    searchQuery: String,
    countries: List<String>,
    onSearchQueryChange: (String) -> Unit,
    onCountryClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        CountriesHeader()

        Spacer(modifier = Modifier.height(16.dp))

        CountrySearchField(
            searchQuery = searchQuery,
            onSearchQueryChange = onSearchQueryChange
        )

        Spacer(modifier = Modifier.height(16.dp))

        CountriesList(
            countries = countries,
            onCountryClick = onCountryClick
        )
    }
}

@Composable
fun CountriesHeader() {
    Text(
        text = "Rest Countries",
        style = MaterialTheme.typography.headlineMedium
    )
}

@Composable
fun CountrySearchField(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit
) {
    OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearchQueryChange,
        modifier = Modifier.fillMaxWidth(),
        label = {
            Text("Search country")
        }
    )
}

@Composable
fun CountriesList(
    countries: List<String>,
    onCountryClick: (String) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(countries) { country ->
            CountryListItem(
                countryName = country,
                onClick = {
                    onCountryClick(country)
                }
            )
        }
    }
}

@Composable
fun CountryListItem(
    countryName: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)
    ) {
        Text(
            text = countryName,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.titleMedium
        )
    }
}



@Composable
fun CountryDetailsScreen(
    countryName: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = countryName,
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

       CountryDetailsContent(countryName = countryName)

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onBackClick
        ) {
            Text("Back")
        }
    }
}

@Composable
fun CountryDetailsContent(
    countryName: String
) {
    Text(text = "Capital: ${getCapital(countryName)}")
    Text(text = "Region: ${getRegion(countryName)}")
    Text(text = "Population: ${getPopulation(countryName)}")
}

fun getCapital(countryName: String): String {
    return when (countryName) {
        "Poland" -> "Warsaw"
        "Germany" -> "Berlin"
        "France" -> "Paris"
        "Spain" -> "Madrid"
        "Italy" -> "Rome"
        "Portugal" -> "Lisbon"
        "Norway" -> "Oslo"
        "Sweden" -> "Stockholm"
        "Finland" -> "Helsinki"
        "Japan" -> "Tokyo"
        "Brazil" -> "Brasilia"
        "Canada" -> "Ottawa"
        else -> "Unknown"
    }
}

fun getRegion(countryName: String): String {
    return when (countryName) {
        "Japan" -> "Asia"
        "Brazil" -> "South America"
        "Canada" -> "North America"
        else -> "Europe"
    }
}

fun getPopulation(countryName: String): String {
    return when (countryName) {
        "Poland" -> "37M"
        "Germany" -> "83M"
        "France" -> "68M"
        "Spain" -> "48M"
        "Italy" -> "59M"
        "Portugal" -> "10M"
        "Norway" -> "5M"
        "Sweden" -> "10M"
        "Finland" -> "5M"
        "Japan" -> "124M"
        "Brazil" -> "203M"
        "Canada" -> "40M"
        else -> "Unknown"
    }
}