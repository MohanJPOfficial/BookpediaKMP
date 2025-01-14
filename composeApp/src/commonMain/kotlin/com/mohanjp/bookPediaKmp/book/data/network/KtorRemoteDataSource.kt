package com.mohanjp.bookPediaKmp.book.data.network

import com.mohanjp.bookPediaKmp.book.data.dto.SearchResponseDto
import com.mohanjp.bookPediaKmp.core.data.util.safeCall
import com.mohanjp.bookPediaKmp.core.domain.util.DataError
import com.mohanjp.bookPediaKmp.core.domain.util.Result
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class KtorRemoteDataSource(
    private val httpClient: HttpClient
) : RemoteBookDataSource {

    override suspend fun searchBooks(
        query: String,
        resultLimit: Int?
    ): Result<SearchResponseDto, DataError.Remote> {
        return safeCall<SearchResponseDto> {
            httpClient.get(
                urlString = SEARCH_BOOKS_URL
            ) {
                parameter("q", query)
                parameter("limit", resultLimit)
                parameter("language", "eng")
                parameter(
                    "fields",
                    "key,title,author_name,author_key,cover_edition_key,cover_i,ratings_average,ratings_count,first_publish_year,language,number_of_pages_median,edition_count"
                )
            }
        }
    }

    private companion object {
        const val BASE_URL = "https://openlibrary.org"
        const val SEARCH_BOOKS_URL = "$BASE_URL/search.json"
    }
}
