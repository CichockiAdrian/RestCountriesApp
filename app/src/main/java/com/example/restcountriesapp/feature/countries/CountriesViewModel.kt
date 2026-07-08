package com.example.restcountriesapp.feature.countries

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restcountriesapp.core.result.DataResult
import com.example.restcountriesapp.domain.usecase.GetCountriesUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CountriesViewModel(
    private val getCountriesUseCase: GetCountriesUseCase
) : ViewModel() {

    private val pageLimit = 25
    private val searchDebounceMs = 500L

    private val _state = MutableStateFlow(CountriesState())
    val state = _state.asStateFlow()

    private var fetchJob: Job? = null
    private var searchDebounceJob: Job? = null

    init {
        loadCountries(refresh = true)
    }

    fun onEvent(event: CountriesEvent) {
        when (event) {
            CountriesEvent.LoadCountries -> {
                loadCountries(refresh = true)
            }

            CountriesEvent.RetryClicked -> {
                loadCountries(refresh = true)
            }

            CountriesEvent.LoadNextPage -> {
                loadCountries(refresh = false)
            }

            is CountriesEvent.SearchChanged -> {
                _state.update { currentState ->
                    currentState.copy(searchQuery = event.query)
                }

                searchDebounceJob?.cancel()

                if (event.query.isBlank()) {
                    loadCountries(refresh = true)
                } else {
                    searchDebounceJob = viewModelScope.launch {
                        delay(searchDebounceMs)
                        loadCountries(refresh = true)
                    }
                }
            }

            CountriesEvent.SearchSubmitted -> {
                searchDebounceJob?.cancel()
                loadCountries(refresh = true)
            }
        }
    }

    fun loadCountries(refresh: Boolean = false) {
        val currentState = _state.value

        if (!refresh && !currentState.hasMoreCountries) return

        if (refresh) {
            fetchJob?.cancel()
        } else {
            if (currentState.isLoading || currentState.isLoadingNextPage) return
        }

        val query = currentState.searchQuery
        val offset = if (refresh) 0 else currentState.nextOffset

        fetchJob = viewModelScope.launch {
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
                val result = getCountriesUseCase(
                    limit = pageLimit,
                    offset = offset,
                    query = query.takeIf { it.isNotBlank() }
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