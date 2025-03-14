@file:OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)

package com.tecknobit.ametista.ui.screens.applications.components

import ametista.composeapp.generated.resources.Res
import ametista.composeapp.generated.resources.logo
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.tecknobit.ametista.APPLICATION_SCREEN
import com.tecknobit.ametista.UPSERT_APPLICATION_SCREEN
import com.tecknobit.ametista.imageLoader
import com.tecknobit.ametista.localUser
import com.tecknobit.ametista.navigator
import com.tecknobit.ametista.ui.components.DeleteApplication
import com.tecknobit.ametista.ui.screens.applications.data.AmetistaApplication
import com.tecknobit.ametista.ui.screens.applications.presentation.ApplicationsScreenViewModel
import com.tecknobit.equinoxcompose.utilities.ExpandedClassComponent
import com.tecknobit.equinoxcompose.utilities.ResponsiveContent
import com.tecknobit.equinoxcore.annotations.Wrapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource

// TODO: TO COMMENT 

/**
 * `ICONS_REGEX` -> the regex to determine whether the application icon is selected one or provided
 * by the server
 */
private const val ICONS_REGEX = "icons"

/**
 * The component to the details of an [AmetistaApplication]
 *
 * @param isTheFirst Whether is the first application of the list displayed
 * @param application The application to display
 * @param viewModel The viewmodel related to the [com.tecknobit.ametista.ui.screens.applications.presenter.ApplicationsScreen]
 */
@Composable
@NonRestartableComposable
fun ApplicationItem(
    isTheFirst: Boolean = false,
    application: AmetistaApplication,
    viewModel: ApplicationsScreenViewModel,
) {
    val isAdmin = localUser.isAdmin()
    val deleteApplication = remember { mutableStateOf(false) }
    val state = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val scope = rememberCoroutineScope()
    ResponsiveContent(
        onExpandedSizeClass = {
            ExpandedApplicationCard(
                isAdmin = isAdmin,
                expandDescription = state,
                scope = scope,
                deleteApplication = deleteApplication,
                application = application,
                viewModel = viewModel
            )
        },
        onMediumSizeClass = {
            ApplicationListItem(
                isTheFirst = isTheFirst,
                isAdmin = isAdmin,
                expandDescription = state,
                scope = scope,
                deleteApplication = deleteApplication,
                application = application,
                viewModel = viewModel
            )
            HorizontalDivider()
        },
        onCompactSizeClass = {
            ApplicationListItem(
                isTheFirst = isTheFirst,
                isAdmin = isAdmin,
                expandDescription = state,
                scope = scope,
                deleteApplication = deleteApplication,
                application = application,
                viewModel = viewModel
            )
            HorizontalDivider()
        }
    )
    ApplicationDescription(
        state = state,
        scope = scope,
        application = application
    )
}

@Composable
@ExpandedClassComponent
@NonRestartableComposable
private fun ExpandedApplicationCard(
    isAdmin: Boolean,
    expandDescription: SheetState,
    scope: CoroutineScope,
    deleteApplication: MutableState<Boolean>,
    application: AmetistaApplication,
    viewModel: ApplicationsScreenViewModel,
) {
    Card(
        modifier = Modifier
            .padding(
                horizontal = 16.dp
            )
            .padding(
                top = 10.dp
            )
            .fillMaxWidth()
            .height(120.dp)
            .clip(CardDefaults.shape)
            .combinedClickable(
                onClick = {
                    navToApplicationScreen(
                        application = application
                    )
                },
                onDoubleClick = {
                    scope.launch {
                        expandDescription.show()
                    }
                },
                onLongClick = if (isAdmin) {
                    {
                        navigator.navigate("$UPSERT_APPLICATION_SCREEN/${application.id}")
                    }
                } else
                    null
            )
    ) {
        ApplicationListItem(
            background = Color.Transparent,
            isAdmin = isAdmin,
            expandDescription = expandDescription,
            scope = scope,
            deleteApplication = deleteApplication,
            application = application,
            viewModel = viewModel
        )
    }
}

@Composable
@NonRestartableComposable
private fun ApplicationListItem(
    background: Color = ListItemDefaults.containerColor,
    isTheFirst: Boolean = false,
    isAdmin: Boolean,
    expandDescription: SheetState,
    scope: CoroutineScope,
    deleteApplication: MutableState<Boolean>,
    application: AmetistaApplication,
    viewModel: ApplicationsScreenViewModel,
) {
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
                onDoubleClick = {
                    scope.launch {
                        expandDescription.show()
                    }
                },
                onLongClick = if (isAdmin) {
                    {
                        navigator.navigate("$UPSERT_APPLICATION_SCREEN/${application.id}")
                    }
                } else
                    null
            ),
        colors = ListItemDefaults.colors(
            containerColor = background
        ),
        leadingContent = {
            ApplicationIcon(
                modifier = Modifier
                    .size(95.dp)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    )
                    .clip(CircleShape),
                application = application
            )
        },
        headlineContent = {
            Text(
                text = application.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        supportingContent = {
            Text(
                text = application.description,
                minLines = 3,
                maxLines = 3,
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
        modifier = modifier,
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

/**
 * Method to get the complete url to display the application icon
 *
 * @param application The application to fetch the icon url
 *
 * @return the complete icon url as [String]
 */
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

/**
 * Method to navigate to the [com.tecknobit.ametista.ui.screens.application.presenter.ApplicationScreen] screen
 *
 * @param application The application related to the screen to show
 */
private fun navToApplicationScreen(
    application: AmetistaApplication,
) {
    navigator.navigate("$APPLICATION_SCREEN/${application.id}")
}