@file:OptIn(ExperimentalFoundationApi::class)

package com.tecknobit.ametista.ui.screens.applications

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.tecknobit.ametista.imageLoader
import com.tecknobit.ametistacore.models.AmetistaApplication
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyColumn

@Composable
@NonRestartableComposable
actual fun Applications(
    paddingValues: PaddingValues,
    viewModel: ApplicationsScreenViewModel,
) {
    PaginatedLazyColumn(
        modifier = Modifier
            .padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding() + 16.dp
            )
            .fillMaxHeight(),
        paginationState = viewModel.paginationState,
        firstPageProgressIndicator = {
            CircularProgressIndicator()
        },
        newPageProgressIndicator = {
            LinearProgressIndicator()
        }
        /*firstPageErrorIndicator = { e -> // from setError
            ... e.message ...
            ... onRetry = { paginationState.retryLastFailedRequest() } ...
        },
        newPageErrorIndicator = { e -> ... },*/
        // The rest of LazyColumn params
    ) {
        items(
            items = listOf(AmetistaApplication("Space"), AmetistaApplication("ggg")), // TODO: USE THE REAL VALUES
            key = { application -> application.id }
        ) { application ->
            ApplicationItem(
                application = application,
                viewModel = viewModel
            )
        }
    }
}

@Composable
@NonRestartableComposable
actual fun ApplicationItem(
    application: AmetistaApplication,
    viewModel: ApplicationsScreenViewModel
) {
    val expandDescription = remember { mutableStateOf(false) }
    val deleteApplication = remember { mutableStateOf(false) }
    ListItem(
        modifier = Modifier
            .combinedClickable(
                onClick = {
                    // TODO: NAV TO APPLICATION
                },
                onDoubleClick = { expandDescription.value = true },
                onLongClick = {
                    // TODO: TO EDIT
                }
            ),
        leadingContent = {
            ApplicationIcon(
                application = application
            )
        },
        headlineContent = {
            Text(
                text = application.name
            )
        },
        supportingContent = {
            Text(
                text = application.description,
                maxLines = 4,
                textAlign = TextAlign.Justify,
                overflow = TextOverflow.Ellipsis
            )
        },
        trailingContent = {
            Column(
                modifier = Modifier
                    .height(110.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(
                    onClick = { deleteApplication.value = true }
                ) {
                    Icon(
                        modifier = Modifier
                            .weight(1f),
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                }
                DeleteApplication(
                    show = deleteApplication,
                    application = application,
                    viewModel = viewModel
                )
                IconButton(
                    onClick = {
                        // TODO: NAV TO APPLICATION
                    }
                ) {
                    Icon(
                        modifier = Modifier
                            .weight(1f),
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = null
                    )
                }
            }
        }
    )
    HorizontalDivider()
    ExpandApplicationDescription(
        expand = expandDescription,
        application = application
    )
}

@NonRestartableComposable
@Composable
actual fun ApplicationIcon(
    modifier: Modifier,
    application: AmetistaApplication
) {
    AsyncImage(
        modifier = modifier
            .size(95.dp)
            .border(
                width = 1.5.dp,
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = CircleShape
            )
            .clip(CircleShape),
        model = ImageRequest.Builder(LocalPlatformContext.current)
            .data(application.icon)
            .crossfade(true)
            .crossfade(500)
            .build(),
        imageLoader = imageLoader,
        contentDescription = "Application icon",
        contentScale = ContentScale.Crop
        // TODO: TO SET ERROR
    )
}