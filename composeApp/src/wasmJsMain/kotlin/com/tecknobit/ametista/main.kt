package com.tecknobit.ametista

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.tecknobit.equinoxcompose.session.setUpSession
import kotlinx.browser.document

/**
 * Method used to start the of `Ametista` webapp
 */
@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    ComposeViewport(document.body!!) {
        setUpSession {
            // TODO: TO SET
        }
        App()
    }
}