package com.tecknobit.ametista.ui.screens.auth

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import com.tecknobit.ametista.helpers.AmetistaLocalUser
import com.tecknobit.ametista.helpers.AmetistaRequester
import com.tecknobit.ametista.navigator
import com.tecknobit.ametista.ui.screens.AmetistaScreen.Companion.APPLICATIONS_SCREEN
import com.tecknobit.equinoxcompose.helpers.viewmodels.EquinoxAuthViewModel

class AuthScreenViewModel : EquinoxAuthViewModel(
    snackbarHostState = SnackbarHostState(),
    requester = AmetistaRequester(
        host = ""
    ), // TODO: TO USE THE REAL ONE 
    localUser = AmetistaLocalUser() // TODO: TO USE THE REAL ONE
) {

    lateinit var isAdmin: MutableState<Boolean>

    lateinit var isAdminSignUp: MutableState<Boolean>

    fun login() {
        // TODO: MAKE THE REAL PROCEDURE THEN
        navigator.navigate(APPLICATIONS_SCREEN)
    }

}