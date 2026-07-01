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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.restcountriesapp.ui.theme.RestCountriesAppTheme
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class Country(
    val name: String,
    val capital: String,
    val region: String,
    val population: String
)

data class HomeState(
    val countries: List<Country> = emptyList(),
    val searchQuery: String = "",
    val selectedCountry: Country? = null
) {
    val filteredCountries: List<Country>
        get() = countries.filter { country ->
            country.name.contains(searchQuery, ignoreCase = true)
        }
}

class HomeViewModel : ViewModel() {
    private val initialCountries = listOf(
        Country("Poland", "Warsaw", "Europe", "37M"),
        Country("Germany", "Berlin", "Europe", "83M"),
        Country("France", "Paris", "Europe", "68M"),
        Country("Spain", "Madrid", "Europe", "48M"),
        Country("Italy", "Rome", "Europe", "59M"),
        Country("Portugal", "Lisbon", "Europe", "10M"),
        Country("Norway", "Oslo", "Europe", "5M"),
        Country("Sweden", "Stockholm", "Europe", "10M"),
        Country("Finland", "Helsinki", "Europe", "5M"),
        Country("Japan", "Tokyo", "Asia", "124M"),
        Country("Brazil", "Brasilia", "South America", "203M"),
        Country("Canada", "Ottawa", "North America", "40M")
    )

    private val _state = MutableStateFlow(
        HomeState(
            countries = initialCountries
        )
    )

    val state = _state.asStateFlow()

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.SearchChanged -> {
                _state.update {currentState ->
                    currentState.copy(searchQuery = event.query)
                }
            }

            is HomeEvent.CountryClicked -> {
                _state.update { currentState ->
                    currentState.copy(selectedCountry = event.country)
                }
            }

            HomeEvent.BackClicked -> {
                _state.update { currentState ->
                    currentState.copy(selectedCountry = null)
                }
            }
        }
    }
}

sealed interface HomeEvent {
    data class SearchChanged(val query: String) : HomeEvent
    data class CountryClicked(val country: Country) : HomeEvent
    data object BackClicked : HomeEvent
}

class MainActivity : ComponentActivity() {
    private val homeViewModel = HomeViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            RestCountriesAppTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    PrimitiveCountriesApp(
                        modifier = Modifier.padding(innerPadding),
                        viewModel = homeViewModel
                    )
                }
            }
        }
    }
}

@Composable
fun PrimitiveCountriesApp(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel
) {

    val state by viewModel.state.collectAsState()

    if (state.selectedCountry == null) {
        CountriesListScreen(
            searchQuery = state.searchQuery,
            countries = state.filteredCountries,
            onSearchQueryChange = { query ->
                viewModel.onEvent(HomeEvent.SearchChanged(query))
            },
            onCountryClick = {country ->
                viewModel.onEvent(HomeEvent.CountryClicked(country))
            },
            modifier = modifier
        )
    } else {
        CountryDetailsScreen(
            country = state.selectedCountry!!,
            onBackClick = {
                viewModel.onEvent(HomeEvent.BackClicked)
            },
            modifier = modifier
        )
    }
}

@Composable
fun CountriesListScreen(
    searchQuery: String,
    countries: List<Country>,
    onSearchQueryChange: (String) -> Unit,
    onCountryClick: (Country) -> Unit,
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
    countries: List<Country>,
    onCountryClick: (Country) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(countries) { country ->
            CountryListItem(
                country = country,
                onClick = {
                    onCountryClick(country)
                }
            )
        }
    }
}

@Composable
fun CountryListItem(
    country: Country,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Text(
            text = country.name,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.titleMedium
        )
    }
}


@Composable
fun CountryDetailsScreen(
    country: Country,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = country.name,
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        CountryDetailsContent(country = country)

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
    country: Country
) {
    Text(text = "Capital: ${country.capital}")
    Text(text = "Region: ${country.region}")
    Text(text = "Population: ${country.population}")
}