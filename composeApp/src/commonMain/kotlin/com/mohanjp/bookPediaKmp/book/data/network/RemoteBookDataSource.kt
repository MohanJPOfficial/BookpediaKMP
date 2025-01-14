package com.mohanjp.bookPediaKmp.book.data.network

import com.mohanjp.bookPediaKmp.book.data.dto.SearchResponseDto
import com.mohanjp.bookPediaKmp.core.domain.util.DataError
import com.mohanjp.bookPediaKmp.core.domain.util.Result

interface RemoteBookDataSource {

    suspend fun searchBooks(
        query: String,
        resultLimit: Int? = null
    ): Result<SearchResponseDto, DataError.Remote>
}
