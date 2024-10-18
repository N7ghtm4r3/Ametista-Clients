package com.tecknobit.ametista.ui.screens.platform

import androidx.compose.runtime.MutableState
import com.tecknobit.ametista.ui.screens.application.ApplicationScreenViewModel
import com.tecknobit.ametistacore.models.AmetistaApplication
import com.tecknobit.ametistacore.models.analytics.AmetistaAnalytic
import com.tecknobit.ametistacore.models.analytics.AmetistaAnalytic.AnalyticType
import com.tecknobit.ametistacore.models.analytics.AmetistaAnalytic.AnalyticType.ISSUE
import io.github.ahmad_hamwi.compose.pagination.PaginationState

class PlatformScreenViewModel(
    initialApplication: AmetistaApplication
) : ApplicationScreenViewModel(
    initialApplication = initialApplication
) {

    lateinit var analyticType: MutableState<AnalyticType>

    val paginationState = PaginationState<Int, AmetistaAnalytic>(
        initialPageKey = 1,
        onRequestPage = { loadItems() }
    )

    private fun loadItems() {
        // TODO: TO LOAD NEXT PAGE
        val items = if (analyticType.value == ISSUE)
            _application.value.issues
        else
        //_application.value.performance
            emptyList<AmetistaAnalytic>()
        paginationState.appendPage(
            items = items,
            nextPageKey = 1,
            isLastPage = true
        )
    }

}