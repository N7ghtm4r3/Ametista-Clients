@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.ametista.ui.components

import ametista.composeapp.generated.resources.Res.string
import ametista.composeapp.generated.resources.brand
import ametista.composeapp.generated.resources.browser
import ametista.composeapp.generated.resources.browser_version
import ametista.composeapp.generated.resources.date
import ametista.composeapp.generated.resources.hide_device_data
import ametista.composeapp.generated.resources.model
import ametista.composeapp.generated.resources.os_version
import ametista.composeapp.generated.resources.show_device_data
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Expanded
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.ametista.getCurrentWidthSizeClass
import com.tecknobit.ametista.ui.screens.platform.PlatformScreenViewModel
import com.tecknobit.ametistacore.models.Platform.WEB
import com.tecknobit.ametistacore.models.analytics.AmetistaAnalytic.AmetistaDevice
import com.tecknobit.ametistacore.models.analytics.issues.IssueAnalytic
import com.tecknobit.ametistacore.models.analytics.issues.WebIssueAnalytic
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

private val fileLineRegex = "([\\w.]+\\.(kt|java):\\d+)".toRegex()

@Composable
@NonRestartableComposable
fun Issue(
    viewModel: PlatformScreenViewModel,
    issue: IssueAnalytic
) {
    val expandStackTrace = remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        ),
        shape = RoundedCornerShape(
            size = 10.dp
        ),
        onClick = { expandStackTrace.value = true }
    ) {
        IssueTitle(
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
        expand = expandStackTrace,
        issue = issue
    )
}

@Composable
@NonRestartableComposable
private fun IssueStackTrace(
    viewModel: PlatformScreenViewModel,
    expand: MutableState<Boolean>,
    issue: IssueAnalytic
) {
    if (expand.value) {
        viewModel.suspendRefresher()
        ModalBottomSheet(
            onDismissRequest = {
                expand.value = false
                viewModel.restartRefresher()
            }
        ) {
            IssueTitle(
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

@Composable
@NonRestartableComposable
private fun IssueTitle(
    issue: IssueAnalytic
) {
    Text(
        modifier = Modifier
            .padding(
                all = 10.dp
            ),
        text = issue.name,
        fontSize = 20.sp
    )
    HorizontalDivider(
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
private fun parsedStackTrace(
    issue: IssueAnalytic
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

@Composable
@NonRestartableComposable
private fun IssueVisibleData(
    issue: IssueAnalytic
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
            data = issue.creationDate
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

@Composable
@NonRestartableComposable
private fun WebIssueBrowserInfo(
    issue: WebIssueAnalytic
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

@Composable
@NonRestartableComposable
private fun IssueDynamicData(
    issue: IssueAnalytic
) {
    val device = issue.device
    when (getCurrentWidthSizeClass()) {
        Expanded -> {
            IssueDeviceData(
                modifier = Modifier
                    .padding(
                        bottom = 16.dp
                    ),
                device = device
            )
        }

        else -> {
            IssueDeviceExpandableData(
                device = device
            )
        }
    }
}

@Composable
@NonRestartableComposable
private fun IssueDeviceExpandableData(
    device: AmetistaDevice
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

@Composable
@NonRestartableComposable
private fun IssueDeviceData(
    modifier: Modifier = Modifier,
    device: AmetistaDevice
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


@Composable
@NonRestartableComposable
private fun IssueSection(
    modifier: Modifier = Modifier,
    header: StringResource,
    data: String
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