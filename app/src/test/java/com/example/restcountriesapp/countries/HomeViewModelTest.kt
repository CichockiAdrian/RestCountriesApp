package com.example.restcountriesapp.countries

import com.example.restcountriesapp.core.result.DataResult
import com.example.restcountriesapp.domain.model.CountriesPage
import com.example.restcountriesapp.domain.model.Country
import com.example.restcountriesapp.domain.usecase.GetCountriesUseCase
import com.example.restcountriesapp.feature.countries.CountriesEvent
import com.example.restcountriesapp.feature.countries.CountriesViewModel
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
    private lateinit var viewModel: CountriesViewModel

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
        viewModel = CountriesViewModel(useCase)
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
        viewModel = CountriesViewModel(useCase)
        advanceUntilIdle()

        //then
        val state = viewModel.state.value

        assertFalse(state.isLoading)
        assertEquals("Network error", state.errorMessage)
        assertTrue(state.countries.isEmpty())
    }

    @Test
    fun `should update searchQuery state immediately when SearchChanged is received`() = runTest {
        //given
        repository.setResult(DataResult.Success(CountriesPage(emptyList(), 0, false)))
        viewModel = CountriesViewModel(useCase)
        advanceUntilIdle()

        //when
        viewModel.onEvent(CountriesEvent.SearchChanged("Poland"))

        //then
        assertEquals("Poland", viewModel.state.value.searchQuery)
    }

    @Test
    fun `should reload countries after debounce when SearchChanged is received`() = runTest {
        //given
        val countries = listOf(Country(name = "Poland", capital = "Warsaw", region = "Europe", population = 37000000, code = "PL", flagUrl = null, latitude = null, longitude = null))
        repository.setResult(DataResult.Success(CountriesPage(countries, 1, false)))
        viewModel = CountriesViewModel(useCase)
        advanceUntilIdle()

        //when - send search event and advance past debounce delay (500ms)
        viewModel.onEvent(CountriesEvent.SearchChanged("Poland"))
        advanceTimeBy(600)
        advanceUntilIdle()

        //then
        val state = viewModel.state.value
        assertEquals("Poland", state.searchQuery)
        assertFalse(state.isLoading)
        assertEquals(countries, state.countries)
    }

    @Test
    fun `should not reload countries before debounce delay expires`() = runTest {
        //given
        val initialPage = CountriesPage(emptyList(), 0, false)
        repository.setResult(DataResult.Success(initialPage))
        viewModel = CountriesViewModel(useCase)
        advanceUntilIdle()

        val afterSearchPage = CountriesPage(
            listOf(Country("Poland", "Warsaw", "Europe", 37000000, "PL", null, null, null)), 1, false
        )
        repository.setResult(DataResult.Success(afterSearchPage))

        //when - send search event but advance less than debounce delay
        viewModel.onEvent(CountriesEvent.SearchChanged("Poland"))
        advanceTimeBy(300)

        //then - countries should NOT be reloaded yet (still empty)
        val state = viewModel.state.value
        assertTrue(state.countries.isEmpty())
    }

    @Test
    fun `should immediately reload countries when SearchSubmitted is received`() = runTest {
        //given
        val countries = listOf(Country(name = "Poland", capital = "Warsaw", region = "Europe", population = 37000000, code = "PL", flagUrl = null, latitude = null, longitude = null))
        repository.setResult(DataResult.Success(CountriesPage(countries, 1, false)))
        viewModel = CountriesViewModel(useCase)
        advanceUntilIdle()

        viewModel.onEvent(CountriesEvent.SearchChanged("Poland"))

        //when - submit immediately without waiting for debounce
        viewModel.onEvent(CountriesEvent.SearchSubmitted)
        advanceUntilIdle()

        //then
        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertEquals(countries, state.countries)
    }

    @Test
    fun `should reset countries list when search query changes`() = runTest {
        //given - first load returns 25 items appended
        val firstPage = CountriesPage(
            countries = listOf(Country("Germany", "Berlin", "Europe", 83000000, "DE", null, null, null)),
            nextOffset = 1,
            hasMore = true
        )
        repository.setResult(DataResult.Success(firstPage))
        viewModel = CountriesViewModel(useCase)
        advanceUntilIdle()
        assertEquals(1, viewModel.state.value.countries.size)

        //when - search changes, a fresh page is loaded (refresh = true, so list is cleared first)
        val searchPage = CountriesPage(
            countries = listOf(Country("Poland", "Warsaw", "Europe", 37000000, "PL", null, null, null)),
            nextOffset = 1,
            hasMore = false
        )
        repository.setResult(DataResult.Success(searchPage))
        viewModel.onEvent(CountriesEvent.SearchChanged("Poland"))
        advanceTimeBy(600)
        advanceUntilIdle()

        //then - countries should be the search result, not appended
        val state = viewModel.state.value
        assertEquals(1, state.countries.size)
        assertEquals("Poland", state.countries.first().name)
        assertFalse(state.hasMoreCountries)
    }
}