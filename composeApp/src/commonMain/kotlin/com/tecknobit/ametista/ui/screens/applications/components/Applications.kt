@file:OptIn(ExperimentalMultiplatform::class)

package com.tecknobit.ametista.ui.screens.applications.components

import ametista.composeapp.generated.resources.Res
import ametista.composeapp.generated.resources.no_applications
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecknobit.ametista.ui.components.FirstPageProgressIndicator
import com.tecknobit.ametista.ui.components.NewPageProgressIndicator
import com.tecknobit.ametista.ui.screens.applications.presentation.ApplicationsScreenViewModel
import com.tecknobit.equinoxcompose.components.EmptyState
import com.tecknobit.equinoxcompose.session.screens.EquinoxNoModelScreen.Companion.MAX_CONTAINER_WIDTH
import com.tecknobit.equinoxcompose.utilities.ExpandedClassComponent
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.COMPACT_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.MEDIUM_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClassComponent
import com.tecknobit.equinoxcompose.utilities.ResponsiveContent
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyColumn
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyVerticalGrid

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
            ApplicationsList(
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
@ExpandedClassComponent
@NonRestartableComposable
private fun ApplicationsGrid(
    viewModel: ApplicationsScreenViewModel,
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PaginatedLazyVerticalGrid(
            modifier = Modifier
                .widthIn(
                    max = MAX_CONTAINER_WIDTH
                ),
            paginationState = viewModel.applicationsState,
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                bottom = 16.dp
            ),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            firstPageProgressIndicator = { FirstPageProgressIndicator() },
            newPageProgressIndicator = { NewPageProgressIndicator() },
            firstPageEmptyIndicator = { NoApplications() }
        ) {
            items(
                items = viewModel.applicationsState.allItems!!,
                key = { application -> application.id }
            ) { application ->
                ApplicationItem(
                    application = application,
                    viewModel = viewModel
                )
            }
        }
    }
}

@Composable
@NonRestartableComposable
@ResponsiveClassComponent(
    classes = [MEDIUM_CONTENT, COMPACT_CONTENT],
)
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
 * The layout to display when the applications list is empty
 */
@Composable
@NonRestartableComposable
private fun NoApplications() {
    EmptyState(
        resource = Res.drawable.no_applications,
        resourceSize = 275.dp,
        contentDescription = "No applications available"
    )
}