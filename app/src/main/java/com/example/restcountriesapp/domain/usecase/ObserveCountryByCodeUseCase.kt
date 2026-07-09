package com.example.restcountriesapp.domain.usecase

import com.example.restcountriesapp.domain.repository.CountryRepository

class ObserveCountryByCodeUseCase(
    private val countryRepository: CountryRepository
) {
    operator fun invoke(code: String) =
        countryRepository.observeCountryByCode(code)
}