package com.mohanjp.bookPediaKmp.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.mohanjp.bookPediaKmp.book.data.local.DatabaseFactory
import com.mohanjp.bookPediaKmp.book.data.local.FavoriteBookDatabase
import com.mohanjp.bookPediaKmp.book.data.network.KtorRemoteDataSource
import com.mohanjp.bookPediaKmp.book.data.network.RemoteBookDataSource
import com.mohanjp.bookPediaKmp.book.data.repository.BookRepositoryImpl
import com.mohanjp.bookPediaKmp.book.domain.repository.BookRepository
import com.mohanjp.bookPediaKmp.book.presentation.SelectedBookViewModel
import com.mohanjp.bookPediaKmp.book.presentation.bookDetail.BookDetailViewModel
import com.mohanjp.bookPediaKmp.book.presentation.bookList.BookListViewModel
import com.mohanjp.bookPediaKmp.core.data.util.HttpClientFactory
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module { 
    single { HttpClientFactory.create(get()) }
    
    singleOf(::KtorRemoteDataSource).bind<RemoteBookDataSource>()
    singleOf(::BookRepositoryImpl).bind<BookRepository>()

    /**
     * Database
     */
    single {
        get<DatabaseFactory>()
            .create()
            .setDriver(BundledSQLiteDriver())
            .build()
    }
    single { get<FavoriteBookDatabase>().favoriteBookDao }

    viewModelOf(::BookListViewModel)
    viewModelOf(::SelectedBookViewModel)
    viewModelOf(::BookDetailViewModel)
}
