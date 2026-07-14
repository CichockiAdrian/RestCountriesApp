package com.example.restcountriesapp.feature.countries.details.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.restcountriesapp.R
import com.example.restcountriesapp.domain.model.CountryWikiInfo
import com.example.restcountriesapp.ui.theme.AppSpacing

@Composable
fun WikiInfoSection(
    wikiInfo: CountryWikiInfo?,
    isLoading: Boolean,
    errorMessage: String?
) {
    Column {
        Text(
            text = "EDITORIAL SUMMARY",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(bottom = AppSpacing.Small)
        )
        
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
        ) {
            Column(
                modifier = Modifier.padding(AppSpacing.Medium)
            ) {
                Text(
                    text = stringResource(R.string.about_country),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(AppSpacing.Small))

                when {
                    isLoading -> {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(vertical = AppSpacing.Medium)
                        )
                    }

                    wikiInfo?.description != null -> {
                        Text(
                            text = wikiInfo.description,
                            style = MaterialTheme.typography.bodyLarge,
                            lineHeight = 24.sp
                        )

                        Spacer(modifier = Modifier.height(AppSpacing.Medium))

                        Text(
                            text = stringResource(R.string.source_wikipedia),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    }

                    errorMessage != null -> {
                        Text(
                            text = stringResource(R.string.wiki_info_unavailable),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
