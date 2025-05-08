@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.ametista.ui.screens.session.presenter

import ametista.composeapp.generated.resources.Res.string
import ametista.composeapp.generated.resources.about_me
import ametista.composeapp.generated.resources.session
import ametista.composeapp.generated.resources.session_members
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentInd
import androidx.compose.material.icons.filled.Groups3
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.tecknobit.ametista.localUser
import com.tecknobit.ametista.ui.components.NavButton
import com.tecknobit.ametista.ui.screens.session.components.AboutMe
import com.tecknobit.ametista.ui.screens.session.components.AddViewer
import com.tecknobit.ametista.ui.screens.session.components.SessionMembers
import com.tecknobit.ametista.ui.screens.session.presentation.SessionScreenViewModel
import com.tecknobit.ametista.ui.screens.session.presentation.SessionScreenViewModel.SessionScreenSection
import com.tecknobit.ametista.ui.screens.session.presentation.SessionScreenViewModel.SessionScreenSection.ABOUT_ME
import com.tecknobit.ametista.ui.screens.session.presentation.SessionScreenViewModel.SessionScreenSection.MEMBERS
import com.tecknobit.ametista.ui.theme.AmetistaTheme
import com.tecknobit.equinoxcompose.annotations.ScreenSection
import com.tecknobit.equinoxcompose.session.screens.EquinoxScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

/**
 * The [SessionScreen] class is used to display the details about the current session
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see EquinoxScreen
 *
 */
class SessionScreen : EquinoxScreen<SessionScreenViewModel>(
    viewModel = SessionScreenViewModel()
) {

    private lateinit var state: SheetState

    private lateinit var scope: CoroutineScope

    /**
     * Method to arrange the content of the screen to display
     */
    @Composable
    override fun ArrangeScreenContent() {
        AmetistaTheme {
            Scaffold(
                topBar = {
                    LargeTopAppBar(
                        colors = TopAppBarDefaults.largeTopAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            titleContentColor = Color.White
                        ),
                        navigationIcon = { NavButton() },
                        title = {
                            Text(
                                text = stringResource(string.session)
                            )
                        }
                    )
                },
                floatingActionButton = {
                    AnimatedVisibility(
                        visible = viewModel.sessionScreenSection.value == MEMBERS && localUser.isAdmin(),
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        FloatingActionButton(
                            onClick = {
                                scope.launch {
                                    state.show()
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.PersonAdd,
                                contentDescription = null
                            )
                        }
                        AddViewer(
                            viewModel = viewModel,
                            state = state,
                            scope = scope
                        )
                    }
                },
                snackbarHost = { SnackbarHost(viewModel.snackbarHostState!!) }
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .padding(
                            top = paddingValues.calculateTopPadding() - 5.dp,
                            bottom = paddingValues.calculateBottomPadding() + 16.dp
                        )
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    SectionSelector()
                    DisplaySection()
                }
            }
        }
    }

    /**
     * The selector used to display the specific session section
     */
    @Composable
    private fun SectionSelector() {
        SingleChoiceSegmentedButtonRow(
            modifier = Modifier
                .padding(
                    bottom = 10.dp
                )
        ) {
            val lastEntry = SessionScreenSection.entries.last()
            SessionScreenSection.entries.forEach { section ->
                SegmentedButton(
                    selected = viewModel.sessionScreenSection.value == section,
                    icon = {
                        Icon(
                            imageVector = section.icon(),
                            contentDescription = null
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(section.tabTitle())
                        )
                    },
                    shape = if (section == lastEntry) {
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
                    onClick = { viewModel.sessionScreenSection.value = section }
                )
            }
        }
    }

    /**
     * Method to get the representative icon for the session section
     *
     * @return the representative icon as [ImageVector]
     */
    private fun SessionScreenSection.icon(): ImageVector {
        return when (this) {
            ABOUT_ME -> Icons.Default.AssignmentInd
            MEMBERS -> Icons.Default.Groups3
        }
    }

    /**
     * Method to get the representative title for the session section
     *
     * @return the representative title as [ImageVector]
     */
    private fun SessionScreenSection.tabTitle(): StringResource {
        return when (this) {
            ABOUT_ME -> string.about_me
            MEMBERS -> string.session_members
        }
    }

    /**
     * Wrapper method to display the specific session section
     */
    @Composable
    @ScreenSection
    private fun DisplaySection() {
        AnimatedContent(
            targetState = viewModel.sessionScreenSection
        ) { section ->
            when (section.value) {
                ABOUT_ME -> {
                    AboutMe(
                        viewModel = viewModel
                    )
                }

                MEMBERS -> {
                    SessionMembers(
                        viewModel = viewModel
                    )
                }
            }
        }
    }

    /**
     * Method to collect or instantiate the states of the screen
     */
    @Composable
    override fun CollectStates() {
        viewModel.sessionScreenSection = remember { mutableStateOf(ABOUT_ME) }
        state = rememberModalBottomSheetState(
            skipPartiallyExpanded = true
        )
        scope = rememberCoroutineScope()
    }

}