@file:OptIn(ExperimentalFoundationApi::class, ExperimentalMultiplatform::class)

package com.tecknobit.ametista.ui.screens.applications.components

import ametista.composeapp.generated.resources.Res
import ametista.composeapp.generated.resources.logo
import ametista.composeapp.generated.resources.no_applications
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
import com.tecknobit.ametista.APPLICATION_SCREEN
import com.tecknobit.ametista.imageLoader
import com.tecknobit.ametista.localUser
import com.tecknobit.ametista.navigator
import com.tecknobit.ametista.ui.components.DeleteApplication
import com.tecknobit.ametista.ui.components.FirstPageProgressIndicator
import com.tecknobit.ametista.ui.components.NewPageProgressIndicator
import com.tecknobit.ametista.ui.screens.applications.data.AmetistaApplication
import com.tecknobit.ametista.ui.screens.applications.presentation.ApplicationsScreenViewModel
import com.tecknobit.equinoxcompose.components.EmptyState
import com.tecknobit.equinoxcompose.utilities.CompactClassComponent
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.EXPANDED_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.MEDIUM_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClassComponent
import com.tecknobit.equinoxcompose.utilities.ResponsiveContent
import com.tecknobit.equinoxcore.annotations.Wrapper
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyColumn
import org.jetbrains.compose.resources.painterResource

/**
 * **ICONS_REGEX** -> the regex to determine whether the application icon is selected one or provided
 * by the server
 */
private const val ICONS_REGEX = "icons"

/**
 * The component to display the applications list
 *
 * @param viewModel The viewmodel related to the [com.tecknobit.ametista.ui.screens.applications.presenter.ApplicationsScreen]
 */
@Composable
@NonRestartableComposable
fun Applications(
    viewModel: ApplicationsScreenViewModel,
) {
    ResponsiveContent(
        onExpandedSizeClass = {
            ApplicationsGrid(
                viewModel = viewModel
            )
        },
        onMediumSizeClass = {
            ApplicationsGrid(
                viewModel = viewModel
            )
        },
        onCompactSizeClass = {
            ApplicationsList(
                viewModel = viewModel
            )
        }
    )
}

@Composable
@NonRestartableComposable
@ResponsiveClassComponent(
    classes = [EXPANDED_CONTENT, MEDIUM_CONTENT],
)
private fun ApplicationsGrid(
    viewModel: ApplicationsScreenViewModel,
) {

}

@Composable
@CompactClassComponent
@NonRestartableComposable
private fun ApplicationsList(
    viewModel: ApplicationsScreenViewModel,
) {
    PaginatedLazyColumn(
        modifier = Modifier
            .padding(
                bottom = 16.dp
            ),
        paginationState = viewModel.applicationsState,
        firstPageProgressIndicator = { FirstPageProgressIndicator() },
        newPageProgressIndicator = { NewPageProgressIndicator() },
        firstPageEmptyIndicator = { NoApplications() }
    ) {
        itemsIndexed(
            items = viewModel.applicationsState.allItems!!,
            key = { _, application -> application.id }
        ) { index, application ->
            ApplicationItem(
                isTheFirst = index == 0,
                application = application,
                viewModel = viewModel
            )
        }
    }
}

/**
 * The component to the details of an [AmetistaApplication]
 *
 * @param isTheFirst Whether is the first application of the list displayed
 * @param application The application to display
 * @param viewModel The viewmodel related to the [com.tecknobit.ametista.ui.screens.applications.presenter.ApplicationsScreen]
 */
@Composable
@NonRestartableComposable
private fun ApplicationItem(
    isTheFirst: Boolean,
    application: AmetistaApplication,
    viewModel: ApplicationsScreenViewModel,
) {
    val isAdmin = localUser.isAdmin()
    val editApplication = remember { mutableStateOf(false) }
    val expandDescription = remember { mutableStateOf(false) }
    val deleteApplication = remember { mutableStateOf(false) }
    if (isTheFirst)
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
                        onDelete = { viewModel.applicationsState.refresh() }
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
    /*ExpandApplicationDescription(
        expand = expandDescription,
        application = application
    )
    if (isAdmin) {
        WorkOnApplication(
            show = editApplication,
            viewModel = viewModel,
            application = application
        )
    }*/
}

/**
 * The component to display the icon of the application
 *
 * @param modifier The [Modifier] to apply to the component
 * @param application The application to display its icon
 */
@Composable
@NonRestartableComposable
private fun ApplicationIcon(
    modifier: Modifier = Modifier,
    application: AmetistaApplication,
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

@Wrapper
fun getApplicationIconCompleteUrl(
    application: AmetistaApplication,
): String {
    return getApplicationIconCompleteUrl(
        url = application.applicationIcon
    )
}

/**
 * Method to get the complete url to display the application icon
 *
 * @param url The slice of the icon url
 *
 * @return the complete icon url as [String]
 */
fun getApplicationIconCompleteUrl(
    url: String,
): String {
    if (!url.startsWith(ICONS_REGEX))
        return url
    return localUser.hostAddress + "/" + url
}

fun navToApplicationScreen(
    application: AmetistaApplication,
) {
    navigator.navigate("$APPLICATION_SCREEN/${application.id}")
}

/**
 * The layout to display when the applications list is empty
 */
@Composable
@NonRestartableComposable
private fun NoApplications() {
    EmptyState(
        resource = Res.drawable.no_applications,
        contentDescription = "No applications available"
    )
}