package com.tecknobit.ametista.ui.screens.applications

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.tecknobit.ametistacore.models.AmetistaApplication
import com.tecknobit.ametistacore.models.AmetistaApplication.Platform
import com.tecknobit.apimanager.annotations.Wrapper
import com.tecknobit.equinoxcompose.helpers.viewmodels.EquinoxViewModel
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

    private fun getApplications() {
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

}