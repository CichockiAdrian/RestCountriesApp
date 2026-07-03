package com.example.restcountriesapp.core.result

sealed interface DataResult<out T> {
    data class Success<T>(val data: T) : DataResult<T>
    data class Failure(val message: String) : DataResult<Nothing>
}
