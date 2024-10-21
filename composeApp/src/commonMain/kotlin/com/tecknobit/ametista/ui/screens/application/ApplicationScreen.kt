@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalRichTextApi::class)

package com.tecknobit.ametista.ui.screens.application

import ametista.composeapp.generated.resources.Res
import ametista.composeapp.generated.resources.check_connection_result
import ametista.composeapp.generated.resources.check_connection_result_text
import ametista.composeapp.generated.resources.connect_platform
import ametista.composeapp.generated.resources.got_it
import ametista.composeapp.generated.resources.implement_the_engine
import ametista.composeapp.generated.resources.implement_the_engine_text
import ametista.composeapp.generated.resources.invoke_connect_method
import ametista.composeapp.generated.resources.invoke_connect_method_text
import ametista.composeapp.generated.resources.no_connected_platforms
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.LinkOff
import androidx.compose.material.icons.outlined.ArrowCircleDown
import androidx.compose.material.icons.outlined.ArrowCircleUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mohamedrejeb.richeditor.annotation.ExperimentalRichTextApi
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichText
import com.pushpal.jetlime.EventPosition
import com.pushpal.jetlime.ItemsList
import com.pushpal.jetlime.JetLimeColumn
import com.pushpal.jetlime.JetLimeEvent
import com.pushpal.jetlime.JetLimeEventDefaults
import com.tecknobit.ametista.displayFontFamily
import com.tecknobit.ametista.helpers.PlatformsCustomGrid
import com.tecknobit.ametista.model.ConnectionProcedureStep
import com.tecknobit.ametista.navigator
import com.tecknobit.ametista.ui.components.DeleteApplication
import com.tecknobit.ametista.ui.components.WorkOnApplication
import com.tecknobit.ametista.ui.screens.AmetistaScreen
import com.tecknobit.ametistacore.models.AmetistaApplication
import com.tecknobit.ametistacore.models.Platform.entries
import com.tecknobit.equinoxcompose.components.EmptyListUI
import com.tecknobit.equinoxcompose.helpers.session.ManagedContent
import org.jetbrains.compose.resources.stringResource

class ApplicationScreen(
    private val applicationId: String
) : AmetistaScreen<ApplicationScreenViewModel>(
    viewModel = ApplicationScreenViewModel(
        applicationId = applicationId
    )
) {

    private lateinit var application: State<AmetistaApplication?>

    private lateinit var showConnectionProcedure: MutableState<Boolean>

    /**
     * Function to arrange the content of the screen to display
     *
     * No-any params required
     */
    @Composable
    override fun ArrangeScreenContent() {
        AnimatedVisibility(
            visible = application.value != null,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            ManagedContent(
                viewModel = viewModel!!,
                content = {
                    Scaffold(
                        topBar = {
                            LargeTopAppBar(
                                colors = TopAppBarDefaults.largeTopAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer
                                ),
                                navigationIcon = { NavButton() },
                                title = {
                                    Text(
                                        text = application.value!!.name
                                    )
                                },
                                actions = {
                                    IconButton(
                                        onClick = { viewModel!!.workOnApplication.value = true }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = null
                                        )
                                    }
                                    WorkOnApplication(
                                        show = viewModel!!.workOnApplication,
                                        viewModel = viewModel!!,
                                        application = application.value
                                    )
                                    val deleteApplication = remember { mutableStateOf(false) }
                                    IconButton(
                                        onClick = { deleteApplication.value = true }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = null
                                        )
                                    }
                                    DeleteApplication(
                                        show = deleteApplication,
                                        application = application.value!!,
                                        viewModel = viewModel!!,
                                        onDelete = { navigator.goBack() }
                                    )
                                }
                            )
                        },
                        snackbarHost = {
                            SnackbarHost(
                                hostState = viewModel!!.snackbarHostState!!
                            )
                        },
                        floatingActionButton = {
                            AnimatedVisibility(
                                visible = application.value!!.platforms.size != entries.size,
                                enter = fadeIn(),
                                exit = fadeOut()
                            ) {
                                FloatingActionButton(
                                    onClick = { showConnectionProcedure.value = true }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Link,
                                        contentDescription = null
                                    )
                                }
                            }
                        }
                    ) { paddingValues ->
                        Column {
                            ExpandableText(
                                paddingValues = paddingValues
                            )
                            PlatformsSections()
                            ConnectionProcedure()
                        }
                    }
                }
            )
        }
    }

    @Composable
    @NonRestartableComposable
    // TODO: THE ORIGINAL COMPONENT MUST BE GENERAL AND NOT SPECIFIC LIKE THIS, BUT ALLOWING THE CUSTOMIZATION
    private fun ExpandableText(
        paddingValues: PaddingValues
    ) {
        var expanded by remember { mutableStateOf(false) }
        var expandable by remember { mutableStateOf(false) }
        Column(
            modifier = Modifier
                .padding(
                    top = paddingValues.calculateTopPadding() + 16.dp,
                    bottom = 16.dp
                )
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .widthIn(
                        max = 750.dp
                    )
                    .verticalScroll(rememberScrollState())
            ) {
                AnimatedVisibility(
                    visible = expanded
                ) {
                    Text(
                        modifier = Modifier
                            .padding(
                                horizontal = 16.dp
                            ),
                        text = application.value!!.description,
                        textAlign = TextAlign.Justify,
                    )
                }
                if (!expanded) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 16.dp
                            ),
                        text = application.value!!.description,
                        textAlign = TextAlign.Justify,
                        onTextLayout = { textLayoutResult ->
                            expandable = textLayoutResult.lineCount >= 5
                        },
                        maxLines = 5,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                if (expandable) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        HorizontalDivider()
                        IconButton(
                            onClick = { expanded = !expanded }
                        ) {
                            Icon(
                                modifier = Modifier
                                    .size(30.dp),
                                imageVector = if (expanded)
                                    Outlined.ArrowCircleUp
                                else
                                    Outlined.ArrowCircleDown,
                                contentDescription = null
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    @NonRestartableComposable
    private fun PlatformsSections() {
        val platforms = application.value!!.platforms
        if (platforms.isNotEmpty()) {
            PlatformsCustomGrid(
                viewModel = viewModel!!,
                applicationPlatforms = platforms
            )
        } else {
            EmptyListUI(
                icon = Icons.Default.LinkOff,
                subText = Res.string.no_connected_platforms
            )
        }
    }

    @Composable
    @NonRestartableComposable
    private fun ConnectionProcedure() {
        if (showConnectionProcedure.value) {
            ModalBottomSheet(
                sheetState = rememberModalBottomSheetState(
                    skipPartiallyExpanded = true
                ),
                onDismissRequest = { showConnectionProcedure.value = false }
            ) {
                Text(
                    modifier = Modifier
                        .padding(
                            start = 16.dp,
                            bottom = 16.dp
                        ),
                    text = stringResource(Res.string.connect_platform),
                    fontFamily = displayFontFamily,
                    fontSize = 22.sp
                )
                HorizontalDivider()
                ConnectionSteps()
                TextButton(
                    modifier = Modifier
                        .padding(
                            end = 10.dp
                        )
                        .align(Alignment.End),
                    onClick = { showConnectionProcedure.value = false }
                ) {
                    Text(
                        text = stringResource(Res.string.got_it)
                    )
                }
            }
        }
    }

    @Composable
    @NonRestartableComposable
    private fun ConnectionSteps() {
        val steps = listOf(
            ConnectionProcedureStep(
                title = stringResource(Res.string.implement_the_engine),
                description = stringResource(Res.string.implement_the_engine_text)
            ),
            ConnectionProcedureStep(
                title = stringResource(Res.string.invoke_connect_method),
                description = stringResource(Res.string.invoke_connect_method_text)
            ),
            ConnectionProcedureStep(
                title = stringResource(Res.string.check_connection_result),
                description = stringResource(Res.string.check_connection_result_text)
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

    @Composable
    @NonRestartableComposable
    private fun ConnectionStep(
        position: EventPosition,
        step: ConnectionProcedureStep
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
                    textAlign = TextAlign.Justify
                )
                val description = rememberRichTextState()
                description.setMarkdown(
                    markdown = step.description
                )
                description.config.linkColor = MaterialTheme.colorScheme.primary
                description.config.linkTextDecoration = TextDecoration.None
                RichText(
                    state = description,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Justify
                )
            }
        }
    }

    /**
     * Function invoked when the [ShowContent] composable has been started
     *
     * No-any params required
     */
    override fun onStart() {
        super.onStart()
        viewModel!!.refreshApplication()
    }

    /**
     * Function to collect or instantiate the states of the screen
     *
     * No-any params required
     */
    @Composable
    override fun CollectStates() {
        application = viewModel!!.application.collectAsState()
        viewModel!!.workOnApplication = remember { mutableStateOf(false) }
        showConnectionProcedure = remember { mutableStateOf(false) }
    }

}