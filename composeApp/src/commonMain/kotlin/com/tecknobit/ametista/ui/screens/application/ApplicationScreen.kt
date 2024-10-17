@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.ametista.ui.screens.application

import ametista.composeapp.generated.resources.Res
import ametista.composeapp.generated.resources.no_connected_platforms
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.LinkOff
import androidx.compose.material.icons.outlined.ArrowCircleDown
import androidx.compose.material.icons.outlined.ArrowCircleUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.tecknobit.ametista.helpers.PlatformsCustomGrid
import com.tecknobit.ametista.navigator
import com.tecknobit.ametistacore.models.AmetistaApplication
import com.tecknobit.ametistacore.models.AmetistaApplication.Platform.entries
import com.tecknobit.equinoxcompose.components.EmptyListUI
import com.tecknobit.equinoxcompose.helpers.session.EquinoxScreen
import com.tecknobit.equinoxcompose.helpers.session.ManagedContent

class ApplicationScreen(
    initialApplication: AmetistaApplication
) : EquinoxScreen<ApplicationScreenViewModel>(
    viewModel = ApplicationScreenViewModel(
        initialApplication = initialApplication
    )
) {

    private lateinit var application: State<AmetistaApplication>

    /**
     * Function to arrange the content of the screen to display
     *
     * No-any params required
     */
    @Composable
    override fun ArrangeScreenContent() {
        ManagedContent(
            viewModel = viewModel!!,
            content = {
                Scaffold(
                    topBar = {
                        LargeTopAppBar(
                            colors = TopAppBarDefaults.largeTopAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            ),
                            navigationIcon = {
                                IconButton(
                                    onClick = { navigator.goBack() }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowBackIosNew,
                                        contentDescription = null
                                    )
                                }
                            },
                            title = {
                                Text(
                                    text = application.value.name
                                )
                            }
                        )
                    },
                    snackbarHost = {
                        SnackbarHost(
                            hostState = viewModel!!.snackbarHostState!!
                        )
                    },
                    floatingActionButton = {
                        AnimatedVisibility(
                            visible = application.value.platforms.size != entries.size,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            FloatingActionButton(
                                onClick = {
                                    // TODO: TO DO
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Link,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                ) { paddingValues ->
                    Column {
                        ExpandableText(
                            paddingValues = paddingValues
                        )
                        PlatformsSections()
                    }
                }
            }
        )
    }

    @Composable
    @NonRestartableComposable
    // TODO: THE ORIGINAL COMPONENT MUST BE GENERAL AND NOT SPECIFIC LIKE THIS, BUT ALLOWING THE CUSTOMIZATION
    private fun ExpandableText(
        paddingValues: PaddingValues
    ) {
        var expanded by remember { mutableStateOf(false) }
        Column(
            modifier = Modifier
                .padding(
                    top = paddingValues.calculateTopPadding() + 16.dp,
                    bottom = 12.dp
                )
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .widthIn(
                        max = 750.dp
                    )
                    .verticalScroll(rememberScrollState())
            ) {
                AnimatedVisibility(
                    visible = expanded
                ) {
                    Text(
                        modifier = Modifier
                            .padding(
                                horizontal = 16.dp
                            ),
                        text = application.value.description,
                        textAlign = TextAlign.Justify,
                    )
                }
                if (!expanded) {
                    Text(
                        modifier = Modifier
                            .padding(
                                horizontal = 16.dp
                            ),
                        text = application.value.description,
                        textAlign = TextAlign.Justify,
                        maxLines = 5,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    HorizontalDivider()
                    IconButton(
                        onClick = { expanded = !expanded }
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(30.dp),
                            imageVector = if (expanded)
                                Outlined.ArrowCircleUp
                            else
                                Outlined.ArrowCircleDown,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }

    @Composable
    @NonRestartableComposable
    private fun PlatformsSections() {
        val platforms = application.value.platforms
        if (platforms.isNotEmpty()) {
            PlatformsCustomGrid(
                viewModel = viewModel!!,
                applicationPlatforms = platforms
            )
        } else {
            EmptyListUI(
                icon = Icons.Default.LinkOff,
                subText = Res.string.no_connected_platforms
            )
        }
    }

    /**
     * Function invoked when the [ShowContent] composable has been started
     *
     * No-any params required
     */
    override fun onStart() {
        super.onStart()
        viewModel!!.getApplication()
    }

    /**
     * Function to collect or instantiate the states of the screen
     *
     * No-any params required
     */
    @Composable
    override fun CollectStates() {
        application = viewModel!!.application.collectAsState()
    }

}