package com.example.restcountriesapp.di

import com.example.restcountriesapp.data.repository.CountryRepositoryImpl
import com.example.restcountriesapp.domain.repository.CountryRepository
import org.koin.dsl.module

val repositoryModule = module {

    single<CountryRepository> {
        CountryRepositoryImpl(api = get())
    }
}