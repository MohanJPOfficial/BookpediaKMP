package com.mohanjp.bookPediaKmp.book.presentation

import androidx.lifecycle.ViewModel
import com.mohanjp.bookPediaKmp.book.domain.model.Book
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SelectedBookViewModel : ViewModel() {

    private val _selectedBook = MutableStateFlow<Book?>(null)
    val selectedBook = _selectedBook.asStateFlow()

    fun onSelectBook(book: Book?) {
        _selectedBook.update {
            book
        }
    }
}
