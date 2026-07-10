package com.example.restcountriesapp.domain.usecase

import com.example.restcountriesapp.domain.repository.AuthRepository

class ObserveAuthStateUseCase(
    private val repository: AuthRepository
) {
    operator fun invoke() = repository.authState
}
