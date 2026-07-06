package com.example.restcountriesapp.data.repository

import com.example.restcountriesapp.core.result.DataResult
import com.example.restcountriesapp.data.mapper.toCountry
import com.example.restcountriesapp.data.remote.RestCountriesApi
import com.example.restcountriesapp.domain.model.CountriesPage
import com.example.restcountriesapp.domain.repository.CountryRepository
import kotlinx.coroutines.CancellationException

class CountryRepositoryImpl(
    private val api: RestCountriesApi
) : CountryRepository {

    override suspend fun getCountries(
        limit: Int,
        offset: Int,
        query: String?
    ): DataResult<CountriesPage> {
        return try {
            val response = api.getCountries(
                limit = limit,
                offset = offset,
                query = query?.takeIf { it.isNotBlank() }
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
        } catch (e: CancellationException) {
            throw e
        } catch (exception: Exception) {
            DataResult.Failure(
                message = exception.message ?: "Unknown error"
            )
        }
    }
}