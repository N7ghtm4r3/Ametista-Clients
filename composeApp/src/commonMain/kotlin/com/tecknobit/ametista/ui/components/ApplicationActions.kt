package com.tecknobit.ametista.ui.components

import ametista.composeapp.generated.resources.Res.string
import ametista.composeapp.generated.resources.add_application
import ametista.composeapp.generated.resources.app_description
import ametista.composeapp.generated.resources.app_name_field
import ametista.composeapp.generated.resources.confirm
import ametista.composeapp.generated.resources.delete_application_text
import ametista.composeapp.generated.resources.delete_application_title
import ametista.composeapp.generated.resources.dismiss
import ametista.composeapp.generated.resources.edit_application
import ametista.composeapp.generated.resources.wrong_app_description
import ametista.composeapp.generated.resources.wrong_app_name_field
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.tecknobit.ametista.displayFontFamily
import com.tecknobit.ametista.helpers.getAppIconPath
import com.tecknobit.ametista.imageLoader
import com.tecknobit.ametista.ui.screens.applications.ApplicationsScreenViewModel
import com.tecknobit.ametista.ui.sharedviewmodels.ApplicationViewModel
import com.tecknobit.ametistacore.helpers.AmetistaValidator.isAppDescriptionValid
import com.tecknobit.ametistacore.helpers.AmetistaValidator.isAppNameValid
import com.tecknobit.ametistacore.models.AmetistaApplication
import com.tecknobit.equinoxcompose.components.EquinoxAlertDialog
import com.tecknobit.equinoxcompose.components.EquinoxOutlinedTextField
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.core.PickerMode
import io.github.vinceglb.filekit.core.PickerType
import org.jetbrains.compose.resources.stringResource

@Composable
@NonRestartableComposable
fun WorkOnApplication(
    show: MutableState<Boolean>,
    viewModel: ApplicationViewModel,
    application: AmetistaApplication? = null,
) {
    if (show.value) {
        val isInEditMode = application != null
        val closeDialog = {
            show.value = false
        }
        viewModel.appIcon = remember {
            mutableStateOf(
                if (isInEditMode)
                    application!!.icon
                else
                    ""
            )
        }
        val primaryContainer = MaterialTheme.colorScheme.primaryContainer
        viewModel.appIconBorderColor = remember { mutableStateOf(primaryContainer) }
        viewModel.appName = remember {
            mutableStateOf(
                if (isInEditMode)
                    application!!.name
                else
                    ""
            )
        }
        viewModel.appNameError = remember { mutableStateOf(false) }
        viewModel.appDescription = remember {
            mutableStateOf(
                if (isInEditMode)
                    application!!.description
                else
                    ""
            )
        }
        viewModel.appDescriptionError = remember { mutableStateOf(false) }
        viewModel.suspendRefresher()
        Dialog(
            onDismissRequest = closeDialog,
            properties = DialogProperties(
                dismissOnBackPress = true
            )
        ) {
            Surface(
                modifier = Modifier
                    .width(400.dp)
                    .heightIn(
                        max = 550.dp
                    ),
                shape = RoundedCornerShape(
                    size = 15.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(
                            horizontal = 16.dp
                        )
                        .padding(
                            top = 16.dp,
                            bottom = 10.dp
                        )
                ) {
                    DialogTitle(
                        closeDialog = closeDialog,
                        isInEditMode = isInEditMode
                    )
                    DialogContent(
                        viewModel = viewModel,
                        primaryContainer = primaryContainer
                    )
                    DialogActions(
                        closeDialog = closeDialog,
                        viewModel = viewModel,
                        application = application
                    )
                }
            }
        }
    }
}

@Composable
@NonRestartableComposable
private fun DialogTitle(
    closeDialog: () -> Unit,
    isInEditMode: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = closeDialog
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = null
            )
        }
        Text(
            text = stringResource(
                if (isInEditMode)
                    string.edit_application
                else
                    string.add_application
            ),
            fontFamily = displayFontFamily,
            fontSize = 20.sp
        )
    }
}

@Composable
@NonRestartableComposable
private fun DialogContent(
    viewModel: ApplicationViewModel,
    primaryContainer: Color
) {
    Column(
        modifier = Modifier
            .padding(
                horizontal = 16.dp
            )
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        AppIconPicker(
            viewModel = viewModel,
            primaryContainer = primaryContainer
        )
        EquinoxOutlinedTextField(
            outlinedTextFieldColors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primaryContainer
            ),
            value = viewModel.appName,
            isError = viewModel.appNameError,
            placeholder = string.app_name_field,
            errorText = string.wrong_app_name_field,
            validator = { isAppNameValid(it) }
        )
        EquinoxOutlinedTextField(
            outlinedTextFieldColors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primaryContainer
            ),
            value = viewModel.appDescription,
            isError = viewModel.appDescriptionError,
            placeholder = string.app_description,
            errorText = string.wrong_app_description,
            maxLines = 6,
            validator = { isAppDescriptionValid(it) }
        )
    }
}

@Composable
@NonRestartableComposable
private fun AppIconPicker(
    viewModel: ApplicationViewModel,
    primaryContainer: Color
) {
    val launcher = rememberFilePickerLauncher(
        type = PickerType.Image,
        mode = PickerMode.Single
    ) { appIcon ->
        val appIconPath = getAppIconPath(
            appIcon = appIcon
        )
        appIconPath?.let { path ->
            viewModel.appIcon.value = path
            viewModel.appIconBorderColor.value = primaryContainer
        }
    }
    Box(
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            modifier = Modifier
                .size(125.dp)
                .border(
                    width = 1.5.dp,
                    color = viewModel.appIconBorderColor.value,
                    shape = CircleShape
                )
                .clip(CircleShape),
            model = ImageRequest.Builder(LocalPlatformContext.current)
                .data(viewModel.appIcon.value)
                .crossfade(true)
                .crossfade(500)
                .build(),
            imageLoader = imageLoader,
            contentDescription = "Application icon",
            contentScale = ContentScale.Crop
            // TODO: TO SET ERROR
        )
        IconButton(
            modifier = Modifier
                .clip(CircleShape)
                .background(Color(0xD0DFD8D8))
                .size(35.dp)
                .align(Alignment.BottomEnd),
            onClick = { launcher.launch() }
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = null
            )
        }
    }
}

@Composable
@NonRestartableComposable
private fun DialogActions(
    closeDialog: () -> Unit,
    viewModel: ApplicationViewModel,
    application: AmetistaApplication?
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Bottom
    ) {
        Row {
            TextButton(
                onClick = closeDialog
            ) {
                Text(
                    text = stringResource(string.dismiss)
                )
            }
            TextButton(
                onClick = {
                    if (viewModel is ApplicationsScreenViewModel) {
                        viewModel.workOnApplication(
                            application = application,
                            onSuccess = closeDialog
                        )
                    } else {
                        viewModel.editApplication(
                            application = application!!,
                            onSuccess = closeDialog
                        )
                    }
                }
            ) {
                Text(
                    text = stringResource(string.confirm)
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
    viewModel: ApplicationViewModel,
    onDelete: (() -> Unit)? = null
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
            viewModel.deleteApplication(
                application = application,
                onDelete = {
                    show.value = false
                    onDelete?.invoke()
                }
            )
        }
    )
}