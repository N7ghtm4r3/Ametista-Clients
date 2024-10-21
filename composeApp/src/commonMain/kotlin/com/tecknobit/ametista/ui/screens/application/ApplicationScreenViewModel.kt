package com.tecknobit.ametista.ui.screens.application

import com.tecknobit.ametista.ui.sharedviewmodels.ApplicationViewModel
import com.tecknobit.ametistacore.models.AmetistaApplication
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

open class ApplicationScreenViewModel(
    applicationId: String
) : ApplicationViewModel() {

    private val _application = MutableStateFlow<AmetistaApplication?>(
        value = null
    )
    val application: StateFlow<AmetistaApplication?> = _application

    fun refreshApplication() {
        // TODO: MAKE THE REQUEST THEN
        // TODO: TO REMOVE
        _application.value = AmetistaApplication("Space")
    }

}