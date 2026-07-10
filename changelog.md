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

## task/countries-feature-structure

- Renamed the previous `home` package concept into a dedicated `feature.countries` feature package.
- Reorganized countries feature files around a clearer feature-based structure.
- Split countries UI into smaller Compose components:
  - `CountriesScreen`
  - `CountriesList`
  - `CountryListItem`
  - `CountrySearchField`
  - `CountryDetailsContent`
- Removed unnecessary nested screen files from `list` and `details` packages.
- Kept `CountriesScreen` as the main screen responsible for composing the countries UI.
- Kept `CountriesRoute` responsible for connecting `CountriesViewModel` with Compose state collection.
- Kept `StateFlow` for persistent UI state such as countries list, loading state, error message, search query and selected country.
- Improved error rendering so the countries list and `Load more` button are not displayed while an error is shown.
- Verified that REST Countries v5 API configuration, authorization header and manifest internet permissions are set correctly.
- Identified current API loading issue as an emulator DNS resolution problem, not an application architecture or Retrofit configuration issue.

## task/nav3-navigation

- Added Navigation 3 runtime and UI dependencies.
- Added navigation keys for countries list and country details.
- Replaced selected-country conditional screen switching with a Navigation 3 back stack.
- Added `NavDisplay` to render countries list and country details entries.
- Navigated to country details using the selected country code.
- Added back navigation from country details to the countries list.

## task/country-details-viewmodel

- Added `GetCountryByCodeUseCase` for loading a single country by code.
- Added `CountryDetailsState` and `CountryDetailsViewModel`.
- Added `CountryDetailsRoute` to connect Navigation 3 details screen with ViewModel state.
- Moved country details loading away from the countries list state.
- Updated repository contract with `getCountryByCode`.
- Updated fake repository for tests.
- Prepared the details flow for a future offline-first implementation.

## task/offline-first-room

- Added Room database for local countries cache.
- Added `CountryEntity`, `CountryDao` and `RestCountriesDatabase`.
- Added mapper between domain `Country` and local `CountryEntity`.
- Registered Room database and DAO in Koin.
- Updated repository to save API results into local database.
- Added fallback to cached countries when API loading fails.
- Added cached country details loading by country code.
- Prepared the data layer for a fuller offline-first single source of truth implementation.


## task/offline-first-ssot

- Refactored countries data flow to use Room as the single source of truth.
- Updated repository so the UI observes countries from the local database via Flow.
- Added synchronization flow where API data is fetched and saved into Room.
- Added SyncCountriesUseCase for API-to-database synchronization.
- Updated CountriesViewModel to observe local data and trigger background sync.
- Updated CountryDetailsViewModel to observe country details from local cache by code.
- Updated Koin modules to provide the new use cases.
- Updated FakeCountryRepository and unit tests for Flow-based offline-first behavior.
- Fixed API synchronization by fetching countries in chunks of 100 due to free plan request limits.

## task/wiki-country-details

- Added optional Wikipedia information section to the country details screen.
- Integrated MediaWiki Action API to fetch short country descriptions.
- Added Wiki remote data source, repository, use case and domain model.
- Updated CountryDetailsViewModel to load Wiki data without blocking local country details.
- Added Wiki loading and unavailable states.
- Split country details UI into smaller components.
- Added localized strings for country details and Wikipedia information.

## task/error-handling-crashlytics

- Added Firebase Crashlytics configuration.
- Added `CrashReporter` abstraction for reporting non-fatal exceptions.
- Added user-facing error handling with Snackbar effects.
- Added `UiEffect.ShowSnackbar` for one-time UI messages.
- Added `ErrorMessageMapper` to map domain error codes to localized UI messages.
- Added English and Polish error messages for network/offline/not found states.
- Improved countries loading state handling to avoid infinite loading on startup.
- Fixed countries search field behavior during loading state.
- Improved offline-first error handling when sync fails but cached data exists.

## task/firebase-auth-google

- Added Firebase Authentication dependency.
- Added FirebaseUI Auth with Google sign-in provider.
- Added authentication state handling.
- Added sign-in flow using FirebaseUI.
- Added sign-out support.
- Added basic authenticated user profile UI.
- Protected app content behind authentication state.

