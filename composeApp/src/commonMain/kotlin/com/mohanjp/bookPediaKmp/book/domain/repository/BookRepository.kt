package com.mohanjp.bookPediaKmp.book.domain.repository

import com.mohanjp.bookPediaKmp.book.domain.model.Book
import com.mohanjp.bookPediaKmp.core.domain.util.DataError
import com.mohanjp.bookPediaKmp.core.domain.util.Result

interface BookRepository {
    suspend fun searchBooks(query: String): Result<List<Book>, DataError.Remote>
}
