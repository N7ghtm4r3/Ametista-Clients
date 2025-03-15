package com.tecknobit.ametista

import androidx.compose.ui.window.ComposeUIViewController
import com.tecknobit.equinoxcompose.session.setUpSession

/**
 * Method used to start the of `Ametista` iOs application
 */
fun MainViewController() = ComposeUIViewController {
    setUpSession {
        localUser.clear()
        requester.setUserCredentials(
            userId = null,
            userToken = null
        )
        navigator.navigate(AUTH_SCREEN)
    }
    App()
}
