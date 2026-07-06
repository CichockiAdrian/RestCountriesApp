package com.example.restcountriesapp.data.remote.dto

data class CountriesMetaDto(
    val total: Int?,
    val count: Int?,
    val limit: Int?,
    val offset: Int?,
    val more: Boolean?
)
