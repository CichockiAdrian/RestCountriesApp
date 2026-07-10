package com.example.restcountriesapp.data.repository

import com.example.restcountriesapp.core.crash.CrashReporter
import com.example.restcountriesapp.core.error.ErrorCode
import com.example.restcountriesapp.core.result.DataResult
import com.example.restcountriesapp.data.mapper.toDomain
import com.example.restcountriesapp.data.remote.wiki.WikiRemoteDataSource
import com.example.restcountriesapp.domain.model.CountryWikiInfo
import com.example.restcountriesapp.domain.repository.WikiRepository

class WikiRepositoryImpl(
    private val wikiRemoteDataSource: WikiRemoteDataSource,
    private val crashReporter: CrashReporter
) : WikiRepository {

    override suspend fun getCountryWikiInfo(
        countryName: String
    ): DataResult<CountryWikiInfo> {
        return try {
            val wikiInfoDto = wikiRemoteDataSource.getCountryInfo(countryName)

            if (wikiInfoDto == null || wikiInfoDto.description.isNullOrBlank()) {
                DataResult.Failure(ErrorCode.WIKI_INFO_NOT_FOUND)
            } else {
                DataResult.Success(wikiInfoDto.toDomain())
            }
        } catch (exception: Exception) {
            crashReporter.recordException(exception)
            DataResult.Failure(ErrorCode.NETWORK_ERROR)
        }
    }
}