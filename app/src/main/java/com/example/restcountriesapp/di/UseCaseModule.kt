package com.example.restcountriesapp.di

import com.example.restcountriesapp.domain.usecase.GetCountriesUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory {
        GetCountriesUseCase(countryRepository = get())
    }
}