package com.tecknobit.ametista.ui.sharedviewmodels

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import com.tecknobit.ametista.requester
import com.tecknobit.ametista.ui.theme.platforms.android.errorDark
import com.tecknobit.ametistacore.helpers.AmetistaValidator.isAppDescriptionValid
import com.tecknobit.ametistacore.helpers.AmetistaValidator.isAppNameValid
import com.tecknobit.ametistacore.models.AmetistaApplication
import com.tecknobit.apimanager.annotations.Structure
import com.tecknobit.equinoxcompose.helpers.viewmodels.EquinoxViewModel

@Structure
abstract class ApplicationViewModel : EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
) {

    lateinit var workOnApplication: MutableState<Boolean>

    lateinit var appIcon: MutableState<String>

    lateinit var appIconBorderColor: MutableState<Color>

    lateinit var appName: MutableState<String>

    lateinit var appNameError: MutableState<Boolean>

    lateinit var appDescription: MutableState<String>

    lateinit var appDescriptionError: MutableState<Boolean>

    fun editApplication(
        application: AmetistaApplication,
        onSuccess: () -> Unit
    ) {
        if (!validForm())
            return
        requester.sendRequest(
            request = {
                requester.editApplication(
                    application = application,
                    icon = appIcon.value,
                    name = appName.value,
                    description = appDescription.value
                )
            },
            onSuccess = { onSuccess.invoke() },
            onFailure = { showSnackbarMessage(it) }
        )
    }

    protected fun validForm(): Boolean {
        if (appIcon.value.isEmpty()) {
            appIconBorderColor.value = errorDark
            return false
        }
        if (!isAppNameValid(appName.value)) {
            appNameError.value = true
            return false
        }
        if (!isAppDescriptionValid(appDescription.value)) {
            appDescriptionError.value = true
            return false
        }
        return true
    }

    fun deleteApplication(
        application: AmetistaApplication,
        onDelete: () -> Unit
    ) {
        requester.sendRequest(
            request = {
                requester.deleteApplication(
                    application = application
                )
            },
            onSuccess = { onDelete.invoke() },
            onFailure = { showSnackbarMessage(it) }
        )
    }

}