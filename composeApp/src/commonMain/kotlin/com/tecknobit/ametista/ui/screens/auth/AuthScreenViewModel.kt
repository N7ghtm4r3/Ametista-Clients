package com.tecknobit.ametista.ui.screens.auth

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import com.tecknobit.ametista.helpers.AmetistaLocalUser
import com.tecknobit.ametista.helpers.AmetistaRequester
import com.tecknobit.ametista.localUser
import com.tecknobit.ametista.navigator
import com.tecknobit.ametista.ui.screens.AmetistaScreen.Companion.APPLICATIONS_SCREEN
import com.tecknobit.ametista.ui.screens.AmetistaScreen.Companion.CHANGE_VIEWER_PASSWORD_SCREEN
import com.tecknobit.ametistacore.models.AmetistaUser.DEFAULT_VIEWER_PASSWORD
import com.tecknobit.apimanager.formatters.JsonHelper
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

    override fun launchApp(
        response: JsonHelper,
        name: String,
        surname: String,
        language: String,
        vararg custom: Any?
    ) {
        super.launchApp(response, name, surname, language, *custom)
        val route = if (localUser.password == DEFAULT_VIEWER_PASSWORD)
            CHANGE_VIEWER_PASSWORD_SCREEN
        else
            APPLICATIONS_SCREEN
        navigator.navigate(route)
    }

}