package com.tecknobit.ametista.ui.screens.platform.presentation

import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.viewModelScope
import com.tecknobit.ametista.requester
import com.tecknobit.ametista.ui.screens.platform.data.AmetistaAnalytic
import com.tecknobit.ametista.ui.screens.platform.data.issues.IssueAnalyticImpl
import com.tecknobit.ametista.ui.screens.platform.data.issues.WebIssueAnalytic
import com.tecknobit.ametista.ui.screens.platform.data.performance.PerformanceData
import com.tecknobit.ametista.ui.screens.platform.data.performance.PerformanceData.PerformanceDataItem
import com.tecknobit.ametista.ui.screens.platform.data.performance.PerformanceDataFilters
import com.tecknobit.ametista.ui.screens.shared.presentations.ApplicationViewModel
import com.tecknobit.ametistacore.enums.AnalyticType
import com.tecknobit.ametistacore.enums.Platform
import com.tecknobit.ametistacore.enums.Platform.WEB
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.network.Requester.Companion.sendPaginatedRequest
import com.tecknobit.equinoxcore.network.Requester.Companion.sendRequest
import com.tecknobit.equinoxcore.network.Requester.Companion.toResponseArrayData
import com.tecknobit.equinoxcore.network.Requester.Companion.toResponseData
import com.tecknobit.equinoxcore.pagination.PaginatedResponse.Companion.DEFAULT_PAGE
import io.github.ahmad_hamwi.compose.pagination.PaginationState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement

/**
 * The **PlatformScreenViewModel** class is the support class used to execute the requests related
 * to the [PlatformScreen]
 *
 * @param applicationId The identifier of the application displayed
 * @param platform The specific platform of the application displayed
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see ApplicationViewModel
 * @see androidx.lifecycle.ViewModel
 * @see com.tecknobit.equinoxcompose.session.Retriever
 * @see EquinoxViewModel
 */
class PlatformScreenViewModel(
    private val applicationId: String,
    private val platform: Platform,
) : EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
) {

    companion object {

        /**
         * `MAX_CHIPS_FILTERS` -> the max value allowed to filters the issues
         */
        const val MAX_CHIPS_FILTERS = 10

    }

    /**
     * `analyticsState` -> the state used to manage the pagination for the [loadIssues] method
     */
    val analyticsState = PaginationState<Int, AmetistaAnalytic>(
        initialPageKey = DEFAULT_PAGE,
        onRequestPage = { pageNumber ->
            loadIssues(
                pageNumber = pageNumber
            )
        }
    )

    /**
     * `analyticType` -> the type of the analytic displayed
     */
    lateinit var analyticType: MutableState<AnalyticType>

    /**
     * `filtersState` -> the state used to contain the filters for the issues
     */
    lateinit var filtersState: MutableState<String>

    /**
     * `_filtersSet` -> whether the filter have been set
     */
    private val _filtersSet = MutableStateFlow(
        value = false
    )
    val filtersSet = _filtersSet.asStateFlow()

    /**
     * `_performanceData` -> the container state for the performance data
     */
    private val _performanceData = MutableStateFlow<PerformanceData?>(
        value = null
    )
    val performanceData = _performanceData.asStateFlow()

    /**
     * `newVersionFilters` -> the new versions to filter the [_performanceData]
     */
    lateinit var newVersionFilters: MutableList<String>

    /**
     * `performanceDataFilters` -> used to to filter the [_performanceData]
     */
    private val performanceDataFilters = PerformanceDataFilters()

    /**
     * Method to load issues
     *
     * @param pageNumber The number of the page to request to the server
     */
    private fun loadIssues(
        pageNumber: Int,
    ) {
        viewModelScope.launch {
            requester.sendPaginatedRequest(
                request = {
                    getIssues(
                        applicationId = applicationId,
                        platform = platform,
                        page = pageNumber,
                        filters = filtersState.value
                    )
                },
                serializer = if (platform == WEB)
                    WebIssueAnalytic.serializer()
                else
                    IssueAnalyticImpl.serializer(),
                onSuccess = { page ->
                    analyticsState.appendPage(
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
        onSuccess: () -> Unit,
    ) {
        _filtersSet.value = filtersState.value.isNotEmpty()
        analyticsState.refresh()
        onSuccess.invoke()
    }

    /**
     * Method to clear the current filters selected by the user
     */
    fun clearFilters() {
        filtersState.value = ""
        _filtersSet.value = false
        analyticsState.refresh()
    }

    /**
     * Method to request the performance data analytics
     */
    fun getPerformanceAnalytics() {
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    getPerformanceData(
                        applicationId = applicationId,
                        platform = platform,
                        performanceDataFilters = performanceDataFilters
                    )
                },
                onSuccess = {
                    _performanceData.value = Json.decodeFromJsonElement(it.toResponseData())
                },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

    /**
     * Method to get all the available version samples for the performance data collected
     *
     * @param data The specific analytic from fetch the versions
     */
    fun getAvailableVersionsSamples(
        data: PerformanceDataItem,
    ): SnapshotStateList<String> {
        val sampleVersions = HashSet<String>()
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    requester.getVersionSamples(
                        applicationId = applicationId,
                        platform = platform,
                        analyticType = data.analyticType
                    )
                },
                onSuccess = {
                    sampleVersions.addAll(Json.decodeFromJsonElement(it.toResponseArrayData()))
                },
                onFailure = { sampleVersions.addAll(data.sampleVersions()) }
            )
        }
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
        onFilter: () -> Unit,
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
        data: PerformanceDataItem,
    ) {
        performanceDataFilters.setFilter(
            type = data.analyticType,
            performanceFilter = null
        )
        getPerformanceAnalytics()
    }

}