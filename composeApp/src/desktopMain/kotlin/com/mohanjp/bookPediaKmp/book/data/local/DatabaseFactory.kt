package com.mohanjp.bookPediaKmp.book.data.local

import androidx.room.Room
import androidx.room.RoomDatabase
import java.io.File

actual class DatabaseFactory {
    actual fun create(): RoomDatabase.Builder<FavoriteBookDatabase> {
        val os = System.getProperty("os.name").lowercase()
        val userHome = System.getProperty("user.home")

        val appDir = when {
            os.contains("win") -> File(System.getenv("APPDATA"), "Bookpedia")

            os.contains("mac") -> File(userHome, "Library/Application Support/Bookpedia")

            else -> File(userHome, ".local/share/bookpedia")
        }

        if(!appDir.exists()) {
            appDir.mkdirs()
        }

        val dbFile = File(appDir, FavoriteBookDatabase.DB_NAME)
        return Room.databaseBuilder(dbFile.absolutePath)
    }
}
