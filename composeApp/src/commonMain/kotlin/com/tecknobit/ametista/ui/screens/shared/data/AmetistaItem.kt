package com.tecknobit.ametista.ui.screens.shared.data

import com.tecknobit.ametistacore.CREATION_DATE_KEY
import kotlinx.serialization.SerialName

interface AmetistaItem {

    val id: String

    val name: String

    @SerialName(CREATION_DATE_KEY)
    val creationDate: Long

}