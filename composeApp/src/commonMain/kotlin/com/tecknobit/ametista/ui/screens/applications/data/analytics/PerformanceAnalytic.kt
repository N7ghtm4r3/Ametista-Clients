package com.tecknobit.ametista.ui.screens.applications.data.analytics

import com.tecknobit.ametistacore.APP_VERSION_KEY
import com.tecknobit.ametistacore.CREATION_DATE_KEY
import com.tecknobit.ametistacore.DATA_UPDATES_KEY
import com.tecknobit.ametistacore.PERFORMANCE_ANALYTIC_TYPE_KEY
import com.tecknobit.ametistacore.PERFORMANCE_VALUE_KEY
import com.tecknobit.ametistacore.enums.PerformanceAnalyticType
import com.tecknobit.ametistacore.enums.Platform
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PerformanceAnalytic(
    override val id: String,
    override val name: String,
    @SerialName(CREATION_DATE_KEY)
    override val creationDate: Long,
    @SerialName(APP_VERSION_KEY)
    override val appVersion: String,
    override val platform: Platform,
    @SerialName(PERFORMANCE_VALUE_KEY)
    val value: Double,
    @SerialName(PERFORMANCE_ANALYTIC_TYPE_KEY)
    val performanceAnalyticType: PerformanceAnalyticType,
    @SerialName(DATA_UPDATES_KEY)
    val dataUpdates: Int,
) : AmetistaAnalytic
