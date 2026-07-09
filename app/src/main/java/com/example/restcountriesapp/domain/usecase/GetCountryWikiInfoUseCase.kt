package com.example.restcountriesapp.domain.usecase

import com.example.restcountriesapp.domain.repository.WikiRepository

class GetCountryWikiInfoUseCase(
    private val wikiRepository: WikiRepository
) {
    suspend operator fun invoke(countryName: String) =
        wikiRepository.getCountryWikiInfo(countryName)
}