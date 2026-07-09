package com.example.restcountriesapp.di

import com.example.restcountriesapp.core.crash.CrashReporter
import com.example.restcountriesapp.core.crash.FirebaseCrashReporter
import org.koin.dsl.module

val crashModule = module {
    single<CrashReporter> {
        FirebaseCrashReporter()
    }
}