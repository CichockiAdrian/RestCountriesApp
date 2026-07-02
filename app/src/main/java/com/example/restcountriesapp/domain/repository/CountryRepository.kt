package com.example.restcountriesapp.domain.repository

import com.example.restcountriesapp.core.result.DataResult
import com.example.restcountriesapp.domain.model.Country

interface CountryRepository {
    suspend fun getCountries(): DataResult<List<Country>>
}