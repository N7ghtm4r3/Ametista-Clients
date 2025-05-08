@file:OptIn(
    ExperimentalMaterial3Api::class, ExperimentalComposeApi::class,
    ExperimentalLayoutApi::class
)

package com.tecknobit.ametista.ui.screens.session.components

import ametista.composeapp.generated.resources.Res
import ametista.composeapp.generated.resources.change_email
import ametista.composeapp.generated.resources.change_language
import ametista.composeapp.generated.resources.change_password
import ametista.composeapp.generated.resources.change_theme
import ametista.composeapp.generated.resources.delete
import ametista.composeapp.generated.resources.logo
import ametista.composeapp.generated.resources.logout
import ametista.composeapp.generated.resources.new_email
import ametista.composeapp.generated.resources.new_password
import ametista.composeapp.generated.resources.wrong_email
import ametista.composeapp.generated.resources.wrong_password
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.tecknobit.ametista.SPLASHSCREEN
import com.tecknobit.ametista.bodyFontFamily
import com.tecknobit.ametista.imageLoader
import com.tecknobit.ametista.localUser
import com.tecknobit.ametista.navigator
import com.tecknobit.ametista.ui.components.DeleteAccount
import com.tecknobit.ametista.ui.components.Logout
import com.tecknobit.ametista.ui.screens.session.presentation.SessionScreenViewModel
import com.tecknobit.equinoxcompose.components.ChameleonText
import com.tecknobit.equinoxcompose.components.EquinoxOutlinedTextField
import com.tecknobit.equinoxcompose.components.EquinoxTextField
import com.tecknobit.equinoxcompose.components.stepper.Step
import com.tecknobit.equinoxcompose.components.stepper.StepContent
import com.tecknobit.equinoxcompose.components.stepper.Stepper
import com.tecknobit.equinoxcompose.session.EquinoxLocalUser.ApplicationTheme
import com.tecknobit.equinoxcompose.utilities.responsiveMaxWidth
import com.tecknobit.equinoxcore.helpers.InputsValidator.Companion.LANGUAGES_SUPPORTED
import com.tecknobit.equinoxcore.helpers.InputsValidator.Companion.isEmailValid
import com.tecknobit.equinoxcore.helpers.InputsValidator.Companion.isPasswordValid
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.core.PickerMode
import io.github.vinceglb.filekit.core.PickerType
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/**
 * Section with the account details of the current [localUser]
 *
 * @param viewModel The viewmodel related to the [com.tecknobit.ametista.ui.screens.session.presenter.SessionScreen]
 */
@Composable
fun AboutMe(
    viewModel: SessionScreenViewModel,
) {
    viewModel.profilePic = remember { mutableStateOf(localUser.profilePic) }
    viewModel.email = remember { mutableStateOf(localUser.email) }
    viewModel.password = remember { mutableStateOf(localUser.password) }
    viewModel.language = remember { mutableStateOf(localUser.language) }
    viewModel.theme = remember { mutableStateOf(localUser.theme) }
    Column(
        modifier = Modifier
            .responsiveMaxWidth()
            .padding(
                horizontal = 16.dp
            ),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        UserDetails(
            viewModel = viewModel
        )
        Settings(
            viewModel = viewModel
        )
    }
}

/**
 * The details of the [localUser]
 */
@Composable
private fun UserDetails(
    viewModel: SessionScreenViewModel,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 5.dp
            ),
        horizontalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        ProfilePicker(
            viewModel = viewModel
        )
        Column {
            localUser.role?.let { role ->
                RoleBadge(
                    role = role
                )
            }
            Text(
                text = localUser.completeName,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = viewModel.email.value,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 14.sp
            )
            ActionButtons(
                viewModel = viewModel
            )
        }
    }
}

/**
 * The profile picker to allow the [localUser] to change his/her profile picture
 */
@Composable
private fun ProfilePicker(
    viewModel: SessionScreenViewModel,
) {
    val launcher = rememberFilePickerLauncher(
        type = PickerType.Image,
        mode = PickerMode.Single
    ) { image ->
        image?.let {
            viewModel.viewModelScope.launch {
                viewModel.changeProfilePic(
                    profilePicName = image.name,
                    profilePicBytes = image.readBytes()
                )
            }
        }
    }
    AsyncImage(
        modifier = Modifier
            .size(120.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = CircleShape
            )
            .clip(CircleShape)
            .clickable { launcher.launch() },
        model = ImageRequest.Builder(LocalPlatformContext.current)
            .data(viewModel.profilePic)
            .crossfade(true)
            .crossfade(500)
            .build(),
        imageLoader = imageLoader,
        contentDescription = "User profile picture",
        contentScale = ContentScale.Crop,
        error = painterResource(Res.drawable.logo)
    )
}

/**
 * The actions can be execute on the [localUser] account such logout and delete account
 */
@Composable
private fun ActionButtons(
    viewModel: SessionScreenViewModel,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        val logout = remember { mutableStateOf(false) }
        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.inversePrimary
            ),
            shape = RoundedCornerShape(
                size = 10.dp
            ),
            onClick = { logout.value = true }
        ) {
            ChameleonText(
                text = stringResource(Res.string.logout),
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                backgroundColor = MaterialTheme.colorScheme.inversePrimary
            )
        }
        Logout(
            viewModel = viewModel,
            show = logout
        )
        val deleteAccount = remember { mutableStateOf(false) }
        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            ),
            shape = RoundedCornerShape(
                size = 10.dp
            ),
            onClick = { deleteAccount.value = true }
        ) {
            Text(
                text = stringResource(Res.string.delete),
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        DeleteAccount(
            viewModel = viewModel,
            show = deleteAccount
        )
    }
}

/**
 * The settings section to customize the [localUser] experience
 */
@Composable
private fun Settings(
    viewModel: SessionScreenViewModel,
) {
    val steps = remember {
        arrayOf(
            Step(
                stepIcon = Icons.Default.AlternateEmail,
                title = Res.string.change_email,
                content = {
                    ChangeEmail(
                        viewModel = viewModel
                    )
                },
                dismissAction = { visible -> visible.value = false },
                confirmAction = { visible ->
                    viewModel.changeEmail(
                        onChange = {
                            visible.value = false
                        }
                    )
                }
            ),
            Step(
                stepIcon = Icons.Default.Password,
                title = Res.string.change_password,
                content = {
                    ChangePassword(
                        viewModel = viewModel
                    )
                },
                dismissAction = { visible -> visible.value = false },
                confirmAction = { visible ->
                    viewModel.changePassword(
                        onChange = {
                            visible.value = false
                        }
                    )
                }
            ),
            Step(
                stepIcon = Icons.Default.Language,
                title = Res.string.change_language,
                content = {
                    ChangeLanguage(
                        viewModel = viewModel
                    )
                },
                dismissAction = { visible -> visible.value = false },
                confirmAction = { visible ->
                    viewModel.changeLanguage(
                        onChange = {
                            visible.value = false
                            navigator.navigate(SPLASHSCREEN)
                        }
                    )
                }
            ),
            Step(
                stepIcon = Icons.Default.Palette,
                title = Res.string.change_theme,
                content = {
                    ChangeTheme(
                        viewModel = viewModel
                    )
                },
                dismissAction = { visible -> visible.value = false },
                confirmAction = { visible ->
                    viewModel.changeTheme(
                        onChange = {
                            visible.value = false
                            navigator.navigate(SPLASHSCREEN)
                        }
                    )
                }
            )
        )
    }
    Stepper(
        steps = steps
    )
}

/**
 * Section to change the [localUser]'s email
 */
@StepContent(
    number = 1
)
@Composable
private fun ChangeEmail(
    viewModel: SessionScreenViewModel,
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    viewModel.newEmail = remember { mutableStateOf("") }
    viewModel.newEmailError = remember { mutableStateOf(false) }
    EquinoxTextField(
        modifier = Modifier
            .focusRequester(focusRequester),
        textFieldColors = TextFieldDefaults.colors(
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent
        ),
        value = viewModel.newEmail,
        textFieldStyle = TextStyle(
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = bodyFontFamily
        ),
        isError = viewModel.newEmailError,
        mustBeInLowerCase = true,
        allowsBlankSpaces = false,
        validator = { isEmailValid(it) },
        errorText = Res.string.wrong_email,
        errorTextStyle = TextStyle(
            fontSize = 14.sp,
            fontFamily = bodyFontFamily
        ),
        placeholder = Res.string.new_email,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Email
        )
    )
}

/**
 * Section to change the [localUser]'s password
 */
@StepContent(
    number = 2
)
@Composable
private fun ChangePassword(
    viewModel: SessionScreenViewModel,
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    viewModel.newPassword = remember { mutableStateOf("") }
    viewModel.newPasswordError = remember { mutableStateOf(false) }
    var hiddenPassword by remember { mutableStateOf(true) }
    EquinoxOutlinedTextField(
        modifier = Modifier
            .focusRequester(focusRequester),
        outlinedTextFieldColors = TextFieldDefaults.colors(
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent
        ),
        value = viewModel.newPassword,
        outlinedTextFieldStyle = TextStyle(
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = bodyFontFamily
        ),
        isError = viewModel.newPasswordError,
        allowsBlankSpaces = false,
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
        validator = { isPasswordValid(it) },
        errorText = stringResource(Res.string.wrong_password),
        errorTextStyle = TextStyle(
            fontSize = 14.sp,
            fontFamily = bodyFontFamily
        ),
        placeholder = stringResource(Res.string.new_password),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Password
        )
    )
}

/**
 * Section to change the [localUser]'s language
 */
@StepContent(
    number = 3
)
@Composable
private fun ChangeLanguage(
    viewModel: SessionScreenViewModel,
) {
    Column(
        modifier = Modifier
            .selectableGroup()
    ) {
        LANGUAGES_SUPPORTED.entries.forEach { entry ->
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = viewModel.language.value == entry.key,
                    onClick = { viewModel.language.value = entry.key }
                )
                Text(
                    text = entry.value
                )
            }
        }
    }
}

/**
 * Section to change the [localUser]'s theme
 */
@StepContent(
    number = 4
)
@Composable
private fun ChangeTheme(
    viewModel: SessionScreenViewModel,
) {
    Column(
        modifier = Modifier
            .selectableGroup()
    ) {
        ApplicationTheme.entries.forEach { entry ->
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = viewModel.theme.value == entry,
                    onClick = { viewModel.theme.value = entry }
                )
                Text(
                    text = entry.name
                )
            }
        }
    }
}