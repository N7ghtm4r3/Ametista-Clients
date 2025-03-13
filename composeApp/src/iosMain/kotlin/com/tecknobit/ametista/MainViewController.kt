package com.tecknobit.ametista

import androidx.compose.ui.window.ComposeUIViewController
import com.tecknobit.equinoxcompose.session.setUpSession

/**
 * Method used to start the of `Ametista` iOs application
 */
fun MainViewController() = ComposeUIViewController {
    setUpSession {
        // TODO TO SET
    }
    App()
}
