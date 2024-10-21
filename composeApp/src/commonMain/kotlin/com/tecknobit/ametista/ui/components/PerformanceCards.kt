package com.tecknobit.ametista.ui.components

import ametista.composeapp.generated.resources.Res.string
import ametista.composeapp.generated.resources.issues_number
import ametista.composeapp.generated.resources.issues_per_session
import ametista.composeapp.generated.resources.launch_time
import ametista.composeapp.generated.resources.network_requests
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.ametista.displayFontFamily
import com.tecknobit.ametista.ui.screens.AmetistaScreen.Companion.CONTAINER_MAX_WIDTH
import com.tecknobit.ametistacore.models.AmetistaApplication.MAX_VERSION_SAMPLES
import com.tecknobit.ametistacore.models.analytics.performance.PerformanceData
import com.tecknobit.ametistacore.models.analytics.performance.PerformanceData.PerformanceDataItem
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

private val axisProperties = GridProperties.AxisProperties(
    enabled = false
)

// TODO: PASS THE REAL VALUES TO EACH CARD

@Composable
@NonRestartableComposable
fun LaunchTime(
    cardHeight: Dp = 200.dp,
    performanceData: PerformanceData?
) {
    if (performanceData == null) {
        NoChartData(
            title = string.launch_time,
            cardHeight = cardHeight,
            icon = Icons.Default.Person,
            infoText = string.network_requests
        )
    } else {
        PerformanceCard(
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
            )
        )
    }
}

@Composable
@NonRestartableComposable
fun NetworkRequests(
    cardHeight: Dp = 200.dp,
    performanceData: PerformanceData?
) {
    if (performanceData == null) {
        NoChartData(
            title = string.network_requests,
            cardHeight = cardHeight,
            icon = Icons.Default.Person,
            infoText = string.network_requests
        )
    } else {
        PerformanceCard(
            title = string.network_requests,
            cardHeight = cardHeight,
            data = performanceData.networkRequests
        )
    }
}

@Composable
@NonRestartableComposable
fun IssuesNumber(
    cardHeight: Dp = 200.dp,
    performanceData: PerformanceData?
) {
    if (performanceData == null) {
        NoChartData(
            title = string.issues_number,
            cardHeight = cardHeight,
            icon = Icons.Default.Person,
            infoText = string.network_requests
        )
    } else {
        PerformanceCard(
            title = string.issues_number,
            cardHeight = cardHeight,
            data = performanceData.totalIssues
        )
    }
}

@Composable
@NonRestartableComposable
fun IssuesPerSessionsNumber(
    cardHeight: Dp = 200.dp,
    performanceData: PerformanceData?
) {
    if (performanceData == null) {
        NoChartData(
            title = string.issues_per_session,
            cardHeight = cardHeight,
            icon = Icons.Default.Person,
            infoText = string.network_requests
        )
    } else {
        PerformanceCard(
            title = string.issues_per_session,
            cardHeight = cardHeight,
            data = performanceData.issuesPerSession
        )
    }
}

@Composable
@NonRestartableComposable
private fun NoChartData(
    title: StringResource,
    cardHeight: Dp,
    icon: ImageVector,
    infoText: StringResource,
) {
    Card(
        modifier = Modifier
            .height(cardHeight)
    ) {
        Text(
            modifier = Modifier
                .padding(
                    top = 16.dp,
                    start = 16.dp
                )
                .widthIn(
                    max = CONTAINER_MAX_WIDTH
                ),
            text = stringResource(title),
            fontFamily = displayFontFamily,
            fontSize = 20.sp
        )
        EmptyListUI(
            icon = icon,
            subText = stringResource(infoText)
        )
    }
}

@Composable
@NonRestartableComposable
private fun PerformanceCard(
    title: StringResource,
    cardHeight: Dp,
    data: PerformanceDataItem,
    popupProperties: PopupProperties? = null
) {
    Card(
        modifier = Modifier
            .height(cardHeight)
    ) {
        Text(
            modifier = Modifier
                .padding(
                    top = 16.dp,
                    start = 16.dp
                )
                .widthIn(
                    max = CONTAINER_MAX_WIDTH
                ),
            text = stringResource(title),
            fontFamily = displayFontFamily,
            fontSize = 20.sp
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