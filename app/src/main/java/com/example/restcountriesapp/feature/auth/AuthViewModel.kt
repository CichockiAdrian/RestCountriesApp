package com.example.restcountriesapp.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restcountriesapp.domain.usecase.ObserveAuthStateUseCase
import com.example.restcountriesapp.domain.usecase.SignOutUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AuthViewModel(
    observeAuthStateUseCase: ObserveAuthStateUseCase,
    private val signOutUseCase: SignOutUseCase
) : ViewModel() {

    val state: StateFlow<AuthState> = observeAuthStateUseCase()
        .map { user ->
            AuthState(
                user = user,
                isLoading = false
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AuthState(isLoading = true)
        )

    fun onEvent(event: AuthEvent) {
        when (event) {
            AuthEvent.SignOutClicked -> {
                viewModelScope.launch {
                    signOutUseCase()
                }
            }
            AuthEvent.SignInClicked -> {
                // Handled in Route via launcher
            }
        }
    }
}
