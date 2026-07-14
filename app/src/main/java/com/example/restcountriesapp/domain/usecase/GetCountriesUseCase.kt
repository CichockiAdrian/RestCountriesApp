package com.example.restcountriesapp.domain.usecase

import com.example.restcountriesapp.domain.repository.CountryRepository

class GetCountriesUseCase(
    private val repository: CountryRepository
) {
    operator fun invoke(
        limit: Int,
        offset: Int,
        query: String? = null,
        region: String? = null
    ) = repository.observeCountries(
        limit = limit,
        offset = offset,
        query = query,
        region = region
    )
}