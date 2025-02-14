package com.mohanjp.bookPediaKmp.book.data.repository

import androidx.sqlite.SQLiteException
import com.mohanjp.bookPediaKmp.book.data.local.FavoriteBookDao
import com.mohanjp.bookPediaKmp.book.data.mappers.toBook
import com.mohanjp.bookPediaKmp.book.data.mappers.toBookEntity
import com.mohanjp.bookPediaKmp.book.data.network.RemoteBookDataSource
import com.mohanjp.bookPediaKmp.book.domain.model.Book
import com.mohanjp.bookPediaKmp.book.domain.repository.BookRepository
import com.mohanjp.bookPediaKmp.core.domain.util.DataError
import com.mohanjp.bookPediaKmp.core.domain.util.EmptyResult
import com.mohanjp.bookPediaKmp.core.domain.util.Result
import com.mohanjp.bookPediaKmp.core.domain.util.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BookRepositoryImpl(
    private val remoteBookDataSource: RemoteBookDataSource,
    private val favoriteBookDao: FavoriteBookDao
) : BookRepository {

    /**
     * remote
     */
    override suspend fun searchBooks(query: String): Result<List<Book>, DataError.Remote> {
        return remoteBookDataSource
            .searchBooks(query)
            .map { dto ->
                dto.results.map { it.toBook() }
            }
    }

    override suspend fun getBookDescription(bookId: String): Result<String?, DataError> {
        val localResult = favoriteBookDao.getFavoriteBook(bookId)

        return if (localResult != null) {
            return Result.Success(localResult.description)
        } else {
            remoteBookDataSource
                .getBookDetails(bookId)
                .map { it.description }
        }
    }

    /**
     * local
     */
    override fun getFavoriteBooks(): Flow<List<Book>> {
        return favoriteBookDao
            .getAllFavoriteBooks()
            .map { entities ->
                entities.map { it.toBook() }
            }
    }

    override fun isBookFavorite(bookId: String): Flow<Boolean> {
        return favoriteBookDao
            .getAllFavoriteBooks()
            .map { entities ->
                entities.any { it.id == bookId }
            }
    }

    override suspend fun markBookAsFavorite(book: Book): EmptyResult<DataError.Local> {
        return try {
            favoriteBookDao.upsertFavoriteBook(book.toBookEntity())
            Result.Success(Unit)
        } catch (e: SQLiteException) {
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun deleteBookFromFavorites(bookId: String) {
        favoriteBookDao.deleteFavoriteBook(bookId)
    }
}
