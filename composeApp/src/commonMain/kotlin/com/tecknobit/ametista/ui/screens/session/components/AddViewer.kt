@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.ametista.ui.screens.session.components

import ametista.composeapp.generated.resources.Res.string
import ametista.composeapp.generated.resources.add_viewer
import ametista.composeapp.generated.resources.add_viewer_info
import ametista.composeapp.generated.resources.email
import ametista.composeapp.generated.resources.name
import ametista.composeapp.generated.resources.surname
import ametista.composeapp.generated.resources.wrong_email
import ametista.composeapp.generated.resources.wrong_name
import ametista.composeapp.generated.resources.wrong_surname
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.ametista.displayFontFamily
import com.tecknobit.ametista.ui.screens.session.presentation.SessionScreenViewModel
import com.tecknobit.equinoxcompose.components.EquinoxOutlinedTextField
import com.tecknobit.equinoxcore.helpers.InputsValidator.Companion.isEmailValid
import com.tecknobit.equinoxcore.helpers.InputsValidator.Companion.isNameValid
import com.tecknobit.equinoxcore.helpers.InputsValidator.Companion.isSurnameValid
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

/**
 * Layout to add a new [com.tecknobit.ametistacore.enums.Role.VIEWER] in the system
 *
 * @param viewModel The viewmodel related to the [com.tecknobit.ametista.ui.screens.session.presenter.SessionScreen]
 * @param state The state useful to manage the visibility of the [ModalBottomSheet]
 * @param scope The coroutine useful to manage the visibility of the [ModalBottomSheet]
 */
@Composable
fun AddViewer(
    viewModel: SessionScreenViewModel,
    state: SheetState,
    scope: CoroutineScope,
) {
    if (state.isVisible) {
        viewModel.viewerName = remember { mutableStateOf("") }
        viewModel.viewerNameError = remember { mutableStateOf(false) }
        viewModel.viewerSurname = remember { mutableStateOf("") }
        viewModel.viewerSurnameError = remember { mutableStateOf(false) }
        viewModel.viewerEmail = remember { mutableStateOf("") }
        viewModel.viewerEmailError = remember { mutableStateOf(false) }
        ModalBottomSheet(
            sheetState = state,
            onDismissRequest = {
                scope.launch {
                    state.hide()
                }
            }
        ) {
            Column(
                modifier = Modifier
                    .padding(
                        bottom = 16.dp
                    )
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = stringResource(string.add_viewer),
                    textAlign = TextAlign.Center,
                    fontFamily = displayFontFamily,
                    fontSize = 20.sp
                )
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    modifier = Modifier
                        .padding(
                            horizontal = 16.dp
                        )
                        .padding(
                            top = 16.dp
                        ),
                    text = stringResource(string.add_viewer_info),
                    textAlign = TextAlign.Justify
                )
                ViewerForm(
                    viewModel = viewModel,
                    state = state,
                    scope = scope
                )
            }
        }
    }
}

/**
 * The form to fill with the [com.tecknobit.ametistacore.enums.Role.VIEWER] information
 *
 * @param viewModel The viewmodel related to the [com.tecknobit.ametista.ui.screens.session.presenter.SessionScreen]
 * @param state The state useful to manage the visibility of the [ModalBottomSheet]
 * @param scope The coroutine useful to manage the visibility of the [ModalBottomSheet]
 */
@Composable
@NonRestartableComposable
private fun ViewerForm(
    viewModel: SessionScreenViewModel,
    state: SheetState,
    scope: CoroutineScope,
) {
    val keyboardOptions = KeyboardOptions(
        imeAction = ImeAction.Next
    )
    Column(
        modifier = Modifier
            .padding(
                horizontal = 16.dp
            )
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            EquinoxOutlinedTextField(
                modifier = Modifier
                    .weight(1f),
                shape = RoundedCornerShape(
                    size = 10.dp
                ),
                value = viewModel.viewerName,
                label = stringResource(string.name),
                keyboardOptions = keyboardOptions,
                errorText = stringResource(string.wrong_name),
                isError = viewModel.viewerNameError,
                validator = { isNameValid(it) }
            )
            EquinoxOutlinedTextField(
                modifier = Modifier
                    .weight(1f),
                shape = RoundedCornerShape(
                    size = 10.dp
                ),
                value = viewModel.viewerSurname,
                label = stringResource(string.surname),
                keyboardOptions = keyboardOptions,
                errorText = stringResource(string.wrong_surname),
                isError = viewModel.viewerSurnameError,
                validator = { isSurnameValid(it) }
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            EquinoxOutlinedTextField(
                modifier = Modifier
                    .weight(1f),
                shape = RoundedCornerShape(
                    size = 10.dp
                ),
                value = viewModel.viewerEmail,
                label = string.email,
                mustBeInLowerCase = true,
                allowsBlankSpaces = false,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Email
                ),
                errorText = string.wrong_email,
                isError = viewModel.viewerEmailError,
                validator = { isEmailValid(it) }
            )
            Column(
                modifier = Modifier
                    .weight(1f),
                horizontalAlignment = Alignment.End
            ) {
                FloatingActionButton(
                    modifier = Modifier
                        .padding(
                            top = 5.dp
                        ),
                    shape = CircleShape,
                    onClick = {
                        viewModel.addViewer(
                            onSuccess = {
                                scope.launch {
                                    state.hide()
                                }
                            }
                        )
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Done,
                        contentDescription = null
                    )
                }
            }
        }
    }
}