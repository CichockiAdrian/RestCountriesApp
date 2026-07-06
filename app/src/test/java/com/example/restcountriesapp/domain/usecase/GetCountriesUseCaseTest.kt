package com.example.restcountriesapp.domain.usecase

import com.example.restcountriesapp.core.result.DataResult
import com.example.restcountriesapp.domain.model.CountriesPage
import com.example.restcountriesapp.testdoubles.FakeCountryRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
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
    fun `should return success when repository returns success`() = runTest {
        //given
        val page = CountriesPage(
            countries = emptyList(),
            nextOffset = 20,
            hasMore = true
        )
        repository.setResult(
            DataResult.Success(page)
        )

        //when
        val result = useCase(limit = 10, offset = 0)

        //then
        assertEquals(DataResult.Success(page), result)
    }

    @Test
    fun `should return failure when repository returns failure`() = runTest {
        //given
        repository.setResult(
            DataResult.Failure("Network error")
        )

        //when
        val result = useCase(limit = 10, offset = 0)

        //then
        assertEquals(
            DataResult.Failure("Network error"),
            result
        )
    }
}