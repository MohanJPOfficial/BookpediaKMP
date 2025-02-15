package com.mohanjp.bookPediaKmp.book.presentation.bookList

import com.mohanjp.bookPediaKmp.book.domain.model.Book
import com.mohanjp.bookPediaKmp.core.presentation.util.UiText

data class BookListScreenUiState(
    val searchQuery: String = "Android",
    val searchResults: List<Book> = emptyList(),
    val favoriteBooks: List<Book> = emptyList(),
    val isLoading: Boolean = true,
    val selectedTabIndex: Int = 0,
    val errorMessage: UiText? = null
)

sealed interface BookListScreenUiAction {
    data class OnSearchQueryChange(val query: String) : BookListScreenUiAction
    data class OnBookClick(val book: Book) : BookListScreenUiAction
    data class OnTabSelected(val index: Int) : BookListScreenUiAction
}

sealed interface BookListScreenUiEvent {
    data class NavigateToNextScreen(val book: Book) : BookListScreenUiEvent
}
