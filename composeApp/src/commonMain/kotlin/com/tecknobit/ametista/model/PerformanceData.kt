package com.tecknobit.ametista.model

import com.tecknobit.ametistacore.models.analytics.performance.PerformanceAnalytic

data class PerformanceData(
    val versionSamples: List<String>,
    val values: List<PerformanceAnalytic>
) {

    fun chartData(): List<Double> {
        return values.map { analytic -> analytic.value }
    }

}
