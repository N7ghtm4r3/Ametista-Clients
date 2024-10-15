package com.tecknobit.ametista

import ametista.composeapp.generated.resources.Res
import ametista.composeapp.generated.resources.app_name
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import com.tecknobit.equinoxcompose.helpers.session.setUpSession
import moe.tlaster.precompose.ProvidePreComposeLocals
import org.jetbrains.compose.resources.stringResource

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = stringResource(Res.string.app_name),
        state = WindowState(
            placement = WindowPlacement.Maximized
        ),
        //icon = TODO: TO SET //painterResource("logo.png")
    ) {
        ProvidePreComposeLocals {
            InitSession()
            App()
        }
    }
}

@Composable
@NonRestartableComposable
private fun InitSession() {
    setUpSession(
        hasBeenDisconnectedAction = {
            // TODO: TO SET
        }
    )
}