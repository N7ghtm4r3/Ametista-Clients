package com.tecknobit.ametista.ui.screens.navigation

import ametista.composeapp.generated.resources.Res
import ametista.composeapp.generated.resources.app_name
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.ametista.CheckForUpdatesAndLaunch
import com.tecknobit.ametista.ui.screens.AmetistaScreen
import com.tecknobit.ametista.ui.theme.AmetistaTheme
import com.tecknobit.equinoxcompose.session.screens.EquinoxNoModelScreen
import org.jetbrains.compose.resources.stringResource

/**
 * The [Splashscreen] class is used to retrieve and load the session data and enter the application's workflow
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see EquinoxScreen
 * @see AmetistaScreen
 */
class Splashscreen : EquinoxNoModelScreen() {

    /**
     * Method to arrange the content of the screen to display
     */
    @Composable
    override fun ArrangeScreenContent() {
        AmetistaTheme {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = MaterialTheme.colorScheme.primary
                    )
                    .padding(
                        bottom = 16.dp
                    )
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Text(
                        text = stringResource(Res.string.app_name),
                        fontSize = 40.sp,
                        color = Color.White
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Text(
                        text = "by Tecknobit",
                        color = Color.White
                    )
                }
            }
            CheckForUpdatesAndLaunch()
        }
    }

    /**
     * Function to collect or instantiate the states of the screen
     */
    @Composable
    override fun CollectStates() {
    }

}