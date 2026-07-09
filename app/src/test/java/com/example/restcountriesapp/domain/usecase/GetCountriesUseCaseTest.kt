package com.example.restcountriesapp.domain.usecase

import com.example.restcountriesapp.core.result.DataResult
import com.example.restcountriesapp.domain.model.CountriesPage
import com.example.restcountriesapp.domain.model.Country
import com.example.restcountriesapp.testdoubles.FakeCountryRepository
import com.example.restcountriesapp.core.error.ErrorCode
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetCountriesUseCaseTest {

    private lateinit var repository: FakeCountryRepository
    private lateinit var useCase: GetCountriesUseCase

    @Before
    fun setup() {
        repository = FakeCountryRepository()
        useCase = GetCountriesUseCase(repository)
    }

    @Test
    fun `should return countries page from repository`() = runTest {
        val countries = listOf(
            Country(
                name = "Poland",
                capital = "Warsaw",
                region = "Europe",
                population = 37000000,
                code = "PL",
                flagUrl = null,
                latitude = null,
                longitude = null
            )
        )

        val page = CountriesPage(
            countries = countries,
            nextOffset = 1,
            hasMore = false
        )

        repository.setResult(DataResult.Success(page))

        val result = useCase(
            limit = 25,
            offset = 0,
            query = null
        ).first()

        assertEquals(countries, result.countries)
        assertEquals(1, result.nextOffset)
        assertFalse(result.hasMore)
    }

    @Test
    fun `should return empty page when repository has no cached data`() = runTest {
        repository.setResult(DataResult.Failure(ErrorCode.NETWORK_ERROR))

        val result = useCase(
            limit = 25,
            offset = 0,
            query = null
        ).first()

        assertTrue(result.countries.isEmpty())
        assertEquals(0, result.nextOffset)
        assertFalse(result.hasMore)
    }
}