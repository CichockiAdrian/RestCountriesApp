package com.example.restcountriesapp.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restcountriesapp.core.result.DataResult
import com.example.restcountriesapp.domain.repository.CountryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val countryRepository: CountryRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    init {
        loadCountries()
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            HomeEvent.LoadCountries -> {
                loadCountries()
            }

            HomeEvent.RetryClicked -> {
                loadCountries()
            }

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

    private fun loadCountries() {
        viewModelScope.launch {
            _state.update { currentState ->
                currentState.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            when (val result = countryRepository.getCountries()) {
                is DataResult.Success -> {
                    _state.update { currentState ->
                        currentState.copy(
                            countries = result.data,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                }

                is DataResult.Failure -> {
                    _state.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            errorMessage = result.message
                        )
                    }
                }
            }
        }
    }
}