package com.tecknobit.ametista.ui.screens.platform.data.performance

import com.tecknobit.ametistacore.enums.PerformanceAnalyticType
import com.tecknobit.ametistacore.enums.PerformanceAnalyticType.ISSUES_PER_SESSION
import com.tecknobit.ametistacore.enums.PerformanceAnalyticType.LAUNCH_TIME
import com.tecknobit.ametistacore.enums.PerformanceAnalyticType.NETWORK_REQUESTS
import com.tecknobit.ametistacore.enums.PerformanceAnalyticType.TOTAL_ISSUES

// TODO: TO COMMENT
class PerformanceDataFilters(
    var launchTimeFilter: PerformanceFilter? = null,
    var networkRequestsFilter: PerformanceFilter? = null,
    var totalIssuesFilter: PerformanceFilter? = null,
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

    data class PerformanceFilter(
        val initialDate: Long,
        val finalDate: Long,
        val versions: List<String>,
    )

}