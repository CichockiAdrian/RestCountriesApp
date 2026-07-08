package com.example.restcountriesapp.data.repository

import com.example.restcountriesapp.core.result.DataResult
import com.example.restcountriesapp.data.mapper.toCountry
import com.example.restcountriesapp.data.remote.RestCountriesApi
import com.example.restcountriesapp.domain.model.CountriesPage
import com.example.restcountriesapp.domain.repository.CountryRepository
import kotlinx.coroutines.CancellationException
import com.example.restcountriesapp.domain.model.Country

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

    override suspend fun getCountryByCode(code: String): DataResult<Country> {
        return when (
            val result = getCountries(
                limit = 250,
                offset = 0,
                query = null
            )
        ) {
            is DataResult.Success -> {
                val country = result.data.countries.firstOrNull { country ->
                    country.code.equals(code, ignoreCase = true)
                }

                if (country != null) {
                    DataResult.Success(country)
                } else {
                    DataResult.Failure("Country not found")
                }
            }

            is DataResult.Failure -> {
                DataResult.Failure(result.message)
            }
        }
    }
}