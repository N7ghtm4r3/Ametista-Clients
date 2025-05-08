@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.ametista.ui.screens.platform.components

import ametista.composeapp.generated.resources.Res.string
import ametista.composeapp.generated.resources.got_it
import ametista.composeapp.generated.resources.issues_number
import ametista.composeapp.generated.resources.issues_number_info
import ametista.composeapp.generated.resources.issues_per_session
import ametista.composeapp.generated.resources.issues_per_session_info
import ametista.composeapp.generated.resources.launch_time
import ametista.composeapp.generated.resources.launch_time_info
import ametista.composeapp.generated.resources.network_requests
import ametista.composeapp.generated.resources.network_requests_info
import ametista.composeapp.generated.resources.no_events
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
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.FilterListOff
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.tecknobit.ametista.displayFontFamily
import com.tecknobit.ametista.ui.icons.ChartNetwork
import com.tecknobit.ametista.ui.screens.platform.data.performance.PerformanceData
import com.tecknobit.ametista.ui.screens.platform.data.performance.PerformanceData.PerformanceDataItem
import com.tecknobit.ametista.ui.screens.platform.presentation.PlatformScreenViewModel
import com.tecknobit.ametistacore.MAX_VERSION_SAMPLES
import com.tecknobit.equinoxcompose.components.EmptyListUI
import com.tecknobit.equinoxcore.annotations.Wrapper
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.DividerProperties
import ir.ehsannarmani.compose_charts.models.DrawStyle
import ir.ehsannarmani.compose_charts.models.GridProperties
import ir.ehsannarmani.compose_charts.models.HorizontalIndicatorProperties
import ir.ehsannarmani.compose_charts.models.LabelHelperProperties
import ir.ehsannarmani.compose_charts.models.Line
import ir.ehsannarmani.compose_charts.models.LineProperties
import ir.ehsannarmani.compose_charts.models.PopupProperties
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import kotlin.math.round

/**
 * `axisProperties** custom axis properties for the [PerformanceCard]
 */
private val axisProperties = GridProperties.AxisProperties(
    enabled = false
)

/**
 * Specific card with the chart of the LAUNCH_TIME analytic
 *
 * @param viewModel The viewmodel related to the [com.tecknobit.ametista.ui.screens.platform.presenter.PlatformScreen] screen
 * @param cardHeight The height of the card
 * @param performanceData The related performance data of the chart
 */
@Wrapper
@Composable
@NonRestartableComposable
fun LaunchTime(
    viewModel: PlatformScreenViewModel,
    cardHeight: Dp = 200.dp,
    performanceData: PerformanceData,
) {
    PerformanceCard(
        viewModel = viewModel,
        title = string.launch_time,
        cardHeight = cardHeight,
        data = performanceData.launchTime,
        popupProperties = PopupProperties(
            textStyle = TextStyle(
                color = Color.White
            ),
            contentBuilder = { value ->
                val factor = 100
                "${round(value * factor) / factor} ms"
            }
        ),
        noDataIcon = Icons.Default.RocketLaunch
    )
}

/**
 * Specific card with the chart of the NETWORK_REQUESTS analytic
 *
 * @param viewModel The viewmodel related to the [com.tecknobit.ametista.ui.screens.platform.presenter.PlatformScreen] screen
 * @param cardHeight The height of the card
 * @param performanceData The related performance data of the chart
 */
@Wrapper
@Composable
@NonRestartableComposable
fun NetworkRequests(
    viewModel: PlatformScreenViewModel,
    cardHeight: Dp = 200.dp,
    performanceData: PerformanceData,
) {
    PerformanceCard(
        viewModel = viewModel,
        title = string.network_requests,
        cardHeight = cardHeight,
        data = performanceData.networkRequests,
        noDataIcon = ChartNetwork
    )
}

/**
 * Specific card with the chart of the TOTAL_ISSUES analytic
 *
 * @param viewModel The viewmodel related to the [com.tecknobit.ametista.ui.screens.platform.presenter.PlatformScreen] screen
 * @param cardHeight The height of the card
 * @param performanceData The related performance data of the chart
 */
@Wrapper
@Composable
@NonRestartableComposable
fun IssuesNumber(
    viewModel: PlatformScreenViewModel,
    cardHeight: Dp = 200.dp,
    performanceData: PerformanceData,
) {
    PerformanceCard(
        viewModel = viewModel,
        title = string.issues_number,
        cardHeight = cardHeight,
        data = performanceData.totalIssues,
        noDataIcon = Icons.Default.BugReport
    )
}

/**
 * Specific card with the chart of the ISSUES_PER_SESSION analytic
 *
 * @param viewModel The viewmodel related to the [com.tecknobit.ametista.ui.screens.platform.presenter.PlatformScreen] screen
 * @param cardHeight The height of the card
 * @param performanceData The related performance data of the chart
 */
@Wrapper
@Composable
@NonRestartableComposable
fun IssuesPerSessionsNumber(
    viewModel: PlatformScreenViewModel,
    cardHeight: Dp = 200.dp,
    performanceData: PerformanceData,
) {
    PerformanceCard(
        viewModel = viewModel,
        title = string.issues_per_session,
        cardHeight = cardHeight,
        data = performanceData.issuesPerSession,
        noDataIcon = Icons.Default.Report
    )
}

/**
 * Container card for the [com.tecknobit.ametista.ui.screens.platform.data.performance.PerformanceAnalytic] analytic
 *
 * @param viewModel The viewmodel related to the [com.tecknobit.ametista.ui.screens.platform.presenter.PlatformScreen] screen
 * @param title The title of the card
 * @param cardHeight The height of the card
 * @param data The performance data for the chart
 * @param popupProperties The properties for the popup
 * @param noDataIcon The icon to display when there are no available data for the [LineChart]
 */
@Composable
private fun PerformanceCard(
    viewModel: PlatformScreenViewModel,
    title: StringResource,
    cardHeight: Dp,
    data: PerformanceDataItem,
    popupProperties: PopupProperties? = null,
    noDataIcon: ImageVector,
) {
    if (data.noDataAvailable()) {
        NoChartData(
            viewModel = viewModel,
            title = title,
            cardHeight = cardHeight,
            icon = noDataIcon,
            data = data
        )
    } else {
        val lineColors = listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.secondary,
            MaterialTheme.colorScheme.tertiary
        )
        val chartData = remember(
            key1 = data
        ) {
            loadChartData(
                lineColors = lineColors,
                data = data,
                popupProperties = popupProperties
            )
        }
        Card(
            modifier = Modifier
                .height(cardHeight)
                // TODO: TO SET
                .widthIn(
                    max = 1280.dp
                )
        ) {
            CardHeader(
                viewModel = viewModel,
                title = title,
                data = data
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
                data = chartData
            )
        }
    }
}

/**
 * The header of the [PerformanceCard] component
 *
 * @param viewModel The viewmodel related to the [com.tecknobit.ametista.ui.screens.platform.presenter.PlatformScreen] screen
 * @param title The title of the card
 * @param data The performance data for the chart
 */
@Composable
@NonRestartableComposable
private fun CardHeader(
    viewModel: PlatformScreenViewModel,
    title: StringResource,
    data: PerformanceDataItem,
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
            title = title,
            data = data
        )
    }
}

/**
 * The actions section of the [PerformanceCard] component
 *
 * @param modifier The modifier to apply to the component
 * @param viewModel The viewmodel related to the [com.tecknobit.ametista.ui.screens.platform.presenter.PlatformScreen] screen
 * @param title The title of the card
 * @param data The performance data for the chart
 */
@Composable
private fun CardActions(
    modifier: Modifier,
    viewModel: PlatformScreenViewModel,
    title: StringResource,
    data: PerformanceDataItem,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End
    ) {
        Row {
            val stateInfo = rememberModalBottomSheetState(
                skipPartiallyExpanded = true
            )
            val scopeInfo = rememberCoroutineScope()
            IconButton(
                onClick = {
                    scopeInfo.launch {
                        stateInfo.show()
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null
                )
            }
            AnalyticInfo(
                state = stateInfo,
                scope = scopeInfo,
                title = title,
                viewModel = viewModel
            )
            if (data.noDataAvailable()) {
                if (data.customFiltered) {
                    IconButton(
                        onClick = {
                            viewModel.clearPerformanceFilter(
                                data = data
                            )
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.FilterListOff,
                            contentDescription = null
                        )
                    }
                }
            } else {
                val stateFilter = rememberModalBottomSheetState(
                    skipPartiallyExpanded = true
                )
                val scopeFilter = rememberCoroutineScope()
                IconButton(
                    onClick = {
                        scopeFilter.launch {
                            stateFilter.show()
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = null
                    )
                }
                FilterChartData(
                    state = stateFilter,
                    scope = scopeFilter,
                    viewModel = viewModel,
                    title = title,
                    data = data
                )
            }
        }
    }
}

/**
 * The component to display what represent the analytic of the [PerformanceCard]
 *
 * @param state The state useful to manage the visibility of the [ModalBottomSheet]
 * @param scope The coroutine useful to manage the visibility of the [ModalBottomSheet]
 * @param title The title of the card
 * @param viewModel The viewmodel related to the [com.tecknobit.ametista.ui.screens.platform.presenter.PlatformScreen] screen
 */
@Composable
private fun AnalyticInfo(
    state: SheetState,
    scope: CoroutineScope,
    title: StringResource,
    viewModel: PlatformScreenViewModel,
) {
    if (state.isVisible) {
        viewModel.suspendRetriever()
        val closeModal = {
            scope.launch {
                state.hide()
                viewModel.restartRetriever()
            }
        }
        ModalBottomSheet(
            onDismissRequest = {
                closeModal()
            }
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = stringResource(title),
                textAlign = TextAlign.Center,
                fontFamily = displayFontFamily,
                fontSize = 20.sp
            )
            val analyticInfo = when (title) {
                string.launch_time -> string.launch_time_info
                string.network_requests -> string.network_requests_info
                string.issues_number -> string.issues_number_info
                else -> string.issues_per_session_info
            }
            HorizontalDivider()
            Text(
                modifier = Modifier
                    .padding(
                        all = 16.dp
                    )
                    .verticalScroll(rememberScrollState()),
                text = stringResource(analyticInfo),
                textAlign = TextAlign.Justify
            )
            TextButton(
                modifier = Modifier
                    .padding(
                        end = 10.dp
                    )
                    .align(Alignment.End),
                onClick = { closeModal() }
            ) {
                Text(
                    text = stringResource(string.got_it)
                )
            }
        }
    }
}

/**
 * The component to display the custom legend for the [PerformanceCard] component
 *
 * @param sampleVersions The sample versions used to for the [LineChart]
 */
@Composable
@NonRestartableComposable
private fun ChartLegend(
    sampleVersions: Set<String>,
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

/**
 * The legend indicator for the [ChartLegend] component
 *
 * @param index The index of sample the in samples available
 * @param sample The sample value (version of the application used as sample)
 */
@Composable
private fun LegendItem(
    index: Int,
    sample: String,
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

/**
 * Method to get color for the chart line based on the index
 *
 * @param index The index of sample the in samples available
 *
 * @return the related color for the chart line [Color]
 */
@Composable
private fun getLineColor(
    index: Int,
): Color {
    return when (index) {
        0 -> MaterialTheme.colorScheme.primary
        1 -> MaterialTheme.colorScheme.secondary
        else -> MaterialTheme.colorScheme.tertiary
    }
}

/**
 * The layout to display when there are no available data for the [PerformanceCard] component
 *
 * @param viewModel The viewmodel related to the [com.tecknobit.ametista.ui.screens.platform.presenter.PlatformScreen] screen
 * @param title The title of the card
 * @param cardHeight The height of the card
 * @param icon The representative icon for the no available chart data
 * @param data The performance data for the chart
 */
@Composable
@NonRestartableComposable
private fun NoChartData(
    viewModel: PlatformScreenViewModel,
    title: StringResource,
    cardHeight: Dp,
    icon: ImageVector,
    data: PerformanceDataItem = PerformanceDataItem(),
) {
    Card(
        modifier = Modifier
            .height(cardHeight)
    ) {
        CardHeader(
            viewModel = viewModel,
            title = title,
            data = data
        )
        EmptyListUI(
            icon = icon,
            subText = stringResource(string.no_events)
        )
    }
}

/**
 * The title component to display a formatted text
 *
 * @param modifier The modifier to apply to the component
 * @param title The title value
 */
@Composable
private fun TitleText(
    modifier: Modifier = Modifier,
    title: StringResource,
) {
    Text(
        modifier = modifier,
        text = stringResource(title),
        fontFamily = displayFontFamily,
        fontSize = 20.sp
    )
}

/**
 * Method load the data for the chart
 *
 * @param lineColors The colors of the lines of the chart
 * @param data The data to use to load the chart
 * @param popupProperties The properties of the popup
 */
private fun loadChartData(
    lineColors: List<Color>,
    data: PerformanceDataItem,
    popupProperties: PopupProperties?,
): List<Line> {
    val chartData = arrayListOf<Line>()
    data.data.entries.forEachIndexed { index, entry ->
        val lineColor = lineColors[index]
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