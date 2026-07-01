package com.example.restcountriesapp.data

import com.example.restcountriesapp.model.Country

class CountriesRepository {

    fun getCountries(): List<Country> {
        return listOf(
            Country("Poland", "Warsaw", "Europe", "37M"),
            Country("Germany", "Berlin", "Europe", "83M"),
            Country("France", "Paris", "Europe", "68M"),
            Country("Spain", "Madrid", "Europe", "48M"),
            Country("Italy", "Rome", "Europe", "59M"),
            Country("Portugal", "Lisbon", "Europe", "10M"),
            Country("Norway", "Oslo", "Europe", "5M"),
            Country("Sweden", "Stockholm", "Europe", "10M"),
            Country("Finland", "Helsinki", "Europe", "5M"),
            Country("Japan", "Tokyo", "Asia", "124M"),
            Country("Brazil", "Brasilia", "South America", "203M"),
            Country("Canada", "Ottawa", "North America", "40M")
        )
    }
}