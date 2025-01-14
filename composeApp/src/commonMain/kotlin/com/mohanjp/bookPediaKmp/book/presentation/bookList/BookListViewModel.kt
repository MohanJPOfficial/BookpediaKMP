package com.mohanjp.bookPediaKmp.book.presentation.bookList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class BookListViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(BookListScreenUiState())
    val uiState = _uiState.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000L),
        _uiState.value
    )

    fun onUiAction(uiAction: BookListScreenUiAction) {
        when (uiAction) {
            is BookListScreenUiAction.OnBookClick -> TODO()

            is BookListScreenUiAction.OnSearchQueryChange -> {
                _uiState.update {
                    it.copy(
                        searchQuery = uiAction.query
                    )
                }
            }

            is BookListScreenUiAction.OnTabSelected -> TODO()
        }
    }
}