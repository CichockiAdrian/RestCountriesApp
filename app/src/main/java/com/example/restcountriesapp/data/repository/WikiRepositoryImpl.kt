package com.example.restcountriesapp.data.repository

import com.example.restcountriesapp.core.error.ErrorCode
import com.example.restcountriesapp.core.result.DataResult
import com.example.restcountriesapp.data.remote.wiki.WikiRemoteDataSource
import com.example.restcountriesapp.domain.model.CountryWikiInfo
import com.example.restcountriesapp.domain.repository.WikiRepository

class WikiRepositoryImpl(
    private val wikiRemoteDataSource: WikiRemoteDataSource
) : WikiRepository {

    override suspend fun getCountryWikiInfo(
        countryName: String
    ): DataResult<CountryWikiInfo> {
        return try {
            val wikiInfo = wikiRemoteDataSource.getCountryInfo(countryName)

            if (wikiInfo == null) {
                DataResult.Failure(ErrorCode.WIKI_INFO_NOT_FOUND)
            } else {
                DataResult.Success(wikiInfo)
            }
        } catch (exception: Exception) {
            DataResult.Failure(ErrorCode.NETWORK_ERROR)
        }
    }
}