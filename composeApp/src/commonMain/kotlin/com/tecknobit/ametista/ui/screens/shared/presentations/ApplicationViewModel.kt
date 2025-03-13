package com.tecknobit.ametista.ui.screens.shared.presentations

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

/**
 * The **ApplicationViewModel** class is the support class used to execute the managing requests related
 * to an [AmetistaApplication]
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see EquinoxViewModel
 * @see ViewModel
 * @see FetcherManagerWrapper
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