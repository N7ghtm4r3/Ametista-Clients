@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.ametista.ui.screens.applications

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.ametista.bodyFontFamily
import com.tecknobit.ametista.displayFontFamily
import com.tecknobit.ametistacore.models.AmetistaApplication

@Composable
@NonRestartableComposable
expect fun Applications(
    paddingValues: PaddingValues,
    viewModel: ApplicationsScreenViewModel
)

@Composable
@NonRestartableComposable
expect fun ApplicationItem(
    application: AmetistaApplication
)

@Composable
@NonRestartableComposable
expect fun ApplicationIcon(
    modifier: Modifier = Modifier,
    application: AmetistaApplication
)

@Composable
@NonRestartableComposable
fun ExpandApplicationDescription(
    expand: MutableState<Boolean>,
    application: AmetistaApplication
) {
    if(expand.value) {
        ModalBottomSheet(
            onDismissRequest = { expand.value = false }
        ) {
            Column (
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    modifier = Modifier
                        .padding(
                            start = 16.dp
                        ),
                    text = application.name,
                    fontFamily = displayFontFamily,
                    fontSize = 20.sp
                )
                HorizontalDivider()
                Text(
                    modifier = Modifier
                        .padding(
                            horizontal = 16.dp
                        ),
                    text = application.description,
                    textAlign = TextAlign.Justify,
                    fontFamily = bodyFontFamily,
                    fontSize = 14.sp
                )
            }
        }
    }
}