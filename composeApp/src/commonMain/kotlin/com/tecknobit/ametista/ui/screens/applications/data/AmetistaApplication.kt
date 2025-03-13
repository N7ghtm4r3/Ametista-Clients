package com.tecknobit.ametista.ui.screens.applications.data

import com.tecknobit.ametista.ui.screens.applications.data.analytics.IssueAnalytic
import com.tecknobit.ametista.ui.screens.applications.data.analytics.PerformanceAnalytic
import com.tecknobit.ametista.ui.screens.shared.data.AmetistaItem
import com.tecknobit.ametistacore.APPLICATION_ICON_KEY
import com.tecknobit.ametistacore.CREATION_DATE_KEY
import com.tecknobit.ametistacore.enums.Platform
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AmetistaApplication(
    override val id: String,
    override val name: String,
    @SerialName(CREATION_DATE_KEY)
    override val creationDate: Long,
    @SerialName(APPLICATION_ICON_KEY)
    val applicationIcon: String,
    val description: String,
    val platforms: Set<Platform>,
    val issues: List<IssueAnalytic>,
    val performanceAnalytics: List<PerformanceAnalytic>,
) : AmetistaItem
