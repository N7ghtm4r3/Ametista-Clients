@file:OptIn(ExperimentalFoundationApi::class)

package com.tecknobit.ametista.ui.screens.applications

import ametista.composeapp.generated.resources.Res
import ametista.composeapp.generated.resources.logo
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.tecknobit.ametista.bodyFontFamily
import com.tecknobit.ametista.displayFontFamily
import com.tecknobit.ametista.imageLoader
import com.tecknobit.ametista.localUser
import com.tecknobit.ametista.ui.components.DeleteApplication
import com.tecknobit.ametista.ui.components.FirstPageProgressIndicator
import com.tecknobit.ametista.ui.components.NewPageProgressIndicator
import com.tecknobit.ametista.ui.components.WorkOnApplication
import com.tecknobit.ametista.ui.screens.applications.presentation.ApplicationsScreenViewModel
import com.tecknobit.ametista.ui.screens.shared.presenters.AmetistaScreen.Companion.CONTAINER_MAX_WIDTH
import com.tecknobit.ametistacore.models.AmetistaApplication
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyVerticalGrid
import org.jetbrains.compose.resources.painterResource

/**
 * The component to display the applications list
 *
 * @param viewModel The viewmodel related to the [com.tecknobit.ametista.ui.screens.applications.ApplicationsScreen]
 */
@Composable
@NonRestartableComposable
actual fun Applications(
    viewModel: ApplicationsScreenViewModel
) {
    Column (
        modifier = Modifier
            .padding(
                bottom = 16.dp
            )
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val applicationsIsEmpty = remember { mutableStateOf(false) }
        PaginatedLazyVerticalGrid(
            modifier = Modifier
                .widthIn(
                    max = CONTAINER_MAX_WIDTH
                ),
            paginationState = viewModel.paginationState,
            columns = GridCells.Adaptive(
                minSize = 400.dp
            ),
            firstPageProgressIndicator = { FirstPageProgressIndicator() },
            newPageProgressIndicator = { NewPageProgressIndicator() }
        ) {
            val applications = viewModel.paginationState.allItems!!
            applicationsIsEmpty.value = applications.isEmpty()
            if(applications.isNotEmpty()) {
                items(
                    items = applications,
                    key = { application -> application.id }
                ) { application ->
                    ApplicationItem(
                        application = application,
                        viewModel = viewModel
                    )
                }
            }
        }
        NoApplications(
            noApplications = applicationsIsEmpty.value
        )
    }
}

/**
 * The component to the details of an [AmetistaApplication]
 *
 * @param isTheFirst Whether is the first application of the list displayed
 * @param application The application to display
 * @param viewModel The viewmodel related to the [com.tecknobit.ametista.ui.screens.applications.ApplicationsScreen]
 */
@Composable
@NonRestartableComposable
actual fun ApplicationItem(
    isTheFirst: Boolean,
    application: AmetistaApplication,
    viewModel: ApplicationsScreenViewModel
) {
    val isAdmin = localUser.isAdmin()
    val editApplication = remember { mutableStateOf(false) }
    val expandDescription = remember { mutableStateOf(false) }
    val deleteApplication = remember { mutableStateOf(false) }
    Card (
        modifier = Modifier
            .padding(
                all = 10.dp
            )
            .size(
                width = 400.dp,
                height = 450.dp
            )
            .clip(
                RoundedCornerShape(
                    size = 15.dp
                )
            )
            .combinedClickable(
                onClick = {
                    navToApplicationScreen(
                        application = application
                    )
                },
                onDoubleClick = { expandDescription.value = true },
                onLongClick = if (isAdmin) {
                    {
                        editApplication.value = true
                    }
                } else
                    null
            ),
        shape = RoundedCornerShape(
            size = 15.dp
        )
    ) {
        ApplicationIcon(
            modifier = Modifier
                .weight(1f),
            application = application
        )
        ApplicationDetails(
            modifier = Modifier
                .weight(1f),
            application = application
        )
        if (isAdmin) {
            HorizontalDivider(
                color = MaterialTheme.colorScheme.primary
            )
            IconButton(
                modifier = Modifier
                    .align(Alignment.End),
                onClick = { deleteApplication.value = true }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            }
            DeleteApplication(
                show = deleteApplication,
                viewModel = viewModel,
                application = application,
                onDelete = { viewModel.paginationState.refresh() }
            )
        }
    }
    ExpandApplicationDescription(
        expand = expandDescription,
        application = application
    )
    if (isAdmin) {
        WorkOnApplication(
            show = editApplication,
            viewModel = viewModel,
            application = application
        )
    }
}

@Composable
@NonRestartableComposable
actual fun ApplicationIcon(
    modifier: Modifier,
    application: AmetistaApplication
) {
    AsyncImage(
        modifier = modifier
            .fillMaxWidth(),
        model = ImageRequest.Builder(LocalPlatformContext.current)
            .data(
                data = getApplicationIconCompleteUrl(
                    application = application
                )
            )
            .crossfade(true)
            .crossfade(500)
            .build(),
        imageLoader = imageLoader,
        contentDescription = "Application icon",
        contentScale = ContentScale.Crop,
        error = painterResource(Res.drawable.logo)
    )
    HorizontalDivider(
        color = MaterialTheme.colorScheme.primary
    )
}

/**
 * The component to display the icon of the application
 *
 * @param modifier The [Modifier] to apply to the component
 * @param application The application to display its icon
 */
@Composable
@NonRestartableComposable
private fun ApplicationDetails(
    modifier: Modifier,
    application: AmetistaApplication
) {
    Column(
        modifier = modifier
            .padding(
                all = 16.dp
            ),
    ) {
        Text(
            text = application.name,
            fontFamily = displayFontFamily,
            fontSize = 20.sp
        )
        Text(
            text = application.description,
            maxLines = 6,
            textAlign = TextAlign.Justify,
            overflow = TextOverflow.Ellipsis,
            fontFamily = bodyFontFamily,
            fontSize = 14.sp
        )
    }
}