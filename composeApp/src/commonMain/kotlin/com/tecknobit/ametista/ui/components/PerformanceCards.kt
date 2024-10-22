@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.ametista.ui.components

import ametista.composeapp.generated.resources.Res.string
import ametista.composeapp.generated.resources.dismiss
import ametista.composeapp.generated.resources.filter
import ametista.composeapp.generated.resources.filter_issues_number
import ametista.composeapp.generated.resources.filter_issues_per_session
import ametista.composeapp.generated.resources.filter_launch_time
import ametista.composeapp.generated.resources.filter_network_requests
import ametista.composeapp.generated.resources.issues_number
import ametista.composeapp.generated.resources.issues_per_session
import ametista.composeapp.generated.resources.launch_time
import ametista.composeapp.generated.resources.network_requests
import ametista.composeapp.generated.resources.no_events
import ametista.composeapp.generated.resources.versions
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.ametista.bodyFontFamily
import com.tecknobit.ametista.displayFontFamily
import com.tecknobit.ametista.ui.icons.ChartNetwork
import com.tecknobit.ametista.ui.screens.AmetistaScreen.Companion.CONTAINER_MAX_WIDTH
import com.tecknobit.ametista.ui.screens.platform.PlatformScreenViewModel
import com.tecknobit.ametistacore.models.AmetistaApplication.MAX_VERSION_SAMPLES
import com.tecknobit.ametistacore.models.analytics.performance.PerformanceData
import com.tecknobit.ametistacore.models.analytics.performance.PerformanceData.PerformanceDataItem
import com.tecknobit.apimanager.annotations.Wrapper
import com.tecknobit.equinoxcompose.components.EmptyListUI
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.DividerProperties
import ir.ehsannarmani.compose_charts.models.DrawStyle
import ir.ehsannarmani.compose_charts.models.GridProperties
import ir.ehsannarmani.compose_charts.models.HorizontalIndicatorProperties
import ir.ehsannarmani.compose_charts.models.LabelHelperProperties
import ir.ehsannarmani.compose_charts.models.Line
import ir.ehsannarmani.compose_charts.models.LineProperties
import ir.ehsannarmani.compose_charts.models.PopupProperties
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import java.util.Locale

private val axisProperties = GridProperties.AxisProperties(
    enabled = false
)

@Wrapper
@Composable
@NonRestartableComposable
fun LaunchTime(
    viewModel: PlatformScreenViewModel,
    cardHeight: Dp = 200.dp,
    performanceData: PerformanceData?
) {
    if (performanceData == null) {
        NoChartData(
            title = string.launch_time,
            cardHeight = cardHeight,
            icon = Icons.Default.RocketLaunch
        )
    } else {
        PerformanceCard(
            viewModel = viewModel,
            title = string.launch_time,
            cardHeight = cardHeight,
            data = performanceData.launchTimes,
            popupProperties = PopupProperties(
                textStyle = TextStyle(
                    color = Color.White
                ),
                contentBuilder = { value ->
                    "%.2f".format(value) + " ms"
                }
            ),
            noDataIcon = Icons.Default.RocketLaunch
        )
    }
}

@Wrapper
@Composable
@NonRestartableComposable
fun NetworkRequests(
    viewModel: PlatformScreenViewModel,
    cardHeight: Dp = 200.dp,
    performanceData: PerformanceData?
) {
    if (performanceData == null) {
        NoChartData(
            title = string.network_requests,
            cardHeight = cardHeight,
            icon = ChartNetwork
        )
    } else {
        PerformanceCard(
            viewModel = viewModel,
            title = string.network_requests,
            cardHeight = cardHeight,
            data = performanceData.networkRequests,
            noDataIcon = ChartNetwork
        )
    }
}

@Wrapper
@Composable
@NonRestartableComposable
fun IssuesNumber(
    viewModel: PlatformScreenViewModel,
    cardHeight: Dp = 200.dp,
    performanceData: PerformanceData?
) {
    if (performanceData == null) {
        NoChartData(
            title = string.issues_number,
            cardHeight = cardHeight,
            icon = Icons.Default.BugReport
        )
    } else {
        PerformanceCard(
            viewModel = viewModel,
            title = string.issues_number,
            cardHeight = cardHeight,
            data = performanceData.totalIssues,
            noDataIcon = Icons.Default.BugReport
        )
    }
}

@Wrapper
@Composable
@NonRestartableComposable
fun IssuesPerSessionsNumber(
    viewModel: PlatformScreenViewModel,
    cardHeight: Dp = 200.dp,
    performanceData: PerformanceData?
) {
    if (performanceData == null) {
        NoChartData(
            title = string.issues_per_session,
            cardHeight = cardHeight,
            icon = Icons.Default.Report
        )
    } else {
        PerformanceCard(
            viewModel = viewModel,
            title = string.issues_per_session,
            cardHeight = cardHeight,
            data = performanceData.issuesPerSession,
            noDataIcon = Icons.Default.Report
        )
    }
}

@Composable
@NonRestartableComposable
private fun PerformanceCard(
    viewModel: PlatformScreenViewModel,
    title: StringResource,
    cardHeight: Dp,
    data: PerformanceDataItem,
    popupProperties: PopupProperties? = null,
    noDataIcon: ImageVector
) {
    if (data.noDataAvailable()) {
        NoChartData(
            title = title,
            cardHeight = cardHeight,
            icon = noDataIcon
        )
    } else {
        Card(
            modifier = Modifier
                .height(cardHeight)
                .widthIn(CONTAINER_MAX_WIDTH)
        ) {
            CardHeader(
                viewModel = viewModel,
                title = title
            )
            ChartLegend(
                sampleVersions = data.sampleVersions()
            )
            LineChart(
                modifier = Modifier
                    .fillMaxSize(),
                gridProperties = GridProperties(
                    enabled = false,
                    xAxisProperties = axisProperties,
                    yAxisProperties = axisProperties
                ),
                dividerProperties = DividerProperties(
                    enabled = false,
                    yAxisProperties = LineProperties(
                        enabled = false
                    )
                ),
                indicatorProperties = HorizontalIndicatorProperties(
                    enabled = false
                ),
                labelHelperProperties = LabelHelperProperties(
                    enabled = false
                ),
                data = loadChartData(
                    data = data,
                    popupProperties = popupProperties
                )
            )
        }
    }
}

@Composable
@NonRestartableComposable
private fun CardHeader(
    viewModel: PlatformScreenViewModel,
    title: StringResource
) {
    Row(
        modifier = Modifier
            .padding(
                top = 5.dp,
                start = 16.dp,
                end = 5.dp
            )
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TitleText(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            title = title
        )
        CardActions(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            viewModel = viewModel,
            title = title
        )
    }
}

@Composable
@NonRestartableComposable
private fun CardActions(
    modifier: Modifier,
    viewModel: PlatformScreenViewModel,
    title: StringResource
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End
    ) {
        Row {
            IconButton(
                onClick = { // TODO: DISPLAY THE INFO
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null
                )
            }
            val filter = remember { mutableStateOf(false) }
            IconButton(
                onClick = { filter.value = true }
            ) {
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = null
                )
            }
            FilterChartData(
                viewModel = viewModel,
                show = filter,
                title = title
            )
        }
    }
}

@Composable
@NonRestartableComposable
private fun FilterChartData(
    viewModel: PlatformScreenViewModel,
    show: MutableState<Boolean>,
    title: StringResource
) {
    if (show.value) {
        viewModel.suspendRefresher()
        val closeModal = {
            show.value = false
            viewModel.restartRefresher()
        }
        ModalBottomSheet(
            sheetState = rememberModalBottomSheetState(
                skipPartiallyExpanded = true
            ),
            onDismissRequest = closeModal
        ) {
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
                FilterSectionHeader(
                    text = string.launch_time
                )
                val dateFormatter = remember { DatePickerDefaults.dateFormatter() }
                val state = rememberDateRangePickerState()
                DateRangePicker(
                    modifier = Modifier
                        .weight(1.2f),
                    title = null,
                    headline = {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            dateFormatter.formatDate(
                                state.selectedStartDateMillis,
                                locale = Locale.getDefault()
                            )
                                ?.let {
                                    Text(
                                        text = it,
                                        fontFamily = bodyFontFamily,
                                        fontSize = 16.sp
                                    )
                                }
                            Text(
                                modifier = Modifier
                                    .padding(
                                        horizontal = 5.dp
                                    ),
                                text = "-"
                            )
                            dateFormatter.formatDate(
                                state.selectedEndDateMillis,
                                locale = Locale.getDefault()
                            )
                                ?.let {
                                    Text(
                                        text = it,
                                        fontFamily = bodyFontFamily,
                                        fontSize = 16.sp
                                    )
                                }
                        }
                    },
                    state = state,
                    showModeToggle = false,
                    colors = DatePickerDefaults.colors(
                        containerColor = Color.Transparent
                    )
                )
                HorizontalDivider()
                FilterSectionHeader(
                    text = string.versions
                )
                LazyVerticalGrid(
                    modifier = Modifier
                        .weight(1f),
                    columns = GridCells.Fixed(3)
                ) {
                    items(
                        items = listOf(
                            "1.0.0",
                            "1.0.0",
                            "1.0.0",
                            "1.0.0",
                            "1.0.0",
                            "1.0.0",
                            "1.0.0",
                            "1.0.0",
                            "1.0.0",
                            "1.0.0",
                            "1.0.0",
                            "1.0.0",
                            "1.0.0",
                            "1.0.0",
                            "1.0.0",
                            "1.0.0",
                            "1.0.0",
                            "1.0.0",
                            "1.0.0",
                            "1.0.0",
                            "1.0.0",
                            "1.0.0",
                            "1.0.0",
                            "1.0.0"
                        )
                    ) { version ->
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = true,
                                onCheckedChange = {

                                }
                            )
                            Text(
                                text = version
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(
                            end = 10.dp
                        )
                ) {
                    TextButton(
                        onClick = closeModal
                    ) {
                        Text(
                            text = stringResource(string.dismiss)
                        )
                    }
                    TextButton(
                        onClick = {
                            viewModel.filterPerformance(
                                onFilter = closeModal
                            )
                        }
                    ) {
                        Text(
                            text = stringResource(string.filter)
                        )
                    }
                }
            }
        }
    }
}

@Composable
@NonRestartableComposable
private fun FilterSectionHeader(
    text: StringResource
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

private fun getFilterTitle(
    title: StringResource
): StringResource {
    return when (title) {
        string.launch_time -> string.filter_launch_time
        string.network_requests -> string.filter_network_requests
        string.issues_number -> string.filter_issues_number
        else -> string.filter_issues_per_session
    }
}

@Composable
@NonRestartableComposable
private fun ChartLegend(
    sampleVersions: Set<String>
) {
    LazyVerticalGrid(
        modifier = Modifier
            .padding(
                vertical = 5.dp
            ),
        columns = GridCells.Fixed(MAX_VERSION_SAMPLES)
    ) {
        itemsIndexed(
            items = sampleVersions.toList()
        ) { index, sample ->
            LegendItem(
                index = index,
                sample = sample
            )
        }
    }
}

@Composable
@NonRestartableComposable
private fun LegendItem(
    index: Int,
    sample: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(
                    color = getLineColor(
                        index = index
                    )
                )
                .size(10.dp)
        )
        Text(
            modifier = Modifier
                .padding(start = 10.dp),
            text = sample,
            fontSize = 14.sp
        )
    }
}

@Composable
@NonRestartableComposable
private fun NoChartData(
    title: StringResource,
    cardHeight: Dp,
    icon: ImageVector
) {
    Card(
        modifier = Modifier
            .height(cardHeight)
    ) {
        TitleText(
            modifier = Modifier
                .padding(
                    top = 16.dp,
                    start = 16.dp
                )
                .widthIn(
                    max = CONTAINER_MAX_WIDTH
                ),
            title = title
        )
        EmptyListUI(
            icon = icon,
            subText = stringResource(string.no_events)
        )
    }
}

@Composable
private fun TitleText(
    modifier: Modifier = Modifier,
    title: StringResource
) {
    Text(
        modifier = modifier,
        text = stringResource(title),
        fontFamily = displayFontFamily,
        fontSize = 20.sp
    )
}

@Composable
@NonRestartableComposable
private fun loadChartData(
    data: PerformanceDataItem,
    popupProperties: PopupProperties?
): List<Line> {
    val chartData = arrayListOf<Line>()
    data.data.entries.forEachIndexed { index, entry ->
        val lineColor = getLineColor(
            index = index
        )
        chartData.add(
            Line(
                label = entry.key,
                values = entry.value.map { analytic -> analytic.value },
                color = SolidColor(lineColor),
                firstGradientFillColor = lineColor.copy(alpha = .5f),
                secondGradientFillColor = Color.Transparent,
                drawStyle = DrawStyle.Fill,
                popupProperties = popupProperties
            )
        )
    }
    return chartData
}

@Composable
private fun getLineColor(
    index: Int
): Color {
    return when (index) {
        0 -> MaterialTheme.colorScheme.primary
        1 -> MaterialTheme.colorScheme.secondary
        else -> MaterialTheme.colorScheme.tertiary
    }
}