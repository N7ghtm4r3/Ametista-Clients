package com.tecknobit.ametista.ui.screens.applications

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.tecknobit.ametista.ui.sharedviewmodels.ApplicationViewModel
import com.tecknobit.ametistacore.models.AmetistaApplication
import com.tecknobit.ametistacore.models.Platform
import com.tecknobit.apimanager.annotations.Wrapper
import io.github.ahmad_hamwi.compose.pagination.PaginationState

class ApplicationsScreenViewModel : ApplicationViewModel() {

    val paginationState = PaginationState<Int, AmetistaApplication>(
        initialPageKey = 1,
        onRequestPage = { getApplications() }
    )

    lateinit var filterQuery: MutableState<String>

    lateinit var platformsFilter: SnapshotStateList<Platform>

    private fun getApplications() {
        // TODO: MAKE THE REAL REQUEST (and use appendPageWithUpdates)
        paginationState.appendPage(
            items = listOf(AmetistaApplication("Space"), AmetistaApplication("Bello")),
            nextPageKey = 1
        )
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
    }

    fun clearFilters() {
        filterQuery.value = ""
        platformsFilter.clear()
    }

    fun filterApplications(): List<AmetistaApplication> {
        val applications = arrayListOf<AmetistaApplication>()
        paginationState.allItems?.let { items ->
            if(filterQuery.value.isEmpty() && platformsFilter.isEmpty())
                applications.addAll(items)
            else
                applications.addAll(items.filter { application -> application.filtersMatch() })
        }
        return applications
    }

    private fun AmetistaApplication.filtersMatch(): Boolean {
        val match = filterQuery.value.isEmpty() || this.name.lowercase().contains(filterQuery.value.lowercase())
        return match && this.platforms.containsAll(platformsFilter)
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