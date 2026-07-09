package com.example.restcountriesapp.feature.countries.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restcountriesapp.core.error.ErrorCode
import com.example.restcountriesapp.core.result.DataResult
import com.example.restcountriesapp.domain.model.CountryWikiInfo
import com.example.restcountriesapp.domain.usecase.GetCountryByCodeUseCase
import com.example.restcountriesapp.domain.usecase.GetCountryWikiInfoUseCase
import com.example.restcountriesapp.feature.common.UiEffect
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CountryDetailsViewModel(
    private val getCountryByCodeUseCase: GetCountryByCodeUseCase,
    private val getCountryWikiInfoUseCase: GetCountryWikiInfoUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CountryDetailsState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<UiEffect>()
    val effect = _effect.asSharedFlow()

    private var loadCountryJob: Job? = null
    private var loadWikiJob: Job? = null
    private var currentCode: String? = null
    private var currentWikiTitle: String? = null

    fun loadCountry(code: String) {
        if (code.isBlank()) {
            _state.update { state ->
                state.copy(
                    country = null,
                    wikiInfo = null,
                    isLoading = false,
                    isWikiLoading = false,
                    errorMessage = ErrorCode.INVALID_COUNTRY_CODE,
                    wikiErrorMessage = null
                )
            }

            viewModelScope.launch {
                _effect.emit(
                    UiEffect.ShowSnackbar(
                        errorCode = ErrorCode.INVALID_COUNTRY_CODE
                    )
                )
            }

            return
        }

        if (currentCode == code && loadCountryJob?.isActive == true) return

        currentCode = code
        currentWikiTitle = null
        loadCountryJob?.cancel()
        loadWikiJob?.cancel()

        loadCountryJob = viewModelScope.launch {
            _state.update { state ->
                state.copy(
                    isLoading = true,
                    wikiInfo = null,
                    errorMessage = null,
                    wikiErrorMessage = null
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

                if (country == null) {
                    _effect.emit(
                        UiEffect.ShowSnackbar(
                            errorCode = ErrorCode.COUNTRY_NOT_FOUND
                        )
                    )
                }

                country?.name?.let { countryName ->
                    loadWikiInfo(countryName)
                }
            }
        }
    }

    private fun loadWikiInfo(countryName: String) {
        if (countryName.isBlank()) return
        if (currentWikiTitle == countryName && loadWikiJob?.isActive == true) return

        currentWikiTitle = countryName
        loadWikiJob?.cancel()

        loadWikiJob = viewModelScope.launch {
            _state.update { state ->
                state.copy(
                    isWikiLoading = true,
                    wikiErrorMessage = null
                )
            }

            when (val result = getCountryWikiInfoUseCase(countryName)) {
                is DataResult.Success<*> -> {
                    val wikiInfo = result.data as? CountryWikiInfo

                    _state.update { state ->
                        state.copy(
                            wikiInfo = wikiInfo,
                            isWikiLoading = false,
                            wikiErrorMessage = null
                        )
                    }
                }

                is DataResult.Failure -> {
                    _state.update { state ->
                        state.copy(
                            wikiInfo = null,
                            isWikiLoading = false,
                            wikiErrorMessage = result.message
                        )
                    }

                    _effect.emit(
                        UiEffect.ShowSnackbar(
                            errorCode = result.message
                        )
                    )
                }
            }
        }
    }
}