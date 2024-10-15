@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.ametista.ui.screens.application

import ametista.composeapp.generated.resources.Res
import ametista.composeapp.generated.resources.no_connected_platforms
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.LinkOff
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecknobit.ametista.helpers.icon
import com.tecknobit.ametista.navigator
import com.tecknobit.ametistacore.models.AmetistaApplication
import com.tecknobit.ametistacore.models.AmetistaApplication.Platform
import com.tecknobit.equinoxcompose.components.EmptyListUI
import com.tecknobit.equinoxcompose.components.Tile
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
                ) { paddingValues ->
                    PlatformsSections()
                }
            }
        )
    }

    @Composable
    @NonRestartableComposable
    private fun PlatformsSections() {
        val platforms = Platform.entries//application.value.platforms
        if (platforms.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                LazyVerticalGrid(
                    modifier = Modifier
                        .sizeIn(
                            maxHeight = 600.dp,
                            maxWidth = 500.dp
                        ),
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(
                        all = 16.dp
                    )
                ) {
                    items(
                        items = platforms,
                        key = { platform -> platform.name }
                    ) { platform ->
                        Tile(
                            icon = platform.icon(),
                            text = platform.name,
                            onClick = {

                            }
                        )
                    }
                }
            }
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