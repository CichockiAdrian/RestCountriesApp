package com.example.restcountriesapp.feature.common

sealed interface UiEffect {
    data class ShowSnackbar(
        val errorCode: String
    ) : UiEffect
}