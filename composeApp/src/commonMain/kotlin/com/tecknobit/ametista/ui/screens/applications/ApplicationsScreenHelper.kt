package com.tecknobit.ametista.ui.screens.applications

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier

@Composable
@NonRestartableComposable
expect fun Applications(
    paddingValues: PaddingValues,
    viewModel: ApplicationsScreenViewModel
)

@Composable
@NonRestartableComposable
expect fun ApplicationItem()

@Composable
@NonRestartableComposable
expect fun ApplicationIcon(
    modifier: Modifier = Modifier
)