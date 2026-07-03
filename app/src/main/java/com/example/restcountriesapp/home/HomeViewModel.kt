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

    private val pageLimit = 25

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    init {
        loadCountries(refresh = true)
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            HomeEvent.LoadCountries -> {
                loadCountries(refresh = true)
            }

            HomeEvent.RetryClicked -> {
                loadCountries(refresh = true)
            }

            HomeEvent.LoadNextPage -> {
                loadCountries(refresh = false)
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

    private fun loadCountries(refresh: Boolean) {
        val currentState = _state.value

        if (currentState.isLoading || currentState.isLoadingNextPage) return
        if (!refresh && !currentState.hasMoreCountries) return

        viewModelScope.launch {
            val offset = if (refresh) 0 else currentState.nextOffset

            _state.update { state ->
                if (refresh) {
                    state.copy(
                        isLoading = true,
                        errorMessage = null,
                        countries = emptyList(),
                        nextOffset = 0,
                        hasMoreCountries = true
                    )
                } else {
                    state.copy(
                        isLoadingNextPage = true,
                        errorMessage = null
                    )
                }
            }

            when (
                val result = countryRepository.getCountries(
                    limit = pageLimit,
                    offset = offset
                )
            ) {
                is DataResult.Success -> {
                    _state.update { state ->
                        state.copy(
                            countries = if (refresh) {
                                result.data.countries
                            } else {
                                state.countries + result.data.countries
                            },
                            nextOffset = result.data.nextOffset,
                            hasMoreCountries = result.data.hasMore,
                            isLoading = false,
                            isLoadingNextPage = false,
                            errorMessage = null
                        )
                    }
                }

                is DataResult.Failure -> {
                    _state.update { state ->
                        state.copy(
                            isLoading = false,
                            isLoadingNextPage = false,
                            errorMessage = result.message
                        )
                    }
                }
            }
        }
    }
}