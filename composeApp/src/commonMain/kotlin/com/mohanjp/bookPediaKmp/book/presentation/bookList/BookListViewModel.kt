package com.mohanjp.bookPediaKmp.book.presentation.bookList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohanjp.bookPediaKmp.book.domain.model.Book
import com.mohanjp.bookPediaKmp.book.domain.repository.BookRepository
import com.mohanjp.bookPediaKmp.core.domain.util.onError
import com.mohanjp.bookPediaKmp.core.domain.util.onSuccess
import com.mohanjp.bookPediaKmp.core.presentation.util.toUiText
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class BookListViewModel(
    private val bookRepository: BookRepository
) : ViewModel() {

    private var cachedBooks = emptyList<Book>()
    private var searchJob: Job? = null

    private val _uiState = MutableStateFlow(BookListScreenUiState())
    val uiState = _uiState
        .onStart {
            if (cachedBooks.isEmpty()) {
                observeSearchQuery()
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000L),
            _uiState.value
        )

    private val _uiEvent = Channel<BookListScreenUiEvent>()
    val  uiEvent = _uiEvent.receiveAsFlow()

    fun onUiAction(uiAction: BookListScreenUiAction) {
        when (uiAction) {
            is BookListScreenUiAction.OnBookClick -> {
                sendUiEvent(BookListScreenUiEvent.NavigateToNextScreen(
                    book = uiAction.book
                ))
            }

            is BookListScreenUiAction.OnSearchQueryChange -> {
                _uiState.update {
                    it.copy(
                        searchQuery = uiAction.query
                    )
                }
            }

            is BookListScreenUiAction.OnTabSelected -> {
                _uiState.update {
                    it.copy(
                        selectedTabIndex = uiAction.index
                    )
                }
            }
        }
    }

    private fun observeSearchQuery() {
        uiState.map { it.searchQuery }
            .distinctUntilChanged()
            .debounce(500L)
            .onEach { query ->
                when {
                    query.isBlank() -> {
                        _uiState.update {
                            it.copy(
                                errorMessage = null,
                                searchResults = cachedBooks
                            )
                        }
                    }

                    query.length >= 2 -> {
                        searchJob?.cancel()
                        searchJob = searchBooks(query)
                    }
                }
            }.launchIn(viewModelScope)
    }

    private fun searchBooks(query: String) = viewModelScope.launch {
        _uiState.update {
            it.copy(
                isLoading = true
            )
        }

        bookRepository
            .searchBooks(query)
            .onSuccess { searchResults ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = null,
                        searchResults = searchResults
                    )
                }
            }.onError { error ->
                _uiState.update {
                    it.copy(
                        searchResults = emptyList(),
                        isLoading = false,
                        errorMessage = error.toUiText()
                    )
                }
            }
    }

    private fun sendUiEvent(event: BookListScreenUiEvent) = viewModelScope.launch {
        _uiEvent.send(event)
    }
}
