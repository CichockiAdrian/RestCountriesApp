package com.example.restcountriesapp.feature.countries.details.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.restcountriesapp.R
import com.example.restcountriesapp.domain.model.Country
import com.example.restcountriesapp.domain.model.CountryWikiInfo
import java.text.NumberFormat

@Composable
fun CountryDetailsContent(
    country: Country,
    wikiInfo: CountryWikiInfo?,
    isWikiLoading: Boolean,
    wikiErrorMessage: String?,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Button(onClick = onBackClick) {
            Text(text = stringResource(R.string.back))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = country.name,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                DetailRow(
                    label = stringResource(R.string.country_code),
                    value = country.code
                )

                DetailRow(
                    label = stringResource(R.string.capital),
                    value = country.capital
                )

                DetailRow(
                    label = stringResource(R.string.region),
                    value = country.region
                )

                DetailRow(
                    label = stringResource(R.string.population),
                    value = NumberFormat.getInstance().format(country.population)
                )

                country.latitude?.let { latitude ->
                    DetailRow(
                        label = stringResource(R.string.latitude),
                        value = latitude.toString()
                    )
                }

                country.longitude?.let { longitude ->
                    DetailRow(
                        label = stringResource(R.string.longitude),
                        value = longitude.toString()
                    )
                }
            }
        }

        WikiInfoSection(
            wikiInfo = wikiInfo,
            isLoading = isWikiLoading,
            errorMessage = wikiErrorMessage
        )
    }
}