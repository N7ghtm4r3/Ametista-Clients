package com.tecknobit.ametista.ui.screens.changeviewerpassword.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import com.tecknobit.ametista.localUser
import com.tecknobit.ametista.navigator
import com.tecknobit.ametista.requester
import com.tecknobit.ametista.ui.screens.shared.presenters.AmetistaScreen.Companion.APPLICATIONS_SCREEN
import com.tecknobit.ametistacore.helpers.AmetistaValidator.isNewPasswordValid
import com.tecknobit.equinoxcompose.helpers.viewmodels.EquinoxViewModel

/**
 * The **ChangeViewerPasswordScreenViewModel** class is the support class used to execute the request
 * to change the default password of a [com.tecknobit.ametistacore.models.AmetistaUser.Role.VIEWER]
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see EquinoxViewModel
 * @see ViewModel
 * @see FetcherManagerWrapper
 */
class ChangeViewerPasswordScreenViewModel : EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
) {

    /**
     * **password** -> the password of the user
     */
    lateinit var password: MutableState<String>

    /**
     * **passwordError** -> whether the [password] field is not valid
     */
    lateinit var passwordError: MutableState<Boolean>

    /**
     * Method to change the default viewer password
     */
    fun changeViewerPassword() {
        if (!isNewPasswordValid(password.value)) {
            passwordError.value = true
            return
        }
        requester.sendRequest(
            request = {
                requester.changeViewerPresetPassword(
                    password = password.value
                )
            },
            onSuccess = {
                localUser.password = password.value
                navigator.navigate(APPLICATIONS_SCREEN)
            },
            onFailure = { showSnackbarMessage(it) }
        )
    }

}