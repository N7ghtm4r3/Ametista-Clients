package com.tecknobit.ametista.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Downloading
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tecknobit.ametista.navigator
import com.tecknobit.equinox.environment.records.EquinoxItem
import com.tecknobit.equinoxcompose.components.EmptyListUI
import com.tecknobit.equinoxcompose.session.screens.EquinoxScreen
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.annotations.Structure

/**
 * The [AmetistaScreen] class is useful to provides the basic behavior of a Ametista's UI screen
 *
 * @param viewModel The support viewmodel for the screen
 *
 * @property V generic type of the viewmodel of the screen
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see EquinoxScreen
 */
@Structure
abstract class AmetistaScreen<V : EquinoxViewModel>(
    viewModel: V? = null
) : EquinoxScreen<V>(
    viewModel = viewModel
) {

    companion object {

        /**
         * **SPLASH_SCREEN** -> route to navigate to the [com.tecknobit.ametista.ui.screens.navigation.Splashscreen]
         */
        const val SPLASHSCREEN = "Splashscreen"

        /**
         * **AUTH_SCREEN** -> route to navigate to the [com.tecknobit.ametista.ui.screens.auth.AuthScreen]
         */
        const val AUTH_SCREEN = "AuthScreen"

        /**
         * **CHANGE_VIEWER_PASSWORD_SCREEN** -> route to navigate to the [com.tecknobit.ametista.ui.screens.changeviewerpassword.ChangeViewerPasswordScreen]
         */
        const val CHANGE_VIEWER_PASSWORD_SCREEN = "ChangeViewerPasswordScreen"

        /**
         * **SESSION_SCREEN** -> route to navigate to the [com.tecknobit.ametista.ui.screens.session.SessionScreen]
         */
        const val SESSION_SCREEN = "SessionScreen"

        /**
         * **APPLICATIONS_SCREEN** -> route to navigate to the [com.tecknobit.ametista.ui.screens.applications.ApplicationsScreen]
         */
        const val APPLICATIONS_SCREEN = "ApplicationsScreen"

        /**
         * **APPLICATION_SCREEN** -> route to navigate to the [com.tecknobit.ametista.ui.screens.application.ApplicationScreen]
         */
        const val APPLICATION_SCREEN = "ApplicationScreen"

        /**
         * **PLATFORM_SCREEN** -> route to navigate to the [com.tecknobit.ametista.ui.screens.platform.PlatformScreen]
         */
        const val PLATFORM_SCREEN = "PlatformScreen"

        /**
         * **CONTAINER_MAX_WIDTH** -> max value of the width for the container
         */
        @JvmStatic
        val CONTAINER_MAX_WIDTH = 1200.dp

    }

    /**
     * Back navigation button
     */
    @Composable
    @NonRestartableComposable
    protected fun NavButton() {
        IconButton(
            onClick = { navigator.goBack() }
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = null,
                tint = Color.White
            )
        }
    }

    /**
     * Loading layout component to show when the data are loading
     *
     * @param data The data to wait the load
     */
    @Composable
    @NonRestartableComposable
    @Deprecated(
        "JUST FOR TESTING WILL BE INTEGRATED IN THE OFFICIAL EQUINOX-COMPOSE LIBRARY",
        ReplaceWith("The official component")
    )
    protected fun LoadingData(
        data: EquinoxItem?
    ) {
        AnimatedVisibility(
            visible = data == null,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Surface {
                EmptyListUI(
                    icon = Icons.Default.Downloading,
                    subText = "Loading data"
                )
            }
        }
    }

}