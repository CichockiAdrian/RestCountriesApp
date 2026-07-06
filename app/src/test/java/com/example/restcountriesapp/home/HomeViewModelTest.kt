package com.example.restcountriesapp.home

import com.example.restcountriesapp.core.result.DataResult
import com.example.restcountriesapp.domain.model.CountriesPage
import com.example.restcountriesapp.domain.usecase.GetCountriesUseCase
import com.example.restcountriesapp.testdoubles.FakeCountryRepository
import com.example.restcountriesapp.util.MainDispatcherRule
import kotlinx.coroutines.test.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: FakeCountryRepository
    private lateinit var useCase: GetCountriesUseCase
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        repository = FakeCountryRepository()
        useCase = GetCountriesUseCase(repository)
    }

    @Test
    fun `should emit success state when data is loaded`() = runTest {
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
        viewModel = HomeViewModel(useCase)
        advanceUntilIdle()

        //then
        val state = viewModel.state.value

        assertFalse(state.isLoading)
        assertEquals(page.countries, state.countries)
    }

    @Test
    fun `should emit error state when repository fails`() = runTest {
        //given
        repository.setResult(
            DataResult.Failure("Network error")
        )

        //when
        viewModel = HomeViewModel(useCase)
        advanceUntilIdle()

        //then
        val state = viewModel.state.value

        assertFalse(state.isLoading)
        assertEquals("Network error", state.errorMessage)
        assertTrue(state.countries.isEmpty())
    }
}