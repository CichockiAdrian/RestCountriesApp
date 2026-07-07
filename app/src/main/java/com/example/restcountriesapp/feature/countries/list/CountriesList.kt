package com.example.restcountriesapp.feature.countries.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.restcountriesapp.R
import com.example.restcountriesapp.domain.model.Country

@Composable
fun CountriesList(
    countries: List<Country>,
    isLoadingNextPage: Boolean,
    hasMoreCountries: Boolean,
    onCountryClick: (Country) -> Unit,
    onLoadNextPage: () -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = countries,
            key = { country -> country.code }
        ) { country ->
            CountryListItem(
                country = country,
                onClick = {
                    onCountryClick(country)
                }
            )
        }

        item {
            if (isLoadingNextPage) {
                Text(
                    text = stringResource(R.string.loading_more_countries),
                    modifier = Modifier.padding(16.dp)
                )
            } else if (hasMoreCountries) {
                Button(
                    onClick = onLoadNextPage,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                ) {
                    Text(text = stringResource(R.string.load_more))
                }
            }
        }
    }
}