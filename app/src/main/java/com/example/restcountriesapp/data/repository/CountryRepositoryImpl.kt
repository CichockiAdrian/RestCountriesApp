package com.example.restcountriesapp.data.repository

import com.example.restcountriesapp.core.result.DataResult
import com.example.restcountriesapp.data.local.dao.CountryDao
import com.example.restcountriesapp.data.mapper.toCountry
import com.example.restcountriesapp.data.mapper.toEntity
import com.example.restcountriesapp.data.remote.RestCountriesApi
import com.example.restcountriesapp.domain.model.CountriesPage
import com.example.restcountriesapp.domain.model.Country
import com.example.restcountriesapp.domain.repository.CountryRepository
import kotlinx.coroutines.CancellationException

class CountryRepositoryImpl(
    private val api: RestCountriesApi,
    private val countryDao: CountryDao
) : CountryRepository {

    override suspend fun getCountries(
        limit: Int,
        offset: Int,
        query: String?
    ): DataResult<CountriesPage> {
        val cleanQuery = query?.takeIf { it.isNotBlank() }

        return try {
            val response = api.getCountries(
                limit = limit,
                offset = offset,
                query = cleanQuery
            )

            val countries = response.data
                ?.objects
                .orEmpty()
                .map { dto ->
                    dto.toCountry()
                }

            countryDao.upsertCountries(
                countries = countries.map { country ->
                    country.toEntity()
                }
            )

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
            getCountriesFromCache(
                limit = limit,
                offset = offset,
                query = cleanQuery,
                fallbackMessage = exception.message ?: "Unknown error"
            )
        }
    }

    override suspend fun getCountryByCode(code: String): DataResult<Country> {
        val cachedCountry = countryDao.getCountryByCode(code)

        if (cachedCountry != null) {
            return DataResult.Success(cachedCountry.toCountry())
        }

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

    private suspend fun getCountriesFromCache(
        limit: Int,
        offset: Int,
        query: String?,
        fallbackMessage: String
    ): DataResult<CountriesPage> {
        val cachedCountries = countryDao
            .getCountriesPage(
                limit = limit,
                offset = offset,
                query = query
            )
            .map { entity ->
                entity.toCountry()
            }

        if (cachedCountries.isEmpty()) {
            return DataResult.Failure(fallbackMessage)
        }

        val totalCachedCountries = countryDao.countCountries(query)
        val nextOffset = offset + cachedCountries.size

        return DataResult.Success(
            CountriesPage(
                countries = cachedCountries,
                nextOffset = nextOffset,
                hasMore = nextOffset < totalCachedCountries
            )
        )
    }
}