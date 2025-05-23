package com.tecknobit.ametista.ui.screens.applications.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewModelScope
import com.tecknobit.ametista.requester
import com.tecknobit.ametista.ui.screens.applications.data.AmetistaApplication
import com.tecknobit.ametista.ui.screens.shared.presentations.ApplicationViewModel
import com.tecknobit.ametistacore.enums.Platform
import com.tecknobit.equinoxcompose.session.setHasBeenDisconnectedValue
import com.tecknobit.equinoxcompose.session.setServerOfflineValue
import com.tecknobit.equinoxcore.annotations.Wrapper
import com.tecknobit.equinoxcore.network.sendPaginatedRequest
import com.tecknobit.equinoxcore.pagination.PaginatedResponse.Companion.DEFAULT_PAGE
import io.github.ahmad_hamwi.compose.pagination.PaginationState
import kotlinx.coroutines.launch

/**
 * The **ApplicationsScreenViewModel** class is the support class used to execute the requests related
 * to the [com.tecknobit.ametista.ui.screens.applications.data.AmetistaApplication]
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see ApplicationViewModel
 * @see androidx.lifecycle.ViewModel
 * @see com.tecknobit.equinoxcompose.session.Retriever
 * @see EquinoxViewModel
 */
class ApplicationsScreenViewModel : ApplicationViewModel() {

    /**
     * `filterQuery` -> the query filter typed
     */
    lateinit var filterQuery: MutableState<String>

    /**
     * `platformsFilter` -> the list of platforms to use as filter
     */
    lateinit var platformsFilter: SnapshotStateList<Platform>

    /**
     * `applicationsState` -> the state used to manage the pagination for the [loadApplications] method
     */
    val applicationsState = PaginationState<Int, AmetistaApplication>(
        initialPageKey = DEFAULT_PAGE,
        onRequestPage = { pageNumber ->
            loadApplications(
                pageNumber = pageNumber
            )
        }
    )

    /**
     * Method to load applications
     *
     * @param pageNumber The number of the page to request to the server
     */
    private fun loadApplications(
        pageNumber: Int
    ) {
        viewModelScope.launch {
            requester.sendPaginatedRequest(
                request = {
                    getApplications(
                        page = pageNumber,
                        name = filterQuery.value,
                        platforms = platformsFilter
                    )
                },
                serializer = AmetistaApplication.serializer(),
                onSuccess = { page ->
                    setServerOfflineValue(false)
                    applicationsState.appendPage(
                        items = page.data,
                        nextPageKey = page.nextPage,
                        isLastPage = page.isLastPage
                    )
                },
                onFailure = { setHasBeenDisconnectedValue(true) },
                onConnectionError = { setServerOfflineValue(true) }
            )
        }
    }

    /**
     * Method to remove or add a platform to the [platformsFilter] list
     *
     * @param platform The platform to add or remove
     */
    @Wrapper
    fun managePlatforms(
        platform: Platform
    ) {
        managePlatforms(
            checked = !platformsFilter.contains(platform),
            platform = platform
        )
    }

    /**
     * Method to remove or add a platform to the [platformsFilter] list
     *
     * @param checked The state from add or delete the platform
     * @param platform The platform to add or remove
     */
    fun managePlatforms(
        checked: Boolean,
        platform: Platform
    ) {
        if(checked)
            platformsFilter.add(platform)
        else
            platformsFilter.remove(platform)
        applicationsState.refresh()
    }

}