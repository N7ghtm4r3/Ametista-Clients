@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.ametista.ui.screens.applications

import ametista.composeapp.generated.resources.Res
import ametista.composeapp.generated.resources.applications
import ametista.composeapp.generated.resources.search_placeholder
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.DesktopWindows
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.FilterListOff
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tecknobit.ametista.ui.icons.Globe
import com.tecknobit.ametista.ui.icons.Ios
import com.tecknobit.ametista.ui.screens.AmetistaScreen
import com.tecknobit.ametistacore.models.AmetistaApplication.Platform
import com.tecknobit.ametistacore.models.AmetistaApplication.Platform.ANDROID
import com.tecknobit.ametistacore.models.AmetistaApplication.Platform.DESKTOP
import com.tecknobit.ametistacore.models.AmetistaApplication.Platform.IOS
import com.tecknobit.ametistacore.models.AmetistaApplication.Platform.WEB
import com.tecknobit.equinoxcompose.components.EquinoxOutlinedTextField
import com.tecknobit.equinoxcompose.helpers.session.ManagedContent
import org.jetbrains.compose.resources.stringResource

class ApplicationsScreen : AmetistaScreen<ApplicationsScreenViewModel>(
    viewModel = ApplicationsScreenViewModel()
) {

    /**
     * Function to arrange the content of the screen to display
     *
     * No-any params required
     */
    @Composable
    override fun ArrangeScreenContent() {
        CloseApplicationOnNavBack()
        ManagedContent(
            viewModel = viewModel!!,
            content = {
                Scaffold(
                    topBar = {
                        LargeTopAppBar(
                            colors = TopAppBarDefaults.largeTopAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            ),
                            title = {
                                Text(
                                    text = stringResource(Res.string.applications),
                                    color = contentColorFor(MaterialTheme.colorScheme.primaryContainer)
                                )
                            }
                        )
                    },
                    snackbarHost = {
                        SnackbarHost(
                            hostState = viewModel!!.snackbarHostState!!
                        )
                    },
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = { viewModel!!.workOnApplication.value = true }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null
                            )
                        }
                        WorkOnApplication(
                            show = viewModel!!.workOnApplication,
                            viewModel = viewModel!!
                        )
                    }
                ) { paddingValues ->
                    Column {
                        FiltersSection(
                            paddingValues = paddingValues
                        )
                        Applications(
                            viewModel = viewModel!!
                        )
                    }
                }
            }
        )
    }

    @Composable
    @NonRestartableComposable
    private fun FiltersSection(
        paddingValues: PaddingValues
    ) {
        Column (
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row (
                modifier = Modifier
                    .padding(
                        top = paddingValues.calculateTopPadding()
                    )
                    .padding(
                        all = 16.dp
                    )
                    .widthIn(
                        max = 800.dp
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                SearchBar(
                    modifier = Modifier
                        .weight(2f)
                )
                PlatformsMenu(
                    modifier = Modifier
                        .weight(1f)
                )
            }
        }
    }

    @Composable
    @NonRestartableComposable
    private fun SearchBar(
        modifier: Modifier
    ) {
        EquinoxOutlinedTextField(
            modifier = modifier,
            outlinedTextFieldColors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primaryContainer
            ),
            value = viewModel!!.filterQuery,
            placeholder = Res.string.search_placeholder,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null
                )
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            )
        )
    }

    @Composable
    @NonRestartableComposable
    private fun PlatformsMenu(
        modifier: Modifier
    ) {
        var expanded by remember { mutableStateOf(false) }
        Column (
            modifier = modifier
                .padding(
                    start = 16.dp
                ),
            horizontalAlignment = Alignment.End
        ) {
            Row {
                IconButton(
                    onClick = {
                        expanded = false
                        viewModel!!.clearFilters()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.FilterListOff,
                        contentDescription = null
                    )
                }
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
                                platform = platform
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    @NonRestartableComposable
    private fun PlatformItem(
        platform: Platform
    ) {
        DropdownMenuItem(
            text = {
                Row (
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
                        checked = viewModel!!.platformsFilter.contains(platform),
                        onCheckedChange = { checked ->
                            viewModel!!.managePlatforms(
                                checked = checked,
                                platform = platform
                            )
                        }
                    )
                }
            },
            onClick = {
                viewModel!!.managePlatforms(
                    platform = platform
                )
            }
        )
    }

    private fun Platform.icon() : ImageVector {
        return when(this) {
            ANDROID -> Icons.Default.Android
            IOS -> Ios
            DESKTOP -> Icons.Default.DesktopWindows
            WEB -> Globe
        }
    }

    /**
     * Function to collect or instantiate the states of the screen
     *
     * No-any params required
     */
    @Composable
    override fun CollectStates() {
        viewModel!!.filterQuery = remember { mutableStateOf("") }
        viewModel!!.platformsFilter = remember { mutableStateListOf() }
        viewModel!!.workOnApplication = remember { mutableStateOf(false) }
    }

}