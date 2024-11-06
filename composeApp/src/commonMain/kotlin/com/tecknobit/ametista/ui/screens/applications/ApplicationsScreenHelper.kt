@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.ametista.ui.screens.applications

import ametista.composeapp.generated.resources.Res.string
import ametista.composeapp.generated.resources.no_applications
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.ametista.bodyFontFamily
import com.tecknobit.ametista.displayFontFamily
import com.tecknobit.ametista.localUser
import com.tecknobit.ametista.navigator
import com.tecknobit.ametista.ui.icons.Boxes
import com.tecknobit.ametista.ui.screens.AmetistaScreen.Companion.APPLICATION_SCREEN
import com.tecknobit.ametistacore.models.AmetistaApplication
import com.tecknobit.equinoxcompose.components.EmptyListUI

@Composable
@NonRestartableComposable
expect fun Applications(
    viewModel: ApplicationsScreenViewModel
)

@Composable
@NonRestartableComposable
fun NoApplications(
    noApplications: Boolean
) {
    AnimatedVisibility(
        visible = noApplications,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        EmptyListUI(
            imageModifier = Modifier
                .size(150.dp),
            icon = Boxes,
            subText = string.no_applications,
            textStyle = TextStyle(
                fontFamily = displayFontFamily,
                fontSize = 20.sp
            )
        )
    }
}

@Composable
@NonRestartableComposable
expect fun ApplicationItem(
    isTheFirst: Boolean = false,
    application: AmetistaApplication,
    viewModel: ApplicationsScreenViewModel
)

fun getApplicationIconCompleteUrl(
    application: AmetistaApplication
): String {
    return localUser.hostAddress + "/" + application.icon
}

fun navToApplicationScreen(
    application: AmetistaApplication
) {
    navigator.navigate("$APPLICATION_SCREEN/${application.id}")
}

@Composable
@NonRestartableComposable
expect fun ApplicationIcon(
    modifier: Modifier = Modifier,
    application: AmetistaApplication
)

@Composable
@NonRestartableComposable
fun ExpandApplicationDescription(
    expand: MutableState<Boolean>,
    application: AmetistaApplication
) {
    if(expand.value) {
        ModalBottomSheet(
            onDismissRequest = { expand.value = false }
        ) {
            Column (
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    modifier = Modifier
                        .padding(
                            start = 16.dp
                        ),
                    text = application.name,
                    fontFamily = displayFontFamily,
                    fontSize = 20.sp
                )
                HorizontalDivider()
                Text(
                    modifier = Modifier
                        .padding(
                            horizontal = 16.dp
                        )
                        .verticalScroll(rememberScrollState()),
                    text = application.description,
                    textAlign = TextAlign.Justify,
                    fontFamily = bodyFontFamily,
                    fontSize = 14.sp
                )
            }
        }
    }
}