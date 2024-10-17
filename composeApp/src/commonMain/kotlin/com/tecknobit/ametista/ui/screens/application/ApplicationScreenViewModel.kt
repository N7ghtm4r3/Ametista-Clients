package com.tecknobit.ametista.ui.screens.application

import com.tecknobit.ametista.ui.sharedviewmodels.ApplicationViewModel
import com.tecknobit.ametistacore.models.AmetistaApplication
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

open class ApplicationScreenViewModel(
    initialApplication: AmetistaApplication
) : ApplicationViewModel() {

    private val _application = MutableStateFlow(
        value = initialApplication
    )
    val application: StateFlow<AmetistaApplication> = _application

    fun refreshApplication() {
        // TODO: MAKE THE REQUEST THEN
    }

}