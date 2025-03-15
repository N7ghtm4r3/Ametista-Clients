package com.tecknobit.ametista.ui.screens.platform.data.performance

import com.tecknobit.ametistacore.FINAL_DATE_KEY
import com.tecknobit.ametistacore.INITIAL_DATE_KEY
import com.tecknobit.ametistacore.enums.PerformanceAnalyticType
import com.tecknobit.ametistacore.enums.PerformanceAnalyticType.ISSUES_PER_SESSION
import com.tecknobit.ametistacore.enums.PerformanceAnalyticType.LAUNCH_TIME
import com.tecknobit.ametistacore.enums.PerformanceAnalyticType.NETWORK_REQUESTS
import com.tecknobit.ametistacore.enums.PerformanceAnalyticType.TOTAL_ISSUES
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The [PerformanceDataFilters] is used to filters the performance data to retrieve
 *
 * @property launchTimeFilter Used to filter the launch time collected
 * @property networkRequestsFilter Used to filter the network requests collected
 * @property totalIssuesFilter Used to filter the total issues collected
 * @property issuesPerSessionFilter Used to filter the issues per session collected
 *
 * @author N7ghtm4r3 - Tecknobit
 */
@Serializable
class PerformanceDataFilters(
    @SerialName("LAUNCH_TIME")
    var launchTimeFilter: PerformanceFilter? = null,
    @SerialName("NETWORK_REQUESTS")
    var networkRequestsFilter: PerformanceFilter? = null,
    @SerialName("TOTAL_ISSUES")
    var totalIssuesFilter: PerformanceFilter? = null,
    @SerialName("ISSUES_PER_SESSION")
    var issuesPerSessionFilter: PerformanceFilter? = null,
) {

    /**
     * Method to set dynamically the filter value based on the analytic type
     *
     * @param type The type of the analytic to set the filter
     * @param performanceFilter The new filter to use
     */
    fun setFilter(
        type: PerformanceAnalyticType,
        performanceFilter: PerformanceFilter?,
    ) {
        when (type) {
            LAUNCH_TIME -> launchTimeFilter = performanceFilter
            NETWORK_REQUESTS -> networkRequestsFilter = performanceFilter
            TOTAL_ISSUES -> totalIssuesFilter = performanceFilter
            ISSUES_PER_SESSION -> issuesPerSessionFilter = performanceFilter
        }
    }

    /**
     * Method to get dynamically the filter value based on the analytic type
     *
     * @param analyticType The type of the analytic to set the filter
     *
     * @return the specific filter as [PerformanceFilter]
     */
    fun getFilter(
        analyticType: PerformanceAnalyticType,
    ): PerformanceFilter? {
        return when (analyticType) {
            LAUNCH_TIME -> launchTimeFilter
            NETWORK_REQUESTS -> networkRequestsFilter
            TOTAL_ISSUES -> totalIssuesFilter
            ISSUES_PER_SESSION -> issuesPerSessionFilter
        }
    }

    /**
     * The [PerformanceFilter] is the container of the filter data
     *
     * @property initialDate The initial date from retrieve the data
     * @property finalDate The final date to retrieve the data
     * @property versions The versions of the data to retrieve
     *
     * @author N7ghtm4r3 - Tecknobit
     */
    @Serializable
    data class PerformanceFilter(
        @SerialName(INITIAL_DATE_KEY)
        val initialDate: Long,
        @SerialName(FINAL_DATE_KEY)
        val finalDate: Long,
        val versions: List<String>,
    )

}