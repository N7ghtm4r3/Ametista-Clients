package com.tecknobit.ametista.ui.components

import com.tecknobit.ametista.helpers.ContextProvider
import com.tecknobit.ametista.helpers.ReviewHelper

/**
 * Method to review the application during the runtime
 *
 * @param flowAction The action to execute after the app has been reviewed or the normal workflow if
 * not reviewed
 */
actual fun reviewApp(
    flowAction: () -> Unit
) {
    val reviewHelper = ReviewHelper(
        activity = ContextProvider.getCurrentActivity()!!
    )
    reviewHelper.reviewInApp(
        flowAction = flowAction
    )
}