package com.tecknobit.ametista.ui.screens.applications

import androidx.compose.material3.SnackbarHostState
import com.tecknobit.equinoxcompose.helpers.viewmodels.EquinoxViewModel
import io.github.ahmad_hamwi.compose.pagination.PaginationState

class ApplicationsScreenViewModel: EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
) {

    val paginationState = PaginationState<Int, String>(
        initialPageKey = 1,
        onRequestPage = {  }
    )

}