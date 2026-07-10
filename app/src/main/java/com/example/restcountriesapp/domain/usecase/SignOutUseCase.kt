package com.example.restcountriesapp.domain.usecase

import com.example.restcountriesapp.domain.repository.AuthRepository

class SignOutUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke() = repository.signOut()
}
