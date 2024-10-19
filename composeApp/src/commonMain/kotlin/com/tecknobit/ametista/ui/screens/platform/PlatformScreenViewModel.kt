package com.tecknobit.ametista.ui.screens.platform

import androidx.compose.runtime.MutableState
import com.dokar.chiptextfield.Chip
import com.dokar.chiptextfield.ChipTextFieldState
import com.tecknobit.ametista.ui.screens.application.ApplicationScreenViewModel
import com.tecknobit.ametistacore.models.AmetistaApplication
import com.tecknobit.ametistacore.models.Platform
import com.tecknobit.ametistacore.models.analytics.AmetistaAnalytic
import com.tecknobit.ametistacore.models.analytics.AmetistaAnalytic.AnalyticType
import com.tecknobit.ametistacore.models.analytics.AmetistaAnalytic.AnalyticType.ISSUE
import io.github.ahmad_hamwi.compose.pagination.PaginationState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PlatformScreenViewModel(
    initialApplication: AmetistaApplication
) : ApplicationScreenViewModel(
    initialApplication = initialApplication
) {

    companion object {

        const val MAX_CHIPS_FILTERS = 10

    }

    private var _filters = HashSet<String>()

    private val _filtersSet = MutableStateFlow(
        value = false
    )
    val filtersSet: StateFlow<Boolean> = _filtersSet

    lateinit var analyticType: MutableState<AnalyticType>

    lateinit var filtersState: ChipTextFieldState<Chip>

    val paginationState = PaginationState<Int, AmetistaAnalytic>(
        initialPageKey = 1,
        onRequestPage = { loadItems() }
    )

    override fun refreshApplication(
        platform: Platform?
    ) {
        // TODO: MAKE Requester TWO DIFFERENT METHODS (ONE WRAPPER) WITH THE FILTERS FIELD ALSO
        // TODO: TO USE _filters
    }

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

    fun filterIssues(
        onSuccess: () -> Unit
    ) {
        _filters = filtersState.chips.map { chip -> chip.text }.toHashSet()
        _filtersSet.value = _filters.isNotEmpty()
        onSuccess.invoke()
    }

    fun clearFilters() {
        _filters.clear()
        _filtersSet.value = false
    }

}