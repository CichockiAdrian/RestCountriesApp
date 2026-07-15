package com.example.restcountriesapp.feature.countries.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.restcountriesapp.core.ads.InlineAdaptiveBannerAd
import com.example.restcountriesapp.domain.model.Country
import com.example.restcountriesapp.ui.theme.AppSpacing

private const val AD_INTERVAL = 15

@Composable
fun CountriesList(
    countries: List<Country>,
    isLoadingNextPage: Boolean,
    hasMoreCountries: Boolean,
    onCountryClick: (Country) -> Unit,
    onLoadNextPage: () -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    val shouldLoadMore = remember {
        derivedStateOf {
            val lastVisibleItem =
                listState.layoutInfo.visibleItemsInfo.lastOrNull()
                    ?: return@derivedStateOf false

            lastVisibleItem.index >=
                    listState.layoutInfo.totalItemsCount - 5
        }
    }

    LaunchedEffect(shouldLoadMore.value) {
        if (
            shouldLoadMore.value &&
            hasMoreCountries &&
            !isLoadingNextPage
        ) {
            onLoadNextPage()
        }
    }

    LazyColumn(
        state = listState,
        modifier = modifier,
        contentPadding = PaddingValues(
            bottom = AppSpacing.ExtraLarge
        ),
        verticalArrangement = Arrangement.spacedBy(
            AppSpacing.Medium
        )
    ) {
        countries.forEachIndexed { index, country ->
            item(
                key = "country-${country.code}",
                contentType = "country"
            ) {
                CountryListItem(
                    country = country,
                    onClick = {
                        onCountryClick(country)
                    }
                )
            }

            val countryPosition = index + 1

            val shouldShowAd =
                countryPosition % AD_INTERVAL == 0 &&
                        (
                                index < countries.lastIndex ||
                                        hasMoreCountries
                                )

            if (shouldShowAd) {
                item(
                    key = "inline-ad-$countryPosition",
                    contentType = "advertisement"
                ) {
                    InlineAdaptiveBannerAd(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                vertical = AppSpacing.Small
                            )
                    )
                }
            }
        }

        if (isLoadingNextPage) {
            item(
                key = "loading-next-page",
                contentType = "loading"
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(AppSpacing.Medium),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}