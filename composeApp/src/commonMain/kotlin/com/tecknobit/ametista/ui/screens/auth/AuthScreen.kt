package com.tecknobit.ametista.ui.screens.auth

import ametista.composeapp.generated.resources.Res
import ametista.composeapp.generated.resources.app_version
import ametista.composeapp.generated.resources.email
import ametista.composeapp.generated.resources.github
import ametista.composeapp.generated.resources.hello
import ametista.composeapp.generated.resources.host
import ametista.composeapp.generated.resources.login
import ametista.composeapp.generated.resources.password
import ametista.composeapp.generated.resources.server_secret
import ametista.composeapp.generated.resources.wrong_email
import ametista.composeapp.generated.resources.wrong_host_address
import ametista.composeapp.generated.resources.wrong_password
import ametista.composeapp.generated.resources.wrong_server_secret
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.ametista.displayFontFamily
import com.tecknobit.equinox.inputs.InputValidator.isEmailValid
import com.tecknobit.equinox.inputs.InputValidator.isHostValid
import com.tecknobit.equinox.inputs.InputValidator.isPasswordValid
import com.tecknobit.equinox.inputs.InputValidator.isServerSecretValid
import com.tecknobit.equinoxcompose.components.EquinoxOutlinedTextField
import com.tecknobit.equinoxcompose.helpers.session.EquinoxScreen
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

class AuthScreen : EquinoxScreen<AuthScreenViewModel>(
    viewModel = AuthScreenViewModel()
) {

    /**
     * Function to arrange the content of the screen to display
     *
     * No-any params required
     */
    @Composable
    override fun ArrangeScreenContent() {
        Scaffold(
            snackbarHost = { SnackbarHost(hostState = viewModel!!.snackbarHostState!!) },
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                HeaderSection()
                FormSection()
            }
        }
    }

    /**
     * Function to create the header section of the activity
     *
     * No-any params required
     */
    @Composable
    private fun HeaderSection() {
        Column(
            modifier = Modifier
                .height(125.dp)
                .background(MaterialTheme.colorScheme.primary),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        all = 16.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = stringResource(Res.string.hello),
                        fontFamily = displayFontFamily,
                        color = Color.White
                    )
                    Text(
                        text = stringResource(Res.string.login),
                        fontFamily = displayFontFamily,
                        fontSize = 35.sp,
                        color = Color.White
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.End
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val uriHandler = LocalUriHandler.current
                        IconButton(
                            onClick = {
                                uriHandler.openUri(
                                    uri = "https://github.com/N7ghtm4r3/Ametista-Clients"
                                )
                            }
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.github),
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                        Text(
                            text = "v. ${stringResource(Res.string.app_version)}",
                            fontFamily = displayFontFamily,
                            fontSize = 12.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }

    /**
     * Function to create the form where the user can fill it with his credentials
     *
     * No-any params required
     */
    @Composable
    @NonRestartableComposable
    private fun FormSection() {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            //CustomerSelector()
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                )
                EquinoxOutlinedTextField(
                    value = viewModel!!.host,
                    label = stringResource(Res.string.host),
                    keyboardOptions = keyboardOptions,
                    errorText = stringResource(Res.string.wrong_host_address),
                    isError = viewModel!!.hostError,
                    validator = { isHostValid(it) }
                )
                EquinoxOutlinedTextField(
                    value = viewModel!!.serverSecret,
                    label = stringResource(Res.string.server_secret),
                    keyboardOptions = keyboardOptions,
                    errorText = stringResource(Res.string.wrong_server_secret),
                    isError = viewModel!!.serverSecretError,
                    validator = { isServerSecretValid(it) }
                )
                EquinoxOutlinedTextField(
                    value = viewModel!!.email,
                    label = stringResource(Res.string.email),
                    mustBeInLowerCase = true,
                    keyboardOptions = keyboardOptions,
                    errorText = stringResource(Res.string.wrong_email),
                    isError = viewModel!!.emailError,
                    validator = { isEmailValid(it) }
                )
                var hiddenPassword by remember { mutableStateOf(true) }
                EquinoxOutlinedTextField(
                    value = viewModel!!.password,
                    label = stringResource(Res.string.password),
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
                    errorText = stringResource(Res.string.wrong_password),
                    isError = viewModel!!.passwordError,
                    validator = { isPasswordValid(it) }
                )
                Button(
                    modifier = Modifier
                        .padding(
                            top = 10.dp
                        )
                        .height(
                            60.dp
                        )
                        .width(300.dp),
                    shape = RoundedCornerShape(
                        size = 10.dp
                    ),
                    onClick = { viewModel!!.login() }
                ) {
                    Text(
                        text = stringResource(Res.string.login)
                    )
                }
            }
        }
    }

    /*
     * Function to select and to adapt the [FormSection] if the user whether the user is a [NovaUser.Role.Customer]
     * or is a [NovaUser.Role.Vendor]
     *
     * No-any params required
     *
    @Composable
    @NonRestartableComposable
    private fun CustomerSelector() {
        AnimatedVisibility(
            visible = viewModel!!.isSignUp.value
        ) {
            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = stringResource(Res.string.i_am_a_customer)
                )
                Switch(
                    checked = viewModel!!.isCustomerAuth.value,
                    onCheckedChange = { isCustomerAuth ->
                        viewModel!!.isCustomerAuth.value = isCustomerAuth
                        if(isCustomerAuth) {
                            viewModel!!.host.value = ""
                            viewModel!!.hostError.value = false
                            viewModel!!.serverSecret.value = ""
                            viewModel!!.serverSecretError.value = false
                        }
                    }
                )
            }
        }
    }*/

    /**
     * Function to collect or instantiate the states of the screen
     *
     * No-any params required
     */
    @Composable
    override fun CollectStates() {
        viewModel!!.host = remember { mutableStateOf("") }
        viewModel!!.hostError = remember { mutableStateOf(false) }
        viewModel!!.serverSecret = remember { mutableStateOf("") }
        viewModel!!.serverSecretError = remember { mutableStateOf(false) }
        viewModel!!.email = remember { mutableStateOf("") }
        viewModel!!.emailError = remember { mutableStateOf(false) }
        viewModel!!.password = remember { mutableStateOf("") }
        viewModel!!.passwordError = remember { mutableStateOf(false) }
    }

}