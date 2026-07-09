package com.example.restcountriesapp.di

import com.example.restcountriesapp.feature.countries.CountriesViewModel
import com.example.restcountriesapp.feature.countries.details.CountryDetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        CountriesViewModel(
            getCountriesUseCase = get(),
            syncCountriesUseCase = get()
        )
    }

    viewModel {
        CountryDetailsViewModel(
            getCountryByCodeUseCase = get()
        )
    }
}