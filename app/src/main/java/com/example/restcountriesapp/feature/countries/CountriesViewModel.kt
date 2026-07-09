package com.example.restcountriesapp.feature.countries

import android.util.Log
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
import kotlinx.coroutines.withTimeoutOrNull

class CountriesViewModel(
    private val getCountriesUseCase: GetCountriesUseCase,
    private val syncCountriesUseCase: SyncCountriesUseCase
) : ViewModel() {

    private val pageLimit = 25
    private val searchDebounceMs = 500L
    private val syncTimeoutMs = 15_000L

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
                // Update query immediately in state so UI shows it
                _state.update { it.copy(searchQuery = event.query) }

                searchDebounceJob?.cancel()
                searchDebounceJob = viewModelScope.launch {
                    delay(searchDebounceMs)
                    loadCountries(refresh = true)
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
        if (!refresh && currentState.isLoadingNextPage) return

        val query = currentState.searchQuery.trim().takeIf { it.isNotBlank() }
        val offset = if (refresh) 0 else currentState.nextOffset

        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            Log.d("CountriesDebug", "loadCountries: refresh=$refresh, query=$query, offset=$offset")

            if (refresh) {
                _state.update { state ->
                    state.copy(
                        // Only show global loading if we have NO countries at all
                        isLoading = state.countries.isEmpty(),
                        nextOffset = 0,
                        hasMoreCountries = true,
                        errorMessage = null
                    )
                }
            } else {
                _state.update { it.copy(isLoadingNextPage = true, errorMessage = null) }
            }

            getCountriesUseCase(
                limit = pageLimit,
                offset = offset,
                query = query
            ).collectLatest { page ->
                Log.d("CountriesDebug", "New page emitted: size=${page.countries.size}")
                
                _state.update { state ->
                    val updatedCountries = if (refresh) {
                        page.countries
                    } else {
                        (state.countries + page.countries).distinctBy { it.code }
                    }

                    state.copy(
                        countries = updatedCountries,
                        nextOffset = page.nextOffset,
                        hasMoreCountries = page.hasMore,
                        isLoading = false,
                        isLoadingNextPage = false,
                        errorMessage = if (updatedCountries.isEmpty() && state.errorMessage != null) {
                            state.errorMessage
                        } else {
                            null
                        }
                    )
                }
            }
        }
    }

    private fun syncCountries() {
        if (syncJob?.isActive == true) return

        syncJob = viewModelScope.launch {
            Log.d("CountriesDebug", "Sync started")
            
            val result = withTimeoutOrNull(syncTimeoutMs) {
                syncCountriesUseCase()
            } ?: DataResult.Failure(ErrorCode.NETWORK_ERROR)

            when (result) {
                is DataResult.Success<*> -> {
                    Log.d("CountriesDebug", "Sync success")
                    _state.update { it.copy(isLoading = false) }
                }

                is DataResult.Failure -> {
                    Log.e("CountriesDebug", "Sync failure: ${result.message}")
                    
                    _state.update { state ->
                        state.copy(
                            isLoading = false,
                            errorMessage = if (state.countries.isEmpty()) result.message else state.errorMessage
                        )
                    }

                    val errorCode = if (_state.value.countries.isEmpty()) {
                        result.message
                    } else {
                        ErrorCode.OFFLINE_MODE
                    }
                    _effect.emit(UiEffect.ShowSnackbar(errorCode))
                }
            }
        }
    }
}
