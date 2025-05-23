@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.ametista.ui.screens.platform.components

import ametista.composeapp.generated.resources.Res.string
import ametista.composeapp.generated.resources.dismiss
import ametista.composeapp.generated.resources.filter
import ametista.composeapp.generated.resources.filter_issues
import ametista.composeapp.generated.resources.filter_issues_number
import ametista.composeapp.generated.resources.filter_issues_per_session
import ametista.composeapp.generated.resources.filter_launch_time
import ametista.composeapp.generated.resources.filter_network_requests
import ametista.composeapp.generated.resources.filter_supporting_text
import ametista.composeapp.generated.resources.invalid_temporal_range
import ametista.composeapp.generated.resources.issues_number
import ametista.composeapp.generated.resources.launch_time
import ametista.composeapp.generated.resources.max_three_sets_at_a_time
import ametista.composeapp.generated.resources.network_requests
import ametista.composeapp.generated.resources.one_set_required
import ametista.composeapp.generated.resources.temporal_range
import ametista.composeapp.generated.resources.versions
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dokar.sonner.Toast
import com.dokar.sonner.ToastType
import com.dokar.sonner.Toaster
import com.dokar.sonner.ToasterState
import com.dokar.sonner.rememberToasterState
import com.tecknobit.ametista.bodyFontFamily
import com.tecknobit.ametista.displayFontFamily
import com.tecknobit.ametista.ui.screens.platform.data.performance.PerformanceData.PerformanceDataItem
import com.tecknobit.ametista.ui.screens.platform.presentation.PlatformScreenViewModel
import com.tecknobit.ametistacore.MAX_TEMPORAL_RANGE
import com.tecknobit.ametistacore.MAX_VERSION_SAMPLES
import com.tecknobit.equinoxcompose.components.EquinoxDialog
import com.tecknobit.equinoxcompose.components.EquinoxOutlinedTextField
import com.tecknobit.equinoxcore.time.TimeFormatter
import com.tecknobit.equinoxcore.time.TimeFormatter.toDateString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

/**
 * The [EquinoxDialog] to use to fill with the filters value to use to fetch the issues
 *
 * @param show Whether the layout is visible or not
 * @param viewModel The viewmodel related to the screen which invoked this component
 */
@Composable
fun FilterDialog(
    show: MutableState<Boolean>,
    viewModel: PlatformScreenViewModel,
) {
    val closeDialog = {
        show.value = false
        viewModel.suspendRetriever()
    }
    EquinoxDialog(
        show = show,
        viewModel = viewModel,
        dialogContent = {
            Surface(
                modifier = Modifier
                    .width(400.dp)
                    .heightIn(
                        max = 550.dp
                    ),
                shape = RoundedCornerShape(
                    size = 15.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(
                            horizontal = 16.dp
                        )
                        .padding(
                            top = 16.dp,
                            bottom = 16.dp
                        )
                ) {
                    Text(
                        text = stringResource(string.filter_issues),
                        fontFamily = displayFontFamily,
                        fontSize = 20.sp
                    )
                    FiltersInput(
                        viewModel = viewModel
                    )
                    ActionButtons(
                        modifier = Modifier
                            .fillMaxWidth(),
                        closeModal = closeDialog,
                        onConfirm = {
                            viewModel.filterIssues(
                                onSuccess = closeDialog
                            )
                        }
                    )
                }
            }
        }
    )
}

/**
 * The [FiltersInput] where insert the filters
 *
 * @param viewModel The viewmodel related to the screen which invoked this component
 */
@Composable
private fun FiltersInput(
    viewModel: PlatformScreenViewModel,
) {
    Column {
        EquinoxOutlinedTextField(
            modifier = Modifier
                .padding(
                    top = 10.dp
                )
                .heightIn(
                    max = 400.dp
                ),
            value = viewModel.filtersState,
            placeholder = ""
        )
        Text(
            text = stringResource(string.filter_supporting_text),
            fontSize = 12.sp
        )
    }
}

/**
 * The layout where select the filters for the performance data
 *
 * @param state The state useful to manage the visibility of the [ModalBottomSheet]
 * @param scope The coroutine useful to manage the visibility of the [ModalBottomSheet]
 * @param viewModel The viewmodel related to the [com.tecknobit.ametista.ui.screens.platform.presenter.PlatformScreen] screen
 * @param title The title of the chart where to use the filters
 * @param data The performance data to filter
 */
@Composable
fun FilterChartData(
    state: SheetState,
    scope: CoroutineScope,
    viewModel: PlatformScreenViewModel,
    title: StringResource,
    data: PerformanceDataItem,
) {
    if (state.isVisible) {
        viewModel.suspendRetriever()
        val closeModal = {
            scope.launch {
                state.hide()
                viewModel.restartRetriever()
            }
        }
        val invalidTemporalRange = stringResource(string.invalid_temporal_range)
        val statePicker = rememberDateRangePickerState(
            initialSelectedStartDateMillis = data.getStartTemporalRangeDate(),
            initialSelectedEndDateMillis = data.getEndTemporalRangeDate(),
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    return utcTimeMillis <= TimeFormatter.currentTimestamp()
                }
            }
        )
        ModalBottomSheet(
            sheetState = state,
            onDismissRequest = { closeModal() }
        ) {
            val toaster = rememberToasterState()
            Column {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = stringResource(
                        getFilterTitle(
                            title = title
                        )
                    ),
                    textAlign = TextAlign.Center,
                    fontFamily = displayFontFamily,
                    fontSize = 20.sp
                )
                TemporalRangeSelector(
                    modifier = Modifier
                        .weight(1.2f),
                    state = statePicker
                )
                VersionsSelector(
                    modifier = Modifier
                        .weight(1f),
                    viewModel = viewModel,
                    data = data
                )
                ActionButtons(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(
                            end = 10.dp
                        ),
                    closeModal = { closeModal() },
                    onConfirm = {
                        if ((statePicker.selectedEndDateMillis!! - statePicker.selectedStartDateMillis!!) > MAX_TEMPORAL_RANGE) {
                            showErrorToast(
                                toaster = toaster,
                                errorMessage = invalidTemporalRange
                            )
                        } else {
                            viewModel.filterPerformance(
                                data = data,
                                state = statePicker,
                                onFilter = { closeModal() }
                            )
                        }
                    }
                )
            }
            Toaster(
                darkTheme = !isSystemInDarkTheme(),
                state = toaster
            )
        }
    }
}

/**
 * The component to select the temporal range to filter the performance data
 *
 * @param modifier The modifier to apply to the component
 * @param state The [DateRangePickerState] for the [DateRangePicker]
 */
@Composable
@NonRestartableComposable
private fun TemporalRangeSelector(
    modifier: Modifier,
    state: DateRangePickerState,
) {
    FilterSectionHeader(
        text = string.temporal_range
    )
    DateRangePicker(
        modifier = modifier,
        title = null,
        headline = {
            DatePickerHeadline(
                state = state
            )
        },
        state = state,
        showModeToggle = false,
        colors = DatePickerDefaults.colors(
            containerColor = Color.Transparent
        )
    )
    HorizontalDivider()
}

/**
 * The headline section of the [TemporalRangeSelector]
 *
 * @param state The [DateRangePickerState] for the [DateRangePicker]
 */
@Composable
@NonRestartableComposable
private fun DatePickerHeadline(
    state: DateRangePickerState,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        SelectedDateIndicator(
            date = state.selectedStartDateMillis
        )
        Text(
            modifier = Modifier
                .padding(
                    horizontal = 5.dp
                ),
            text = "-"
        )
        SelectedDateIndicator(
            date = state.selectedEndDateMillis
        )
    }
}

/**
 * The date indicator section to display the selected range
 *
 * @param date The date selected
 */
@Composable
@NonRestartableComposable
private fun SelectedDateIndicator(
    date: Long?,
) {
    date?.let {
        Text(
            text = date.toDateString(),
            fontFamily = bodyFontFamily,
            fontSize = 16.sp
        )
    }
}

/**
 * The component to select the versions samples to filter the performance data
 *
 * @param modifier The modifier to apply to the selector
 * @param viewModel The viewmodel related to the [com.tecknobit.ametista.ui.screens.platform.presenter.PlatformScreen] screen
 * @param data The performance data to filter
 */
@Composable
private fun VersionsSelector(
    modifier: Modifier,
    viewModel: PlatformScreenViewModel,
    data: PerformanceDataItem,
) {
    val toaster = rememberToasterState()
    val currentVersionFilters = data.sampleVersions()
    val tooManySets = stringResource(string.max_three_sets_at_a_time)
    val oneSetRequired = stringResource(string.one_set_required)
    viewModel.newVersionFilters = remember { ArrayList() }
    LaunchedEffect(currentVersionFilters) {
        viewModel.newVersionFilters.addAll(currentVersionFilters)
    }
    val sampleVersions = remember { mutableStateListOf<String>() }
    LaunchedEffect(Unit) {
        sampleVersions.addAll(
            viewModel.getAvailableVersionsSamples(
                data = data
            )
        )
    }
    FilterSectionHeader(
        text = string.versions
    )
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(MAX_VERSION_SAMPLES)
    ) {
        items(
            items = sampleVersions
        ) { version ->
            var checked by remember { mutableStateOf(currentVersionFilters.contains(version)) }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = checked,
                    onCheckedChange = { selected ->
                        val currentFilters = viewModel.newVersionFilters.size
                        if (selected) {
                            if (currentFilters >= MAX_VERSION_SAMPLES) {
                                showErrorToast(
                                    toaster = toaster,
                                    errorMessage = tooManySets
                                )
                            } else {
                                viewModel.newVersionFilters.add(version)
                                checked = true
                            }
                        } else {
                            if (currentFilters >= 2) {
                                viewModel.newVersionFilters.remove(version)
                                checked = false
                            } else {
                                showErrorToast(
                                    toaster = toaster,
                                    errorMessage = oneSetRequired
                                )
                            }
                        }
                    }
                )
                Text(
                    text = version
                )
            }
        }
    }
    Toaster(
        darkTheme = !isSystemInDarkTheme(),
        state = toaster
    )
}

/**
 * The filter header component
 *
 * @param text The header text
 */
@Composable
private fun FilterSectionHeader(
    text: StringResource,
) {
    Text(
        modifier = Modifier
            .padding(
                top = 16.dp,
                start = 16.dp
            ),
        text = stringResource(text),
        fontSize = 18.sp
    )
}

/**
 * Method to get the title related to the filter
 *
 * @param title The title as [StringResource] from fetch the title of the filter
 *
 * @return filter title as [StringResource]
 */
private fun getFilterTitle(
    title: StringResource,
): StringResource {
    return when (title) {
        string.launch_time -> string.filter_launch_time
        string.network_requests -> string.filter_network_requests
        string.issues_number -> string.filter_issues_number
        else -> string.filter_issues_per_session
    }
}

/**
 * The actions section for the [FilterChartData] component
 *
 * @param modifier The modifier to apply to the component
 * @param closeModal The action to execute when the modal has been closed
 * @param onConfirm The action to execute when the user confirm the action
 */
@Composable
private fun ActionButtons(
    modifier: Modifier,
    closeModal: () -> Unit,
    onConfirm: () -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.End
    ) {
        TextButton(
            onClick = closeModal
        ) {
            Text(
                text = stringResource(string.dismiss)
            )
        }
        TextButton(
            onClick = onConfirm
        ) {
            Text(
                text = stringResource(string.filter)
            )
        }
    }
}

/**
 * Method show the error occurred during filtering
 *
 * @param toaster The toaster to use to display the [errorMessage]
 * @param errorMessage The error message to display
 */
private fun showErrorToast(
    toaster: ToasterState,
    errorMessage: String,
) {
    toaster.show(
        Toast(
            message = errorMessage,
            type = ToastType.Error
        )
    )
}