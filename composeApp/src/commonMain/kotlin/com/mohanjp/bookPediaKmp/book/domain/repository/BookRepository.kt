package com.mohanjp.bookPediaKmp.book.domain.repository

import com.mohanjp.bookPediaKmp.book.domain.model.Book
import com.mohanjp.bookPediaKmp.core.domain.util.DataError
import com.mohanjp.bookPediaKmp.core.domain.util.EmptyResult
import com.mohanjp.bookPediaKmp.core.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface BookRepository {

    /**
     * remote
     */
    suspend fun searchBooks(query: String): Result<List<Book>, DataError.Remote>
    suspend fun getBookDescription(bookId: String): Result<String?, DataError>

    /**
     * local
     */
    fun getFavoriteBooks(): Flow<List<Book>>
    fun isBookFavorite(bookId: String): Flow<Boolean>
    suspend fun markBookAsFavorite(book: Book): EmptyResult<DataError.Local>
    suspend fun deleteBookFromFavorites(bookId: String)
}
