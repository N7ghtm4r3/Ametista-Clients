package com.tecknobit.ametista.ui.screens.changeviewerpassword.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.lifecycle.viewModelScope
import com.tecknobit.ametista.APPLICATIONS_SCREEN
import com.tecknobit.ametista.localUser
import com.tecknobit.ametista.navigator
import com.tecknobit.ametista.requester
import com.tecknobit.ametistacore.helpers.AmetistaValidator.isNewPasswordValid
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.network.Requester.Companion.sendRequest
import kotlinx.coroutines.launch

/**
 * The **ChangeViewerPasswordScreenViewModel** class is the support class used to execute the request
 * to change the default password of a [com.tecknobit.ametistacore.enums.Role.VIEWER]
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see androidx.lifecycle.ViewModel
 * @see com.tecknobit.equinoxcompose.session.Retriever
 * @see EquinoxViewModel
 */
class ChangeViewerPasswordScreenViewModel : EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
) {

    /**
     * `password` -> the password of the user
     */
    lateinit var password: MutableState<String>

    /**
     * `passwordError` -> whether the [password] field is not valid
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
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    changeViewerPresetPassword(
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

}