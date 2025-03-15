@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalRichTextApi::class)

package com.tecknobit.ametista.ui.screens.application.components

import ametista.composeapp.generated.resources.Res.string
import ametista.composeapp.generated.resources.application_id_text
import ametista.composeapp.generated.resources.application_id_title
import ametista.composeapp.generated.resources.check_connection_result
import ametista.composeapp.generated.resources.check_connection_result_text
import ametista.composeapp.generated.resources.connect_platform
import ametista.composeapp.generated.resources.create_config_file
import ametista.composeapp.generated.resources.create_config_file_text
import ametista.composeapp.generated.resources.got_it
import ametista.composeapp.generated.resources.implement_the_engine
import ametista.composeapp.generated.resources.implement_the_engine_text
import ametista.composeapp.generated.resources.invoke_connect_method
import ametista.composeapp.generated.resources.invoke_connect_method_text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mohamedrejeb.richeditor.annotation.ExperimentalRichTextApi
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material.RichText
import com.pushpal.jetlime.EventPosition
import com.pushpal.jetlime.ItemsList
import com.pushpal.jetlime.JetLimeColumn
import com.pushpal.jetlime.JetLimeEvent
import com.pushpal.jetlime.JetLimeEventDefaults
import com.tecknobit.ametista.displayFontFamily
import com.tecknobit.ametista.ui.screens.application.data.ConnectionProcedureStep
import com.tecknobit.equinoxcompose.utilities.copyOnClipboard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

/**
 * Step of the procedure to connect a platform for an application to the system
 *
 * @param state The state useful to manage the visibility of the [ModalBottomSheet]
 * @param scope The coroutine useful to manage the visibility of the [ModalBottomSheet]
 * @param applicationId The identifier of the application
 */
@Composable
@NonRestartableComposable
fun ConnectionProcedure(
    state: SheetState,
    scope: CoroutineScope,
    applicationId: String,
) {
    if (state.isVisible) {
        ModalBottomSheet(
            sheetState = state,
            onDismissRequest = {
                scope.launch {
                    state.hide()
                }
            }
        ) {
            Text(
                modifier = Modifier
                    .padding(
                        start = 16.dp,
                        bottom = 16.dp
                    ),
                text = stringResource(string.connect_platform),
                fontFamily = displayFontFamily,
                fontSize = 22.sp
            )
            HorizontalDivider()
            ConnectionSteps(
                applicationId = applicationId
            )
            TextButton(
                modifier = Modifier
                    .padding(
                        end = 10.dp
                    )
                    .align(Alignment.End),
                onClick = {
                    scope.launch {
                        state.hide()
                    }
                }
            ) {
                Text(
                    text = stringResource(string.got_it)
                )
            }
        }
    }
}

/**
 * Steps list of the [ConnectionProcedure] component
 *
 * @param applicationId The identifier of the application
 */
@Composable
@NonRestartableComposable
private fun ConnectionSteps(
    applicationId: String,
) {
    val steps = listOf(
        ConnectionProcedureStep(
            title = stringResource(string.implement_the_engine),
            description = stringResource(string.implement_the_engine_text)
        ),
        ConnectionProcedureStep(
            title = stringResource(string.create_config_file),
            description = stringResource(string.create_config_file_text)
        ),
        ConnectionProcedureStep(
            title = stringResource(string.application_id_title),
            description = stringResource(string.application_id_text),
            onClick = {
                copyOnClipboard(
                    content = applicationId
                )
            }
        ),
        ConnectionProcedureStep(
            title = stringResource(string.invoke_connect_method),
            description = stringResource(string.invoke_connect_method_text)
        ),
        ConnectionProcedureStep(
            title = stringResource(string.check_connection_result),
            description = stringResource(string.check_connection_result_text)
        )
    )
    JetLimeColumn(
        contentPadding = PaddingValues(
            start = 16.dp,
            top = 16.dp,
            end = 16.dp,
            bottom = 10.dp
        ),
        itemsList = ItemsList(
            items = steps
        ),
        key = { _, step -> step.title }
    ) { _, step, position ->
        ConnectionStep(
            position = position,
            step = step
        )
    }
}

/**
 * Dedicated component to display a [ConnectionProcedureStep]
 *
 * @param position The number of the step
 * @param step The step to represent
 */
@Composable
@NonRestartableComposable
private fun ConnectionStep(
    position: EventPosition,
    step: ConnectionProcedureStep,
) {
    JetLimeEvent(
        style = JetLimeEventDefaults.eventStyle(
            position = position
        ),
    ) {
        Column {
            val title = rememberRichTextState()
            title.setMarkdown(
                markdown = step.title
            )
            RichText(
                state = title,
                fontSize = 20.sp,
                textAlign = TextAlign.Justify,
                color = MaterialTheme.colorScheme.onSurface
            )
            val description = rememberRichTextState()
            description.setMarkdown(
                markdown = step.description
            )
            description.config.linkColor = MaterialTheme.colorScheme.primary
            description.config.linkTextDecoration = TextDecoration.None
            RichText(
                modifier = Modifier
                    .clickable(
                        enabled = step.onClick != null,
                        onClick = {
                            step.onClick?.invoke()
                        }
                    ),
                color = MaterialTheme.colorScheme.onSurface,
                state = description,
                fontSize = 14.sp,
                textAlign = TextAlign.Justify
            )
        }
    }
}