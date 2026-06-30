# RestCountriesApp

A simple Android application built with Kotlin and Jetpack Compose.

The project displays a list of countries, allows searching by country name and shows basic country details.
The application is developed step by step, starting from a primitive implementation and gradually refactoring it into a cleaner architecture.

## Project purpose

The main goal of this project is not only to build a working app, but also to show the process of improving code structure over time.

The development starts with a simple implementation in one file and then evolves through separate branches into a more structured Android application.

## Development process

The full step-by-step development process is described in [`changelog.md`](./changelog.md).

Each branch represents one architectural improvement, for example:

* primitive Compose implementation,
* extracting smaller composable functions,
* adding a `Country` model,
* introducing screen state,
* adding events,
* moving logic to ViewModel,
* preparing repository/API structure,
* adding dependency injection.

## Current scope

The app currently includes:

* Jetpack Compose UI,
* mock country list,
* country search,
* country details screen,
* local Compose state,
* gradual refactoring toward better architecture.

## Tech stack

* Kotlin
* Jetpack Compose
* Material 3
* Android Studio
* Gradle Kotlin DSL

## Planned architecture direction

The project is being refactored toward a cleaner structure based on:

* UI state,
* events/intents,
* ViewModel,
* models,
* repository layer,
* dependency injection.

Target flow:

```text
Screen
  -> sends user events

ViewModel
  -> handles events
  -> updates state

State
  -> is observed by Compose UI
```

## Running the project

Open the project in Android Studio and run it on an emulator or Android device.

You can also build it from terminal:

```bash
./gradlew build
```

## Notes

This project is intentionally developed from a simple version to a more structured one.
Some earlier branches may contain primitive or less optimal code on purpose, because they are part of the learning and refactoring process.
