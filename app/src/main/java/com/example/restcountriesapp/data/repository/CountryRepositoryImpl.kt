package com.example.restcountriesapp.data.repository

import com.example.restcountriesapp.core.result.DataResult
import com.example.restcountriesapp.data.mapper.toCountry
import com.example.restcountriesapp.data.remote.RestCountriesApi
import com.example.restcountriesapp.domain.model.CountriesPage
import com.example.restcountriesapp.domain.repository.CountryRepository

class CountryRepositoryImpl(
    private val api: RestCountriesApi
) : CountryRepository {

    override suspend fun getCountries(
        limit: Int,
        offset: Int
    ): DataResult<CountriesPage> {
        return try {
            val response = api.getCountries(
                limit = limit,
                offset = offset
            )

            val countries = response.data
                ?.objects
                .orEmpty()
                .map { dto ->
                    dto.toCountry()
                }

            val meta = response.data?.meta

            val nextOffset = (meta?.offset ?: offset) + (meta?.count ?: countries.size)
            val hasMore = meta?.more ?: false

            DataResult.Success(
                CountriesPage(
                    countries = countries,
                    nextOffset = nextOffset,
                    hasMore = hasMore
                )
            )
        } catch (exception: Exception) {
            DataResult.Failure(
                message = exception.message ?: "Unknown error"
            )
        }
    }
}