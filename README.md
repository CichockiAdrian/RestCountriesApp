# 🌍 RestCountriesApp

**RestCountriesApp** to nowoczesna aplikacja na Androida służąca do przeglądania informacji o krajach z całego świata. Projekt został stworzony z naciskiem na **Clean Architecture**, **Offline-first** oraz nowoczesne biblioteki Jetpack Compose.

Aplikacja ewoluowała od prostego prototypu do pełnowymiarowej architektury produkcyjnej, dokumentując każdy krok refaktoryzacji.

---

## 🚀 Główne Funkcje

*   🔐 **Logowanie Google**: Bezpieczny dostęp dzięki Firebase Authentication (Google Sign-In).
*   📦 **Offline-first**: Dane są zapisywane lokalnie (Room), dzięki czemu możesz przeglądać kraje bez dostępu do sieci.
*   🔍 **Zaawansowane Wyszukiwanie**: Szybkie filtrowanie krajów po nazwie lub kodzie (np. "PL").
*   📖 **Integracja z Wikipedią**: Automatyczne pobieranie opisów krajów prosto z MediaWiki API.
*   📉 **Paginacja**: Płynne przewijanie listy dzięki ładowaniu danych w paczkach.
*   🛡️ **Stabilność**: Monitorowanie błędów za pomocą Firebase Crashlytics.
*   🌐 **Wielojęzyczność**: Pełne wsparcie dla języka polskiego i angielskiego.

---

## 🛠️ Stos Technologiczny

*   **UI**: Jetpack Compose (Material 3)
*   **Architektura**: Clean Architecture + MVI/UDF (Unidirectional Data Flow)
*   **DI**: Koin (Wstrzykiwanie zależności)
*   **Database**: Room (Single Source of Truth)
*   **Network**: Retrofit + OkHttp + Gson
*   **Navigation**: Navigation 3 (najnowsze podejście oparte na Compose)
*   **Backend**: Firebase (Auth, Crashlytics, Analytics)
*   **Images**: Coil 3
*   **Concurrency**: Kotlin Coroutines & Flow

---

## 🏗️ Architektura Systemu

Projekt podzielony jest na jasne warstwy:

1.  **Data Layer**: Implementacja API i Bazy Danych. Odpowiada za synchronizację danych (Remote -> Local).
2.  **Domain Layer**: Czysty Kotlin. Zawiera logikę biznesową (Use Cases) oraz modele domenowe. Nie zna Androida.
3.  **Presentation Layer**: ViewModele i Composable. Reaguje na stany i wysyła eventy.

---

## 📸 Screeny

| Logowanie | Lista Krajów | Szczegóły |
| :---: | :---: | :---: |
| ![Login](https://github.com/CichockiAdrian/RestCountriesApp/blob/main/Zrzut%20ekranu%202026-07-10%20o%2010.38.07.png) | ![List](https://github.com/CichockiAdrian/RestCountriesApp/blob/main/Zrzut%20ekranu%202026-07-10%20o%2010.39.00.png) | ![Details](https://github.com/CichockiAdrian/RestCountriesApp/blob/main/Zrzut%20ekranu%202026-07-10%20o%2010.38.43.png) |

---

## 📜 Historia Zmian (Changelog)

### task/firebase-auth-google
*   Dodanie logowania przez Google (Firebase Auth + FirebaseUI).
*   Zabezpieczenie aplikacji ekranem logowania.
*   Dodanie profilu użytkownika z opcją wylogowania.

### task/error-handling-crashlytics
*   Konfiguracja Firebase Crashlytics.
*   Wprowadzenie Snackbarów dla błędów sieciowych i trybu offline.
*   Mapowanie błędów domenowych na komunikaty lokalizowane.

### task/wiki-country-details
*   Integracja z MediaWiki API (opisy krajów w szczegółach).
*   Dynamiczne ładowanie danych z zewnątrz.

### task/offline-first-ssot
*   Wprowadzenie Room jako "Single Source of Truth".
*   Synchronizacja danych API -> Baza Danych w tle.
*   Pełna reaktywność UI na zmiany w bazie (Flow).

### task/nav3-navigation
*   Migracja na Navigation 3.
*   Zarządzanie backstackiem za pomocą kluczy nawigacyjnych.

---

## ⚙️ Konfiguracja

Aby uruchomić projekt, potrzebujesz:
1.  Pliku `google-services.json` w folderze `app/` (skonfigurowanego w Firebase Console).
2.  Tokena do API Rest Countries zapisanego w `local.properties`:
    `REST_COUNTRIES_TOKEN=twoj_token`

---

## 👨‍💻 Autor
**Adrian Cichocki**
