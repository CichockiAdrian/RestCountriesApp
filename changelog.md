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


