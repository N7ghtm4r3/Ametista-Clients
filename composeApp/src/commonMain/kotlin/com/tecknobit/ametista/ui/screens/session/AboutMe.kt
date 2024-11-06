@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.ametista.ui.screens.session

import ametista.composeapp.generated.resources.Res
import ametista.composeapp.generated.resources.change_email
import ametista.composeapp.generated.resources.change_password
import ametista.composeapp.generated.resources.confirm
import ametista.composeapp.generated.resources.delete_account
import ametista.composeapp.generated.resources.delete_message
import ametista.composeapp.generated.resources.dismiss
import ametista.composeapp.generated.resources.email
import ametista.composeapp.generated.resources.language
import ametista.composeapp.generated.resources.logout
import ametista.composeapp.generated.resources.logout_message
import ametista.composeapp.generated.resources.new_email
import ametista.composeapp.generated.resources.new_password
import ametista.composeapp.generated.resources.password
import ametista.composeapp.generated.resources.theme
import ametista.composeapp.generated.resources.wrong_email
import ametista.composeapp.generated.resources.wrong_password
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AutoMode
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.tecknobit.ametista.displayFontFamily
import com.tecknobit.ametista.getImagePath
import com.tecknobit.ametista.imageLoader
import com.tecknobit.ametista.localUser
import com.tecknobit.ametista.navigator
import com.tecknobit.ametista.ui.components.RoleBadge
import com.tecknobit.ametista.ui.screens.AmetistaScreen.Companion.CONTAINER_MAX_WIDTH
import com.tecknobit.ametista.ui.screens.AmetistaScreen.Companion.SPLASHSCREEN
import com.tecknobit.ametistacore.helpers.AmetistaValidator.isNewPasswordValid
import com.tecknobit.equinox.environment.records.EquinoxUser.ApplicationTheme
import com.tecknobit.equinox.environment.records.EquinoxUser.ApplicationTheme.Dark
import com.tecknobit.equinox.environment.records.EquinoxUser.ApplicationTheme.Light
import com.tecknobit.equinox.inputs.InputValidator.LANGUAGES_SUPPORTED
import com.tecknobit.equinox.inputs.InputValidator.isEmailValid
import com.tecknobit.equinoxcompose.components.EquinoxAlertDialog
import com.tecknobit.equinoxcompose.components.EquinoxOutlinedTextField
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.core.PickerMode
import io.github.vinceglb.filekit.core.PickerType
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

private lateinit var viewModel: SessionScreenViewModel

@Composable
@NonRestartableComposable
fun AboutMe(
    screenViewModel: SessionScreenViewModel
) {
    viewModel = screenViewModel
    Column(
        modifier = Modifier
            .widthIn(
                max = CONTAINER_MAX_WIDTH
            )
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ProfilePicPicker()
            Text(
                text = localUser.completeName
            )
            RoleBadge(
                role = localUser.getRole()!!
            )
        }
        HorizontalDivider(
            color = MaterialTheme.colorScheme.primary
        )
        EmailSection()
        PasswordSection()
        LanguageSection()
        ThemeSection()
        UserActions()
    }
}

@Composable
@NonRestartableComposable
private fun ProfilePicPicker() {
    val currentProfilePic = remember { mutableStateOf(localUser.profilePic) }
    val picker = rememberFilePickerLauncher(
        type = PickerType.Image,
        mode = PickerMode.Single
    ) { profilePic ->
        val profilePicPath = getImagePath(
            imagePic = profilePic
        )
        profilePicPath?.let {
            viewModel.changeProfilePic(
                imagePath = profilePicPath,
                profilePic = currentProfilePic
            )
        }
    }
    AsyncImage(
        modifier = Modifier
            .size(125.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = CircleShape
            )
            .clip(CircleShape)
            .clickable { picker.launch() },
        model = ImageRequest.Builder(LocalPlatformContext.current)
            .data(currentProfilePic.value)
            .crossfade(true)
            .crossfade(500)
            .build(),
        imageLoader = imageLoader,
        contentDescription = "User profile picture",
        contentScale = ContentScale.Crop
        // TODO: TO SET ERROR
    )
}

/**
 * Function to display the section of the user's email and allowing the user to change it
 *
 * No-any params required
 */
@Composable
@NonRestartableComposable
private fun EmailSection() {
    val changeEmail = remember { mutableStateOf(false) }
    var userEmail by remember { mutableStateOf(localUser.email) }
    viewModel.newEmail = remember { mutableStateOf("") }
    viewModel.newEmailError = remember { mutableStateOf(false) }
    val resetEmailLayout = {
        viewModel.newEmail.value = ""
        viewModel.newEmailError.value = false
        changeEmail.value = false
    }
    UserData(
        header = Res.string.email,
        data = userEmail,
        editAction = { changeEmail.value = true }
    )
    EquinoxAlertDialog(
        onDismissAction = resetEmailLayout,
        icon = Icons.Default.Email,
        show = changeEmail,
        title = Res.string.change_email,
        text = {
            EquinoxOutlinedTextField(
                value = viewModel.newEmail,
                label = Res.string.new_email,
                mustBeInLowerCase = true,
                errorText = Res.string.wrong_email,
                isError = viewModel.newEmailError,
                validator = { isEmailValid(it) }
            )
        },
        confirmAction = {
            viewModel.changeEmail(
                onSuccess = {
                    userEmail = viewModel.newEmail.value
                    resetEmailLayout.invoke()
                }
            )
        },
        confirmText = Res.string.confirm,
        dismissText = Res.string.dismiss
    )
}

/**
 * Function to display the section of the user's password and allowing the user to change it
 *
 * No-any params required
 */
@Composable
@NonRestartableComposable
private fun PasswordSection() {
    val changePassword = remember { mutableStateOf(false) }
    viewModel.newPassword = remember { mutableStateOf("") }
    viewModel.newPasswordError = remember { mutableStateOf(false) }
    val resetPasswordLayout = {
        viewModel.newPassword.value = ""
        viewModel.newPasswordError.value = false
        changePassword.value = false
    }
    var hiddenPassword by remember { mutableStateOf(true) }
    UserData(
        header = Res.string.password,
        data = "****",
        editAction = { changePassword.value = true }
    )
    EquinoxAlertDialog(
        onDismissAction = resetPasswordLayout,
        icon = Icons.Default.Password,
        show = changePassword,
        title = Res.string.change_password,
        text = {
            EquinoxOutlinedTextField(
                value = viewModel.newPassword,
                label = Res.string.new_password,
                trailingIcon = {
                    IconButton(
                        onClick = { hiddenPassword = !hiddenPassword }
                    ) {
                        Icon(
                            imageVector = if (hiddenPassword)
                                Icons.Default.Visibility
                            else
                                Icons.Default.VisibilityOff,
                            contentDescription = null
                        )
                    }
                },
                visualTransformation = if (hiddenPassword)
                    PasswordVisualTransformation()
                else
                    VisualTransformation.None,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                errorText = Res.string.wrong_password,
                isError = viewModel.newPasswordError,
                validator = { isNewPasswordValid(it) }
            )
        },
        confirmAction = {
            viewModel.changePassword(
                onSuccess = resetPasswordLayout
            )
        },
        confirmText = Res.string.confirm,
        dismissText = Res.string.dismiss
    )
}

@Composable
@NonRestartableComposable
private fun LanguageSection() {
    val changeLanguage = remember { mutableStateOf(false) }
    UserData(
        header = Res.string.language,
        data = LANGUAGES_SUPPORTED[localUser.language]!!,
        editAction = { changeLanguage.value = true }
    )
    ChangeLanguage(
        changeLanguage = changeLanguage
    )
}

@Composable
@NonRestartableComposable
private fun ThemeSection() {
    val changeTheme = remember { mutableStateOf(false) }
    UserData(
        header = Res.string.theme,
        data = localUser.theme.name,
        editAction = { changeTheme.value = true }
    )
    ChangeTheme(
        changeTheme = changeTheme
    )
}

@Composable
@NonRestartableComposable
private fun UserData(
    header: StringResource,
    data: String,
    editAction: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(
                start = 16.dp
            )
    ) {
        Column(
            modifier = Modifier
                .weight(2f)
        ) {
            Text(
                text = stringResource(header),
                fontFamily = displayFontFamily,
                fontSize = 14.sp
            )
            Text(
                text = data,
                fontSize = 20.sp
            )
        }
        Column(
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.End
        ) {
            IconButton(
                onClick = editAction
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null
                )
            }
        }
    }
    HorizontalDivider(
        color = MaterialTheme.colorScheme.primary
    )
}

/**
 * Function to allow the user to change the current language setting
 *
 * @param changeLanguage: the state whether display this section
 */
@Composable
@NonRestartableComposable
private fun ChangeLanguage(
    changeLanguage: MutableState<Boolean>
) {
    ChangeInfo(
        showModal = changeLanguage
    ) {
        LANGUAGES_SUPPORTED.keys.forEach { language ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        viewModel.changeLanguage(
                            newLanguage = language,
                            onSuccess = {
                                changeLanguage.value = false
                                navToSplash()
                            }
                        )
                    }
                    .padding(
                        all = 16.dp
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Flag,
                    contentDescription = null,
                    tint = if (localUser.language == language)
                        MaterialTheme.colorScheme.primary
                    else
                        LocalContentColor.current
                )
                Text(
                    text = LANGUAGES_SUPPORTED[language]!!,
                    fontFamily = displayFontFamily
                )
            }
            HorizontalDivider()
        }
    }
}

/**
 * Function to allow the user to change the current theme setting
 *
 * @param changeTheme: the state whether display this section
 */
@Composable
@NonRestartableComposable
private fun ChangeTheme(
    changeTheme: MutableState<Boolean>
) {
    ChangeInfo(
        showModal = changeTheme
    ) {
        ApplicationTheme.entries.forEach { theme ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        viewModel.changeTheme(
                            newTheme = theme,
                            onChange = {
                                changeTheme.value = false
                                navToSplash()
                            }
                        )
                    }
                    .padding(
                        all = 16.dp
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = when (theme) {
                        Light -> Icons.Default.LightMode
                        Dark -> Icons.Default.DarkMode
                        else -> Icons.Default.AutoMode
                    },
                    contentDescription = null,
                    tint = if (localUser.theme == theme)
                        MaterialTheme.colorScheme.primary
                    else
                        LocalContentColor.current
                )
                Text(
                    text = theme.toString(),
                    fontFamily = displayFontFamily
                )
            }
            HorizontalDivider()
        }
    }
}

/**
 * Function to allow the user to change a current setting
 *
 * @param showModal: the state whether display the [ModalBottomSheet]
 * @param sheetState: the state to apply to the [ModalBottomSheet]
 * @param onDismissRequest: the action to execute when the the [ModalBottomSheet] has been dismissed
 * @param content: the content to display
 */
@Composable
@NonRestartableComposable
private fun ChangeInfo(
    showModal: MutableState<Boolean>,
    sheetState: SheetState = rememberModalBottomSheetState(),
    onDismissRequest: () -> Unit = { showModal.value = false },
    content: @Composable ColumnScope.() -> Unit
) {
    if (showModal.value) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = onDismissRequest
        ) {
            Column(
                content = content
            )
        }
    }
}

@Composable
@NonRestartableComposable
private fun UserActions() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                end = 10.dp
            ),
        horizontalArrangement = Arrangement.End
    ) {
        LogoutSection()
        DeleteAccountSection()
    }
}

@Composable
@NonRestartableComposable
private fun LogoutSection() {
    val logout = remember { mutableStateOf(false) }
    Button(
        shape = RoundedCornerShape(
            size = 10.dp
        ),
        onClick = { logout.value = true }
    ) {
        Text(
            text = stringResource(Res.string.logout)
        )
    }
    EquinoxAlertDialog(
        icon = Icons.AutoMirrored.Filled.Logout,
        show = logout,
        title = Res.string.logout,
        text = Res.string.logout_message,
        confirmAction = {
            viewModel.logout {
                navToSplash()
            }
        },
        confirmText = Res.string.confirm,
        dismissText = Res.string.dismiss
    )
}

@Composable
@NonRestartableComposable
private fun DeleteAccountSection() {
    val deleteAccount = remember { mutableStateOf(false) }
    Button(
        modifier = Modifier
            .padding(
                start = 10.dp
            ),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.error
        ),
        shape = RoundedCornerShape(
            size = 10.dp
        ),
        onClick = { deleteAccount.value = true }
    ) {
        Text(
            text = stringResource(Res.string.delete_account)
        )
    }
    EquinoxAlertDialog(
        modifier = Modifier
            .widthIn(
                max = 400.dp
            ),
        icon = Icons.Default.Delete,
        show = deleteAccount,
        title = Res.string.delete_account,
        text = Res.string.delete_message,
        confirmAction = {
            viewModel.deleteAccount {
                navToSplash()
            }
        },
        confirmText = Res.string.confirm,
        dismissText = Res.string.dismiss
    )
}

private fun navToSplash() {
    navigator.navigate(SPLASHSCREEN)
}