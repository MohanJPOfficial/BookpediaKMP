package com.mohanjp.bookPediaKmp.book.presentation.bookDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BookDetailViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(BookDetailScreenUiState())
    val uiState = _uiState.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000L),
        _uiState.value
    )

    private val _uiEvent = Channel<BookDetailScreenUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onUiAction(uiAction: BookDetailScreenUiAction) {
        when(uiAction) {
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

    private fun sendUiEvent(event: BookDetailScreenUiEvent) = viewModelScope.launch {
        _uiEvent.send(event)
    }
}