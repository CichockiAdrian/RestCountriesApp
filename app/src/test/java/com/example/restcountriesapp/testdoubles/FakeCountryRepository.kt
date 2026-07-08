package com.example.restcountriesapp.testdoubles

import com.example.restcountriesapp.core.error.ErrorCode
import com.example.restcountriesapp.core.result.DataResult
import com.example.restcountriesapp.domain.model.CountriesPage
import com.example.restcountriesapp.domain.model.Country
import com.example.restcountriesapp.domain.repository.CountryRepository

class FakeCountryRepository : CountryRepository {

    private var result: DataResult<CountriesPage> =
        DataResult.Success(
            CountriesPage(
                countries = emptyList(),
                nextOffset = 0,
                hasMore = false
            )
        )

    fun setResult(result: DataResult<CountriesPage>) {
        this.result = result
    }

    override suspend fun getCountries(
        limit: Int,
        offset: Int,
        query: String?
    ): DataResult<CountriesPage> {
        return result
    }

    override suspend fun getCountryByCode(code: String): DataResult<Country> {
        return when (val currentResult = result) {
            is DataResult.Success -> {
                val country = currentResult.data.countries.firstOrNull { country ->
                    country.code.equals(code, ignoreCase = true)
                }

                if (country != null) {
                    DataResult.Success(country)
                } else {
                    DataResult.Failure(ErrorCode.COUNTRY_NOT_FOUND)
                }
            }

            is DataResult.Failure -> {
                DataResult.Failure(currentResult.message)
            }
        }
    }
}