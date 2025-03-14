package com.tecknobit.ametista.ui.screens.applications.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tecknobit.ametista.ui.screens.applications.presentation.ApplicationsScreenViewModel
import com.tecknobit.ametistacore.enums.Platform
import icon

/**
 * The menu with the available platforms to use as filters
 *
 * @param viewModel The support viewmodel for the scree
 */
@Composable
@NonRestartableComposable
fun PlatformsMenu(
    viewModel: ApplicationsScreenViewModel,
) {
    var expanded by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .padding(
                start = 16.dp
            ),
        horizontalAlignment = Alignment.End
    ) {
        IconButton(
            onClick = { expanded = true }
        ) {
            Icon(
                imageVector = Icons.Default.FilterList,
                contentDescription = null
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                Platform.entries.forEach { platform ->
                    PlatformItem(
                        platform = platform,
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

/**
 * Platform item menu of the [PlatformsMenu] component
 *
 * @param platform The related platform
 * @param viewModel The support viewmodel for the scree
 */
@Composable
@NonRestartableComposable
private fun PlatformItem(
    platform: Platform,
    viewModel: ApplicationsScreenViewModel,
) {
    DropdownMenuItem(
        text = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    modifier = Modifier
                        .weight(1f),
                    imageVector = platform.icon(),
                    contentDescription = null
                )
                Text(
                    modifier = Modifier
                        .weight(1f),
                    text = platform.name,
                    textAlign = TextAlign.Center
                )
                Checkbox(
                    modifier = Modifier
                        .weight(1f),
                    checked = viewModel.platformsFilter.contains(platform),
                    onCheckedChange = { checked ->
                        viewModel.managePlatforms(
                            checked = checked,
                            platform = platform
                        )
                    }
                )
            }
        },
        onClick = {
            viewModel.managePlatforms(
                platform = platform
            )
        }
    )
}