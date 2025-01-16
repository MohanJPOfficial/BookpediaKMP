package com.mohanjp.bookPediaKmp.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.mohanjp.bookPediaKmp.book.presentation.bookList.BookListScreenRoot
import com.mohanjp.bookPediaKmp.book.presentation.bookList.BookListViewModel
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
            composable<Route.BookList> {
                val viewModel = koinViewModel<BookListViewModel>()

                BookListScreenRoot(
                    viewModel = viewModel
                ) { book ->
                    navController.navigate(
                        Route.BookDetail(book.id)
                    )
                }
            }

            composable<Route.BookDetail> { entry ->
                val args = entry.toRoute<Route.BookDetail>()

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Book detail screen >> Book ID: ${args.id} ")
                }
            }
        }
    }
}
