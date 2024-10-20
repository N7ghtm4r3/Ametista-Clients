package com.tecknobit.ametista.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.DividerProperties
import ir.ehsannarmani.compose_charts.models.DrawStyle
import ir.ehsannarmani.compose_charts.models.GridProperties
import ir.ehsannarmani.compose_charts.models.HorizontalIndicatorProperties
import ir.ehsannarmani.compose_charts.models.LabelHelperProperties
import ir.ehsannarmani.compose_charts.models.Line
import ir.ehsannarmani.compose_charts.models.LineProperties

@Composable
fun MultiLineChart(
    title: String
) {
    Card {
        LineChart(
            modifier = Modifier
                .size(150.dp),
            gridProperties = GridProperties(
                enabled = false,
                xAxisProperties = GridProperties.AxisProperties(
                    enabled = false
                ),
                yAxisProperties = GridProperties.AxisProperties(
                    enabled = false,
                    lineCount = 0
                )
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
            data = listOf(
                Line(
                    label = "Windows",
                    values = listOf(28.0, 41.0, 5.0, 10.0, 35.0),
                    color = SolidColor(MaterialTheme.colorScheme.primary),
                    drawStyle = DrawStyle.Fill
                ),
                Line(
                    label = "Linux",
                    values = listOf(2.0, 12.0, 33.0, 43.0, 65.0),
                    color = SolidColor(MaterialTheme.colorScheme.secondary),
                    drawStyle = DrawStyle.Fill
                )
            )
        )
    }
}

