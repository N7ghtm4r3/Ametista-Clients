package com.tecknobit.ametista.ui.screens.platform.data.performance

import com.tecknobit.ametista.ui.screens.platform.data.AmetistaAnalytic
import com.tecknobit.ametista.ui.screens.shared.data.AmetistaItem
import com.tecknobit.ametistacore.APP_VERSION_KEY
import com.tecknobit.ametistacore.CREATION_DATE_KEY
import com.tecknobit.ametistacore.PERFORMANCE_ANALYTIC_TYPE_KEY
import com.tecknobit.ametistacore.PERFORMANCE_VALUE_KEY
import com.tecknobit.ametistacore.enums.PerformanceAnalyticType
import com.tecknobit.ametistacore.enums.Platform
import com.tecknobit.equinoxcore.helpers.NAME_KEY
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The [PerformanceAnalytic] represents an analytic about performance
 *
 * @property id The identifier of the analytic
 * @property name The name of the analytic
 * @property creationDate The date when the analytic has been created
 * @property appVersion The platform of the analytic
 * @property platform The description of the analytic
 * @property value The value of the analytic
 * @property performanceAnalyticType The type of the analytic
 *
 * @author N7ghtm4r3 - Tecknobit
 *
 * @see AmetistaItem
 * @see AmetistaAnalytic
 */
@Serializable
data class PerformanceAnalytic(
    override val id: String,
    @SerialName(NAME_KEY)
    private val _name: String?,
    @SerialName(CREATION_DATE_KEY)
    override val creationDate: Long,
    @SerialName(APP_VERSION_KEY)
    override val appVersion: String,
    override val platform: Platform,
    @SerialName(PERFORMANCE_VALUE_KEY)
    val value: Double,
    @SerialName(PERFORMANCE_ANALYTIC_TYPE_KEY)
    val performanceAnalyticType: PerformanceAnalyticType,
) : AmetistaAnalytic {

    /**
     * `name` -> the name of the analytic
     */
    override val name: String
        get() = ""

}
