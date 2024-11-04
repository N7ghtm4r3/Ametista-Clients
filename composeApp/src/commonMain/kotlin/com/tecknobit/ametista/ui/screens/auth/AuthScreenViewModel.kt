package com.tecknobit.ametista.ui.screens.auth

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import com.tecknobit.ametista.localUser
import com.tecknobit.ametista.navigator
import com.tecknobit.ametista.requester
import com.tecknobit.ametista.ui.screens.AmetistaScreen.Companion.APPLICATIONS_SCREEN
import com.tecknobit.ametista.ui.screens.AmetistaScreen.Companion.CHANGE_VIEWER_PASSWORD_SCREEN
import com.tecknobit.ametistacore.models.AmetistaUser.DEFAULT_VIEWER_PASSWORD
import com.tecknobit.ametistacore.models.AmetistaUser.LANGUAGE_KEY
import com.tecknobit.ametistacore.models.AmetistaUser.ROLE_KEY
import com.tecknobit.ametistacore.models.AmetistaUser.Role.ADMIN
import com.tecknobit.ametistacore.models.AmetistaUser.Role.VIEWER
import com.tecknobit.apimanager.formatters.JsonHelper
import com.tecknobit.equinox.annotations.CustomParametersOrder
import com.tecknobit.equinox.environment.records.EquinoxUser.getValidUserLanguage
import com.tecknobit.equinox.inputs.InputValidator.DEFAULT_LANGUAGE
import com.tecknobit.equinox.inputs.InputValidator.LANGUAGES_SUPPORTED
import com.tecknobit.equinoxcompose.helpers.viewmodels.EquinoxAuthViewModel

class AuthScreenViewModel : EquinoxAuthViewModel(
    snackbarHostState = SnackbarHostState(),
    requester = requester,
    localUser = localUser
) {

    lateinit var isAdmin: MutableState<Boolean>

    lateinit var isAdminSignUp: MutableState<Boolean>

    fun login() {
        if (isAdmin.value)
            adminAuth()
        else
            viewerSignIn()
    }

    private fun adminAuth() {
        if (isAdminSignUp.value)
            adminSignUp()
        else
            adminsSignIn()
    }

    private fun adminSignUp() {
        if (signUpFormIsValid()) {
            val language = getUserLanguage()
            requester.changeHost(
                host = host.value
            )
            requester.sendRequest(
                request = {
                    requester.adminSignUp(
                        adminCode = serverSecret.value,
                        name = name.value,
                        surname = surname.value,
                        email = email.value,
                        password = password.value,
                        language = language
                    )
                },
                onSuccess = { response ->
                    launchApp(
                        response = response,
                        name = name.value,
                        surname = surname.value,
                        language = language,
                        custom = arrayOf(ADMIN)
                    )
                },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

    private fun getUserLanguage(): String {
        val currentLanguageTag = getValidUserLanguage()
        val language = LANGUAGES_SUPPORTED[currentLanguageTag]
        return if (language == null)
            DEFAULT_LANGUAGE
        else
            currentLanguageTag
    }

    private fun adminsSignIn() {
        if (signInFormIsValid()) {
            requester.changeHost(
                host = host.value
            )
            requester.sendRequest(
                request = {
                    requester.adminSignIn(
                        adminCode = serverSecret.value,
                        email = email.value,
                        password = password.value
                    )
                },
                onSuccess = { response ->
                    launchApp(
                        response = response,
                        name = name.value,
                        surname = surname.value,
                        language = response.getString(LANGUAGE_KEY),
                        custom = arrayOf(ADMIN)
                    )
                },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

    private fun viewerSignIn() {
        if (signInFormIsValid()) {
            requester.changeHost(
                host = host.value
            )
            requester.sendRequest(
                request = {
                    requester.viewerSignIn(
                        serverSecret = serverSecret.value,
                        email = email.value,
                        password = password.value
                    )
                },
                onSuccess = { response ->
                    launchApp(
                        response = response,
                        name = name.value,
                        surname = surname.value,
                        language = response.getString(LANGUAGE_KEY),
                        custom = arrayOf(VIEWER)
                    )
                },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

    @CustomParametersOrder(order = [ROLE_KEY])
    override fun launchApp(
        response: JsonHelper,
        name: String,
        surname: String,
        language: String,
        vararg custom: Any?
    ) {
        super.launchApp(response, name, surname, language, custom[0])
        val route = if (localUser.password == DEFAULT_VIEWER_PASSWORD)
            CHANGE_VIEWER_PASSWORD_SCREEN
        else
            APPLICATIONS_SCREEN
        navigator.navigate(route)
    }

}