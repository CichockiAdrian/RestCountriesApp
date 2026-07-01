package com.example.restcountriesapp.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.restcountriesapp.data.CountriesRepository

class HomeViewModelFactory(
    private val countriesRepository: CountriesRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(countriesRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}