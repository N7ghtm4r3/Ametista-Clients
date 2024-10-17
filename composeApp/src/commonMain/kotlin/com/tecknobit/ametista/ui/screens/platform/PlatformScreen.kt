@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.ametista.ui.screens.platform

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.tecknobit.ametista.displayFontFamily
import com.tecknobit.ametista.helpers.Theme
import com.tecknobit.ametista.ui.screens.AmetistaScreen
import com.tecknobit.ametistacore.models.AmetistaApplication
import com.tecknobit.ametistacore.models.AmetistaApplication.Platform
import com.tecknobit.equinoxcompose.helpers.session.ManagedContent

class PlatformScreen(
    initialApplication: AmetistaApplication,
    private val platform: Platform
) : AmetistaScreen<PlatformScreenViewModel>(
    viewModel = PlatformScreenViewModel(
        initialApplication = initialApplication
    )
) {

    private lateinit var application: State<AmetistaApplication>

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
                        }
                    ) {

                    }
                }
            )
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel!!.refreshApplication()
    }

    /**
     * Function to collect or instantiate the states of the screen
     *
     * No-any params required
     */
    @Composable
    override fun CollectStates() {
        application = viewModel!!.application.collectAsState()
    }

}