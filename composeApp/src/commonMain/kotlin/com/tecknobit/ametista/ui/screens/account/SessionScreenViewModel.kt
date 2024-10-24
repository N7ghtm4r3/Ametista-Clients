package com.tecknobit.ametista.ui.screens.account

import androidx.compose.material3.SnackbarHostState
import com.tecknobit.ametista.helpers.AmetistaLocalUser
import com.tecknobit.ametista.helpers.AmetistaRequester
import com.tecknobit.equinoxcompose.helpers.viewmodels.EquinoxProfileViewModel

class SessionScreenViewModel : EquinoxProfileViewModel(
    snackbarHostState = SnackbarHostState(),
    requester = AmetistaRequester(""), // TODO: USE THE REAL ONE
    localUser = AmetistaLocalUser()// TODO: USE THE REAL ONE
) {

    fun logout(
        onLogout: () -> Unit
    ) {
        // TODO: MAKE THE REAL PROCEDURE THEN
        onLogout.invoke()
    }

}