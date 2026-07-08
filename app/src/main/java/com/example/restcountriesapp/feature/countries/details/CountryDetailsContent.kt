package com.example.restcountriesapp.feature.countries.details

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.restcountriesapp.R
import com.example.restcountriesapp.domain.model.Country
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CountryDetailsContent(
    country: Country,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(onClick = onBackClick) {
            Text(text = stringResource(R.string.back))
        }

        Spacer(modifier = Modifier.height(18.dp))

        CountryFlagHeader(country = country)

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = country.name,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CountryInfoRow(
                    label = "Capital",
                    value = country.capital.ifBlank { "No capital" }
                )

                CountryInfoRow(
                    label = "Region",
                    value = country.region.ifBlank { "N/A" }
                )

                CountryInfoRow(
                    label = "Population",
                    value = country.population.formatPopulation()
                )

                CountryInfoRow(
                    label = "Code",
                    value = country.code.ifBlank { "N/A" }
                )
            }
        }
    }
}

@Composable
private fun CountryFlagHeader(
    country: Country
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(170.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(24.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        if (country.flagUrl.isNullOrBlank()) {
            Text(
                text = country.code.take(2).ifBlank { "?" },
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            AsyncImage(
                model = country.flagUrl,
                contentDescription = "Flag of ${country.name}",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp)
                    .padding(12.dp),
                contentScale = ContentScale.Fit
            )
        }
    }
}

@Composable
private fun CountryInfoRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}

private fun Long.formatPopulation(): String {
    return NumberFormat.getNumberInstance(Locale.US).format(this)
}