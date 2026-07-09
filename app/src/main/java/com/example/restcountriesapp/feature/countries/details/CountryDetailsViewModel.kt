package com.example.restcountriesapp.feature.countries.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restcountriesapp.core.error.ErrorCode
import com.example.restcountriesapp.domain.usecase.GetCountryByCodeUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CountryDetailsViewModel(
    private val getCountryByCodeUseCase: GetCountryByCodeUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CountryDetailsState())
    val state = _state.asStateFlow()

    private var loadJob: Job? = null
    private var currentCode: String? = null

    fun loadCountry(code: String) {
        if (code.isBlank()) {
            _state.update { state ->
                state.copy(
                    country = null,
                    isLoading = false,
                    errorMessage = ErrorCode.INVALID_COUNTRY_CODE
                )
            }
            return
        }

        if (currentCode == code && loadJob?.isActive == true) return

        currentCode = code
        loadJob?.cancel()

        loadJob = viewModelScope.launch {
            _state.update { state ->
                state.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            getCountryByCodeUseCase(code).collectLatest { country ->
                _state.update { state ->
                    state.copy(
                        country = country,
                        isLoading = false,
                        errorMessage = if (country == null) {
                            ErrorCode.COUNTRY_NOT_FOUND
                        } else {
                            null
                        }
                    )
                }
            }
        }
    }
}