package com.example.restcountriesapp.di

import com.example.restcountriesapp.home.CountriesViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        CountriesViewModel(getCountriesUseCase = get())
    }
}