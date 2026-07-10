package com.example.restcountriesapp.domain.repository

import com.example.restcountriesapp.domain.model.AuthUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val authState: Flow<AuthUser?>
    fun getCurrentUser(): AuthUser?
    suspend fun signOut()
}
