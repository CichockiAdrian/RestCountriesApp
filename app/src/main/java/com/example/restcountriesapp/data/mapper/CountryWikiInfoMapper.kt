package com.example.restcountriesapp.data.mapper

import com.example.restcountriesapp.data.remote.wiki.dto.CountryWikiInfoDto
import com.example.restcountriesapp.domain.model.CountryWikiInfo

fun CountryWikiInfoDto.toDomain(): CountryWikiInfo {
    return CountryWikiInfo(
        title = title,
        description = description,
        thumbnailUrl = thumbnailUrl,
        pageUrl = pageUrl
    )
}