package com.tecknobit.ametista.ui.screens.applications

import androidx.compose.material3.SnackbarHostState
import com.tecknobit.ametistacore.models.AmetistaApplication
import com.tecknobit.equinoxcompose.helpers.viewmodels.EquinoxViewModel
import io.github.ahmad_hamwi.compose.pagination.PaginationState

class ApplicationsScreenViewModel: EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
) {

    val paginationState = PaginationState<Int, AmetistaApplication>(
        initialPageKey = 1,
        onRequestPage = { getApplications() }
    )

    private fun getApplications() {
        paginationState.appendPage(
            items = listOf(AmetistaApplication()),
            nextPageKey = 1
        )
    }

}