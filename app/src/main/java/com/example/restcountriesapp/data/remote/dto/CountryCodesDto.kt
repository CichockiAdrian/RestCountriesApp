package com.example.restcountriesapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CountryCodesDto(
    @SerializedName("alpha_2")
    val alpha2: String?
)
