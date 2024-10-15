package com.tecknobit.ametista.ui.screens.application

import androidx.compose.material3.SnackbarHostState
import com.tecknobit.ametistacore.models.AmetistaApplication
import com.tecknobit.equinoxcompose.helpers.viewmodels.EquinoxViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ApplicationScreenViewModel(
    initialApplication: AmetistaApplication
) : EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
) {

    private val _application = MutableStateFlow(
        value = initialApplication
    )
    val application: StateFlow<AmetistaApplication> = _application

    fun getApplication() {
        // TODO: MAKE THE REQUEST THEN
    }

}