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
import com.tecknobit.equinoxcompose.utilities.ExpandedClassComponent
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.COMPACT_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.MEDIUM_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClassComponent
import com.tecknobit.equinoxcompose.utilities.ResponsiveContent

/**
 * Section where are displayed the data about the current performance the application has
 *
 * @param viewModel The viewmodel related to the [com.tecknobit.ametista.ui.screens.platform.presenter.PlatformScreen]
 * @param performanceData The data about the performance
 */
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

/**
 * Custom [LazyVerticalGrid] used to display the performance data
 *
 * @param viewModel The viewmodel related to the [com.tecknobit.ametista.ui.screens.platform.presenter.PlatformScreen]
 * @param performanceData The data about the performance
 */
@Composable
@ExpandedClassComponent
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

/**
 * Custom [LazyColumn] used to display the performance data
 *
 * @param viewModel The viewmodel related to the [com.tecknobit.ametista.ui.screens.platform.presenter.PlatformScreen]
 * @param performanceData The data about the performance
 */
@Composable
@NonRestartableComposable
@ResponsiveClassComponent(
    classes = [MEDIUM_CONTENT, COMPACT_CONTENT]
)
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