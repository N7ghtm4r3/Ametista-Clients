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
import com.tecknobit.ametista.ui.sharedviewmodels.ApplicationViewModel
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

/**
 * The **PlatformScreenViewModel** class is the support class used to execute the requests related
 * to the [PlatformScreen]
 *
 * @param applicationId The identifier of the application displayed
 * @param platform The specific platform of the application displayed
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see ApplicationViewModel
 * @see EquinoxViewModel
 * @see ViewModel
 * @see FetcherManagerWrapper
 */
class PlatformScreenViewModel(
    val applicationId: String,
    val platform: Platform
) : EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
) {

    companion object {

        /**
         * **MAX_CHIPS_FILTERS** -> the max value allowed to filters the issues
         */
        const val MAX_CHIPS_FILTERS = 10

    }

    /**
     * **paginationState** -> the state used to manage the pagination for the [loadIssues] method
     */
    val paginationState = PaginationState<Int, AmetistaAnalytic>(
        initialPageKey = DEFAULT_PAGE,
        onRequestPage = { pageNumber ->
            loadIssues(
                pageNumber = pageNumber
            )
        }
    )

    /**
     * **analyticType** -> the type of the analytic displayed
     */
    lateinit var analyticType: MutableState<AnalyticType>

    /**
     * **filtersState** -> the state used to contain the filters for the issues
     */
    lateinit var filtersState: ChipTextFieldState<Chip>

    /**
     * **_filters** -> the filters selected
     */
    private var _filters = HashSet<String>()

    /**
     * **_filtersSet** -> whether the filter have been set
     */
    private val _filtersSet = MutableStateFlow(
        value = false
    )
    val filtersSet: StateFlow<Boolean> = _filtersSet

    /**
     * **_performanceData** -> the container state for the performance data
     */
    private val _performanceData = MutableStateFlow<PerformanceData?>(
        value = null
    )
    val performanceData: StateFlow<PerformanceData?> = _performanceData

    /**
     * **newVersionFilters** -> the new versions to filter the [_performanceData]
     */
    lateinit var newVersionFilters: MutableList<String>

    /**
     * **performanceDataFilters** -> used to to filter the [_performanceData]
     */
    private val performanceDataFilters = PerformanceDataFilters()

    /**
     * Method to load issues
     *
     * @param pageNumber The number of the page to request to the server
     */
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

    /**
     * Method to filter the issues to load
     *
     * @param onSuccess The action to execute when the filter operation has been executed
     */
    fun filterIssues(
        onSuccess: () -> Unit
    ) {
        _filters = filtersState.chips.map { chip -> chip.text }.toHashSet()
        _filtersSet.value = _filters.isNotEmpty()
        paginationState.refresh()
        onSuccess.invoke()
    }

    /**
     * Method to clear the current filters selected by the user
     */
    fun clearFilters() {
        _filters.clear()
        _filtersSet.value = false
        paginationState.refresh()
    }

    /**
     * Method to request the performance data analytics
     */
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

    /**
     * Method to get all the available version samples for the performance data collected
     *
     * @param data The specific analytic from fetch the versions
     */
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

    /**
     * Method to filter the performance data collected
     *
     * @param data The specific analytic to filter
     * @param state The container state to get the initial and the final temporal dates range
     * @param onFilter The action to execute when the filter has done
     */
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

    /**
     * Method to clear the current for the performance data filters selected by the user
     *
     * @param data The specific analytic to filtered
     */
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