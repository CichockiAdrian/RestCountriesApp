package com.example.restcountriesapp.data.repository

import com.example.restcountriesapp.core.result.DataResult
import com.example.restcountriesapp.data.mapper.toCountry
import com.example.restcountriesapp.data.remote.RestCountriesApi
import com.example.restcountriesapp.domain.model.Country
import com.example.restcountriesapp.domain.repository.CountryRepository

class CountryRepositoryImpl(
    private val api: RestCountriesApi
) : CountryRepository {

    override suspend fun getCountries(): DataResult<List<Country>> {
        return try {
            val response = api.getCountries()

            val countries = response.data
                ?.objects
                .orEmpty()
                .map { dto ->
                    dto.toCountry()
                }

            DataResult.Success(countries)
        } catch (exception: Exception) {
            DataResult.Failure(
                message = exception.message ?: "Unknown error"
            )
        }
    }
}