package com.example.restcountriesapp.feature.countries

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restcountriesapp.core.error.ErrorCode
import com.example.restcountriesapp.core.result.DataResult
import com.example.restcountriesapp.domain.usecase.GetCountriesUseCase
import com.example.restcountriesapp.domain.usecase.SyncCountriesUseCase
import com.example.restcountriesapp.feature.common.UiEffect
import com.example.restcountriesapp.feature.countries.navigation.CountriesListKey
import com.example.restcountriesapp.feature.countries.navigation.CountryDetailsKey
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull

class CountriesViewModel(
    private val getCountriesUseCase: GetCountriesUseCase,
    private val syncCountriesUseCase: SyncCountriesUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val pageLimit = 25
    private val searchDebounceMs = 500L
    private val syncTimeoutMs = 15_000L

    private val _internalState = MutableStateFlow(CountriesState())
    
    val searchQuery = savedStateHandle.getStateFlow("search_query", "")
    val selectedRegion = savedStateHandle.getStateFlow("selected_region", "All")

    val state: StateFlow<CountriesState> = combine(
        _internalState,
        searchQuery,
        selectedRegion
    ) { state, query, region ->
        state.copy(searchQuery = query, selectedRegion = region)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CountriesState()
    )

    private val _effect = MutableSharedFlow<UiEffect>(replay = 1)
    val effect = _effect.asSharedFlow()

    // Persistent navigation backstack
    private val _navigationStack = MutableStateFlow<List<Any>>(listOf(CountriesListKey))
    val navigationStack: StateFlow<List<Any>> = _navigationStack.asStateFlow()

    private var fetchJob: Job? = null
    private var syncJob: Job? = null

    init {
        loadCountries(refresh = true)
        syncCountries()
        
        viewModelScope.launch {
            combine(searchQuery, selectedRegion) { q, r -> q to r }
                .collectLatest {
                    delay(searchDebounceMs)
                    loadCountries(refresh = true)
                }
        }
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
                savedStateHandle["search_query"] = event.query
            }

            is CountriesEvent.RegionChanged -> {
                savedStateHandle["selected_region"] = event.region
            }

            CountriesEvent.SearchSubmitted -> {
                loadCountries(refresh = true)
            }
        }
    }

    fun navigateToDetails(countryCode: String) {
        _navigationStack.update { it + CountryDetailsKey(countryCode) }
    }

    fun navigateBack() {
        _navigationStack.update { 
            if (it.size > 1) it.dropLast(1) else it
        }
    }

    private fun loadCountries(refresh: Boolean = false) {
        val currentState = state.value

        if (!refresh && !currentState.hasMoreCountries) return
        if (!refresh && currentState.isLoadingNextPage) return

        val query = searchQuery.value.trim().takeIf { it.isNotBlank() }
        val region = selectedRegion.value.takeIf { it != "All" }
        val offset = if (refresh) 0 else currentState.nextOffset

        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            if (refresh) {
                _internalState.update { it.copy(
                    isLoading = it.countries.isEmpty(),
                    nextOffset = 0,
                    hasMoreCountries = true,
                    errorMessage = null
                ) }
            } else {
                _internalState.update { it.copy(isLoadingNextPage = true, errorMessage = null) }
            }

            getCountriesUseCase(
                limit = pageLimit,
                offset = offset,
                query = query,
                region = region
            ).collectLatest { page ->
                _internalState.update { state ->
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
            val result = withTimeoutOrNull(syncTimeoutMs) {
                syncCountriesUseCase()
            } ?: DataResult.Failure(ErrorCode.NETWORK_ERROR)

            when (result) {
                is DataResult.Success<*> -> {
                    _internalState.update { it.copy(isLoading = false) }
                }

                is DataResult.Failure -> {
                    _internalState.update { state ->
                        state.copy(
                            isLoading = false,
                            errorMessage = if (state.countries.isEmpty()) result.message else state.errorMessage
                        )
                    }

                    val errorCode = if (state.value.countries.isEmpty()) {
                        result.message
                    } else {
                        if (result.message == ErrorCode.NETWORK_ERROR) {
                            ErrorCode.OFFLINE_MODE
                        } else {
                            result.message
                        }
                    }
                    _effect.emit(UiEffect.ShowSnackbar(errorCode))
                }
            }
        }
    }
}
