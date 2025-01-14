package com.mohanjp.bookPediaKmp.book.data.repository

import com.mohanjp.bookPediaKmp.book.data.mappers.toBook
import com.mohanjp.bookPediaKmp.book.data.network.RemoteBookDataSource
import com.mohanjp.bookPediaKmp.book.domain.model.Book
import com.mohanjp.bookPediaKmp.book.domain.repository.BookRepository
import com.mohanjp.bookPediaKmp.core.domain.util.DataError
import com.mohanjp.bookPediaKmp.core.domain.util.Result
import com.mohanjp.bookPediaKmp.core.domain.util.map

class BookRepositoryImpl(
    private val remoteBookDataSource: RemoteBookDataSource
) : BookRepository {

    override suspend fun searchBooks(query: String): Result<List<Book>, DataError.Remote> {
        return remoteBookDataSource
            .searchBooks(query)
            .map { dto ->
                dto.results.map { it.toBook() }
            }
    }
}
