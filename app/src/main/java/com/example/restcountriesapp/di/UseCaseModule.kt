package com.example.restcountriesapp.di

import com.example.restcountriesapp.domain.usecase.GetCountriesUseCase
import com.example.restcountriesapp.domain.usecase.GetCountryByCodeUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory {
        GetCountriesUseCase(countryRepository = get())
    }
    factory {
        GetCountryByCodeUseCase(countryRepository = get())
    }
}