package com.example.restcountriesapp.core.crash

interface CrashReporter {
    fun log(message: String)
    fun recordException(throwable: Throwable)
}