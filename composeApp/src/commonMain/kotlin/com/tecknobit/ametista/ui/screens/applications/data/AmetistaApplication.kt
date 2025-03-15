package com.tecknobit.ametista.ui.screens.applications.data

import com.tecknobit.ametista.ui.screens.platform.data.issues.IssueAnalyticImpl
import com.tecknobit.ametista.ui.screens.platform.data.performance.PerformanceAnalytic
import com.tecknobit.ametista.ui.screens.shared.data.AmetistaItem
import com.tecknobit.ametistacore.APPLICATION_ICON_KEY
import com.tecknobit.ametistacore.CREATION_DATE_KEY
import com.tecknobit.ametistacore.PERFORMANCE_ANALYTICS_KEY
import com.tecknobit.ametistacore.enums.Platform
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The [AmetistaApplication] class is used represent an application of the `Ametista` system
 *
 * @property id The identifier of the application
 * @property name The name of the application
 * @property creationDate The date when the application has been created
 * @property applicationIcon The icon of the application
 * @property description The description of the application
 * @property platforms The platforms of the application
 * @property issues The issues of the application
 * @property performanceAnalytics The analytics performance of the application
 *
 * @author N7ghtm4r3 - Tecknobit
 *
 * @see AmetistaItem
 *
 */
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
    val issues: List<IssueAnalyticImpl> = emptyList(),
    @SerialName(PERFORMANCE_ANALYTICS_KEY)
    val performanceAnalytics: List<PerformanceAnalytic> = emptyList(),
) : AmetistaItem
