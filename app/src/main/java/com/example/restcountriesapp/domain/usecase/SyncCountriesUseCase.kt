package com.example.restcountriesapp.domain.usecase

import com.example.restcountriesapp.domain.repository.CountryRepository

class SyncCountriesUseCase(
    private val countryRepository: CountryRepository
) {
    suspend operator fun invoke() =
        countryRepository.syncCountries()
}