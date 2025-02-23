package com.mohanjp.bookPediaKmp.app

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.mohanjp.bookPediaKmp.book.presentation.SelectedBookViewModel
import com.mohanjp.bookPediaKmp.book.presentation.bookDetail.BookDetailScreenRoot
import com.mohanjp.bookPediaKmp.book.presentation.bookDetail.BookDetailScreenUiAction
import com.mohanjp.bookPediaKmp.book.presentation.bookDetail.BookDetailViewModel
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
            composable<Route.BookList>(
                exitTransition = { slideOutHorizontally() },
                popEnterTransition = { slideInHorizontally() }
            ) { entry ->
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

            composable<Route.BookDetail>(
                enterTransition = {
                    slideInHorizontally { initialOffset ->
                        initialOffset
                    }
                },
                exitTransition = {
                    slideOutHorizontally { initialOffset ->
                        initialOffset
                    }
                }
            ) { entry ->
                val args = entry.toRoute<Route.BookDetail>()

                val selectedBookSharedViewModel = entry.sharedKoinViewModel<SelectedBookViewModel>(navController)
                val viewModel = koinViewModel<BookDetailViewModel>()
                val selectedBook by selectedBookSharedViewModel.selectedBook.collectAsStateWithLifecycle()

                LaunchedEffect(selectedBook) {
                    selectedBook?.let { book ->
                        viewModel.onUiAction(BookDetailScreenUiAction.OnSelectedBookChange(book))
                    }
                }

                BookDetailScreenRoot(
                    viewModel = viewModel,
                    navigateBack = {
                        navController.navigateUp()
                    }
                )
            }
        }
    }
}
