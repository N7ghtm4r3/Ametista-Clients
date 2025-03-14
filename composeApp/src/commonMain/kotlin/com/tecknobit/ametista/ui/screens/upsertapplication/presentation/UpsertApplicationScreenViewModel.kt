package com.tecknobit.ametista.ui.screens.upsertapplication.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewModelScope
import com.tecknobit.ametista.helpers.KReviewer
import com.tecknobit.ametista.navigator
import com.tecknobit.ametista.requester
import com.tecknobit.ametista.ui.screens.applications.data.AmetistaApplication
import com.tecknobit.ametista.ui.theme.platforms.android.errorDark
import com.tecknobit.ametistacore.helpers.AmetistaValidator.isAppDescriptionValid
import com.tecknobit.ametistacore.helpers.AmetistaValidator.isAppNameValid
import com.tecknobit.equinoxcompose.session.setHasBeenDisconnectedValue
import com.tecknobit.equinoxcompose.session.setServerOfflineValue
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.network.Requester.Companion.sendRequest
import com.tecknobit.equinoxcore.network.Requester.Companion.toResponseData
import io.github.vinceglb.filekit.core.PlatformFile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement

class UpsertApplicationScreenViewModel(
    private val applicationId: String? = null,
) : EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
) {

    private val _application = MutableStateFlow<AmetistaApplication?>(
        value = null
    )
    val application = _application.asStateFlow()

    /**
     * **appIcon** -> the application icon
     */
    lateinit var appIcon: MutableState<String>

    var appIconPayload: PlatformFile? = null

    /**
     * **appIconBorderColor** -> the color used for the application icon picker
     */
    lateinit var appIconBorderColor: MutableState<Color>

    /**
     * **appName** -> the value of the application name
     */
    lateinit var appName: MutableState<String>

    /**
     * **appNameError** -> whether the [appName] field is not valid
     */
    lateinit var appNameError: MutableState<Boolean>

    /**
     * **appDescription** -> the value of the application description
     */
    lateinit var appDescription: MutableState<String>

    /**
     * **appDescriptionError** -> whether the [appDescription] field is not valid
     */
    lateinit var appDescriptionError: MutableState<Boolean>

    fun retrieveApplication() {
        if (applicationId == null)
            return
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    requester.getApplication(
                        applicationId = applicationId
                    )
                },
                onSuccess = {
                    setServerOfflineValue(false)
                    _application.value = Json.decodeFromJsonElement(it.toResponseData())
                },
                onFailure = { setHasBeenDisconnectedValue(true) },
                onConnectionError = { setServerOfflineValue(true) }
            )
        }
    }

    /**
     * Method to work on an application
     */
    fun upsertApplication() {
        if (applicationId == null)
            addApplication()
        else
            editApplication()
    }

    /**
     * Method to add a new application on the system
     */
    private fun addApplication() {
        if (!validForm())
            return
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    addApplication(
                        iconBytes = appIconPayload?.readBytes(),
                        iconName = appIconPayload?.name,
                        name = appName.value,
                        description = appDescription.value
                    )
                },
                onSuccess = {
                    val kReviewer = KReviewer()
                    kReviewer.reviewInApp {
                        navigator.goBack()
                    }
                },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

    /**
     * Method to edit an existing application
     */
    private fun editApplication() {
        if (!validForm())
            return
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    editApplication(
                        applicationId = applicationId!!,
                        iconBytes = appIconPayload?.readBytes(),
                        iconName = appIconPayload?.name,
                        name = appName.value,
                        description = appDescription.value
                    )
                },
                onSuccess = {
                    val kReviewer = KReviewer()
                    kReviewer.reviewInApp {
                        navigator.goBack()
                    }
                },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

    /**
     * Method to check the validity of the form as
     *
     * @return validity of the form as [Boolean]
     */
    private fun validForm(): Boolean {
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

}