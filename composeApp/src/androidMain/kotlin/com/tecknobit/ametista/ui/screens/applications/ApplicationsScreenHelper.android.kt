@file:OptIn(ExperimentalFoundationApi::class)

package com.tecknobit.ametista.ui.screens.applications

import ametista.composeapp.generated.resources.Res
import ametista.composeapp.generated.resources.logo
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.tecknobit.ametista.localUser
import com.tecknobit.ametista.ui.components.DeleteApplication
import com.tecknobit.ametista.ui.components.FirstPageProgressIndicator
import com.tecknobit.ametista.ui.components.NewPageProgressIndicator
import com.tecknobit.ametista.ui.components.WorkOnApplication
import com.tecknobit.ametistacore.models.AmetistaApplication
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyColumn
import org.jetbrains.compose.resources.painterResource

/**
 * The component to display the applications list
 *
 * @param viewModel The viewmodel related to the [com.tecknobit.ametista.ui.screens.applications.ApplicationsScreen]
 */
@Composable
@NonRestartableComposable
actual fun Applications(
    viewModel: ApplicationsScreenViewModel,
) {
    val applicationsIsEmpty = remember { mutableStateOf(false) }
    PaginatedLazyColumn(
        modifier = Modifier
            .padding(
                bottom = 16.dp
            ),
        paginationState = viewModel.paginationState,
        firstPageProgressIndicator = { FirstPageProgressIndicator() },
        newPageProgressIndicator = { NewPageProgressIndicator() }
    ) {
        val applications = viewModel.paginationState.allItems!!
        applicationsIsEmpty.value = applications.isEmpty()
        if(applications.isNotEmpty()) {
            itemsIndexed(
                items = applications,
                key = { _ , application -> application.id }
            ) { index, application ->
                ApplicationItem(
                    isTheFirst = index == 0,
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
    if(isTheFirst)
        HorizontalDivider()
    ListItem(
        modifier = Modifier
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
            if (isAdmin) {
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
                        viewModel = viewModel,
                        onDelete = { viewModel.paginationState.refresh() }
                    )
                    IconButton(
                        onClick = {
                            navToApplicationScreen(
                                application = application
                            )
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
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
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
    if (isAdmin) {
        WorkOnApplication(
            show = editApplication,
            viewModel = viewModel,
            application = application
        )
    }
}

/**
 * The component to display the icon of the application
 *
 * @param modifier The [Modifier] to apply to the component
 * @param application The application to display its icon
 */
@Composable
@NonRestartableComposable
actual fun ApplicationIcon(
    modifier: Modifier,
    application: AmetistaApplication
) {
    AsyncImage(
        modifier = modifier
            .size(95.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = CircleShape
            )
            .clip(CircleShape),
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
}