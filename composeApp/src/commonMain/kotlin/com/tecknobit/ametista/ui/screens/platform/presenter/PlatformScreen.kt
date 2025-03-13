@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.ametista.ui.screens.platform.presenter

import ametista.composeapp.generated.resources.Res.string
import ametista.composeapp.generated.resources.no_issues
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.FilterListOff
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.ametista.displayFontFamily
import com.tecknobit.ametista.getCurrentWidthSizeClass
import com.tecknobit.ametista.helpers.Theme
import com.tecknobit.ametista.ui.components.FilterDialog
import com.tecknobit.ametista.ui.components.FirstPageProgressIndicator
import com.tecknobit.ametista.ui.components.Issue
import com.tecknobit.ametista.ui.components.IssuesNumber
import com.tecknobit.ametista.ui.components.IssuesPerSessionsNumber
import com.tecknobit.ametista.ui.components.LaunchTime
import com.tecknobit.ametista.ui.components.NetworkRequests
import com.tecknobit.ametista.ui.components.NewPageProgressIndicator
import com.tecknobit.ametista.ui.screens.applications.presenter.ApplicationsScreen
import com.tecknobit.ametista.ui.screens.platform.presentation.PlatformScreenViewModel
import com.tecknobit.ametista.ui.screens.shared.presenters.AmetistaScreen
import com.tecknobit.ametistacore.models.AmetistaApplication
import com.tecknobit.ametistacore.models.Platform
import com.tecknobit.ametistacore.models.analytics.AmetistaAnalytic.AnalyticType
import com.tecknobit.ametistacore.models.analytics.AmetistaAnalytic.AnalyticType.ISSUE
import com.tecknobit.ametistacore.models.analytics.AmetistaAnalytic.AnalyticType.PERFORMANCE
import com.tecknobit.ametistacore.models.analytics.AmetistaAnalytic.AnalyticType.entries
import com.tecknobit.ametistacore.models.analytics.issues.IssueAnalytic
import com.tecknobit.ametistacore.models.analytics.performance.PerformanceData
import com.tecknobit.equinoxcompose.components.EmptyListUI
import com.tecknobit.equinoxcompose.helpers.session.EquinoxScreen
import com.tecknobit.equinoxcompose.helpers.session.ManagedContent
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyColumn

/**
 * The [ApplicationsScreen] class is used to display the list of [AmetistaApplication] registered by
 * the system
 *
 * @param applicationId The identifier of the application displayed
 * @param applicationName
 * @param platform The specific platform of the application displayed
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see EquinoxScreen
 * @see AmetistaScreen
 */
class PlatformScreen(
    applicationId: String,
    private val applicationName: String,
    private val platform: Platform
) : AmetistaScreen<PlatformScreenViewModel>(
    viewModel = PlatformScreenViewModel(
        applicationId = applicationId,
        platform = platform
    )
) {

    /**
     * **filtersSet** -> whether the filters has been set
     */
    private lateinit var filtersSet: State<Boolean>

    /**
     * **filterList** -> the list of the current filters
     */
    private lateinit var filterList: MutableState<Boolean>

    /**
     * **performanceData** -> the performance data state container
     */
    private lateinit var performanceData: State<PerformanceData?>

    /**
     * Function to arrange the content of the screen to display
     */
    @Composable
    override fun ArrangeScreenContent() {
        platform.Theme {
            ManagedContent(
                viewModel = viewModel,
                content = {
                    Scaffold(
                        topBar = {
                            LargeTopAppBar(
                                colors = TopAppBarDefaults.largeTopAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    titleContentColor = Color.White
                                ),
                                navigationIcon = { NavButton() },
                                title = {
                                    Column {
                                        Text(
                                            text = platform.name,
                                            fontSize = 14.sp,
                                            fontFamily = displayFontFamily,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = applicationName
                                        )
                                    }
                                }
                            )
                        },
                        snackbarHost = { SnackbarHost(viewModel.snackbarHostState!!) },
                        floatingActionButton = { FilterButton() }
                    ) { paddingValues ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = paddingValues.calculateTopPadding() - 5.dp,
                                    bottom = 16.dp
                                )
                                .padding(
                                    horizontal = 16.dp
                                ),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            AnalyticsSelector()
                            AnalyticsItems()
                        }
                    }
                }
            )
        }
    }

    /**
     * The custom [FloatingActionButton] used to manage the filtering operations
     */
    @Composable
    @NonRestartableComposable
    private fun FilterButton() {
        AnimatedVisibility(
            visible = viewModel.analyticType.value == ISSUE,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            if (!filtersSet.value) {
                FloatingActionButton(
                    onClick = { filterList.value = true }
                ) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = null
                    )
                }
                FilterDialog(
                    show = filterList,
                    viewModel = viewModel
                )
            } else {
                FloatingActionButton(
                    onClick = { viewModel.clearFilters() }
                ) {
                    Icon(
                        imageVector = Icons.Default.FilterListOff,
                        contentDescription = null
                    )
                }
            }
        }
    }

    /**
     * The selector used to display the specific analytic data
     */
    @Composable
    @NonRestartableComposable
    private fun AnalyticsSelector() {
        SingleChoiceSegmentedButtonRow {
            val lastEntry = entries.last()
            entries.forEach { type ->
                SegmentedButton(
                    selected = viewModel.analyticType.value == type,
                    icon = {
                        Icon(
                            imageVector = type.icon(),
                            contentDescription = null
                        )
                    },
                    label = {
                        Text(
                            text = type.tabTitle
                        )
                    },
                    shape = if (type == lastEntry) {
                        RoundedCornerShape(
                            topEnd = 0.dp,
                            bottomEnd = 10.dp
                        )
                    } else {
                        RoundedCornerShape(
                            topStart = 0.dp,
                            bottomStart = 10.dp
                        )
                    },
                    onClick = { viewModel.analyticType.value = type }
                )
            }
        }
    }

    /**
     * Method to get the representative icon for the analytic section
     *
     * @return the representative icon as [ImageVector]
     */
    private fun AnalyticType.icon(): ImageVector {
        return when (this) {
            ISSUE -> Icons.Default.BugReport
            PERFORMANCE -> Icons.Default.Speed
        }
    }

    /**
     * The available analytic can be choose
     */
    @Composable
    @NonRestartableComposable
    private fun AnalyticsItems() {
        Column(
            modifier = Modifier
                .padding(
                    vertical = 16.dp
                )
                .widthIn(
                    max = CONTAINER_MAX_WIDTH
                )
        ) {
            AnimatedVisibility(
                visible = viewModel.analyticType.value == ISSUE
            ) {
                Issues()
            }
            AnimatedVisibility(
                visible = viewModel.analyticType.value == PERFORMANCE
            ) {
                LaunchedEffect(Unit) {
                    viewModel.getPerformanceAnalytics()
                }
                Performance()
            }
        }
    }

    /**
     * List of the [IssueAnalytic] related to the application displayed
     */
    @Composable
    @NonRestartableComposable
    private fun Issues() {
        val issuesIsEmpty = remember { mutableStateOf(false) }
        PaginatedLazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            paginationState = viewModel.paginationState,
            firstPageProgressIndicator = { FirstPageProgressIndicator() },
            newPageProgressIndicator = { NewPageProgressIndicator() },
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(
                bottom = 16.dp
            )
        ) {
            val issues = viewModel.paginationState.allItems!!
            issuesIsEmpty.value = issues.isEmpty()
            if (issues.isNotEmpty()) {
                items(
                    items = issues,
                    key = { issue -> issue.id }
                ) { issue ->
                    Issue(
                        viewModel = viewModel,
                        issue = issue as IssueAnalytic
                    )
                }
            }
        }
        NoIssues(
            noIssues = issuesIsEmpty.value
        )
    }

    /**
     * Wrapper component to arrange based on the current window with size the performance data
     */
    @Composable
    @NonRestartableComposable
    private fun Performance() {
        val windowWithSize = getCurrentWidthSizeClass()
        when (windowWithSize) {
            WindowWidthSizeClass.Expanded -> {
                PerformanceGrid()
            }

            else -> {
                PerformanceColumn()
            }
        }
    }

    /**
     * Custom grid to display the performance data on the grid layout
     */
    @Composable
    @NonRestartableComposable
    private fun PerformanceGrid() {
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
                        performanceData = performanceData.value
                    )
                }
                item {
                    NetworkRequests(
                        viewModel = viewModel,
                        cardHeight = cardHeight,
                        performanceData = performanceData.value
                    )
                }
                item {
                    IssuesNumber(
                        viewModel = viewModel,
                        cardHeight = cardHeight,
                        performanceData = performanceData.value
                    )
                }
                item {
                    IssuesPerSessionsNumber(
                        viewModel = viewModel,
                        cardHeight = cardHeight,
                        performanceData = performanceData.value
                    )
                }
            }
        }
    }

    /**
     * Custom column to display the performance data on the column layout
     */
    @Composable
    @NonRestartableComposable
    private fun PerformanceColumn() {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                LaunchTime(
                    viewModel = viewModel,
                    performanceData = performanceData.value
                )
            }
            item {
                NetworkRequests(
                    viewModel = viewModel,
                    performanceData = performanceData.value
                )
            }
            item {
                IssuesNumber(
                    viewModel = viewModel,
                    performanceData = performanceData.value
                )
            }
            item {
                IssuesPerSessionsNumber(
                    viewModel = viewModel,
                    performanceData = performanceData.value
                )
            }
        }
    }

    /**
     * Layout visible when no issues have been reported
     *
     * @param noIssues Whether there are some issues available
     */
    @Composable
    @NonRestartableComposable
    private fun NoIssues(
        noIssues: Boolean,
    ) {
        AnimatedVisibility(
            visible = noIssues,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            EmptyListUI(
                imageModifier = Modifier
                    .size(150.dp),
                icon = Icons.Default.BugReport,
                subText = string.no_issues,
                textStyle = TextStyle(
                    fontFamily = displayFontFamily,
                    fontSize = 20.sp
                )
            )
        }
    }

    /**
     * Function to collect or instantiate the states of the screen
     */
    @Composable
    override fun CollectStates() {
        filtersSet = viewModel.filtersSet.collectAsState()
        filterList = remember { mutableStateOf(false) }
        viewModel.analyticType = remember { mutableStateOf(ISSUE) }
        performanceData = viewModel.performanceData.collectAsState()
    }

}