package com.tecknobit.ametista.model

/**
 * The [ConnectionProcedureStep] is used represent a single step for the connection procedure
 *
 * @param title The title of the step
 * @param description The description of the step
 * @param onClick The action to execute when the [description] clicked
 *
 * @author N7ghtm4r3 - Tecknobit
 *
 */
data class ConnectionProcedureStep(
    val title: String,
    val description: String,
    val onClick: (() -> Unit)? = null
)