package com.example.restcountriesapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CountryFlagDto(
    @SerializedName("url_png")
    val urlPng: String?,

    @SerializedName("url_svg")
    val urlSvg: String?
)
