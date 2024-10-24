@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.ametista.ui.screens.account

import ametista.composeapp.generated.resources.Res
import ametista.composeapp.generated.resources.about_me
import ametista.composeapp.generated.resources.session
import ametista.composeapp.generated.resources.session_members
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
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.tecknobit.ametista.localUser
import com.tecknobit.ametista.ui.screens.AmetistaScreen
import com.tecknobit.ametista.ui.screens.account.SessionScreenViewModel.SessionScreenSection
import com.tecknobit.ametista.ui.screens.account.SessionScreenViewModel.SessionScreenSection.ABOUT_ME
import com.tecknobit.ametista.ui.screens.account.SessionScreenViewModel.SessionScreenSection.MEMBERS
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

class SessionScreen : AmetistaScreen<SessionScreenViewModel>(
    viewModel = SessionScreenViewModel()
) {

    /**
     * Function to arrange the content of the screen to display
     *
     * No-any params required
     */
    @Composable
    override fun ArrangeScreenContent() {
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
                            text = stringResource(Res.string.session)
                        )
                    }
                )
            },
            floatingActionButton = {
                AnimatedVisibility(
                    visible = viewModel!!.sessionScreenSection.value == MEMBERS && localUser.isAdmin(),
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    FloatingActionButton(
                        onClick = {
                            // TODO: TO ADD NEW VIEWER
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.PersonAdd,
                            contentDescription = null
                        )
                    }
                }
            }
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

    @Composable
    @NonRestartableComposable
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
                    selected = viewModel!!.sessionScreenSection.value == section,
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
                    onClick = { viewModel!!.sessionScreenSection.value = section }
                )
            }
        }
    }

    private fun SessionScreenSection.icon(): ImageVector {
        return when (this) {
            ABOUT_ME -> Icons.Default.AssignmentInd
            MEMBERS -> Icons.Default.Groups3
        }
    }

    private fun SessionScreenSection.tabTitle(): StringResource {
        return when (this) {
            ABOUT_ME -> Res.string.about_me
            MEMBERS -> Res.string.session_members
        }
    }

    @Composable
    @NonRestartableComposable
    private fun DisplaySection() {
        AnimatedVisibility(
            visible = viewModel!!.sessionScreenSection.value == ABOUT_ME
        ) {
            AboutMe(
                screenViewModel = viewModel!!
            )
        }
        AnimatedVisibility(
            visible = viewModel!!.sessionScreenSection.value == MEMBERS
        ) {
            SessionMembers(
                screenViewModel = viewModel!!
            )
        }
    }

    /**
     * Function to collect or instantiate the states of the screen
     *
     * No-any params required
     */
    @Composable
    override fun CollectStates() {
        viewModel!!.sessionScreenSection = remember { mutableStateOf(ABOUT_ME) }
    }

}