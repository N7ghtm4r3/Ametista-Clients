package com.tecknobit.ametista.ui.screens.platform.data

import com.tecknobit.ametista.ui.screens.shared.data.AmetistaItem
import com.tecknobit.ametistacore.APP_VERSION_KEY
import com.tecknobit.ametistacore.enums.Platform
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName

/**
 * The [AmetistaItem] is used represent an analytic of the `Ametista` system
 *
 * @author N7ghtm4r3 - Tecknobit
 *
 *
 * @see AmetistaItem
 */
@Polymorphic
interface AmetistaAnalytic : AmetistaItem {

    /**
     * `appVersion` -> the application version of the analytic
     */
    @SerialName(APP_VERSION_KEY)
    val appVersion: String

    /**
     * `platform` -> the platform of the analytic
     */
    val platform: Platform

}
