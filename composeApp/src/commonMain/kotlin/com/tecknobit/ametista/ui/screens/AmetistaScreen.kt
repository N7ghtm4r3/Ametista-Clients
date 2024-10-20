package com.tecknobit.ametista.ui.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.unit.dp
import com.tecknobit.ametista.navigator
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

        const val PLATFORM_SCREEN = "PlatformScreen"

        @JvmStatic
        val CONTAINER_MAX_WIDTH = 1200.dp

    }

    @Composable
    @NonRestartableComposable
    protected fun NavButton() {
        IconButton(
            onClick = { navigator.goBack() }
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = null
            )
        }
    }

}