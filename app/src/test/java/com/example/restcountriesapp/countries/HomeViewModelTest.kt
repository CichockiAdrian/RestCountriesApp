package com.example.restcountriesapp.countries

import com.example.restcountriesapp.core.result.DataResult
import com.example.restcountriesapp.domain.model.CountriesPage
import com.example.restcountriesapp.domain.model.Country
import com.example.restcountriesapp.domain.usecase.GetCountriesUseCase
import com.example.restcountriesapp.domain.usecase.SyncCountriesUseCase
import com.example.restcountriesapp.feature.countries.CountriesEvent
import com.example.restcountriesapp.feature.countries.CountriesViewModel
import com.example.restcountriesapp.testdoubles.FakeCountryRepository
import com.example.restcountriesapp.util.MainDispatcherRule
import com.example.restcountriesapp.core.error.ErrorCode
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: FakeCountryRepository
    private lateinit var getCountriesUseCase: GetCountriesUseCase
    private lateinit var syncCountriesUseCase: SyncCountriesUseCase
    private lateinit var viewModel: CountriesViewModel

    @Before
    fun setup() {
        repository = FakeCountryRepository()
        getCountriesUseCase = GetCountriesUseCase(repository)
        syncCountriesUseCase = SyncCountriesUseCase(repository)
    }

    private fun createViewModel(): CountriesViewModel {
        return CountriesViewModel(
            getCountriesUseCase = getCountriesUseCase,
            syncCountriesUseCase = syncCountriesUseCase
        )
    }

    @Test
    fun `should emit success state when data is loaded`() = runTest {
        val page = CountriesPage(
            countries = emptyList(),
            nextOffset = 20,
            hasMore = true
        )

        repository.setResult(DataResult.Success(page))

        viewModel = createViewModel()
        advanceUntilIdle()

        val state = viewModel.state.value

        assertFalse(state.isLoading)
        assertEquals(page.countries, state.countries)
    }

    @Test
    fun `should emit error state when repository fails`() = runTest {
        repository.setResult(DataResult.Failure(ErrorCode.NETWORK_ERROR))

        viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onEvent(CountriesEvent.RetryClicked)
        advanceUntilIdle()

        val state = viewModel.state.value

        assertFalse(state.isLoading)
        assertEquals(ErrorCode.NETWORK_ERROR, state.errorMessage)
        assertTrue(state.countries.isEmpty())
    }

    @Test
    fun `should update searchQuery state immediately when SearchChanged is received`() = runTest {
        repository.setResult(
            DataResult.Success(
                CountriesPage(emptyList(), 0, false)
            )
        )

        viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onEvent(CountriesEvent.SearchChanged("Poland"))

        assertEquals("Poland", viewModel.state.value.searchQuery)
    }

    @Test
    fun `should reload countries after debounce when SearchChanged is received`() = runTest {
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

        repository.setResult(
            DataResult.Success(
                CountriesPage(countries, 1, false)
            )
        )

        viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onEvent(CountriesEvent.SearchChanged("Poland"))
        advanceTimeBy(600)
        advanceUntilIdle()

        val state = viewModel.state.value

        assertEquals("Poland", state.searchQuery)
        assertFalse(state.isLoading)
        assertEquals(countries, state.countries)
    }

    @Test
    fun `should not reload countries before debounce delay expires`() = runTest {
        repository.setResult(
            DataResult.Success(
                CountriesPage(emptyList(), 0, false)
            )
        )

        viewModel = createViewModel()
        advanceUntilIdle()

        val afterSearchPage = CountriesPage(
            countries = listOf(
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
            ),
            nextOffset = 1,
            hasMore = false
        )

        repository.setResult(DataResult.Success(afterSearchPage))

        viewModel.onEvent(CountriesEvent.SearchChanged("Poland"))
        advanceTimeBy(300)

        val state = viewModel.state.value

        assertTrue(state.countries.isEmpty())
    }

    @Test
    fun `should immediately reload countries when SearchSubmitted is received`() = runTest {
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

        repository.setResult(
            DataResult.Success(
                CountriesPage(countries, 1, false)
            )
        )

        viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onEvent(CountriesEvent.SearchChanged("Poland"))
        viewModel.onEvent(CountriesEvent.SearchSubmitted)
        advanceUntilIdle()

        val state = viewModel.state.value

        assertFalse(state.isLoading)
        assertEquals(countries, state.countries)
    }

    @Test
    fun `should reset countries list when search query changes`() = runTest {
        val firstPage = CountriesPage(
            countries = listOf(
                Country(
                    name = "Germany",
                    capital = "Berlin",
                    region = "Europe",
                    population = 83000000,
                    code = "DE",
                    flagUrl = null,
                    latitude = null,
                    longitude = null
                )
            ),
            nextOffset = 1,
            hasMore = true
        )

        repository.setResult(DataResult.Success(firstPage))

        viewModel = createViewModel()
        advanceUntilIdle()

        assertEquals(1, viewModel.state.value.countries.size)

        val searchPage = CountriesPage(
            countries = listOf(
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
            ),
            nextOffset = 1,
            hasMore = false
        )

        repository.setResult(DataResult.Success(searchPage))

        viewModel.onEvent(CountriesEvent.SearchChanged("Poland"))
        advanceTimeBy(600)
        advanceUntilIdle()

        val state = viewModel.state.value

        assertEquals(1, state.countries.size)
        assertEquals("Poland", state.countries.first().name)
        assertFalse(state.hasMoreCountries)
    }
}
