package com.example.restcountriesapp.feature.auth

import com.example.restcountriesapp.domain.model.AuthUser

data class AuthState(
    val user: AuthUser? = null,
    val isLoading: Boolean = true
)
