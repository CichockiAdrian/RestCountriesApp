package com.example.restcountriesapp.domain.repository

import com.example.restcountriesapp.core.result.DataResult
import com.example.restcountriesapp.domain.model.CountriesPage
import com.example.restcountriesapp.domain.model.Country

interface CountryRepository {
    suspend fun getCountries(
        limit: Int = 25,
        offset: Int = 0,
        query: String? = null
    ): DataResult<CountriesPage>

    suspend fun getCountryByCode(code: String): DataResult<Country>
}