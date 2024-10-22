package com.tecknobit.ametista.ui.screens.platform

import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.dokar.chiptextfield.Chip
import com.dokar.chiptextfield.ChipTextFieldState
import com.tecknobit.ametistacore.models.AmetistaApplication
import com.tecknobit.ametistacore.models.Platform
import com.tecknobit.ametistacore.models.analytics.AmetistaAnalytic
import com.tecknobit.ametistacore.models.analytics.AmetistaAnalytic.AnalyticType
import com.tecknobit.ametistacore.models.analytics.performance.PerformanceAnalytic
import com.tecknobit.ametistacore.models.analytics.performance.PerformanceData
import com.tecknobit.equinoxcompose.helpers.viewmodels.EquinoxViewModel
import io.github.ahmad_hamwi.compose.pagination.PaginationState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.random.Random

class PlatformScreenViewModel(
    applicationId: String,
    platform: Platform
) : EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
) {

    companion object {

        const val MAX_CHIPS_FILTERS = 10

    }

    val paginationState = PaginationState<Int, AmetistaAnalytic>(
        initialPageKey = 1,
        onRequestPage = { loadIssues() }
    )

    lateinit var analyticType: MutableState<AnalyticType>

    lateinit var filtersState: ChipTextFieldState<Chip>

    private var _filters = HashSet<String>()

    private val _filtersSet = MutableStateFlow(
        value = false
    )
    val filtersSet: StateFlow<Boolean> = _filtersSet

    val versionSamplesFilters = SnapshotStateList<String>()

    private val _performanceData = MutableStateFlow<PerformanceData?>(
        value = null
    )
    val performanceData: StateFlow<PerformanceData?> = _performanceData

    lateinit var newVersionFilters: MutableList<String>

    private fun loadIssues() {
        // TODO: MAKE THE REAL REQUEST THEN ALSO WITH _filters
        val items = AmetistaApplication("Space").issues
        // TODO: TO LOAD NEXT PAGE
        paginationState.appendPage(
            items = items,
            nextPageKey = 1,
            isLastPage = true
        )
    }

    fun filterIssues(
        onSuccess: () -> Unit
    ) {
        _filters = filtersState.chips.map { chip -> chip.text }.toHashSet()
        _filtersSet.value = _filters.isNotEmpty()
        onSuccess.invoke()
    }

    fun clearFilters() {
        _filters.clear()
        _filtersSet.value = false
    }

    fun getPerformanceAnalytics() {
        // TODO: MAKE THE REAL REQUEST THEN
        // TODO: LOAD FILTERS 
        /*versionSamplesFilters.clear() 
        versionSamplesFilters.add("from request response")*/
        if (Random.Default.nextBoolean() || true) {
            _performanceData.value = PerformanceData(
                "gaga",
                PerformanceData.PerformanceDataItem(
                    mutableMapOf<String?, MutableList<PerformanceAnalytic>?>().apply {
                        put(
                            "1.0.0",
                            MutableList(10) { PerformanceAnalytic(Random.Default.nextDouble()) })
                        put(
                            "1.0.1",
                            MutableList(10) { PerformanceAnalytic(Random.Default.nextDouble()) })
                        put(
                            "1.0.2",
                            MutableList(10) { PerformanceAnalytic(Random.Default.nextDouble()) })
                    }
                ),
                PerformanceData.PerformanceDataItem(
                    mutableMapOf<String?, MutableList<PerformanceAnalytic>?>().apply {
                        put(
                            "1.0.0",
                            MutableList(10) { PerformanceAnalytic(Random.Default.nextDouble()) })
                        put(
                            "1.0.1",
                            MutableList(10) { PerformanceAnalytic(Random.Default.nextDouble()) })
                        put(
                            "1.0.2",
                            MutableList(10) { PerformanceAnalytic(Random.Default.nextDouble()) })
                    }
                ),
                PerformanceData.PerformanceDataItem(
                    mutableMapOf<String?, MutableList<PerformanceAnalytic>?>().apply {
                        put(
                            "1.0.0",
                            MutableList(10) { PerformanceAnalytic(Random.Default.nextDouble()) })
                        put(
                            "1.0.1",
                            MutableList(10) { PerformanceAnalytic(Random.Default.nextDouble()) })
                        put(
                            "1.0.2",
                            MutableList(10) { PerformanceAnalytic(Random.Default.nextDouble()) })
                    }
                ),
                PerformanceData.PerformanceDataItem(
                    mutableMapOf<String?, MutableList<PerformanceAnalytic>?>().apply {
                        put(
                            "1.0.0",
                            MutableList(10) { PerformanceAnalytic(Random.Default.nextDouble()) })
                        put(
                            "1.0.1",
                            MutableList(10) { PerformanceAnalytic(Random.Default.nextDouble()) })
                        put(
                            "1.0.2",
                            MutableList(10) { PerformanceAnalytic(Random.Default.nextDouble()) })
                    }
                )
            )
        }
    }

    fun getAvailableVersionsForPerformanceItem(
        data: PerformanceData.PerformanceDataItem
    ): SnapshotStateList<String> {
        // TODO: MAKE THE REQUEST THEN
        return mutableStateListOf("1.0.0", "1.0.1", "1.0.2", "1.0.3", "1.0.4", "1.0.5", "1.1.0")
    }

    @OptIn(ExperimentalMaterial3Api::class)
    fun filterPerformance(
        data: PerformanceData.PerformanceDataItem,
        state: DateRangePickerState,
        onFilter: () -> Unit
    ) {
        // TODO: MAKE THE REQUEST THEN 
        onFilter.invoke()
    }

}