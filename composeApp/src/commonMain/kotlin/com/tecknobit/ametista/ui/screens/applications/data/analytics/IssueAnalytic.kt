package com.tecknobit.ametista.ui.screens.applications.data.analytics

import com.tecknobit.ametista.ui.screens.applications.data.AmetistaDevice
import com.tecknobit.ametistacore.APP_VERSION_KEY
import com.tecknobit.ametistacore.CREATION_DATE_KEY
import com.tecknobit.ametistacore.enums.Platform
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IssueAnalytic(
    override val id: String,
    override val name: String,
    @SerialName(CREATION_DATE_KEY)
    override val creationDate: Long,
    @SerialName(APP_VERSION_KEY)
    override val appVersion: String,
    override val platform: Platform,
    val device: AmetistaDevice,
) : AmetistaAnalytic
