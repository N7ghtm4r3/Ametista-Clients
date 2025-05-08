@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMultiplatform::class)

package com.tecknobit.ametista.ui.screens.platform.presenter

import ametista.composeapp.generated.resources.Res
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
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.ametista.displayFontFamily
import com.tecknobit.ametista.ui.components.FirstPageProgressIndicator
import com.tecknobit.ametista.ui.components.NavButton
import com.tecknobit.ametista.ui.components.NewPageProgressIndicator
import com.tecknobit.ametista.ui.components.Theme
import com.tecknobit.ametista.ui.components.noIssues
import com.tecknobit.ametista.ui.screens.applications.presenter.ApplicationsScreen
import com.tecknobit.ametista.ui.screens.platform.components.FilterDialog
import com.tecknobit.ametista.ui.screens.platform.components.Issue
import com.tecknobit.ametista.ui.screens.platform.components.Performance
import com.tecknobit.ametista.ui.screens.platform.data.issues.IssueAnalytic
import com.tecknobit.ametista.ui.screens.platform.data.performance.PerformanceData
import com.tecknobit.ametista.ui.screens.platform.presentation.PlatformScreenViewModel
import com.tecknobit.ametista.ui.theme.AppTypography
import com.tecknobit.ametistacore.enums.AnalyticType
import com.tecknobit.ametistacore.enums.AnalyticType.*
import com.tecknobit.ametistacore.enums.Platform
import com.tecknobit.equinoxcompose.components.EmptyState
import com.tecknobit.equinoxcompose.session.ManagedContent
import com.tecknobit.equinoxcompose.session.screens.EquinoxScreen
import com.tecknobit.equinoxcompose.utilities.awaitNullItemLoaded
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyColumn
import org.jetbrains.compose.resources.stringResource

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
 *
 */
class PlatformScreen(
    applicationId: String,
    private val applicationName: String,
    private val platform: Platform,
) : EquinoxScreen<PlatformScreenViewModel>(
    viewModel = PlatformScreenViewModel(
        applicationId = applicationId,
        platform = platform
    )
) {

    /**
     * `filtersSet` -> whether the filters has been set
     */
    private lateinit var filtersSet: State<Boolean>

    /**
     * `filterList` -> the list of the current filters
     */
    private lateinit var filterList: MutableState<Boolean>

    /**
     * `performanceData` -> the performance data state container
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
                // TODO: TO SET
                .widthIn(
                    max = 1280.dp
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
                awaitNullItemLoaded(
                    itemToWait = performanceData.value
                ) { data ->
                    Performance(
                        viewModel = viewModel,
                        performanceData = data
                    )
                }
            }
        }
    }

    /**
     * List of the [IssueAnalytic] related to the application displayed
     */
    @Composable
    @NonRestartableComposable
    private fun Issues() {
        PaginatedLazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            paginationState = viewModel.analyticsState,
            firstPageProgressIndicator = { FirstPageProgressIndicator() },
            newPageProgressIndicator = { NewPageProgressIndicator() },
            firstPageEmptyIndicator = {
                EmptyState(
                    resource = platform.noIssues(),
                    resourceSize = 300.dp,
                    contentDescription = "No issues reported",
                    title = stringResource(Res.string.no_issues),
                    titleStyle = AppTypography.titleMedium
                )
            },
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(
                bottom = 16.dp
            )
        ) {
            items(
                items = viewModel.analyticsState.allItems!!,
                key = { issue -> issue.id }
            ) { issue ->
                Issue(
                    viewModel = viewModel,
                    issue = issue as IssueAnalytic
                )
            }
        }
    }

    /**
     * Function to collect or instantiate the states of the screen
     */
    @Composable
    override fun CollectStates() {
        filtersSet = viewModel.filtersSet.collectAsState()
        filterList = remember { mutableStateOf(false) }
        viewModel.filtersState = remember { mutableStateOf("") }
        viewModel.analyticType = remember { mutableStateOf(ISSUE) }
        performanceData = viewModel.performanceData.collectAsState()
    }

}