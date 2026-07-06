package com.example.restcountriesapp.testdoubles

import androidx.compose.ui.geometry.Offset
import com.example.restcountriesapp.core.result.DataResult
import com.example.restcountriesapp.domain.model.CountriesPage
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

    fun setResult(result: DataResult<CountriesPage>){
        this.result = result
    }

    override suspend fun getCountries(
        limit: Int,
        offset: Int
    ): DataResult<CountriesPage> {
        return result
    }
}