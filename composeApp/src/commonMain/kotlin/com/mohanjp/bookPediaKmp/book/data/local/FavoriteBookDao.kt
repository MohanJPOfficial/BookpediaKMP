package com.mohanjp.bookPediaKmp.book.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.mohanjp.bookPediaKmp.book.data.local.entity.BookEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteBookDao {

    @Upsert
    suspend fun upsertFavoriteBook(book: BookEntity)

    @Query("SELECT * FROM BookEntity")
    fun getAllFavoriteBooks(): Flow<List<BookEntity>>

    @Query("SELECT * FROM BookEntity WHERE id = :id")
    suspend fun getFavoriteBook(id: String): BookEntity?

    @Query("DELETE FROM BookEntity WHERE id = :id")
    suspend fun deleteFavoriteBook(id: String)
}
