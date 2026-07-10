package com.example.restcountriesapp.di

import com.example.restcountriesapp.data.repository.AuthRepositoryImpl
import com.example.restcountriesapp.domain.repository.AuthRepository
import com.example.restcountriesapp.domain.usecase.ObserveAuthStateUseCase
import com.example.restcountriesapp.domain.usecase.SignOutUseCase
import com.example.restcountriesapp.feature.auth.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val authModule = module {
    single { FirebaseAuth.getInstance() }
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    factory { ObserveAuthStateUseCase(get()) }
    factory { SignOutUseCase(get()) }
    viewModel { AuthViewModel(get(), get()) }
}
