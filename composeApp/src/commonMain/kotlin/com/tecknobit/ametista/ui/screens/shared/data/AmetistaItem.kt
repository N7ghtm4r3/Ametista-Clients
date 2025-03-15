package com.tecknobit.ametista.ui.screens.shared.data

import com.tecknobit.ametistacore.CREATION_DATE_KEY
import kotlinx.serialization.SerialName

/**
 * The [AmetistaItem] is used represent an item of the `Ametista` system
 *
 * @author N7ghtm4r3 - Tecknobit
 *
 */
interface AmetistaItem {

    /**
     * `id` -> the identifier of the item
     */
    val id: String

    /**
     * `name` -> the name of the item
     */
    val name: String

    /**
     * `creationDate` -> the date when the item has been created
     */
    @SerialName(CREATION_DATE_KEY)
    val creationDate: Long

}