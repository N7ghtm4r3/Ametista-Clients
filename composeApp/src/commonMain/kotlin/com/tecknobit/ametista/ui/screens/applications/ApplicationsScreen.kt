package com.tecknobit.ametista.ui.screens.applications

import androidx.compose.runtime.Composable
import com.tecknobit.ametista.ui.screens.AmetistaScreen

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

    }

    /**
     * Function to collect or instantiate the states of the screen
     *
     * No-any params required
     */
    @Composable
    override fun CollectStates() {
    }

}