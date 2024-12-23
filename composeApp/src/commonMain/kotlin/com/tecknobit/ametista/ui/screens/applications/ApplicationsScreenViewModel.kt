package com.tecknobit.ametista.ui.screens.applications

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewModelScope
import com.tecknobit.ametista.requester
import com.tecknobit.ametista.ui.sharedviewmodels.ApplicationViewModel
import com.tecknobit.ametistacore.helpers.pagination.PaginatedResponse.Companion.DEFAULT_PAGE
import com.tecknobit.ametistacore.models.AmetistaApplication
import com.tecknobit.ametistacore.models.Platform
import com.tecknobit.apimanager.annotations.Wrapper
import com.tecknobit.equinoxcompose.helpers.session.setHasBeenDisconnectedValue
import com.tecknobit.equinoxcompose.helpers.session.setServerOfflineValue
import com.tecknobit.equinoxcompose.helpers.viewmodels.EquinoxViewModel
import io.github.ahmad_hamwi.compose.pagination.PaginationState
import kotlinx.coroutines.launch

/**
 * The **ApplicationsScreenViewModel** class is the support class used to execute the requests related
 * to the [ApplicationsScreen]
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see ApplicationViewModel
 * @see EquinoxViewModel
 * @see ViewModel
 * @see FetcherManagerWrapper
 */
class ApplicationsScreenViewModel : ApplicationViewModel() {

    /**
     * **paginationState** -> the state used to manage the pagination for the [loadApplications] method
     */
    val paginationState = PaginationState<Int, AmetistaApplication>(
        initialPageKey = DEFAULT_PAGE,
        onRequestPage = { pageNumber ->
            loadApplications(
                pageNumber = pageNumber
            )
        }
    )

    /**
     * **filterQuery** -> the query filter typed
     */
    lateinit var filterQuery: MutableState<String>

    /**
     * **platformsFilter** -> the list of platforms to use as filter
     */
    lateinit var platformsFilter: SnapshotStateList<Platform>

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
                supplier = { jApplication -> AmetistaApplication(jApplication) },
                onSuccess = { page ->
                    setServerOfflineValue(false)
                    paginationState.appendPage(
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
        paginationState.refresh()
    }

    /**
     * Method to clear the current filters selected by the user
     */
    fun clearFilters() {
        val platformsFilterChanged = platformsFilter.isNotEmpty()
        if (platformsFilterChanged)
            platformsFilter.clear()
        if (filterQuery.value.isNotEmpty() || platformsFilterChanged) {
            filterQuery.value = ""
            paginationState.refresh()
        }
    }

    /**
     * Method to work on an application
     *
     * @param application The application to work on
     * @param onSuccess The action to execute when the operation on the application has been executed
     */
    fun workOnApplication(
        application: AmetistaApplication?,
        onSuccess: () -> Unit
    ) {
        if (application == null) {
            addApplication(
                onSuccess = {
                    onSuccess.invoke()
                    paginationState.refresh()
                }
            )
        } else {
            editApplication(
                application = application,
                onSuccess = {
                    onSuccess.invoke()
                    paginationState.refresh()
                }
            )
        }
    }

    /**
     * Method to add a new application on the system
     *
     * @param onSuccess The action to execute when the operation on the application has been executed
     */
    private fun addApplication(
        onSuccess: () -> Unit
    ) {
        if (!validForm())
            return
        requester.sendRequest(
            request = {
                requester.addApplication(
                    icon = appIcon.value,
                    name = appName.value,
                    description = appDescription.value
                )
            },
            onSuccess = { onSuccess.invoke() },
            onFailure = { showSnackbarMessage(it) }
        )
    }

}