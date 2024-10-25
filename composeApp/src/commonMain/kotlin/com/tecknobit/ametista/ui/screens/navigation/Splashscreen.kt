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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.ametista.localUser
import com.tecknobit.ametista.navigator
import com.tecknobit.ametista.ui.screens.AmetistaScreen
import com.tecknobit.ametistacore.models.AmetistaUser.DEFAULT_VIEWER_PASSWORD
import com.tecknobit.equinoxcompose.helpers.viewmodels.EquinoxViewModel
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource

class Splashscreen: AmetistaScreen<EquinoxViewModel>() {

    @Composable
    override fun ArrangeScreenContent() {
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

        // TODO: TO REMOVE
        LaunchedEffect(true) {
            delay(250)
            // TODO: MAKE THE REAL NAVIGATION (INCLUDE THE ROUTE TO CHANGE THE VIEWER PASSWORD ALSO)
            val route = if (localUser.userId == null)
                AUTH_SCREEN
            else if (localUser.password == DEFAULT_VIEWER_PASSWORD)
                CHANGE_VIEWER_PASSWORD_SCREEN
            else
                APPLICATIONS_SCREEN
            navigator.navigate(route)
        }
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