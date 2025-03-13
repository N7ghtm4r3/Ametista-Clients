@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMultiplatform::class)

package com.tecknobit.ametista.ui.screens.applications.presenter

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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.tecknobit.ametista.CloseApplicationOnNavBack
import com.tecknobit.ametista.localUser
import com.tecknobit.ametista.ui.screens.applications.components.Applications
import com.tecknobit.ametista.ui.screens.applications.components.PlatformsMenu
import com.tecknobit.ametista.ui.screens.applications.components.ProfilePic
import com.tecknobit.ametista.ui.screens.applications.presentation.ApplicationsScreenViewModel
import com.tecknobit.ametista.ui.theme.AmetistaTheme
import com.tecknobit.equinoxcompose.components.DebouncedOutlinedTextField
import com.tecknobit.equinoxcompose.session.ManagedContent
import com.tecknobit.equinoxcompose.session.screens.EquinoxScreen
import org.jetbrains.compose.resources.stringResource

/**
 * The [ApplicationsScreen] class is used to display the list of Ametista's application registered by
 * the system
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see EquinoxScreen
 * 
 */
class ApplicationsScreen : EquinoxScreen<ApplicationsScreenViewModel>(
    viewModel = ApplicationsScreenViewModel()
) {

    /**
     * Method to arrange the content of the screen to display
     */
    @Composable
    override fun ArrangeScreenContent() {
        AmetistaTheme {
            CloseApplicationOnNavBack()
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
                                title = {
                                    Text(
                                        text = stringResource(Res.string.applications)
                                    )
                                },
                                actions = { ProfilePic() }
                            )
                        },
                        snackbarHost = {
                            SnackbarHost(
                                hostState = viewModel.snackbarHostState!!
                            )
                        },
                        floatingActionButton = {
                            if (localUser.isAdmin()) {
                                FloatingActionButton(
                                    onClick = { viewModel.workOnApplication.value = true }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = null
                                    )
                                }
                                // TODO: TO SET
                                /*WorkOnApplication(
                                    show = viewModel.workOnApplication,
                                    viewModel = viewModel
                                )*/
                            }
                        }
                    ) { paddingValues ->
                        Column {
                            FiltersSection(
                                paddingValues = paddingValues
                            )
                            Applications(
                                viewModel = viewModel
                            )
                        }
                    }
                }
            )
        }
    }

    /**
     * Section dedicated to the filters section allows the user to filter the list of the applications
     * fetched from the server
     *
     * @param paddingValues The padding to apply to the section
     */
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
                        max = MAX_CONTAINER_WIDTH
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
                        .weight(1f),
                    viewModel = viewModel
                )
            }
        }
    }

    /**
     * The search bar used to type the name to filter the applications list
     *
     * @param modifier The modifier to apply to the component
     */
    @Composable
    @NonRestartableComposable
    private fun SearchBar(
        modifier: Modifier
    ) {
        DebouncedOutlinedTextField(
            modifier = modifier,
            value = viewModel.filterQuery,
            debounce = { viewModel.applicationsState.refresh() },
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

    /**
     * Method to collect or instantiate the states of the screen
     */
    @Composable
    override fun CollectStates() {
        viewModel.filterQuery = remember { mutableStateOf("") }
        viewModel.platformsFilter = remember { mutableStateListOf() }
        viewModel.workOnApplication = remember { mutableStateOf(false) }
    }

}