package com.example.restcountriesapp.domain.usecase

import com.example.restcountriesapp.domain.repository.CountryRepository

class ObserveCountriesUseCase(
    private val countryRepository: CountryRepository
) {
    operator fun invoke(
        limit: Int,
        offset: Int,
        query: String? = null
    ) = countryRepository.observeCountries(
        limit = limit,
        offset = offset,
        query = query
    )
}