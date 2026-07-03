package com.example.restcountriesapp.di

import com.example.restcountriesapp.home.HomeViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        HomeViewModel(getCountriesUseCase = get())
    }
}