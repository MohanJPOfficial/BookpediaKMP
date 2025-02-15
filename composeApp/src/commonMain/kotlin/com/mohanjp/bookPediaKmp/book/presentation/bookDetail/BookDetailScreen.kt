package com.mohanjp.bookPediaKmp.book.presentation.bookDetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import bookpediakmp.composeapp.generated.resources.Res
import bookpediakmp.composeapp.generated.resources.description_unavailable
import bookpediakmp.composeapp.generated.resources.languages
import bookpediakmp.composeapp.generated.resources.pages
import bookpediakmp.composeapp.generated.resources.rating
import bookpediakmp.composeapp.generated.resources.synopsis
import com.mohanjp.bookPediaKmp.book.presentation.bookDetail.components.BlurredImageBackground
import com.mohanjp.bookPediaKmp.book.presentation.bookDetail.components.BookChip
import com.mohanjp.bookPediaKmp.book.presentation.bookDetail.components.ChipSize
import com.mohanjp.bookPediaKmp.book.presentation.bookDetail.components.TitledContent
import com.mohanjp.bookPediaKmp.core.presentation.util.ObserveAsEvent
import com.mohanjp.bookPediaKmp.core.presentation.util.PulseAnimation
import com.mohanjp.bookPediaKmp.core.presentation.util.SandYellow
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.round

@Composable
fun BookDetailScreenRoot(
    viewModel: BookDetailViewModel = koinViewModel(),
    navigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    viewModel.uiEvent.ObserveAsEvent { uiEvent ->
        when (uiEvent) {
            BookDetailScreenUiEvent.NavigateBack -> navigateBack()
        }
    }

    BookDetailScreen(
        uiState = uiState,
        onUiAction = viewModel::onUiAction
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun BookDetailScreen(
    uiState: BookDetailScreenUiState,
    onUiAction: (BookDetailScreenUiAction) -> Unit
) {
    BlurredImageBackground(
        imageUrl = uiState.book?.imageUrl,
        isFavorite = uiState.isFavorite,
        onFavoriteClick = {
            onUiAction(BookDetailScreenUiAction.OnFavoriteClick)
        },
        onBackClick = {
            onUiAction(BookDetailScreenUiAction.OnBackClick)
        },
        modifier = Modifier.fillMaxSize()
    ) {
        if (uiState.book != null) {
            Column(
                modifier = Modifier
                    .widthIn(max = 700.dp)
                    .fillMaxWidth()
                    .padding(
                        vertical = 16.dp,
                        horizontal = 24.dp
                    )
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                /**
                 * title
                 */
                Text(
                    text = uiState.book.title,
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center
                )

                /**
                 * authors
                 */
                Text(
                    text = uiState.book.authors.joinToString(),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )

                /**
                 * ratings
                 */
                Row(
                    modifier = Modifier
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    uiState.book.averageRating?.let { rating ->
                        TitledContent(
                            title = stringResource(Res.string.rating)
                        ) {
                            BookChip {
                                Text(
                                    text = "${round(rating * 10) / 10.0}"
                                )
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = SandYellow
                                )
                            }
                        }
                    }
                }

                /**
                 * page count
                 */
                uiState.book.numPages?.let { pageCount ->
                    TitledContent(
                        title = stringResource(Res.string.pages)
                    ) {
                        BookChip {
                            Text(text = pageCount.toString())
                        }
                    }
                }

                /**
                 * languages
                 */
                uiState.book.languages.takeIf { it.isNotEmpty() }?.let { languages ->
                    TitledContent(
                        title = stringResource(Res.string.languages),
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                    ) {
                        FlowRow(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.wrapContentSize(Alignment.Center)
                        ) {
                            languages.forEach { language ->
                                BookChip(
                                    size = ChipSize.SMALL,
                                    modifier = Modifier.padding(2.dp)
                                ) {
                                    Text(
                                        text = language.uppercase(),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }
                }

                /**
                 * synopsis
                 */
                Text(
                    text = stringResource(Res.string.synopsis),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .fillMaxWidth()
                        .padding(
                            top = 24.dp,
                            bottom = 8.dp
                        )
                )

                if (uiState.isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        PulseAnimation(
                            modifier = Modifier.size(60.dp)
                        )
                    }
                } else {
                    Text(
                        text = if (uiState.book.description.isNullOrBlank()) {
                            stringResource(Res.string.description_unavailable)
                        } else {
                            uiState.book.description
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Justify,
                        color = if (uiState.book.description.isNullOrBlank()) {
                            Color.Black.copy(alpha = 0.4f)
                        } else {
                            Color.Black
                        },
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                    )
                }
            }
        }
    }
}
