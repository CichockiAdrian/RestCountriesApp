package com.example.restcountriesapp.domain.repository

import com.example.restcountriesapp.core.result.DataResult
import com.example.restcountriesapp.domain.model.CountriesPage
import com.example.restcountriesapp.domain.model.Country
import kotlinx.coroutines.flow.Flow

interface CountryRepository {

    fun observeCountries(
        limit: Int = 25,
        offset: Int = 0,
        query: String? = null
    ): Flow<CountriesPage>

    fun observeCountryByCode(code: String): Flow<Country?>

    suspend fun syncCountries(): DataResult<Unit>
}