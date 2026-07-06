# Changelog

## task/init-app

* Created initial Android project with Kotlin and Jetpack Compose.
* Prepared first development branch.
* Added primitive countries screen in `MainActivity`.
* Added mock countries list.
* Added basic country search.
* Added simple country details view.
* Added local Compose state for search query and selected country.

## task/extract-composables

* Refactored primitive countries screen into smaller composable functions.
* Extracted countries list screen.
* Extracted search field composable.
* Extracted countries list composable.
* Extracted country list item composable.
* Extracted country details screen and details content.
* Kept the same primitive behavior without adding architecture yet.

## task/add-country-model

* Added basic `Country` data model.
* Replaced string-based country list with `Country` objects.
* Moved country details data into the `Country` model.
* Removed primitive helper functions for capital, region and population.
* Updated list, search and details UI to use `Country` instead of `String`.

## task/add-home-state

- Added `HomeState` to describe the countries screen state.
- Replaced separate local state variables with a single state object.
- Moved filtered countries calculation into `HomeState`.
- Prepared screen state structure for future ViewModel refactor.

## task/add-home-events

* Added `HomeEvent` to describe user actions on the countries screen.
* Added events for search changes, country selection and back action.
* Replaced direct state updates in UI callbacks with centralized event handling.
* Prepared the screen logic for a future ViewModel and MVI-style refactor.

## task/add-home-viewmodel

- Added `HomeViewModel` for countries screen logic.
- Moved `HomeState` ownership from composable to ViewModel.
- Moved `HomeEvent` handling from composable to ViewModel.
- Exposed screen state with `StateFlow`.
- Updated Compose UI to observe state and send events to ViewModel.

## task/add-viewmodel-factory

- Added basic `CountriesRepository` for mock country data.
- Updated `HomeViewModel` to receive dependencies through constructor.
- Added custom `HomeViewModelFactory`.
- Replaced manual ViewModel creation with Compose `viewModel(factory = ...)`.
- Prepared dependency creation flow for future Koin integration.

## task/split-home-files

* Split the previous single-file implementation into separate files.
* Moved `Country` model to the `model` package.
* Moved `CountriesRepository` to the `data` package.
* Moved `HomeState`, `HomeEvent`, `HomeViewModel` and `HomeViewModelFactory` to the `home` package.
* Added `HomeRoute` to connect `HomeViewModel` with Compose UI.
* Updated state collection to use `collectAsStateWithLifecycle()`.
* Kept `HomeScreen` focused on rendering state and sending user events.
* Improved code readability and separation of responsibilities.

## task/rest-countries-api

- Added domain layer with `Country` model and `CountryRepository` interface.
- Added `DataResult` for success and failure handling.
- Added Retrofit API interface for REST Countries.
- Added DTO models for API response.
- Added mapper from DTO to domain model.
- Added `CountryRepositoryImpl` as the only class responsible for network data source.
- Added manual `AppContainer` with Retrofit and OkHttp configured through Builder pattern.
- Updated `HomeViewModel` to load countries asynchronously from repository.
- Added loading and error state handling.

## task/rest-countries-api

- Added Retrofit integration with REST Countries v5 API.
- Added OkHttp authorization header using `REST_COUNTRIES_TOKEN` from local configuration.
- Added DTO models for v5 API response structure.
- Added mapper from API DTO to domain `Country` model.
- Added `CountryRepository` abstraction and network implementation.
- Added `DataResult` for success and error handling.
- Updated `HomeViewModel` to load countries asynchronously from repository.
- Added loading, error and retry UI state handling.

## task/add-pagination

- Added paginated loading of countries using `limit` and `offset`.
- Added pagination metadata handling with `nextOffset` and `hasMore`.
- Added `Load more` UI state and next page loading support.
- Moved home screen UI strings to `strings.xml`.

## task/koinimplementation

- Added Koin dependencies for Android and Jetpack Compose integration.
- Added `RestCountriesApplication` to start Koin when the app launches.
- Registered Koin modules for network, repository and ViewModel dependencies.
- Moved `OkHttpClient`, `Retrofit`, `RestCountriesApi`, `CountryRepository` and `HomeViewModel` creation to Koin modules.
- Replaced manual `HomeViewModelFactory` usage with `koinViewModel()`.
- Removed manual dependency passing from `MainActivity` and `HomeRoute`.
- Removed `AppContainer` and `HomeViewModelFactory` after migrating dependency injection to Koin.

## task/add-usecase-layer

- Added `GetCountriesUseCase` between `HomeViewModel` and `CountryRepository`.
- Added Koin `UseCaseModule`.
- Updated `HomeViewModel` to depend on use case instead of repository.
- Updated ViewModel dependency injection to provide `GetCountriesUseCase`.
- Kept existing countries loading and pagination behavior unchanged.

## task/implement-unit-tests

- Implemented unit tests for `GetCountriesUseCase` to verify repository response forwarding (success and failure scenarios).
- Implemented unit tests for `HomeViewModel` to verify state transitions during country list loading (success, failure, loading, and error states).
- Created `FakeCountryRepository` test double to mock repository data sources.
- Configured `MainDispatcherRule` to properly manage and reset the `Dispatchers.Main` coroutine context during testing.