package com.example.restcountriesapp.feature.countries.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
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
import com.example.restcountriesapp.domain.model.CountryWikiInfo
import com.example.restcountriesapp.feature.countries.details.components.DetailRow
import com.example.restcountriesapp.feature.countries.details.components.WikiInfoSection
import com.example.restcountriesapp.ui.theme.AppDimensions
import com.example.restcountriesapp.ui.theme.AppSpacing
import java.text.NumberFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryDetailsContent(
    country: Country,
    wikiInfo: CountryWikiInfo?,
    isWikiLoading: Boolean,
    wikiErrorMessage: String?,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        val isWide = maxWidth > 600.dp
        
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TopAppBar(
                title = {},
                navigationIcon = {
                    TextButton(onClick = onBackClick) {
                        Text(text = "← " + stringResource(R.string.back))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )

            if (isWide) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = AppSpacing.Large)
                        .verticalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(AppSpacing.ExtraLarge)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        CountryVisualSection(country = country)
                    }
                    Column(modifier = Modifier.weight(1.2f)) {
                        CountryInformationSection(
                            country = country,
                            wikiInfo = wikiInfo,
                            isWikiLoading = isWikiLoading,
                            wikiErrorMessage = wikiErrorMessage
                        )
                        Spacer(modifier = Modifier.height(AppSpacing.ExtraLarge))
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(AppSpacing.Medium)
                ) {
                    CountryVisualSection(country = country)
                    Spacer(modifier = Modifier.height(AppSpacing.Large))
                    CountryInformationSection(
                        country = country,
                        wikiInfo = wikiInfo,
                        isWikiLoading = isWikiLoading,
                        wikiErrorMessage = wikiErrorMessage
                    )
                    Spacer(modifier = Modifier.height(AppSpacing.ExtraLarge))
                }
            }
        }
    }
}

@Composable
private fun CountryVisualSection(country: Country) {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(AppDimensions.FlagAspectRatio)
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            if (country.flagUrl.isNullOrBlank()) {
                Text(
                    text = country.code,
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                AsyncImage(
                    model = country.flagUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillBounds
                )
            }
        }
        
        Spacer(modifier = Modifier.height(AppSpacing.Large))
        
        Text(
            text = country.code,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.tertiary
        )
        Text(
            text = country.name,
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = country.region,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun CountryInformationSection(
    country: Country,
    wikiInfo: CountryWikiInfo?,
    isWikiLoading: Boolean,
    wikiErrorMessage: String?
) {
    Column {
        Text(
            text = "VITAL STATISTICS",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(bottom = AppSpacing.Small)
        )
        
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surface,
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
        ) {
            Column(modifier = Modifier.padding(AppSpacing.Medium)) {
                DetailRow(
                    label = stringResource(R.string.capital),
                    value = country.capital.ifBlank { "Unknown" }
                )
                DetailRow(
                    label = stringResource(R.string.population),
                    value = NumberFormat.getInstance().format(country.population)
                )
                country.latitude?.let {
                    DetailRow(label = stringResource(R.string.latitude), value = it.toString())
                }
                country.longitude?.let {
                    DetailRow(label = stringResource(R.string.longitude), value = it.toString())
                }
            }
        }

        Spacer(modifier = Modifier.height(AppSpacing.Large))

        WikiInfoSection(
            wikiInfo = wikiInfo,
            isLoading = isWikiLoading,
            errorMessage = wikiErrorMessage
        )
    }
}
