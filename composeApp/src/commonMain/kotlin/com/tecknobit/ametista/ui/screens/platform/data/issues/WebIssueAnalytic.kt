package com.tecknobit.ametista.ui.screens.platform.data.issues

import com.tecknobit.ametista.ui.screens.applications.data.AmetistaDevice
import com.tecknobit.ametistacore.APP_VERSION_KEY
import com.tecknobit.ametistacore.BROWSER_VERSION_KEY
import com.tecknobit.ametistacore.CREATION_DATE_KEY
import com.tecknobit.ametistacore.enums.Platform
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WebIssueAnalytic(
    override val id: String,
    override val name: String,
    @SerialName(CREATION_DATE_KEY)
    override val creationDate: Long,
    @SerialName(APP_VERSION_KEY)
    override val appVersion: String,
    override val platform: Platform,
    override val issue: String,
    override val device: AmetistaDevice,
    val browser: String,
    @SerialName(BROWSER_VERSION_KEY)
    val browserVersion: String,
) : IssueAnalytic
