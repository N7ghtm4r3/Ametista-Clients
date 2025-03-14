package com.tecknobit.ametista.ui.screens.platform.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecknobit.ametista.ui.screens.platform.data.performance.PerformanceData
import com.tecknobit.ametista.ui.screens.platform.presentation.PlatformScreenViewModel
import com.tecknobit.equinoxcompose.utilities.ResponsiveContent

// TODO: TO COMMENT

@Composable
@NonRestartableComposable
fun Performance(
    viewModel: PlatformScreenViewModel,
    performanceData: PerformanceData,
) {
    ResponsiveContent(
        onExpandedSizeClass = {
            PerformanceGrid(
                viewModel = viewModel,
                performanceData = performanceData
            )
        },
        onMediumSizeClass = {
            PerformanceColumn(
                viewModel = viewModel,
                performanceData = performanceData
            )
        },
        onCompactSizeClass = {
            PerformanceColumn(
                viewModel = viewModel,
                performanceData = performanceData
            )
        }
    )
}

@Composable
@NonRestartableComposable
private fun PerformanceGrid(
    viewModel: PlatformScreenViewModel,
    performanceData: PerformanceData,
) {
    val cardHeight = 350.dp
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                LaunchTime(
                    viewModel = viewModel,
                    cardHeight = cardHeight,
                    performanceData = performanceData
                )
            }
            item {
                NetworkRequests(
                    viewModel = viewModel,
                    cardHeight = cardHeight,
                    performanceData = performanceData
                )
            }
            item {
                IssuesNumber(
                    viewModel = viewModel,
                    cardHeight = cardHeight,
                    performanceData = performanceData
                )
            }
            item {
                IssuesPerSessionsNumber(
                    viewModel = viewModel,
                    cardHeight = cardHeight,
                    performanceData = performanceData
                )
            }
        }
    }
}

@Composable
@NonRestartableComposable
private fun PerformanceColumn(
    viewModel: PlatformScreenViewModel,
    performanceData: PerformanceData,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            LaunchTime(
                viewModel = viewModel,
                performanceData = performanceData
            )
        }
        item {
            NetworkRequests(
                viewModel = viewModel,
                performanceData = performanceData
            )
        }
        item {
            IssuesNumber(
                viewModel = viewModel,
                performanceData = performanceData
            )
        }
        item {
            IssuesPerSessionsNumber(
                viewModel = viewModel,
                performanceData = performanceData
            )
        }
    }
}