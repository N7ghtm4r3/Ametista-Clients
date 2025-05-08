@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMultiplatform::class)

package com.tecknobit.ametista.ui.screens.application.presenter

import ametista.composeapp.generated.resources.Res
import ametista.composeapp.generated.resources.no_connected_platforms
import ametista.composeapp.generated.resources.no_platforms
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.tecknobit.ametista.UPSERT_APPLICATION_SCREEN
import com.tecknobit.ametista.localUser
import com.tecknobit.ametista.navigator
import com.tecknobit.ametista.ui.components.DeleteApplication
import com.tecknobit.ametista.ui.components.NavButton
import com.tecknobit.ametista.ui.components.PlatformsCustomGrid
import com.tecknobit.ametista.ui.screens.application.components.ConnectionProcedure
import com.tecknobit.ametista.ui.screens.application.presentation.ApplicationScreenViewModel
import com.tecknobit.ametista.ui.screens.applications.data.AmetistaApplication
import com.tecknobit.ametista.ui.theme.AmetistaTheme
import com.tecknobit.ametista.ui.theme.AppTypography
import com.tecknobit.ametistacore.enums.Platform.entries
import com.tecknobit.equinoxcompose.components.EmptyState
import com.tecknobit.equinoxcompose.components.ExpandableText
import com.tecknobit.equinoxcompose.session.ManagedContent
import com.tecknobit.equinoxcompose.session.screens.EquinoxScreen
import com.tecknobit.equinoxcompose.utilities.responsiveAssignment
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

/**
 * The [ApplicationScreen] class is used to display the [AmetistaApplication] details and work on it
 *
 * @param applicationId The identifier of the application displayed
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see EquinoxScreen
 *
 */
class ApplicationScreen(
    private val applicationId: String,
) : EquinoxScreen<ApplicationScreenViewModel>(
    viewModel = ApplicationScreenViewModel(
        applicationId = applicationId
    )
) {

    /**
     * `application` -> the application state container
     */
    private lateinit var application: State<AmetistaApplication?>

    /**
     * Method to arrange the content of the screen to display
     */
    @Composable
    override fun ArrangeScreenContent() {
        AmetistaTheme {
            val isAdmin = localUser.isAdmin()
            ManagedContent(
                viewModel = viewModel,
                initialDelay = 500,
                loadingRoutine = { application.value != null },
                content = {
                    Scaffold(
                        topBar = {
                            LargeTopAppBar(
                                colors = TopAppBarDefaults.largeTopAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    titleContentColor = Color.White,
                                    actionIconContentColor = Color.White
                                ),
                                navigationIcon = { NavButton() },
                                title = {
                                    Text(
                                        text = application.value!!.name
                                    )
                                },
                                actions = {
                                    if (isAdmin) {
                                        IconButton(
                                            onClick = {
                                                navigator.navigate(
                                                    route = "$UPSERT_APPLICATION_SCREEN/$applicationId"
                                                )
                                            }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Edit,
                                                contentDescription = null
                                            )
                                        }
                                        val deleteApplication =
                                            remember { mutableStateOf(false) }
                                        IconButton(
                                            onClick = { deleteApplication.value = true }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = null
                                            )
                                        }
                                        DeleteApplication(
                                            show = deleteApplication,
                                            application = application.value!!,
                                            viewModel = viewModel,
                                            onDelete = { navigator.goBack() }
                                        )
                                    }
                                }
                            )
                        },
                        snackbarHost = {
                            SnackbarHost(
                                hostState = viewModel.snackbarHostState!!
                            )
                        },
                        floatingActionButton = {
                            AnimatedVisibility(
                                visible = application.value!!.platforms.size != entries.size && isAdmin,
                                enter = fadeIn(),
                                exit = fadeOut()
                            ) {
                                val state = rememberModalBottomSheetState(
                                    skipPartiallyExpanded = true
                                )
                                val scope = rememberCoroutineScope()
                                FloatingActionButton(
                                    onClick = {
                                        scope.launch {
                                            state.show()
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Link,
                                        contentDescription = null
                                    )
                                }
                                ConnectionProcedure(
                                    state = state,
                                    scope = scope,
                                    applicationId = applicationId
                                )
                            }
                        }
                    ) { paddingValues ->
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .navigationBarsPadding(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Column(
                                modifier = Modifier
                                    // TODO: TO SET
                                    .widthIn(
                                        max = 1280.dp
                                    )
                            ) {
                                ExpandableText(
                                    containerModifier = Modifier
                                        .padding(
                                            top = paddingValues.calculateTopPadding() + 16.dp,
                                            bottom = 5.dp
                                        )
                                        .fillMaxWidth(),
                                    textModifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            horizontal = 16.dp
                                        ),
                                    text = application.value!!.description,
                                    maxLines = 5,
                                    overflow = TextOverflow.Ellipsis
                                )
                                PlatformsSections()
                            }
                        }
                    }
                }
            )
        }
    }

    /**
     * Section related to the [com.tecknobit.ametistacore.enums.Platform] connected of the [application]
     */
    @Composable
    private fun PlatformsSections() {
        val platforms = application.value!!.platforms
        if (platforms.isNotEmpty()) {
            PlatformsCustomGrid(
                viewModel = viewModel,
                applicationPlatforms = platforms
            )
        } else {
            EmptyState(
                resource = Res.drawable.no_platforms,
                contentDescription = "No platforms available",
                resourceSize = responsiveAssignment(
                    onExpandedSizeClass = { 350.dp },
                    onMediumSizeClass = { 275.dp },
                    onCompactSizeClass = { 250.dp }
                ),
                title = stringResource(Res.string.no_connected_platforms),
                titleStyle = AppTypography.titleMedium
            )
        }
    }

    /**
     * Method invoked when the [ShowContent] composable has been started
     */
    override fun onStart() {
        super.onStart()
        viewModel.refreshApplication()
    }

    /**
     * Method to collect or instantiate the states of the screen
     */
    @Composable
    override fun CollectStates() {
        application = viewModel.application.collectAsState()
    }

}