package com.tecknobit.ametista.ui.components

import ametista.composeapp.generated.resources.Res
import ametista.composeapp.generated.resources.Res.string
import ametista.composeapp.generated.resources.confirm
import ametista.composeapp.generated.resources.delete_account
import ametista.composeapp.generated.resources.delete_application_text
import ametista.composeapp.generated.resources.delete_application_title
import ametista.composeapp.generated.resources.delete_message
import ametista.composeapp.generated.resources.dismiss
import ametista.composeapp.generated.resources.logout
import ametista.composeapp.generated.resources.logout_message
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.ametista.SPLASHSCREEN
import com.tecknobit.ametista.displayFontFamily
import com.tecknobit.ametista.navigator
import com.tecknobit.ametista.ui.screens.applications.data.AmetistaApplication
import com.tecknobit.ametista.ui.screens.session.presentation.SessionScreenViewModel
import com.tecknobit.ametista.ui.screens.shared.presentations.ApplicationViewModel
import com.tecknobit.equinoxcompose.components.EquinoxAlertDialog
import org.jetbrains.compose.resources.stringResource

/**
 * `titleStyle` the style to apply to the title of the [EquinoxAlertDialog]
 */
val titleStyle = TextStyle(
    fontFamily = displayFontFamily,
    fontSize = 20.sp
)

/**
 * The [EquinoxAlertDialog] to warn the user about the application deletion
 *
 * @param show whether the layout is visible or not
 * @param application The application to delete
 * @param viewModel The viewmodel related to the [com.tecknobit.ametista.ui.screens.application.presenter.ApplicationScreen]
 * @param onDelete The action to execute when the application has been deleted
 */
@Composable
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

/**
 * Alert to warn about the logout action
 *
 * @param viewModel The support viewmodel for the screen
 * @param show Whether the alert is shown
 */
@Composable
fun Logout(
    viewModel: SessionScreenViewModel,
    show: MutableState<Boolean>,
) {
    EquinoxAlertDialog(
        icon = Icons.AutoMirrored.Filled.Logout,
        modifier = Modifier
            .widthIn(
                max = 400.dp
            ),
        viewModel = viewModel,
        show = show,
        title = Res.string.logout,
        titleStyle = titleStyle,
        text = Res.string.logout_message,
        confirmAction = {
            viewModel.clearSession {
                show.value = false
                navigator.navigate(SPLASHSCREEN)
            }
        }
    )
}

/**
 * Alert to warn about the account deletion
 *
 * @param viewModel The support viewmodel for the screen
 * @param show Whether the alert is shown
 */
@Composable
fun DeleteAccount(
    viewModel: SessionScreenViewModel,
    show: MutableState<Boolean>,
) {
    EquinoxAlertDialog(
        icon = Icons.Default.Delete,
        modifier = Modifier
            .widthIn(
                max = 400.dp
            ),
        viewModel = viewModel,
        show = show,
        title = Res.string.delete_account,
        titleStyle = titleStyle,
        text = Res.string.delete_message,
        confirmAction = {
            viewModel.deleteAccount(
                onDelete = {
                    show.value = false
                    navigator.navigate(SPLASHSCREEN)
                }
            )
        }
    )
}