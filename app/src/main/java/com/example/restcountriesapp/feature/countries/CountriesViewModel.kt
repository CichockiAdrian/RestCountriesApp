package com.example.restcountriesapp.feature.countries

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restcountriesapp.core.error.ErrorCode
import com.example.restcountriesapp.core.result.DataResult
import com.example.restcountriesapp.domain.usecase.GetCountriesUseCase
import com.example.restcountriesapp.domain.usecase.SyncCountriesUseCase
import com.example.restcountriesapp.feature.common.UiEffect
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CountriesViewModel(
    private val getCountriesUseCase: GetCountriesUseCase,
    private val syncCountriesUseCase: SyncCountriesUseCase
) : ViewModel() {

    private val pageLimit = 25
    private val searchDebounceMs = 500L

    private val _state = MutableStateFlow(CountriesState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<UiEffect>()
    val effect = _effect.asSharedFlow()

    private var fetchJob: Job? = null
    private var searchDebounceJob: Job? = null
    private var syncJob: Job? = null

    init {
        loadCountries(refresh = true)
        syncCountries()
    }

    fun onEvent(event: CountriesEvent) {
        when (event) {
            CountriesEvent.LoadCountries -> {
                loadCountries(refresh = true)
                syncCountries()
            }

            CountriesEvent.RetryClicked -> {
                loadCountries(refresh = true)
                syncCountries()
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
                    syncCountries()
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
        if (!refresh && (currentState.isLoading || currentState.isLoadingNextPage)) return

        val query = currentState.searchQuery.takeIf { it.isNotBlank() }
        val offset = if (refresh) 0 else currentState.nextOffset

        fetchJob?.cancel()

        fetchJob = viewModelScope.launch {
            _state.update { state ->
                if (refresh) {
                    state.copy(
                        isLoading = state.countries.isEmpty(),
                        countries = emptyList(),
                        nextOffset = 0,
                        hasMoreCountries = true,
                        errorMessage = null
                    )
                } else {
                    state.copy(
                        isLoadingNextPage = true,
                        errorMessage = null
                    )
                }
            }

            getCountriesUseCase(
                limit = pageLimit,
                offset = offset,
                query = query
            ).collectLatest { page ->
                _state.update { state ->
                    val updatedCountries = if (refresh) {
                        page.countries
                    } else {
                        (state.countries + page.countries).distinctBy { country ->
                            country.code
                        }
                    }

                    state.copy(
                        countries = updatedCountries,
                        nextOffset = page.nextOffset,
                        hasMoreCountries = page.hasMore,
                        isLoading = false,
                        isLoadingNextPage = false,
                        errorMessage = if (updatedCountries.isNotEmpty()) {
                            null
                        } else {
                            state.errorMessage
                        }
                    )
                }
            }
        }
    }

    private fun syncCountries() {
        syncJob?.cancel()

        syncJob = viewModelScope.launch {
            when (val result = syncCountriesUseCase()) {
                is DataResult.Success<*> -> {
                    _state.update { state ->
                        state.copy(
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
                            errorMessage = if (state.countries.isEmpty()) {
                                result.message
                            } else {
                                null
                            }
                        )
                    }

                    val snackbarErrorCode = if (_state.value.countries.isEmpty()) {
                        result.message
                    } else {
                        ErrorCode.OFFLINE_MODE
                    }

                    _effect.emit(
                        UiEffect.ShowSnackbar(
                            errorCode = snackbarErrorCode
                        )
                    )
                }
            }
        }
    }
}