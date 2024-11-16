package com.tecknobit.ametista.ui.components

/**
 * Method to review the application during the runtime
 *
 * @param flowAction The action to execute after the app has been reviewed or the normal workflow if
 * not reviewed
 */
actual fun reviewApp(
    flowAction: () -> Unit
) {
    flowAction.invoke()
}