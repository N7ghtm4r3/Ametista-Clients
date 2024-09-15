package com.tecknobit.ametista

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.tecknobit.equinoxcompose.helpers.session.EquinoxScreen

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Ametista",
    ) {
        App()
    }
}