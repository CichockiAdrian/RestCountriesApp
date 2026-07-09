package com.example.restcountriesapp.domain.repository

import com.example.restcountriesapp.core.result.DataResult
import com.example.restcountriesapp.domain.model.CountryWikiInfo

interface WikiRepository {
    suspend fun getCountryWikiInfo(countryName: String): DataResult<CountryWikiInfo>
}