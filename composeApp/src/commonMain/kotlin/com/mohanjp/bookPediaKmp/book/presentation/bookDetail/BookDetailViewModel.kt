package com.mohanjp.bookPediaKmp.book.presentation.bookDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.mohanjp.bookPediaKmp.app.Route
import com.mohanjp.bookPediaKmp.book.domain.repository.BookRepository
import com.mohanjp.bookPediaKmp.core.domain.util.onSuccess
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BookDetailViewModel(
    private val bookRepository: BookRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val bookId = savedStateHandle.toRoute<Route.BookDetail>().id

    private val _uiState = MutableStateFlow(BookDetailScreenUiState())
    val uiState = _uiState
        .onStart {
            fetchBookDescription()
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000L),
            _uiState.value
        )

    private val _uiEvent = Channel<BookDetailScreenUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onUiAction(uiAction: BookDetailScreenUiAction) {
        when (uiAction) {
            BookDetailScreenUiAction.OnBackClick -> {
                sendUiEvent(BookDetailScreenUiEvent.NavigateBack)
            }

            BookDetailScreenUiAction.OnFavoriteClick -> {

            }

            is BookDetailScreenUiAction.OnSelectedBookChange -> {
                _uiState.update {
                    it.copy(
                        book = uiAction.book
                    )
                }
            }
        }
    }

    private fun fetchBookDescription() = viewModelScope.launch {
        bookRepository
            .getBookDescription(bookId)
            .onSuccess { description ->
                _uiState.update {
                    it.copy(
                        book = it.book?.copy(
                            description = description
                        ),
                        isLoading = false
                    )
                }
            }
    }

    private fun sendUiEvent(event: BookDetailScreenUiEvent) = viewModelScope.launch {
        _uiEvent.send(event)
    }
}