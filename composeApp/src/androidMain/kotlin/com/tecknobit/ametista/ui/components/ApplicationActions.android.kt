package com.tecknobit.ametista.ui.components

import com.tecknobit.ametista.helpers.ContextProvider
import com.tecknobit.ametista.helpers.ReviewHelper

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