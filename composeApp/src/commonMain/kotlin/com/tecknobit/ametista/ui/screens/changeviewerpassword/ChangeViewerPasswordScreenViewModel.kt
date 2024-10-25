package com.tecknobit.ametista.ui.screens.changeviewerpassword

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import com.tecknobit.ametista.navigator
import com.tecknobit.ametista.ui.screens.AmetistaScreen.Companion.APPLICATIONS_SCREEN
import com.tecknobit.ametistacore.helpers.AmetistaValidator.isNewPasswordValid
import com.tecknobit.equinoxcompose.helpers.viewmodels.EquinoxViewModel

class ChangeViewerPasswordScreenViewModel : EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
) {

    lateinit var password: MutableState<String>

    lateinit var passwordError: MutableState<Boolean>

    fun changeViewerPassword() {
        if (!isNewPasswordValid(password.value)) {
            passwordError.value = true
            return
        }
        // TODO: MAKE THE REQUEST THEN
        navigator.navigate(APPLICATIONS_SCREEN)
    }

}