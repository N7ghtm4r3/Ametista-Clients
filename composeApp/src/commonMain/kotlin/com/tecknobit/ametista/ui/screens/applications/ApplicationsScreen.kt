@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.ametista.ui.screens.applications

import ametista.composeapp.generated.resources.Res
import ametista.composeapp.generated.resources.applications
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecknobit.ametista.ui.screens.AmetistaScreen
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyColumn
import org.jetbrains.compose.resources.stringResource

class ApplicationsScreen : AmetistaScreen<ApplicationsScreenViewModel>(
    viewModel = ApplicationsScreenViewModel()
) {

    /**
     * Function to arrange the content of the screen to display
     *
     * No-any params required
     */
    @Composable
    override fun ArrangeScreenContent() {
        Scaffold(
            topBar = {
                LargeTopAppBar(
                    colors = TopAppBarDefaults.largeTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    title = {
                        Text(
                            text = stringResource(Res.string.applications),
                            color = contentColorFor(MaterialTheme.colorScheme.primaryContainer)
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

                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null
                    )
                }
            }
        ) { paddingValues ->
            Applications(
                paddingValues = paddingValues
            )
        }
    }

    @Composable
    @NonRestartableComposable
    private fun Applications(
        paddingValues: PaddingValues
    ) {
        Column (
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PaginatedLazyColumn(
                modifier = Modifier
                    .padding(
                        top = paddingValues.calculateTopPadding() + 16.dp,
                        bottom = paddingValues.calculateBottomPadding() + 16.dp
                    )
                    .fillMaxHeight(),
                paginationState = viewModel!!.paginationState,
                firstPageProgressIndicator = {
                    CircularProgressIndicator()
                },
                newPageProgressIndicator = {
                    LinearProgressIndicator()
                },
                horizontalAlignment = Alignment.CenterHorizontally
                /*firstPageErrorIndicator = { e -> // from setError
                    ... e.message ...
                    ... onRetry = { paginationState.retryLastFailedRequest() } ...
                },
                newPageErrorIndicator = { e -> ... },*/
                // The rest of LazyColumn params
            ) {
                items(
                    items = listOf(""),
                    key = { it }
                ) {
                    ApplicationItem()
                }
            }
        }
    }

    /**
     * Function to collect or instantiate the states of the screen
     *
     * No-any params required
     */
    @Composable
    override fun CollectStates() {
    }

}