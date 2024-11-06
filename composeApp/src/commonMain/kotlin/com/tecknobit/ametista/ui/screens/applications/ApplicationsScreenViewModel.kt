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
import io.github.ahmad_hamwi.compose.pagination.PaginationState
import kotlinx.coroutines.launch

class ApplicationsScreenViewModel : ApplicationViewModel() {

    val paginationState = PaginationState<Int, AmetistaApplication>(
        initialPageKey = DEFAULT_PAGE,
        onRequestPage = { pageNumber ->
            loadApplications(
                pageNumber = pageNumber
            )
        }
    )

    lateinit var filterQuery: MutableState<String>

    lateinit var platformsFilter: SnapshotStateList<Platform>

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

    @Wrapper
    fun managePlatforms(
        platform: Platform
    ) {
        managePlatforms(
            checked = !platformsFilter.contains(platform),
            platform = platform
        )
    }

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

    fun clearFilters() {
        val platformsFilterChanged = platformsFilter.isNotEmpty()
        if (platformsFilterChanged)
            platformsFilter.clear()
        if (filterQuery.value.isNotEmpty() || platformsFilterChanged) {
            filterQuery.value = ""
            paginationState.refresh()
        }
    }

    fun workOnApplication(
        onSuccess: () -> Unit,
        application: AmetistaApplication?
    ) {
        if (application == null) {
            addApplication(
                onSuccess = onSuccess
            )
        } else {
            editApplication(
                application = application,
                onSuccess = onSuccess
            )
        }
    }

    private fun addApplication(
        onSuccess: () -> Unit
    ) {
        if (!validForm())
            return
        // TODO: MAKE THE REQUEST THEN
        onSuccess.invoke()
    }

}