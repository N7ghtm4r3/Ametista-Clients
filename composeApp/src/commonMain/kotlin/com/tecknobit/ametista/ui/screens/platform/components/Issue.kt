@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.ametista.ui.screens.platform.components

import ametista.composeapp.generated.resources.Res.string
import ametista.composeapp.generated.resources.brand
import ametista.composeapp.generated.resources.browser
import ametista.composeapp.generated.resources.browser_version
import ametista.composeapp.generated.resources.date
import ametista.composeapp.generated.resources.hide_device_data
import ametista.composeapp.generated.resources.model
import ametista.composeapp.generated.resources.os_version
import ametista.composeapp.generated.resources.show_device_data
import ametista.composeapp.generated.resources.stacktrace_copied
import ametista.composeapp.generated.resources.version
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.ametista.ui.screens.applications.data.AmetistaDevice
import com.tecknobit.ametista.ui.screens.platform.data.issues.IssueAnalytic
import com.tecknobit.ametista.ui.screens.platform.data.issues.WebIssueAnalytic
import com.tecknobit.ametista.ui.screens.platform.presentation.PlatformScreenViewModel
import com.tecknobit.ametistacore.enums.Platform.WEB
import com.tecknobit.equinoxcompose.utilities.LayoutCoordinator
import com.tecknobit.equinoxcompose.utilities.ResponsiveContent
import com.tecknobit.equinoxcompose.utilities.copyOnClipboard
import com.tecknobit.equinoxcore.time.TimeFormatter.toDateString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

/**
 * `fileLineRegex** the regex to use to highlight the (Line Class.extension) from the error has been thrown
 */
private val fileLineRegex = "([\\w.]+\\.(kt|java):\\d+)".toRegex()

/**
 * The component to represent the details of an [IssueAnalytic]
 *
 * @param viewModel The viewmodel related to the [com.tecknobit.ametista.ui.screens.platform.presenter.PlatformScreen] screen
 * @param issue The issue to represent
 */
@Composable
fun Issue(
    viewModel: PlatformScreenViewModel,
    issue: IssueAnalytic,
) {
    val state = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val scope = rememberCoroutineScope()
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        ),
        shape = RoundedCornerShape(
            size = 10.dp
        ),
        onClick = {
            scope.launch {
                state.show()
            }
        }
    ) {
        IssueTitle(
            viewModel = viewModel,
            issue = issue
        )
        IssueVisibleData(
            issue = issue
        )
        IssueDynamicData(
            issue = issue
        )
    }
    IssueStackTrace(
        viewModel = viewModel,
        state = state,
        scope = scope,
        issue = issue
    )
}

/**
 * The component to display the stack trace of the error
 *
 * @param viewModel The viewmodel related to the [com.tecknobit.ametista.ui.screens.platform.presenter.PlatformScreen] screen
 * @param state The state useful to manage the visibility of the [ModalBottomSheet]
 * @param scope The coroutine useful to manage the visibility of the [ModalBottomSheet]
 * @param issue The issue to represent
 */
@Composable
private fun IssueStackTrace(
    viewModel: PlatformScreenViewModel,
    state: SheetState,
    scope: CoroutineScope,
    issue: IssueAnalytic,
) {
    if (state.isVisible) {
        viewModel.suspendRetriever()
        ModalBottomSheet(
            onDismissRequest = {
                scope.launch {
                    state.hide()
                    viewModel.restartRetriever()
                }
            }
        ) {
            IssueTitle(
                viewModel = viewModel,
                state = state,
                scope = scope,
                issue = issue
            )
            Text(
                modifier = Modifier
                    .padding(
                        horizontal = 16.dp
                    )
                    .padding(
                        bottom = 16.dp
                    )
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                text = parsedStackTrace(
                    issue = issue
                ),
                textAlign = TextAlign.Justify
            )
        }
    }
}

/**
 * The section title of the [Issue] component
 *
 * @param viewModel The viewmodel related to the [com.tecknobit.ametista.ui.screens.platform.presenter.PlatformScreen] screen
 * @param state The state useful to manage the visibility of the [ModalBottomSheet]
 * @param scope The coroutine useful to manage the visibility of the [ModalBottomSheet]
 * @param issue The issue represented
 */
@Composable
@NonRestartableComposable
private fun IssueTitle(
    viewModel: PlatformScreenViewModel,
    state: SheetState? = null,
    scope: CoroutineScope? = null,
    issue: IssueAnalytic,
) {
    Row(
        modifier = Modifier
            .padding(
                start = 10.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = issue.name,
            fontSize = 20.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        IconButton(
            onClick = {
                copyOnClipboard(
                    content = issue.issue,
                    onCopy = {
                        scope?.launch {
                            state!!.hide()
                        }
                        viewModel.showSnackbarMessage(
                            message = string.stacktrace_copied
                        )
                    }
                )
            }
        ) {
            Icon(
                imageVector = Icons.Default.ContentCopy,
                contentDescription = null
            )
        }
    }
    HorizontalDivider(
        color = MaterialTheme.colorScheme.primary
    )
}

/**
 * Method to get the stacktrace of an [Issue] formatted
 *
 * @param issue The issue from fetch its stacktrace
 */
@Composable
private fun parsedStackTrace(
    issue: IssueAnalytic,
): AnnotatedString {
    return buildAnnotatedString {
        issue.issue.lines().forEach { line ->
            val matchResult = fileLineRegex.find(line)
            if (matchResult != null) {
                append(line.substring(0, matchResult.range.first))
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colorScheme.primary
                    )
                ) {
                    append(matchResult.value)
                }
                append(line.substring(matchResult.range.last + 1))
            } else
                append(line)
            append("\n")
        }
    }
}

/**
 * Section to display the default data of an issue
 *
 * @param issue The issue to represent
 */
@Composable
@NonRestartableComposable
private fun IssueVisibleData(
    issue: IssueAnalytic,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 16.dp,
            )
            .padding(
                horizontal = 16.dp
            ),
    ) {
        IssueSection(
            modifier = Modifier
                .weight(2f)
                .fillMaxWidth(),
            header = string.date,
            data = issue.creationDate.toDateString()
        )
        IssueSection(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            header = string.version,
            data = issue.appVersion
        )
    }
    if (issue.platform == WEB) {
        WebIssueBrowserInfo(
            issue = issue as WebIssueAnalytic
        )
    }
}

/**
 * Section to display the data of a [WebIssueAnalytic]
 *
 * @param issue The web-issue to represent
 */
@Composable
@NonRestartableComposable
private fun WebIssueBrowserInfo(
    issue: WebIssueAnalytic,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 16.dp,
            )
            .padding(
                horizontal = 16.dp
            ),
    ) {
        IssueSection(
            modifier = Modifier
                .weight(2f)
                .fillMaxWidth(),
            header = string.browser,
            data = issue.browser
        )
        IssueSection(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            header = string.browser_version,
            data = issue.browserVersion
        )
    }
}

/**
 * Section to display the device data related to an issue
 *
 * @param issue The issue to represent
 */
@Composable
@LayoutCoordinator
private fun IssueDynamicData(
    issue: IssueAnalytic,
) {
    val device = issue.device
    ResponsiveContent(
        onExpandedSizeClass = {
            IssueDeviceData(
                modifier = Modifier
                    .padding(
                        bottom = 16.dp
                    ),
                device = device
            )
        },
        onMediumSizeClass = {
            IssueDeviceExpandableData(
                device = device
            )
        },
        onCompactSizeClass = {
            IssueDeviceExpandableData(
                device = device
            )
        }
    )
}

/**
 * The container component for the data of an [AmetistaDevice] related to the [Issue] represented
 *
 * @param device The device to represent
 */
@Composable
private fun IssueDeviceExpandableData(
    device: AmetistaDevice,
) {
    val showDeviceData = remember { mutableStateOf(false) }
    AnimatedVisibility(
        visible = showDeviceData.value
    ) {
        IssueDeviceData(
            device = device
        )
    }
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextButton(
            onClick = { showDeviceData.value = !showDeviceData.value }
        ) {
            Text(
                text = stringResource(
                    if (showDeviceData.value)
                        string.hide_device_data
                    else
                        string.show_device_data
                )
            )
        }
    }
}

/**
 * The data of an [AmetistaDevice] related to the [Issue] represented
 *
 * @param modifier The modifier to apply to the component
 * @param device The device to represent
 */
@Composable
@NonRestartableComposable
private fun IssueDeviceData(
    modifier: Modifier = Modifier,
    device: AmetistaDevice,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                top = 10.dp,
            )
            .padding(
                horizontal = 16.dp
            ),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row {
            IssueSection(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxWidth(),
                header = string.brand,
                data = device.brand
            )
            IssueSection(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                header = string.model,
                data = device.model
            )
        }
        IssueSection(
            header = string.os_version,
            data = device.osVersion
        )
    }
}

/**
 * The container component to display the issue data whit its header
 *
 * @param modifier The modifier to apply to the component
 * @param header The header representative of the data to display
 * @param data The issue data to display
 */
@Composable
private fun IssueSection(
    modifier: Modifier = Modifier,
    header: StringResource,
    data: String,
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = stringResource(header),
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = data
        )
    }
}