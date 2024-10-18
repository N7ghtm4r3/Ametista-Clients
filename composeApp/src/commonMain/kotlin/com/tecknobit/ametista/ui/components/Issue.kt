package com.tecknobit.ametista.ui.components

import ametista.composeapp.generated.resources.Res.string
import ametista.composeapp.generated.resources.brand
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Expanded
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.ametista.helpers.getCurrentWidthSizeClass
import com.tecknobit.ametistacore.models.analytics.AmetistaAnalytic.AmetistaDevice
import com.tecknobit.ametistacore.models.analytics.IssueAnalytic
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
@NonRestartableComposable
fun Issue(
    issue: IssueAnalytic
) {
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
            // TODO: EXPAND STACKTRACE OR FIND A WAY TO DISPLAY IT 
        }
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