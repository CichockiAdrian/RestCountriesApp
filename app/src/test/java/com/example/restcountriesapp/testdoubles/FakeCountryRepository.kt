package com.example.restcountriesapp.testdoubles

import com.example.restcountriesapp.core.result.DataResult
import com.example.restcountriesapp.domain.model.CountriesPage
import com.example.restcountriesapp.domain.model.Country
import com.example.restcountriesapp.domain.repository.CountryRepository
import com.example.restcountriesapp.core.error.ErrorCode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeCountryRepository : CountryRepository {

    var countries: List<Country> = emptyList()
    var shouldReturnError: Boolean = false
    var errorMessage: String = ErrorCode.NETWORK_ERROR
    private var customPage: CountriesPage? = null

    fun setResult(result: DataResult<CountriesPage>) {
        when (result) {
            is DataResult.Success<*> -> {
                val page = result.data as CountriesPage

                countries = page.countries
                customPage = page
                shouldReturnError = false
                errorMessage = "Test error"
            }

            is DataResult.Failure -> {
                customPage = null
                shouldReturnError = true
                errorMessage = result.message
            }
        }
    }

    override fun observeCountries(
        limit: Int,
        offset: Int,
        query: String?,
        region: String?
    ): Flow<CountriesPage> {
        if (shouldReturnError) {
            return flowOf(
                CountriesPage(
                    countries = emptyList(),
                    nextOffset = offset,
                    hasMore = false
                )
            )
        }

        customPage?.let { page ->
            return flowOf(page)
        }

        var filteredCountries = query
            ?.takeIf { it.isNotBlank() }
            ?.let { searchQuery ->
                countries.filter { country ->
                    country.name.contains(searchQuery, ignoreCase = true) ||
                            country.code.contains(searchQuery, ignoreCase = true)
                }
            }
            ?: countries

        region?.takeIf { it.isNotBlank() && it != "All" }?.let { selectedRegion ->
            filteredCountries = filteredCountries.filter { country ->
                country.region.equals(selectedRegion, ignoreCase = true)
            }
        }

        val pageCountries = filteredCountries
            .drop(offset)
            .take(limit)

        val nextOffset = offset + pageCountries.size

        return flowOf(
            CountriesPage(
                countries = pageCountries,
                nextOffset = nextOffset,
                hasMore = nextOffset < filteredCountries.size
            )
        )
    }

    override fun observeCountryByCode(code: String): Flow<Country?> {
        val country = countries.firstOrNull { country ->
            country.code.equals(code, ignoreCase = true)
        }

        return flowOf(country)
    }

    override suspend fun syncCountries(): DataResult<Unit> {
        return if (shouldReturnError) {
            DataResult.Failure(errorMessage)
        } else {
            DataResult.Success(Unit)
        }
    }
}