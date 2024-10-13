package com.tecknobit.ametista.ui.screens.applications

import androidx.compose.material3.SnackbarHostState
import com.tecknobit.equinoxcompose.helpers.viewmodels.EquinoxViewModel
import io.github.ahmad_hamwi.compose.pagination.PaginationState
import kotlin.random.Random

class ApplicationsScreenViewModel: EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
) {

    val paginationState = PaginationState<Int, String>(
        initialPageKey = 1,
        onRequestPage = { getApplications() }
    )

    private fun getApplications() {
        paginationState.appendPage(
            items = listOf(Random.nextInt().toString()),
            nextPageKey = 1
        )
    }

}