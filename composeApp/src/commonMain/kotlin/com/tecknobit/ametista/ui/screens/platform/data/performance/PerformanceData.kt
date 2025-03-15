package com.tecknobit.ametista.ui.screens.platform.data.performance

import com.tecknobit.ametistacore.ISSUES_PER_SESSION_KEY
import com.tecknobit.ametistacore.IS_CUSTOM_FILTERED_KEY
import com.tecknobit.ametistacore.LAUNCH_TIME_KEY
import com.tecknobit.ametistacore.MAX_TEMPORAL_RANGE
import com.tecknobit.ametistacore.NETWORK_REQUESTS_KEY
import com.tecknobit.ametistacore.PERFORMANCE_ANALYTIC_TYPE_KEY
import com.tecknobit.ametistacore.TOTAL_ISSUES_KEY
import com.tecknobit.ametistacore.enums.PerformanceAnalyticType
import com.tecknobit.ametistacore.enums.PerformanceAnalyticType.LAUNCH_TIME
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The [PerformanceData] represents all the data about the performances of an application
 *
 * @property launchTime The container of the launch time collected
 * @property networkRequests The container of the network requests collected
 * @property totalIssues The container of the total issues collected
 * @property issuesPerSession The container of the issues per session collected
 *
 * @author N7ghtm4r3 - Tecknobit
 */
@Serializable
data class PerformanceData(
    @SerialName(LAUNCH_TIME_KEY)
    val launchTime: PerformanceDataItem,
    @SerialName(NETWORK_REQUESTS_KEY)
    val networkRequests: PerformanceDataItem,
    @SerialName(TOTAL_ISSUES_KEY)
    val totalIssues: PerformanceDataItem,
    @SerialName(ISSUES_PER_SESSION_KEY)
    val issuesPerSession: PerformanceDataItem,
) {

    /**
     * The [PerformanceDataItem] represents the data about a specific type of performance
     *
     * @property data The collected data
     * @property analyticType The type of the analytic collected
     * @property customFiltered Whether the collected data have been filtered with custom filters
     *
     * @author N7ghtm4r3 - Tecknobit
     */
    @Serializable
    data class PerformanceDataItem(
        val data: Map<String, List<PerformanceAnalytic>> = emptyMap(),
        @SerialName(PERFORMANCE_ANALYTIC_TYPE_KEY)
        val analyticType: PerformanceAnalyticType = LAUNCH_TIME,
        @SerialName(IS_CUSTOM_FILTERED_KEY)
        val customFiltered: Boolean = false,
    ) {

        /**
         * Method to get keys of the [.data] instance
         *
         * @return the keys of the [.data] instance as [Set] of [String]
         */
        fun sampleVersions(): Set<String> {
            return data.keys
        }

        /**
         * Method to check if there are no data available in the [.data] instance
         *
         * @return whether there are no data available as `boolean`
         */
        fun noDataAvailable(): Boolean {
            val values = data.values
            return values.isEmpty() || values.first().isEmpty()
        }

        /**
         * Method to get the initial date of the temporal range of each list present in the [.data]
         *
         * @return initial date timestamp as `long`
         *
         * @apiNote this method does not require a sort because is already sorted when retried from the database
         */
        fun getStartTemporalRangeDate(): Long {
            var startDate = Int.MAX_VALUE.toLong()
            val endDate = getEndTemporalRangeDate()
            for (analytics in data.values) {
                val checkTimestamp: Long = analytics[0].creationDate
                if (checkTimestamp < startDate)
                    startDate = checkTimestamp
            }
            if (endDate - startDate >= MAX_TEMPORAL_RANGE)
                startDate = endDate - MAX_TEMPORAL_RANGE
            return startDate
        }

        /**
         * Method to get the final date of the temporal range of each list present in the [.data]
         *
         * @return final date timestamp as `long`
         *
         * @apiNote this method does not require a sort because is already sorted when retried from the database
         */
        fun getEndTemporalRangeDate(): Long {
            var endDate: Long = 0
            for (analytics in data.values) {
                val checkTimestamp: Long = analytics[analytics.lastIndex].creationDate
                if (checkTimestamp > endDate)
                    endDate = checkTimestamp
            }
            return endDate
        }

    }

}