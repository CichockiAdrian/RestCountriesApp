package com.example.restcountriesapp.di

import com.example.restcountriesapp.domain.usecase.GetCountriesUseCase
import com.example.restcountriesapp.domain.usecase.GetCountryByCodeUseCase
import com.example.restcountriesapp.domain.usecase.SyncCountriesUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory { GetCountriesUseCase(get()) }
    factory { GetCountryByCodeUseCase(get()) }
    factory { SyncCountriesUseCase(get()) }
}