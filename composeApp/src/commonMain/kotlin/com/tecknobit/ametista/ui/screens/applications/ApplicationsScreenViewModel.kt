@file:OptIn(ExperimentalPaginationApi::class)

package com.tecknobit.ametista.ui.screens.applications

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.Color
import com.tecknobit.ametista.ui.theme.errorDark
import com.tecknobit.ametistacore.helpers.AmetistaValidator.isAppDescriptionValid
import com.tecknobit.ametistacore.helpers.AmetistaValidator.isAppNameValid
import com.tecknobit.ametistacore.models.AmetistaApplication
import com.tecknobit.ametistacore.models.AmetistaApplication.Platform
import com.tecknobit.apimanager.annotations.Wrapper
import com.tecknobit.equinoxcompose.helpers.viewmodels.EquinoxViewModel
import io.github.ahmad_hamwi.compose.pagination.ExperimentalPaginationApi
import io.github.ahmad_hamwi.compose.pagination.PaginationState

class ApplicationsScreenViewModel: EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
) {

    val paginationState = PaginationState<Int, AmetistaApplication>(
        initialPageKey = 1,
        onRequestPage = { getApplications() }
    )

    lateinit var filterQuery: MutableState<String>

    lateinit var platformsFilter: SnapshotStateList<Platform>

    lateinit var workOnApplication: MutableState<Boolean>

    lateinit var appIcon: MutableState<String>

    lateinit var appIconBorderColor: MutableState<Color>

    lateinit var appName: MutableState<String>

    lateinit var appNameError: MutableState<Boolean>

    lateinit var appDescription: MutableState<String>

    lateinit var appDescriptionError: MutableState<Boolean>

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
        application: AmetistaApplication?
    ) {
        if (appIcon.value.isEmpty()) {
            appIconBorderColor.value = errorDark
            return
        }
        if (!isAppNameValid(appName.value)) {
            appNameError.value = true
            return
        }
        if (!isAppDescriptionValid(appDescription.value)) {
            appDescriptionError.value = true
            return
        }
        if (application == null)
            addApplication()
        else {
            editApplication(
                application = application
            )
        }
    }

    private fun addApplication() {
        // TODO: MAKE THE REQUEST THEN
        workOnApplication.value = false
    }

    private fun editApplication(
        application: AmetistaApplication
    ) {
        // TODO: MAKE THE REQUEST THEN
        workOnApplication.value = false
    }

}