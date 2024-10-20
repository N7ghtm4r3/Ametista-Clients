package com.tecknobit.ametista.ui.components

import ametista.composeapp.generated.resources.Res
import ametista.composeapp.generated.resources.issues_number
import ametista.composeapp.generated.resources.issues_percent
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.ametista.displayFontFamily
import com.tecknobit.ametista.ui.screens.AmetistaScreen.Companion.CONTAINER_MAX_WIDTH
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
import kotlin.random.Random

private val axisProperties = GridProperties.AxisProperties(
    enabled = false
)

// TODO: PASS THE REAL VALUES TO EACH CARD

@Composable
@NonRestartableComposable
fun LaunchTime(
    cardHeight: Dp = 200.dp
) {
    PerformanceCard(
        title = Res.string.launch_time,
        cardHeight = cardHeight,
        data = mutableMapOf<String, List<Double>>().apply {
            put("1.0.0", List(10) { Random.Default.nextDouble() })
            put("1.0.1", List(10) { Random.Default.nextDouble() })
            put("1.0.2", List(10) { Random.Default.nextDouble() })
        },
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

@Composable
@NonRestartableComposable
fun NetworkRequests(
    cardHeight: Dp = 200.dp
) {
    PerformanceCard(
        title = Res.string.network_requests,
        cardHeight = cardHeight,
        data = mutableMapOf<String, List<Double>>().apply {
            put("1.0.0", List(10) { Random.Default.nextDouble() })
            put("1.0.1", List(10) { Random.Default.nextDouble() })
            put("1.0.2", List(10) { Random.Default.nextDouble() })
        }
    )
}

@Composable
@NonRestartableComposable
fun IssuesNumber(
    cardHeight: Dp = 200.dp
) {
    PerformanceCard(
        title = Res.string.issues_number,
        cardHeight = cardHeight,
        data = mutableMapOf<String, List<Double>>().apply {
            put("1.0.0", List(10) { Random.Default.nextDouble() })
            put("1.0.1", List(10) { Random.Default.nextDouble() })
            put("1.0.2", List(10) { Random.Default.nextDouble() })
        }
    )
}

@Composable
@NonRestartableComposable
fun IssuesPerSessionsNumber(
    cardHeight: Dp = 200.dp
) {
    PerformanceCard(
        title = Res.string.issues_percent,
        cardHeight = cardHeight,
        data = mutableMapOf<String, List<Double>>().apply {
            put("1.0.0", List(10) { Random.Default.nextDouble() })
            put("1.0.1", List(10) { Random.Default.nextDouble() })
            put("1.0.2", List(10) { Random.Default.nextDouble() })
        }
    )
}

@Composable
@NonRestartableComposable
private fun PerformanceCard(
    title: StringResource,
    cardHeight: Dp,
    data: Map<String, List<Double>>,
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
            samples = data.keys
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
    samples: Set<String>
) {
    LazyVerticalGrid(
        modifier = Modifier
            .padding(
                vertical = 5.dp
            ),
        columns = GridCells.Fixed(3)
    ) {
        itemsIndexed(
            items = samples.toList()
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
    data: Map<String, List<Double>>,
    popupProperties: PopupProperties?
): List<Line> {
    val chartData = arrayListOf<Line>()
    data.entries.forEachIndexed { index, entry ->
        val values = entry.value
        val lineColor = getLineColor(
            index = index
        )
        chartData.add(
            Line(
                label = entry.key,
                values = values,
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