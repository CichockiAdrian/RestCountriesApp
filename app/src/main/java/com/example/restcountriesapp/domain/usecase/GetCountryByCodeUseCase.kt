package com.example.restcountriesapp.domain.usecase

import com.example.restcountriesapp.core.result.DataResult
import com.example.restcountriesapp.domain.model.Country
import com.example.restcountriesapp.domain.repository.CountryRepository

class GetCountryByCodeUseCase(
    private val countryRepository: CountryRepository
) {
    suspend operator fun invoke(code: String): DataResult<Country> {
        return countryRepository.getCountryByCode(code)
    }
}