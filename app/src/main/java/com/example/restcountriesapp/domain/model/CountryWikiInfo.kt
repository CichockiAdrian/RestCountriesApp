package com.example.restcountriesapp.domain.model

data class CountryWikiInfo(
    val title: String,
    val description: String?,
    val thumbnailUrl: String?,
    val pageUrl: String?
)