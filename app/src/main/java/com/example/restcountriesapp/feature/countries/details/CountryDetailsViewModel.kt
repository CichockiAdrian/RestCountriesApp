package com.example.restcountriesapp.feature.countries.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restcountriesapp.core.result.DataResult
import com.example.restcountriesapp.domain.usecase.GetCountryByCodeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CountryDetailsViewModel(
    private val getCountryByCodeUseCase: GetCountryByCodeUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CountryDetailsState())
    val state = _state.asStateFlow()

    fun loadCountry(code: String) {
        val currentCountry = _state.value.country

        if (
            currentCountry?.code.equals(code, ignoreCase = true) ||
            _state.value.isLoading
        ) {
            return
        }

        viewModelScope.launch {
            _state.update { state ->
                state.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            when (val result = getCountryByCodeUseCase(code)) {
                is DataResult.Success -> {
                    _state.update { state ->
                        state.copy(
                            country = result.data,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                }

                is DataResult.Failure -> {
                    _state.update { state ->
                        state.copy(
                            isLoading = false,
                            errorMessage = result.message
                        )
                    }
                }
            }
        }
    }
}