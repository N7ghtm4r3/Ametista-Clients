package com.tecknobit.ametista.ui.screens.auth.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import com.tecknobit.ametista.localUser
import com.tecknobit.ametista.navigator
import com.tecknobit.ametista.requester
import com.tecknobit.ametista.ui.screens.shared.presenters.AmetistaScreen.Companion.APPLICATIONS_SCREEN
import com.tecknobit.ametista.ui.screens.shared.presenters.AmetistaScreen.Companion.CHANGE_VIEWER_PASSWORD_SCREEN
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
import com.tecknobit.equinoxcompose.helpers.viewmodels.EquinoxViewModel

/**
 * The **AuthScreenViewModel** class is the support class used to execute the authentication requests to the backend
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see EquinoxAuthViewModel
 * @see EquinoxViewModel
 * @see ViewModel
 * @see FetcherManagerWrapper
 */
class AuthScreenViewModel : EquinoxAuthViewModel(
    snackbarHostState = SnackbarHostState(),
    requester = requester,
    localUser = localUser
) {

    /**
     * **isAdmin** -> whether the user who trying to authenticate is an [ADMIN]
     */
    lateinit var isAdmin: MutableState<Boolean>

    /**
     * **isAdminSignUp** -> whether the user is an [ADMIN] and the auth operation is a sign-up operation
     */
    lateinit var isAdminSignUp: MutableState<Boolean>

    /**
     * Method to execute the authentication ope
     */
    fun login() {
        if (isAdmin.value)
            adminAuth()
        else
            viewerSignIn()
    }

    /**
     * Method to execute the [ADMIN] authentication ope
     */
    private fun adminAuth() {
        if (isAdminSignUp.value)
            adminSignUp()
        else
            adminsSignIn()
    }

    /**
     * Method to execute the [ADMIN] sign-up ope
     */
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

    /**
     * Method to get the current user language
     *
     * @return the user language as [String]
     */
    private fun getUserLanguage(): String {
        val currentLanguageTag = getValidUserLanguage()
        val language = LANGUAGES_SUPPORTED[currentLanguageTag]
        return if (language == null)
            DEFAULT_LANGUAGE
        else
            currentLanguageTag
    }

    /**
     * Method to execute the [ADMIN] sign-in ope
     */
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

    /**
     * Method to execute the [VIEWER] sign-in ope
     */
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

    /**
     * Method to launch the application after the authentication request, will be instantiated with the user details
     * both the [requester] and the [localUser]
     *
     * @param response The response of the authentication request
     * @param name The name of the user
     * @param surname The surname of the user
     * @param language The language of the user
     * @param custom The custom parameters added in a customization of the [EquinoxUser]
     */
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