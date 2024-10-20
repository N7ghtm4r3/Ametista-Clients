@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.ametista.ui.screens.platform

import ametista.composeapp.generated.resources.Res.string
import ametista.composeapp.generated.resources.no_issues
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.FilterListOff
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.ametista.displayFontFamily
import com.tecknobit.ametista.helpers.Theme
import com.tecknobit.ametista.ui.components.FilterDialog
import com.tecknobit.ametista.ui.components.Issue
import com.tecknobit.ametista.ui.components.MultiLineChart
import com.tecknobit.ametista.ui.screens.AmetistaScreen
import com.tecknobit.ametistacore.models.AmetistaApplication
import com.tecknobit.ametistacore.models.Platform
import com.tecknobit.ametistacore.models.analytics.AmetistaAnalytic.AnalyticType
import com.tecknobit.ametistacore.models.analytics.AmetistaAnalytic.AnalyticType.ISSUE
import com.tecknobit.ametistacore.models.analytics.AmetistaAnalytic.AnalyticType.PERFORMANCE
import com.tecknobit.ametistacore.models.analytics.AmetistaAnalytic.AnalyticType.entries
import com.tecknobit.ametistacore.models.analytics.issues.IssueAnalytic
import com.tecknobit.equinoxcompose.components.EmptyListUI
import com.tecknobit.equinoxcompose.helpers.session.ManagedContent
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyColumn

class PlatformScreen(
    initialApplication: AmetistaApplication,
    private val platform: Platform
) : AmetistaScreen<PlatformScreenViewModel>(
    viewModel = PlatformScreenViewModel(
        initialApplication = initialApplication
    )
) {

    private lateinit var application: State<AmetistaApplication>

    private lateinit var filtersSet: State<Boolean>

    private lateinit var filterList: MutableState<Boolean>

    /**
     * Function to arrange the content of the screen to display
     *
     * No-any params required
     */
    @Composable
    override fun ArrangeScreenContent() {
        platform.Theme {
            ManagedContent(
                viewModel = viewModel!!,
                content = {
                    Scaffold(
                        topBar = {
                            LargeTopAppBar(
                                colors = TopAppBarDefaults.largeTopAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.primary
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
                                            text = application.value.name
                                        )
                                    }
                                }
                            )
                        },
                        floatingActionButton = { FilterButton() }
                    ) { paddingValues ->
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
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

    @Composable
    @NonRestartableComposable
    private fun FilterButton() {
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
                viewModel = viewModel!!
            )
        } else {
            FloatingActionButton(
                onClick = { viewModel!!.clearFilters() }
            ) {
                Icon(
                    imageVector = Icons.Default.FilterListOff,
                    contentDescription = null
                )
            }
        }
    }

    @Composable
    @NonRestartableComposable
    private fun AnalyticsSelector() {
        SingleChoiceSegmentedButtonRow {
            val lastEntry = entries.last()
            entries.forEach { type ->
                SegmentedButton(
                    selected = viewModel!!.analyticType.value == type,
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
                    onClick = { viewModel!!.analyticType.value = type }
                )
            }
        }
    }

    private fun AnalyticType.icon(): ImageVector {
        return when (this) {
            ISSUE -> Icons.Default.BugReport
            PERFORMANCE -> Icons.Default.Speed
        }
    }

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
                visible = viewModel!!.analyticType.value == ISSUE
            ) {
                Issues()
            }
            AnimatedVisibility(
                visible = viewModel!!.analyticType.value == PERFORMANCE
            ) {
                Performance()
            }
        }
    }

    @Composable
    @NonRestartableComposable
    private fun Issues() {
        val issuesIsEmpty = remember { mutableStateOf(false) }
        PaginatedLazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            paginationState = viewModel!!.paginationState,
            firstPageProgressIndicator = {
                CircularProgressIndicator()
            },
            newPageProgressIndicator = {
                LinearProgressIndicator()
            },
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(
                bottom = 16.dp
            )
            // TODO: TO SET
            /*firstPageErrorIndicator = { e -> // from setError
                ... e.message ...
                ... onRetry = { paginationState.retryLastFailedRequest() } ...
            },
            // TODO: TO SET
            newPageErrorIndicator = { e -> ... },*/
            // The rest of LazyColumn params
        ) {
            val issues = viewModel!!.paginationState.allItems!!
            issuesIsEmpty.value = issues.isEmpty()
            if (issues.isNotEmpty()) {
                items(
                    items = issues,
                    key = { issue -> issue.id }
                ) { issue ->
                    Issue(
                        viewModel = viewModel!!,
                        issue = issue as IssueAnalytic
                    )
                }
            }
        }
        NoIssues(
            noIssues = issuesIsEmpty.value
        )
    }

    @Composable
    @NonRestartableComposable
    private fun Performance() {
        FirstLaunch()
    }

    @Composable
    @NonRestartableComposable
    private fun FirstLaunch() {
        MultiLineChart(
            title = "First launch"
        )
    }

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

    override fun onStart() {
        super.onStart()
        viewModel!!.refreshApplication(
            platform = platform
        )
    }

    /**
     * Function to collect or instantiate the states of the screen
     *
     * No-any params required
     */
    @Composable
    override fun CollectStates() {
        application = viewModel!!.application.collectAsState()
        filtersSet = viewModel!!.filtersSet.collectAsState()
        filterList = remember { mutableStateOf(false) }
        viewModel!!.analyticType = remember { mutableStateOf(ISSUE) }
    }

}