@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.ametista.ui.screens.applications

import ametista.composeapp.generated.resources.Res.string
import ametista.composeapp.generated.resources.confirm
import ametista.composeapp.generated.resources.delete_application_text
import ametista.composeapp.generated.resources.delete_application_title
import ametista.composeapp.generated.resources.dismiss
import ametista.composeapp.generated.resources.no_applications
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Delete
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
import com.tecknobit.ametistacore.models.AmetistaApplication
import com.tecknobit.equinoxcompose.components.EmptyListUI
import com.tecknobit.equinoxcompose.components.EquinoxAlertDialog
import org.jetbrains.compose.resources.stringResource

@Composable
@NonRestartableComposable
expect fun Applications(
    viewModel: ApplicationsScreenViewModel
)

@Composable
@NonRestartableComposable
fun NoApplications() {
    EmptyListUI(
        icon = Icons.Default.Cancel,
        subText = string.no_applications,
        textStyle = TextStyle(
            fontFamily = displayFontFamily
        )
    )
}

@Composable
@NonRestartableComposable
expect fun ApplicationItem(
    isTheFirst: Boolean = false,
    application: AmetistaApplication,
    viewModel: ApplicationsScreenViewModel
)

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
                        ),
                    text = application.description,
                    textAlign = TextAlign.Justify,
                    fontFamily = bodyFontFamily,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
@NonRestartableComposable
fun DeleteApplication(
    show: MutableState<Boolean>,
    application: AmetistaApplication,
    viewModel: ApplicationsScreenViewModel
) {
    EquinoxAlertDialog(
        modifier = Modifier
            .widthIn(
                max = 400.dp
            ),
        show = show,
        viewModel = viewModel,
        icon = Icons.Default.Delete,
        title = stringResource(string.delete_application_title, application.name),
        text = stringResource(string.delete_application_text),
        dismissText = stringResource(string.dismiss),
        confirmText = stringResource(string.confirm),
        confirmAction = {
            // TODO: MAKE THE REQUEST THEN
            show.value = false
            viewModel.restartRefresher()
        }
    )
}