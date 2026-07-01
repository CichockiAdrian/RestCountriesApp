package com.example.restcountriesapp.home

import androidx.lifecycle.ViewModel
import com.example.restcountriesapp.data.CountriesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HomeViewModel(
    private val countriesRepository: CountriesRepository

) : ViewModel() {

    private val _state = MutableStateFlow(
        HomeState(
            countries = countriesRepository.getCountries()
        )
    )

    val state = _state.asStateFlow()

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.SearchChanged -> {
                _state.update { currentState ->
                    currentState.copy(searchQuery = event.query)
                }
            }

            is HomeEvent.CountryClicked -> {
                _state.update { currentState ->
                    currentState.copy(selectedCountry = event.country)
                }
            }

            HomeEvent.BackClicked -> {
                _state.update { currentState ->
                    currentState.copy(selectedCountry = null)
                }
            }
        }
    }
}