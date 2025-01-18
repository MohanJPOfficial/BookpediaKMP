package com.mohanjp.bookPediaKmp.book.presentation.bookDetail

import com.mohanjp.bookPediaKmp.book.domain.model.Book

data class BookDetailScreenUiState(
    val isLoading: Boolean = true,
    val isFavorite: Boolean = false,
    val book: Book? = null
)

sealed interface BookDetailScreenUiAction {
    data class OnSelectedBookChange(val book: Book) : BookDetailScreenUiAction
    data object OnBackClick : BookDetailScreenUiAction
    data object OnFavoriteClick : BookDetailScreenUiAction
}

sealed interface BookDetailScreenUiEvent {
    data object NavigateBack : BookDetailScreenUiEvent
}
