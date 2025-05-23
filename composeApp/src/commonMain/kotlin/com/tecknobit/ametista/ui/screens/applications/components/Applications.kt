@file:OptIn(ExperimentalMultiplatform::class, ExperimentalComposeApi::class)

package com.tecknobit.ametista.ui.screens.applications.components

import ametista.composeapp.generated.resources.Res
import ametista.composeapp.generated.resources.no_applications
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecknobit.ametista.ui.components.FirstPageProgressIndicator
import com.tecknobit.ametista.ui.components.NewPageProgressIndicator
import com.tecknobit.ametista.ui.screens.applications.presentation.ApplicationsScreenViewModel
import com.tecknobit.equinoxcompose.components.EmptyState
import com.tecknobit.equinoxcompose.utilities.responsiveMaxWidth
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyColumn

/**
 * The component to display the applications list
 *
 * @param viewModel The viewmodel related to the [com.tecknobit.ametista.ui.screens.applications.presenter.ApplicationsScreen]
 */
@Composable
fun Applications(
    viewModel: ApplicationsScreenViewModel,
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PaginatedLazyColumn(
            modifier = Modifier
                .responsiveMaxWidth()
                .padding(
                    bottom = 16.dp
                )
                .animateContentSize(),
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
}

/**
 * The layout to display when the applications list is empty
 */
@Composable
@NonRestartableComposable
private fun NoApplications() {
    EmptyState(
        containerModifier = Modifier
            .fillMaxSize(),
        resource = Res.drawable.no_applications,
        resourceSize = 275.dp,
        contentDescription = "No applications available"
    )
}