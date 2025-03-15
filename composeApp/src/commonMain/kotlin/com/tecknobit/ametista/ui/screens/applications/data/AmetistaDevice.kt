package com.tecknobit.ametista.ui.screens.applications.data

import com.tecknobit.ametista.ui.screens.shared.data.AmetistaItem
import com.tecknobit.ametistacore.OS_VERSION_KEY
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The [AmetistaDevice] class is used represent a device of the `Ametista` system
 *
 * @property id The identifier of the device
 * @property brand The brand of the device
 * @property model The model of the device
 * @property os The operative system of the device
 * @property osVersion The version of the operative system of the device
 *
 * @author N7ghtm4r3 - Tecknobit
 *
 * @see AmetistaItem
 *
 */
@Serializable
data class AmetistaDevice(
    val id: String,
    val brand: String,
    val model: String,
    val os: String,
    @SerialName(OS_VERSION_KEY)
    val osVersion: String,
)
