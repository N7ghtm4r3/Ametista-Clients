package com.tecknobit.ametista.ui.components

import ametista.composeapp.generated.resources.Res.string
import ametista.composeapp.generated.resources.confirm
import ametista.composeapp.generated.resources.delete_application_text
import ametista.composeapp.generated.resources.delete_application_title
import ametista.composeapp.generated.resources.dismiss
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecknobit.ametista.ui.screens.applications.data.AmetistaApplication
import com.tecknobit.ametista.ui.screens.shared.presentations.ApplicationViewModel
import com.tecknobit.equinoxcompose.components.EquinoxAlertDialog
import org.jetbrains.compose.resources.stringResource

/**
 * The [EquinoxAlertDialog] to warn the user about the application deletion
 *
 * @param show whether the layout is visible or not
 * @param application The application to delete
 * @param viewModel The viewmodel related to the [com.tecknobit.ametista.ui.screens.application.presenter.ApplicationScreen]
 * @param onDelete The action to execute when the application has been deleted
 */
@Composable
@NonRestartableComposable
fun DeleteApplication(
    show: MutableState<Boolean>,
    application: AmetistaApplication,
    viewModel: ApplicationViewModel,
    onDelete: () -> Unit,
) {
    EquinoxAlertDialog(
        modifier = Modifier
            .widthIn(
                max = 400.dp
            ),
        show = show,
        viewModel = viewModel,
        icon = Icons.Default.Delete,
        title = stringResource(
            resource = string.delete_application_title,
            application.name
        ),
        text = stringResource(string.delete_application_text),
        dismissText = stringResource(string.dismiss),
        confirmText = stringResource(string.confirm),
        confirmAction = {
            viewModel.deleteApplication(
                application = application,
                onDelete = {
                    show.value = false
                    onDelete.invoke()
                }
            )
        }
    )
}