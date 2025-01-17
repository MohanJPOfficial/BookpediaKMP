package com.mohanjp.bookPediaKmp.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.mohanjp.bookPediaKmp.book.presentation.SelectedBookViewModel
import com.mohanjp.bookPediaKmp.book.presentation.bookList.BookListScreenRoot
import com.mohanjp.bookPediaKmp.book.presentation.bookList.BookListViewModel
import com.mohanjp.bookPediaKmp.core.presentation.util.sharedKoinViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Route.BookGraph
    ) {
        navigation<Route.BookGraph>(
            startDestination = Route.BookList
        ) {
            composable<Route.BookList> { entry ->
                val viewModel = koinViewModel<BookListViewModel>()
                val selectedBookSharedViewModel =
                    entry.sharedKoinViewModel<SelectedBookViewModel>(navController)

                LaunchedEffect(true) {
                    selectedBookSharedViewModel.onSelectBook(null)
                }

                BookListScreenRoot(
                    viewModel = viewModel
                ) { book ->
                    selectedBookSharedViewModel.onSelectBook(book)
                    navController.navigate(
                        Route.BookDetail(book.id)
                    )
                }
            }

            composable<Route.BookDetail> { entry ->
                val args = entry.toRoute<Route.BookDetail>()

                val selectedBookSharedViewModel = entry.sharedKoinViewModel<SelectedBookViewModel>(navController)
                val selectedBook by selectedBookSharedViewModel.selectedBook.collectAsStateWithLifecycle()

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Book detail screen >> Book: $selectedBook ")
                }
            }
        }
    }
}
