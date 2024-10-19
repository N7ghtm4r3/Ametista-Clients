package com.tecknobit.ametista.ui.screens.application

import com.tecknobit.ametista.ui.sharedviewmodels.ApplicationViewModel
import com.tecknobit.ametistacore.models.AmetistaApplication
import com.tecknobit.ametistacore.models.Platform
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

open class ApplicationScreenViewModel(
    initialApplication: AmetistaApplication
) : ApplicationViewModel() {

    protected val _application = MutableStateFlow(
        value = initialApplication
    )
    val application: StateFlow<AmetistaApplication> = _application

    open fun refreshApplication(
        platform: Platform? = null
    ) {
        // TODO: MAKE THE REQUEST THEN
    }

}