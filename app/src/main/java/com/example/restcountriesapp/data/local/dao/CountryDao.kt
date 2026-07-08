package com.example.restcountriesapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.restcountriesapp.data.local.entity.CountryEntity

@Dao
interface CountryDao {

    @Query(
        """
        SELECT * FROM countries
        WHERE (:query IS NULL OR name LIKE '%' || :query || '%' OR code LIKE '%' || :query || '%')
        ORDER BY name
        LIMIT :limit OFFSET :offset
        """
    )
    suspend fun getCountriesPage(
        limit: Int,
        offset: Int,
        query: String?
    ): List<CountryEntity>

    @Query(
        """
        SELECT COUNT(*) FROM countries
        WHERE (:query IS NULL OR name LIKE '%' || :query || '%' OR code LIKE '%' || :query || '%')
        """
    )
    suspend fun countCountries(query: String?): Int

    @Query("SELECT * FROM countries WHERE code = :code LIMIT 1")
    suspend fun getCountryByCode(code: String): CountryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCountries(countries: List<CountryEntity>)
}