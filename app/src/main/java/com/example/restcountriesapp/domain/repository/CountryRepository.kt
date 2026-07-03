package com.example.restcountriesapp.domain.repository

import com.example.restcountriesapp.core.result.DataResult
import com.example.restcountriesapp.domain.model.CountriesPage

interface CountryRepository {
    suspend fun getCountries(
        limit: Int = 25,
        offset: Int = 0
    ): DataResult<CountriesPage>
}