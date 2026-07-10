package com.example.restcountriesapp.feature.auth

sealed interface AuthEvent {
    data object SignInClicked : AuthEvent
    data object SignOutClicked : AuthEvent
}
