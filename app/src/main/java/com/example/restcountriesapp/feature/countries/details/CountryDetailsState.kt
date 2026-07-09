package com.example.restcountriesapp.feature.countries.details

import com.example.restcountriesapp.domain.model.Country
import com.example.restcountriesapp.domain.model.CountryWikiInfo

data class CountryDetailsState(
    val country: Country? = null,
    val wikiInfo: CountryWikiInfo? = null,
    val isLoading: Boolean = false,
    val isWikiLoading: Boolean = false,
    val errorMessage: String? = null,
    val wikiErrorMessage: String? = null
)