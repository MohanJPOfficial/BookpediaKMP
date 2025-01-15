package com.mohanjp.bookPediaKmp.di

import com.mohanjp.bookPediaKmp.book.data.network.KtorRemoteDataSource
import com.mohanjp.bookPediaKmp.book.data.network.RemoteBookDataSource
import com.mohanjp.bookPediaKmp.book.data.repository.BookRepositoryImpl
import com.mohanjp.bookPediaKmp.book.domain.repository.BookRepository
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
    
    viewModelOf(::BookListViewModel)
}
