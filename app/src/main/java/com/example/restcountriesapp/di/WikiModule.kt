package com.example.restcountriesapp.di

import com.example.restcountriesapp.data.remote.wiki.WikiRemoteDataSource
import com.example.restcountriesapp.data.repository.WikiRepositoryImpl
import com.example.restcountriesapp.domain.repository.WikiRepository
import com.example.restcountriesapp.domain.usecase.GetCountryWikiInfoUseCase
import okhttp3.OkHttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module

val wikiModule = module {
    single(named("wikiOkHttpClient")) {
        OkHttpClient.Builder()
            .build()
    }

    single {
        WikiRemoteDataSource(
            client = get(named("wikiOkHttpClient"))
        )
    }

    single<WikiRepository> {
        WikiRepositoryImpl(
            wikiRemoteDataSource = get()
        )
    }

    factory {
        GetCountryWikiInfoUseCase(
            wikiRepository = get()
        )
    }
}