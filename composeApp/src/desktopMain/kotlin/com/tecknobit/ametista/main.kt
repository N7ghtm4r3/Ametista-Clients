package com.tecknobit.ametista

import ametista.composeapp.generated.resources.Res
import ametista.composeapp.generated.resources.app_name
import ametista.composeapp.generated.resources.logo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import com.tecknobit.ametista.ui.screens.AmetistaScreen.Companion.AUTH_SCREEN
import com.tecknobit.equinoxcompose.helpers.session.setUpSession
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/**
 * The [main] function is used as entry point of Nova's application for Desktop
 *
 * @author N7ghtm4r3 - Tecknobit
 *
 */
fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = stringResource(Res.string.app_name),
        state = WindowState(
            placement = WindowPlacement.Maximized
        ),
        icon = painterResource(Res.drawable.logo)
    ) {
        InitSession()
        App()
    }
}

/**
 * Function to init the instances for the session
 *
 */
@Composable
@NonRestartableComposable
private fun InitSession() {
    setUpSession(
        hasBeenDisconnectedAction = {
            localUser.clear()
            requester.setUserCredentials(
                userId = null,
                userToken = null
            )
            navigator.navigate(AUTH_SCREEN)
        }
    )
}