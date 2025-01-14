package com.mohanjp.bookPediaKmp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.mohanjp.bookPediaKmp.book.domain.model.Book
import com.mohanjp.bookPediaKmp.book.presentation.bookList.BookListScreen
import com.mohanjp.bookPediaKmp.book.presentation.bookList.BookListScreenUiState
import com.mohanjp.bookPediaKmp.book.presentation.bookList.components.BookSearchBar

@Preview
@Composable
fun BookSearchBarPreview() {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        BookSearchBar(
            searchQuery = "",
            onSearchQueryChange = {},
            onImeSearch = {},
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

private val books = (1..100).map {
    Book(
        id = it.toString(),
        title = "Book $it",
        imageUrl = "https://test.com",
        authors = listOf("Arthuro Roman"),
        description = "Description $it",
        languages = emptyList(),
        firstPublishYear = null,
        averageRating = 4.632,
        ratingCount = 5,
        numPages = 79,
        numEditions = 7
    )
}

@Preview
@Composable
private fun BookListScreenPreview() {
    BookListScreen(
        uiState = BookListScreenUiState(
            searchResults = books
        ),
        onUiAction = {}
    )
}
