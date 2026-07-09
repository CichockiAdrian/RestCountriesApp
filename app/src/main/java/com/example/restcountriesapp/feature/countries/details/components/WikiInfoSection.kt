package com.example.restcountriesapp.feature.countries.details.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.restcountriesapp.R
import com.example.restcountriesapp.domain.model.CountryWikiInfo

@Composable
fun WikiInfoSection(
    wikiInfo: CountryWikiInfo?,
    isLoading: Boolean,
    errorMessage: String?
) {
    Spacer(modifier = Modifier.height(16.dp))

    Card {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.about_country),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            when {
                isLoading -> {
                    CircularProgressIndicator()

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = stringResource(R.string.wiki_info_loading),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                wikiInfo?.description != null -> {
                    Text(
                        text = wikiInfo.description,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = stringResource(R.string.source_wikipedia),
                        style = MaterialTheme.typography.labelSmall
                    )
                }

                errorMessage != null -> {
                    Text(
                        text = stringResource(R.string.wiki_info_unavailable),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}