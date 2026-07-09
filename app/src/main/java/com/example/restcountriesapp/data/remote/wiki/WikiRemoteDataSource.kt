package com.example.restcountriesapp.data.remote.wiki

import com.example.restcountriesapp.domain.model.CountryWikiInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class WikiRemoteDataSource(
    private val client: OkHttpClient
) {

    suspend fun getCountryInfo(countryName: String): CountryWikiInfo? {
        return withContext(Dispatchers.IO) {
            val url = HttpUrl.Builder()
                .scheme("https")
                .host("en.wikipedia.org")
                .addPathSegment("w")
                .addPathSegment("api.php")
                .addQueryParameter("action", "query")
                .addQueryParameter("format", "json")
                .addQueryParameter("formatversion", "2")
                .addQueryParameter("redirects", "1")
                .addQueryParameter("prop", "extracts|pageimages|info")
                .addQueryParameter("exintro", "1")
                .addQueryParameter("explaintext", "1")
                .addQueryParameter("pithumbsize", "600")
                .addQueryParameter("inprop", "url")
                .addQueryParameter("titles", countryName)
                .build()

            val request = Request.Builder()
                .url(url)
                .header("User-Agent", "RestCountriesApp/1.0 (https://github.com/CichockiAdrian/RestCountriesApp)")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return@withContext null

                val body = response.body?.string().orEmpty()
                if (body.isBlank()) return@withContext null

                val json = JSONObject(body)
                val query = json.optJSONObject("query") ?: return@withContext null
                val pages = query.optJSONArray("pages") ?: return@withContext null
                if (pages.length() == 0) return@withContext null

                val page = pages.optJSONObject(0) ?: return@withContext null
                if (page.optBoolean("missing", false)) return@withContext null

                val title = page.optString("title").takeIf { it.isNotBlank() } ?: countryName
                val description = page.optString("extract").takeIf { it.isNotBlank() }

                val thumbnailUrl = page
                    .optJSONObject("thumbnail")
                    ?.optString("source")
                    ?.takeIf { it.isNotBlank() }

                val pageUrl = page
                    .optString("fullurl")
                    .takeIf { it.isNotBlank() }

                CountryWikiInfo(
                    title = title,
                    description = description,
                    thumbnailUrl = thumbnailUrl,
                    pageUrl = pageUrl
                )
            }
        }
    }
}