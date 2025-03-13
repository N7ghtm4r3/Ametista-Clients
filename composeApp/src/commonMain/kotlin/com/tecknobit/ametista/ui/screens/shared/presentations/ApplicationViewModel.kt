package com.tecknobit.ametista.ui.screens.shared.presentations

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewModelScope
import com.tecknobit.ametista.requester
import com.tecknobit.ametista.ui.screens.applications.data.AmetistaApplication
import com.tecknobit.ametista.ui.theme.platforms.android.errorDark
import com.tecknobit.ametistacore.helpers.AmetistaValidator.isAppDescriptionValid
import com.tecknobit.ametistacore.helpers.AmetistaValidator.isAppNameValid
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.annotations.Structure
import com.tecknobit.equinoxcore.network.Requester.Companion.sendRequest
import kotlinx.coroutines.launch
import kotlinx.serialization.json.buildJsonObject

/**
 * The **ApplicationViewModel** class is the support class used to execute the managing requests related
 * to an [AmetistaApplication]
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see androidx.lifecycle.ViewModel
 * @see com.tecknobit.equinoxcompose.session.Retriever
 * @see EquinoxViewModel
 */
@Structure
abstract class ApplicationViewModel : EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
) {

    /**
     * **workOnApplication** -> the state to allow the creation or the editing of an application
     */
    lateinit var workOnApplication: MutableState<Boolean>

    /**
     * **appIcon** -> the application icon
     */
    lateinit var appIcon: MutableState<String>

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

    /**
     * Method to edit an existing application
     *
     * @param application The application to edit
     * @param onSuccess The action to execute when the application has been edited
     */
    fun editApplication(
        application: AmetistaApplication,
        onSuccess: () -> Unit
    ) {
        if (!validForm())
            return
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    // TODO: TO SET
                    buildJsonObject { }
                    /*editApplication(
                        application = application,
                        icon = appIcon.value,
                        name = appName.value,
                        description = appDescription.value
                    )*/
                },
                onSuccess = { onSuccess.invoke() },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

    /**
     * Method to check the validity of the form as
     *
     * @return validity of the form as [Boolean]
     */
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

    /**
     * Method to delete an application
     *
     * @param application The application to delete
     * @param onDelete The action to execute when the application has been deleted
     */
    open fun deleteApplication(
        application: AmetistaApplication,
        onDelete: () -> Unit
    ) {
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    deleteApplication(
                        application = application
                    )
                },
                onSuccess = { onDelete.invoke() },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

}