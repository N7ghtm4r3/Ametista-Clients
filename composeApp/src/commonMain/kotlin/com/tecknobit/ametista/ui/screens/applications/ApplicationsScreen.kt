@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.ametista.ui.screens.applications

import ametista.composeapp.generated.resources.Res
import ametista.composeapp.generated.resources.applications
import ametista.composeapp.generated.resources.search_placeholder
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.tecknobit.ametista.CloseApplicationOnNavBack
import com.tecknobit.ametista.getCurrentWidthSizeClass
import com.tecknobit.ametista.helpers.icon
import com.tecknobit.ametista.imageLoader
import com.tecknobit.ametista.localUser
import com.tecknobit.ametista.navigator
import com.tecknobit.ametista.ui.components.WorkOnApplication
import com.tecknobit.ametista.ui.screens.AmetistaScreen
import com.tecknobit.ametistacore.models.Platform
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
                                containerColor = MaterialTheme.colorScheme.primary,
                                titleContentColor = Color.White
                            ),
                            title = {
                                Text(
                                    text = stringResource(Res.string.applications)
                                )
                            },
                            actions = {
                                val size = when (getCurrentWidthSizeClass()) {
                                    WindowWidthSizeClass.Expanded -> 55.dp
                                    else -> 65.dp
                                }
                                AsyncImage(
                                    modifier = Modifier
                                        .border(
                                            width = 1.dp,
                                            color = Color.White,
                                            shape = CircleShape
                                        )
                                        .size(size)
                                        .clip(CircleShape)
                                        .clickable { navigator.navigate(SESSION_SCREEN) },
                                    model = ImageRequest.Builder(LocalPlatformContext.current)
                                        .data(localUser.profilePic)
                                        .crossfade(true)
                                        .crossfade(500)
                                        .build(),
                                    imageLoader = imageLoader,
                                    contentDescription = "Application icon",
                                    contentScale = ContentScale.Crop
                                    // TODO: TO SET ERROR
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
                        max = CONTAINER_MAX_WIDTH
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

    /**
     * Function to collect or instantiate the states of the screen
     *
     * No-any params required
     */
    @Composable
    override fun CollectStates() {
        viewModel!!.filterQuery = remember { mutableStateOf("") }
        viewModel!!.platformsFilter = remember { Platform.entries.toMutableStateList() }
        viewModel!!.workOnApplication = remember { mutableStateOf(false) }
    }

}