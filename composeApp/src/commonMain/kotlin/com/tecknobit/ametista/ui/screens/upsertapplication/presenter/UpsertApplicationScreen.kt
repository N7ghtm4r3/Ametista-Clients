package com.tecknobit.ametista.ui.screens.upsertapplication.presenter

import ametista.composeapp.generated.resources.Res
import ametista.composeapp.generated.resources.add_application
import ametista.composeapp.generated.resources.app_description
import ametista.composeapp.generated.resources.app_name_field
import ametista.composeapp.generated.resources.confirm
import ametista.composeapp.generated.resources.edit_application
import ametista.composeapp.generated.resources.logo
import ametista.composeapp.generated.resources.wrong_app_description
import ametista.composeapp.generated.resources.wrong_app_name_field
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.tecknobit.ametista.imageLoader
import com.tecknobit.ametista.ui.components.NavButton
import com.tecknobit.ametista.ui.screens.applications.components.getApplicationIconCompleteUrl
import com.tecknobit.ametista.ui.screens.applications.data.AmetistaApplication
import com.tecknobit.ametista.ui.screens.upsertapplication.presentation.UpsertApplicationScreenViewModel
import com.tecknobit.ametista.ui.theme.AmetistaTheme
import com.tecknobit.ametistacore.helpers.AmetistaValidator.isAppDescriptionValid
import com.tecknobit.ametistacore.helpers.AmetistaValidator.isAppNameValid
import com.tecknobit.equinoxcompose.components.EquinoxOutlinedTextField
import com.tecknobit.equinoxcompose.session.ManagedContent
import com.tecknobit.equinoxcompose.session.screens.EquinoxScreen
import com.tecknobit.equinoxcompose.utilities.responsiveAssignment
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.core.PickerMode
import io.github.vinceglb.filekit.core.PickerType
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/**
 * The `UpsertApplicationScreen` class is used to allow the user to create or update an application
 *
 * @param applicationId The identifier of the application to edit
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see EquinoxScreen
 *
 */
class UpsertApplicationScreen(
    applicationId: String?,
) : EquinoxScreen<UpsertApplicationScreenViewModel>(
    viewModel = UpsertApplicationScreenViewModel(
        applicationId = applicationId
    )
) {

    /**
     * `isUpdating` -> whether the operation is an update of an existing application
     */
    private val isUpdating = applicationId != null

    /**
     * `application` -> the application to edit
     */
    private lateinit var application: State<AmetistaApplication?>

    /**
     * Method to arrange the content of the screen to display
     */
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun ArrangeScreenContent() {
        AmetistaTheme {
            ManagedContent(
                viewModel = viewModel,
                initialDelay = 500,
                loadingRoutine = if (isUpdating) {
                    {
                        application.value != null
                    }
                } else
                    null,
                content = {
                    CollectStatesAfterLoading()
                    Scaffold(
                        topBar = {
                            LargeTopAppBar(
                                navigationIcon = { NavButton() },
                                colors = TopAppBarDefaults.largeTopAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    titleContentColor = Color.White
                                ),
                                title = {
                                    Text(
                                        text = stringResource(
                                            if (isUpdating)
                                                Res.string.edit_application
                                            else
                                                Res.string.add_application
                                        ),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            )
                        }
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(
                                    top = it.calculateTopPadding() + 16.dp,
                                    bottom = 16.dp
                                )
                                .padding(
                                    horizontal = 16.dp
                                )
                                .verticalScroll(rememberScrollState())
                                .navigationBarsPadding(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Form()
                        }
                    }
                }
            )
        }
    }

    /**
     * Form where the user can insert the application details to update or insert an application
     */
    @Composable
    @NonRestartableComposable
    private fun Form() {
        Column(
            modifier = Modifier
                // TODO: TO SET
                .widthIn(
                    max = 1280.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            AppIconPicker()
            EquinoxOutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(
                    size = 10.dp
                ),
                value = viewModel.appName,
                isError = viewModel.appNameError,
                placeholder = Res.string.app_name_field,
                errorText = Res.string.wrong_app_name_field,
                validator = { name -> isAppNameValid(name) },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                )
            )
            EquinoxOutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(
                    size = 10.dp
                ),
                value = viewModel.appDescription,
                isError = viewModel.appDescriptionError,
                placeholder = Res.string.app_description,
                errorText = Res.string.wrong_app_description,
                minLines = 10,
                maxLines = 10,
                validator = { description -> isAppDescriptionValid(description) },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                )
            )
            UpsertButton()
        }
    }

    /**
     * The picker to choose the application icon
     */
    @Composable
    @NonRestartableComposable
    private fun AppIconPicker() {
        val launcher = rememberFilePickerLauncher(
            type = PickerType.Image,
            mode = PickerMode.Single
        ) { appIcon ->
            if (appIcon?.path != null) {
                viewModel.appIcon.value = appIcon.path!!
                viewModel.appIconPayload = appIcon
            }
        }
        Box(
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(125.dp)
                    .border(
                        width = 1.dp,
                        color = viewModel.appIconBorderColor.value,
                        shape = CircleShape
                    )
                    .clip(CircleShape),
                model = ImageRequest.Builder(LocalPlatformContext.current)
                    .data(
                        data = getApplicationIconCompleteUrl(
                            url = viewModel.appIcon.value
                        )
                    )
                    .crossfade(true)
                    .crossfade(500)
                    .build(),
                imageLoader = imageLoader,
                contentDescription = "Application icon",
                contentScale = ContentScale.Crop,
                error = painterResource(Res.drawable.logo)
            )
            IconButton(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color(0xD0DFD8D8))
                    .size(35.dp)
                    .align(Alignment.BottomEnd),
                onClick = { launcher.launch() }
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null
                )
            }
        }
    }

    /**
     * Custom [Button] to execute the update or insert action
     */
    @Composable
    @NonRestartableComposable
    private fun UpsertButton() {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = responsiveAssignment(
                onExpandedSizeClass = { Alignment.End },
                onMediumSizeClass = { Alignment.End },
                onCompactSizeClass = { Alignment.CenterHorizontally }
            )
        ) {
            Button(
                modifier = responsiveAssignment(
                    onExpandedSizeClass = {
                        Modifier
                    },
                    onMediumSizeClass = {
                        Modifier
                    },
                    onCompactSizeClass = {
                        Modifier
                            .height(45.dp)
                            .fillMaxWidth()
                    }
                ),
                shape = RoundedCornerShape(
                    size = 10.dp
                ),
                onClick = { viewModel.upsertApplication() }
            ) {
                Text(
                    text = stringResource(Res.string.confirm)
                )
            }
        }
    }

    /**
     * Method invoked when the [ShowContent] composable has been started
     */
    override fun onStart() {
        super.onStart()
        viewModel.retrieveApplication()
    }

    /**
     * Method to collect or instantiate the states of the screen
     */
    @Composable
    override fun CollectStates() {
        application = viewModel.application.collectAsState(
            initial = null
        )
        viewModel.appNameError = remember { mutableStateOf(false) }
        viewModel.appDescriptionError = remember { mutableStateOf(false) }
    }

    /**
     * Method to collect or instantiate the states of the screen after a loading required to
     * correctly assign an initial value to the states
     */
    @Composable
    @NonRestartableComposable
    override fun CollectStatesAfterLoading() {
        super.CollectStatesAfterLoading()
        viewModel.appIcon = remember {
            mutableStateOf(
                if (isUpdating)
                    application.value!!.applicationIcon
                else
                    ""
            )
        }
        val primary = MaterialTheme.colorScheme.primary
        viewModel.appIconBorderColor = remember { mutableStateOf(primary) }
        viewModel.appName = remember {
            mutableStateOf(
                if (isUpdating)
                    application.value!!.name
                else
                    ""
            )
        }
        viewModel.appDescription = remember {
            mutableStateOf(
                if (isUpdating)
                    application.value!!.description
                else
                    ""
            )
        }
    }

}