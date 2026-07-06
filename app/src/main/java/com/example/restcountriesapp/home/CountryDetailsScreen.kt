package com.example.restcountriesapp.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.restcountriesapp.R
import com.example.restcountriesapp.domain.model.Country

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

        Button(onClick = onBackClick) {
            Text(stringResource(R.string.back))
        }
    }
}

@Composable
fun CountryDetailsContent(
    country: Country
) {
    Text(text = stringResource(R.string.country_capital, country.capital))
    Text(text = stringResource(R.string.country_region, country.region))
    Text(text = stringResource(R.string.country_population, country.population.toString()))
}
