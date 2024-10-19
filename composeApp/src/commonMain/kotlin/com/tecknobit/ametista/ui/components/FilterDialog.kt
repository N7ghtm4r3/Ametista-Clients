package com.tecknobit.ametista.ui.components

import ametista.composeapp.generated.resources.Res.string
import ametista.composeapp.generated.resources.dismiss
import ametista.composeapp.generated.resources.filter
import ametista.composeapp.generated.resources.filter_issues
import ametista.composeapp.generated.resources.filter_supporting_text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dokar.chiptextfield.Chip
import com.dokar.chiptextfield.m3.OutlinedChipTextField
import com.dokar.chiptextfield.rememberChipTextFieldState
import com.tecknobit.ametista.displayFontFamily
import com.tecknobit.ametista.ui.screens.platform.PlatformScreenViewModel
import com.tecknobit.ametista.ui.screens.platform.PlatformScreenViewModel.Companion.MAX_CHIPS_FILTERS
import org.jetbrains.compose.resources.stringResource

@Composable
@NonRestartableComposable
fun FilterDialog(
    show: MutableState<Boolean>,
    viewModel: PlatformScreenViewModel
) {
    val closeDialog = {
        show.value = false
        viewModel.suspendRefresher()
    }
    EquinoxDialog(
        show = show,
        viewModel = viewModel,
        content = {
            viewModel.filtersState = rememberChipTextFieldState()
            Text(
                text = stringResource(string.filter_issues),
                fontFamily = displayFontFamily,
                fontSize = 20.sp
            )
            FiltersInput(
                viewModel = viewModel
            )
            DialogButtons(
                closeDialog = closeDialog,
                viewModel = viewModel
            )
        }
    )
}

@Composable
@NonRestartableComposable
private fun FiltersInput(
    viewModel: PlatformScreenViewModel
) {
    Column {
        OutlinedChipTextField(
            modifier = Modifier
                .padding(
                    top = 10.dp
                )
                .heightIn(
                    max = 400.dp
                ),
            enabled = viewModel.filtersState.chips.size <= MAX_CHIPS_FILTERS,
            state = viewModel.filtersState,
            onSubmit = { text ->
                if (viewModel.filtersState.chips.size == MAX_CHIPS_FILTERS)
                    viewModel.filtersState.clearTextFieldFocus()
                Chip(text)
            }
        )
        Text(
            text = stringResource(string.filter_supporting_text),
            fontSize = 12.sp
        )
    }
}

@Composable
@NonRestartableComposable
private fun DialogButtons(
    closeDialog: () -> Unit,
    viewModel: PlatformScreenViewModel
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.End
    ) {
        TextButton(
            onClick = closeDialog
        ) {
            Text(
                text = stringResource(string.dismiss)
            )
        }
        TextButton(
            onClick = {
                viewModel.filterIssues(
                    onSuccess = closeDialog
                )
            }
        ) {
            Text(
                text = stringResource(string.filter)
            )
        }
    }
}