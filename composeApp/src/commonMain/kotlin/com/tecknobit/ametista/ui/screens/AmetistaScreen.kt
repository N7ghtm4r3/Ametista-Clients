package com.tecknobit.ametista.ui.screens

import com.tecknobit.apimanager.annotations.Structure
import com.tecknobit.equinoxcompose.helpers.session.EquinoxScreen
import com.tecknobit.equinoxcompose.helpers.viewmodels.EquinoxViewModel

@Structure
abstract class AmetistaScreen<V : EquinoxViewModel>(
    viewModel: V? = null
) : EquinoxScreen<V>(
    viewModel = viewModel
) {

    companion object {

        const val SPLASHSCREEN = "Splashscreen"

        const val APPLICATIONS_SCREEN = "ApplicationsScreen"

        const val APPLICATION_SCREEN = "ApplicationScreen"

    }

}