package com.tecknobit.ametista.ui.screens.applications.data

import com.tecknobit.ametista.ui.screens.shared.data.AmetistaItem
import com.tecknobit.ametistacore.OS_VERSION_KEY
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AmetistaDevice(
    override val id: String,
    val brand: String,
    val model: String,
    val os: String,
    @SerialName(OS_VERSION_KEY)
    val osVersion: String,
) : AmetistaItem
