package com.tecknobit.ametista.ui.screens.platform

import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.viewModelScope
import com.dokar.chiptextfield.Chip
import com.dokar.chiptextfield.ChipTextFieldState
import com.tecknobit.ametista.requester
import com.tecknobit.ametistacore.helpers.pagination.PaginatedResponse.Companion.DEFAULT_PAGE
import com.tecknobit.ametistacore.models.Platform
import com.tecknobit.ametistacore.models.Platform.WEB
import com.tecknobit.ametistacore.models.analytics.AmetistaAnalytic
import com.tecknobit.ametistacore.models.analytics.AmetistaAnalytic.AnalyticType
import com.tecknobit.ametistacore.models.analytics.issues.IssueAnalytic
import com.tecknobit.ametistacore.models.analytics.issues.WebIssueAnalytic
import com.tecknobit.ametistacore.models.analytics.performance.PerformanceData
import com.tecknobit.ametistacore.models.analytics.performance.PerformanceData.PerformanceDataItem
import com.tecknobit.ametistacore.models.analytics.performance.PerformanceDataFilters
import com.tecknobit.equinox.Requester.Companion.responseData
import com.tecknobit.equinoxcompose.helpers.viewmodels.EquinoxViewModel
import io.github.ahmad_hamwi.compose.pagination.PaginationState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONArray

class PlatformScreenViewModel(
    val applicationId: String,
    val platform: Platform
) : EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
) {

    companion object {

        const val MAX_CHIPS_FILTERS = 10

    }

    val paginationState = PaginationState<Int, AmetistaAnalytic>(
        initialPageKey = DEFAULT_PAGE,
        onRequestPage = { pageNumber ->
            loadIssues(
                pageNumber = pageNumber
            )
        }
    )

    lateinit var analyticType: MutableState<AnalyticType>

    lateinit var filtersState: ChipTextFieldState<Chip>

    private var _filters = HashSet<String>()

    private val _filtersSet = MutableStateFlow(
        value = false
    )
    val filtersSet: StateFlow<Boolean> = _filtersSet

    private val _performanceData = MutableStateFlow<PerformanceData?>(
        value = null
    )
    val performanceData: StateFlow<PerformanceData?> = _performanceData

    lateinit var newVersionFilters: MutableList<String>

    private val performanceDataFilters = PerformanceDataFilters()

    private fun loadIssues(
        pageNumber: Int
    ) {
        viewModelScope.launch {
            requester.sendPaginatedRequest(
                request = {
                    getIssues(
                        applicationId = applicationId,
                        platform = platform,
                        page = pageNumber,
                        filters = _filters
                    )
                },
                supplier = { jIssue ->
                    if (platform == WEB)
                        WebIssueAnalytic(jIssue)
                    else
                        IssueAnalytic(jIssue)
                },
                onSuccess = { page ->
                    paginationState.appendPage(
                        items = page.data,
                        nextPageKey = page.nextPage,
                        isLastPage = page.isLastPage
                    )
                },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

    fun filterIssues(
        onSuccess: () -> Unit
    ) {
        _filters = filtersState.chips.map { chip -> chip.text }.toHashSet()
        _filtersSet.value = _filters.isNotEmpty()
        paginationState.refresh()
        onSuccess.invoke()
    }

    fun clearFilters() {
        _filters.clear()
        _filtersSet.value = false
        paginationState.refresh()
    }

    fun getPerformanceAnalytics() {
        requester.sendRequest(
            request = {
                requester.getPerformanceData(
                    applicationId = applicationId,
                    platform = platform,
                    performanceDataFilters = performanceDataFilters
                )
            },
            onSuccess = { response ->
                _performanceData.value = PerformanceData(response.responseData())
            },
            onFailure = { showSnackbarMessage(it) }
        )
    }

    fun getAvailableVersionsSamples(
        data: PerformanceDataItem
    ): SnapshotStateList<String> {
        val sampleVersions = HashSet<String>()
        requester.sendRequest(
            request = {
                requester.getVersionSamples(
                    applicationId = applicationId,
                    platform = platform,
                    analyticType = data.analyticType
                )
            },
            onSuccess = { response ->
                val jSampleVersions = response.responseData<JSONArray>()
                for (j in 0 until jSampleVersions.length())
                    sampleVersions.add(jSampleVersions.getString(j))
            },
            onFailure = { sampleVersions.addAll(data.sampleVersions()) }
        )
        return sampleVersions.toMutableStateList()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    fun filterPerformance(
        data: PerformanceDataItem,
        state: DateRangePickerState,
        onFilter: () -> Unit
    ) {
        performanceDataFilters.setFilter(
            data.analyticType,
            PerformanceDataFilters.PerformanceFilter(
                state.selectedStartDateMillis!!,
                state.selectedEndDateMillis!!,
                newVersionFilters
            )
        )
        onFilter.invoke()
        getPerformanceAnalytics()
    }

    fun clearPerformanceFilter(
        data: PerformanceDataItem
    ) {
        performanceDataFilters.setFilter(
            data.analyticType,
            null
        )
        getPerformanceAnalytics()
    }

}