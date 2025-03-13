package com.tecknobit.ametista.ui.screens.applications.data.analytics

import com.tecknobit.ametista.ui.screens.shared.data.AmetistaItem
import com.tecknobit.ametistacore.APP_VERSION_KEY
import com.tecknobit.ametistacore.enums.Platform
import kotlinx.serialization.SerialName

interface AmetistaAnalytic : AmetistaItem {

    @SerialName(APP_VERSION_KEY)
    val appVersion: String

    val platform: Platform

}
