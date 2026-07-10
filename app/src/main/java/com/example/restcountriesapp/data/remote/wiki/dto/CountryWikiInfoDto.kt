package com.example.restcountriesapp.data.remote.wiki.dto

data class CountryWikiInfoDto(
    val title: String,
    val description: String?,
    val thumbnailUrl: String?,
    val pageUrl: String?
)