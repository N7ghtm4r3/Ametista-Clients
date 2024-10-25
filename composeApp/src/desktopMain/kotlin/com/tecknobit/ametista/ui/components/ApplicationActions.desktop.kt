package com.tecknobit.ametista.ui.components

actual fun reviewApp(
    flowAction: () -> Unit
) {
    flowAction.invoke()
}