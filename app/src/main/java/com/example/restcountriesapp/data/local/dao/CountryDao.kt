package com.example.restcountriesapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.restcountriesapp.data.local.entity.CountryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CountryDao {

    @Query(
        """
        SELECT * FROM countries
        WHERE (:query IS NULL OR name LIKE '%' || :query || '%' OR code LIKE '%' || :query || '%')
        AND (:region IS NULL OR region = :region)
        ORDER BY name
        LIMIT :limit OFFSET :offset
        """
    )
    fun observeCountriesPage(
        limit: Int,
        offset: Int,
        query: String?,
        region: String?
    ): Flow<List<CountryEntity>>

    @Query(
        """
        SELECT COUNT(*) FROM countries
        WHERE (:query IS NULL OR name LIKE '%' || :query || '%' OR code LIKE '%' || :query || '%')
        AND (:region IS NULL OR region = :region)
        """
    )
    fun observeCountriesCount(query: String?, region: String?): Flow<Int>

    @Query("SELECT * FROM countries WHERE code = :code LIMIT 1")
    fun observeCountryByCode(code: String): Flow<CountryEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCountries(countries: List<CountryEntity>)
}