package com.example.restcountriesapp.domain.usecase

import com.example.restcountriesapp.core.result.DataResult
import com.example.restcountriesapp.domain.model.CountriesPage
import com.example.restcountriesapp.domain.repository.CountryRepository

class GetCountriesUseCase(
    private val countryRepository: CountryRepository
) {
    suspend operator fun invoke(
        limit: Int,
        offset: Int
    ): DataResult<CountriesPage> {
        return countryRepository.getCountries(
            limit = limit,
            offset = offset
        )
    }
}