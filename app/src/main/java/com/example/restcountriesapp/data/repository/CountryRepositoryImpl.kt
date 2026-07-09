package com.example.restcountriesapp.data.repository

import com.example.restcountriesapp.core.result.DataResult
import com.example.restcountriesapp.data.local.dao.CountryDao
import com.example.restcountriesapp.data.mapper.toCountry
import com.example.restcountriesapp.data.mapper.toEntity
import com.example.restcountriesapp.data.remote.RestCountriesApi
import com.example.restcountriesapp.domain.model.CountriesPage
import com.example.restcountriesapp.domain.model.Country
import com.example.restcountriesapp.domain.repository.CountryRepository
import com.example.restcountriesapp.core.error.ErrorCode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class CountryRepositoryImpl(
    private val api: RestCountriesApi,
    private val countryDao: CountryDao
) : CountryRepository {

    override fun observeCountries(
        limit: Int,
        offset: Int,
        query: String?
    ): Flow<CountriesPage> {
        val cleanQuery = query?.takeIf { it.isNotBlank() }

        return combine(
            countryDao.observeCountriesPage(
                limit = limit,
                offset = offset,
                query = cleanQuery
            ),
            countryDao.observeCountriesCount(cleanQuery)
        ) { entities, totalCount ->
            val countries = entities.map { entity -> entity.toCountry() }
            val nextOffset = offset + countries.size

            CountriesPage(
                countries = countries,
                nextOffset = nextOffset,
                hasMore = nextOffset < totalCount
            )
        }
    }

    override fun observeCountryByCode(code: String): Flow<Country?> {
        return countryDao
            .observeCountryByCode(code)
            .map { entity -> entity?.toCountry() }
    }

    override suspend fun syncCountries(): DataResult<Unit> {
        return try {
            val pageLimit = 100
            var offset = 0
            var hasMore = true

            while (hasMore) {
                val response = api.getCountries(
                    limit = pageLimit,
                    offset = offset,
                    query = null
                )

                val countries = response.data
                    ?.objects
                    .orEmpty()
                    .map { dto -> dto.toCountry() }

                if (countries.isNotEmpty()) {
                    countryDao.upsertCountries(
                        countries = countries.map { country -> country.toEntity() }
                    )
                }

                hasMore = countries.size == pageLimit
                offset += pageLimit
            }

            DataResult.Success(Unit)
        } catch (exception: Exception) {
            DataResult.Failure(exception.message ?: ErrorCode.NETWORK_ERROR)
        }
    }
}