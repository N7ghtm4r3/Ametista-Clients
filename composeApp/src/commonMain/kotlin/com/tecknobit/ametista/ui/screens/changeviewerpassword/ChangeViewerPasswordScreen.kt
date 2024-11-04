@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.ametista.ui.screens.changeviewerpassword

import ametista.composeapp.generated.resources.Res
import ametista.composeapp.generated.resources.change_preset_password
import ametista.composeapp.generated.resources.change_preset_password_info
import ametista.composeapp.generated.resources.confirm
import ametista.composeapp.generated.resources.password
import ametista.composeapp.generated.resources.wrong_password
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tecknobit.ametista.CloseApplicationOnNavBack
import com.tecknobit.ametista.ui.screens.AmetistaScreen
import com.tecknobit.ametista.ui.theme.AmetistaTheme
import com.tecknobit.ametistacore.helpers.AmetistaValidator.isNewPasswordValid
import com.tecknobit.equinoxcompose.components.EquinoxOutlinedTextField
import org.jetbrains.compose.resources.stringResource

class ChangeViewerPasswordScreen : AmetistaScreen<ChangeViewerPasswordScreenViewModel>(
    viewModel = ChangeViewerPasswordScreenViewModel()
) {

    /**
     * Function to arrange the content of the screen to display
     *
     * No-any params required
     */
    @Composable
    override fun ArrangeScreenContent() {
        AmetistaTheme {
            CloseApplicationOnNavBack()
            Scaffold(
                topBar = {
                    LargeTopAppBar(
                        colors = TopAppBarDefaults.largeTopAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            titleContentColor = Color.White
                        ),
                        title = {
                            Text(
                                text = stringResource(Res.string.change_preset_password)
                            )
                        }
                    )
                },
                snackbarHost = { SnackbarHost(viewModel!!.snackbarHostState!!) }
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .padding(
                            top = paddingValues.calculateTopPadding()
                        )
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            modifier = Modifier
                                .width(300.dp),
                            text = stringResource(Res.string.change_preset_password_info),
                            textAlign = TextAlign.Justify
                        )
                        var hiddenPassword by remember { mutableStateOf(true) }
                        EquinoxOutlinedTextField(
                            value = viewModel!!.password,
                            label = stringResource(Res.string.password),
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
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password
                            ),
                            errorText = stringResource(Res.string.wrong_password),
                            isError = viewModel!!.passwordError,
                            validator = { isNewPasswordValid(it) }
                        )
                        Button(
                            modifier = Modifier
                                .height(
                                    60.dp
                                )
                                .width(300.dp),
                            shape = RoundedCornerShape(
                                size = 10.dp
                            ),
                            onClick = { viewModel!!.changeViewerPassword() }
                        ) {
                            Text(
                                text = stringResource(Res.string.confirm)
                            )
                        }
                    }
                }
            }
        }
    }

    /**
     * Function to collect or instantiate the states of the screen
     *
     * No-any params required
     */
    @Composable
    override fun CollectStates() {
        viewModel!!.password = remember { mutableStateOf("") }
        viewModel!!.passwordError = remember { mutableStateOf(false) }
    }

}