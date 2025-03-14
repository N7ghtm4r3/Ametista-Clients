// TODO: TO SET 
//@file:OptIn(ExperimentalMaterial3Api::class)
//
//package com.tecknobit.ametista.ui.screens.session.presenter
//
//import ametista.composeapp.generated.resources.Res.string
//import ametista.composeapp.generated.resources.about_me
//import ametista.composeapp.generated.resources.add_viewer
//import ametista.composeapp.generated.resources.add_viewer_info
//import ametista.composeapp.generated.resources.email
//import ametista.composeapp.generated.resources.name
//import ametista.composeapp.generated.resources.session
//import ametista.composeapp.generated.resources.session_members
//import ametista.composeapp.generated.resources.surname
//import ametista.composeapp.generated.resources.wrong_email
//import ametista.composeapp.generated.resources.wrong_name
//import ametista.composeapp.generated.resources.wrong_surname
//import androidx.compose.animation.AnimatedVisibility
//import androidx.compose.animation.fadeIn
//import androidx.compose.animation.fadeOut
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.AssignmentInd
//import androidx.compose.material.icons.filled.Done
//import androidx.compose.material.icons.filled.Groups3
//import androidx.compose.material.icons.filled.PersonAdd
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.FloatingActionButton
//import androidx.compose.material3.HorizontalDivider
//import androidx.compose.material3.Icon
//import androidx.compose.material3.LargeTopAppBar
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.ModalBottomSheet
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.SegmentedButton
//import androidx.compose.material3.SingleChoiceSegmentedButtonRow
//import androidx.compose.material3.SnackbarHost
//import androidx.compose.material3.Text
//import androidx.compose.material3.TopAppBarDefaults
//import androidx.compose.material3.rememberModalBottomSheetState
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.MutableState
//import androidx.compose.runtime.NonRestartableComposable
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.vector.ImageVector
//import androidx.compose.ui.text.input.ImeAction
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.tecknobit.ametista.displayFontFamily
//import com.tecknobit.ametista.localUser
//import com.tecknobit.ametista.ui.screens.session.components.AboutMe
//import com.tecknobit.ametista.ui.screens.session.components.SessionMembers
//import com.tecknobit.ametista.ui.screens.session.presentation.SessionScreenViewModel
//import com.tecknobit.ametista.ui.screens.session.presentation.SessionScreenViewModel.SessionScreenSection
//import com.tecknobit.ametista.ui.screens.session.presentation.SessionScreenViewModel.SessionScreenSection.ABOUT_ME
//import com.tecknobit.ametista.ui.screens.session.presentation.SessionScreenViewModel.SessionScreenSection.MEMBERS
//import com.tecknobit.ametista.ui.theme.AmetistaTheme
//import com.tecknobit.equinox.inputs.InputValidator.isEmailValid
//import com.tecknobit.equinox.inputs.InputValidator.isNameValid
//import com.tecknobit.equinox.inputs.InputValidator.isSurnameValid
//import com.tecknobit.equinoxcompose.components.EquinoxOutlinedTextField
//import com.tecknobit.equinoxcompose.helpers.session.EquinoxScreen
//import com.tecknobit.equinoxcompose.session.screens.EquinoxScreen
//import org.jetbrains.compose.resources.StringResource
//import org.jetbrains.compose.resources.stringResource
//
///**
// * The [SessionScreen] class is used to display the details about the current session
// *
// * @author N7ghtm4r3 - Tecknobit
// * @see EquinoxScreen
// * 
// */
//class SessionScreen : EquinoxScreen<SessionScreenViewModel>(
//    viewModel = SessionScreenViewModel()
//) {
//
//    /**
//     * `addViewer` -> Whether the user requested to add a new user in the system
//     */
//    private lateinit var addViewer: MutableState<Boolean>
//
//    /**
//     * Method to arrange the content of the screen to display
//     */
//    @Composable
//    override fun ArrangeScreenContent() {
//        AmetistaTheme {
//            Scaffold(
//                topBar = {
//                    LargeTopAppBar(
//                        colors = TopAppBarDefaults.largeTopAppBarColors(
//                            containerColor = MaterialTheme.colorScheme.primary,
//                            titleContentColor = Color.White
//                        ),
//                        navigationIcon = { NavButton() },
//                        title = {
//                            Text(
//                                text = stringResource(string.session)
//                            )
//                        }
//                    )
//                },
//                floatingActionButton = {
//                    AnimatedVisibility(
//                        visible = viewModel.sessionScreenSection.value == MEMBERS && localUser.isAdmin(),
//                        enter = fadeIn(),
//                        exit = fadeOut()
//                    ) {
//                        FloatingActionButton(
//                            onClick = { addViewer.value = true }
//                        ) {
//                            Icon(
//                                imageVector = Icons.Default.PersonAdd,
//                                contentDescription = null
//                            )
//                        }
//                        AddViewer()
//                    }
//                },
//                snackbarHost = { SnackbarHost(viewModel.snackbarHostState!!) }
//            ) { paddingValues ->
//                Column(
//                    modifier = Modifier
//                        .padding(
//                            top = paddingValues.calculateTopPadding() - 5.dp,
//                            bottom = paddingValues.calculateBottomPadding() + 16.dp
//                        )
//                        .fillMaxSize(),
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    SectionSelector()
//                    DisplaySection()
//                }
//            }
//        }
//    }
//
//    /**
//     * The selector used to display the specific session section
//     */
//    @Composable
//    @NonRestartableComposable
//    private fun SectionSelector() {
//        SingleChoiceSegmentedButtonRow(
//            modifier = Modifier
//                .padding(
//                    bottom = 10.dp
//                )
//        ) {
//            val lastEntry = SessionScreenSection.entries.last()
//            SessionScreenSection.entries.forEach { section ->
//                SegmentedButton(
//                    selected = viewModel.sessionScreenSection.value == section,
//                    icon = {
//                        Icon(
//                            imageVector = section.icon(),
//                            contentDescription = null
//                        )
//                    },
//                    label = {
//                        Text(
//                            text = stringResource(section.tabTitle())
//                        )
//                    },
//                    shape = if (section == lastEntry) {
//                        RoundedCornerShape(
//                            topEnd = 0.dp,
//                            bottomEnd = 10.dp
//                        )
//                    } else {
//                        RoundedCornerShape(
//                            topStart = 0.dp,
//                            bottomStart = 10.dp
//                        )
//                    },
//                    onClick = { viewModel.sessionScreenSection.value = section }
//                )
//            }
//        }
//    }
//
//    /**
//     * Method to get the representative icon for the session section
//     *
//     * @return the representative icon as [ImageVector]
//     */
//    private fun SessionScreenSection.icon(): ImageVector {
//        return when (this) {
//            ABOUT_ME -> Icons.Default.AssignmentInd
//            MEMBERS -> Icons.Default.Groups3
//        }
//    }
//
//    /**
//     * Method to get the representative title for the session section
//     *
//     * @return the representative title as [ImageVector]
//     */
//    private fun SessionScreenSection.tabTitle(): StringResource {
//        return when (this) {
//            ABOUT_ME -> string.about_me
//            MEMBERS -> string.session_members
//        }
//    }
//
//    /**
//     * Wrapper method to display the specific session section
//     */
//    @Composable
//    @NonRestartableComposable
//    private fun DisplaySection() {
//        AnimatedVisibility(
//            visible = viewModel.sessionScreenSection.value == ABOUT_ME
//        ) {
//            AboutMe(
//                screenViewModel = viewModel
//            )
//        }
//        AnimatedVisibility(
//            visible = viewModel.sessionScreenSection.value == MEMBERS
//        ) {
//            SessionMembers(
//                screenViewModel = viewModel
//            )
//        }
//    }
//
//    /**
//     * Layout to add a new [com.tecknobit.ametistacore.models.AmetistaUser.Role.VIEWER] in the system
//     */
//    @Composable
//    @NonRestartableComposable
//    private fun AddViewer() {
//        if (addViewer.value) {
//            viewModel.suspendRefresher()
//            viewModel.viewerName = remember { mutableStateOf("") }
//            viewModel.viewerNameError = remember { mutableStateOf(false) }
//            viewModel.viewerSurname = remember { mutableStateOf("") }
//            viewModel.viewerSurnameError = remember { mutableStateOf(false) }
//            viewModel.viewerEmail = remember { mutableStateOf("") }
//            viewModel.viewerEmailError = remember { mutableStateOf(false) }
//            val closeModal = {
//                viewModel.restartRefresher()
//                addViewer.value = false
//            }
//            ModalBottomSheet(
//                sheetState = rememberModalBottomSheetState(
//                    skipPartiallyExpanded = true
//                ),
//                onDismissRequest = closeModal
//            ) {
//                Column(
//                    modifier = Modifier
//                        .padding(
//                            bottom = 16.dp
//                        )
//                ) {
//                    Text(
//                        modifier = Modifier
//                            .fillMaxWidth(),
//                        text = stringResource(string.add_viewer),
//                        textAlign = TextAlign.Center,
//                        fontFamily = displayFontFamily,
//                        fontSize = 20.sp
//                    )
//                    HorizontalDivider(
//                        color = MaterialTheme.colorScheme.primary
//                    )
//                    Text(
//                        modifier = Modifier
//                            .padding(
//                                horizontal = 16.dp
//                            )
//                            .padding(
//                                top = 16.dp
//                            ),
//                        text = stringResource(string.add_viewer_info),
//                        textAlign = TextAlign.Justify
//                    )
//                    ViewerForm()
//                }
//            }
//        }
//    }
//
//    /**
//     * The form to fill with the [com.tecknobit.ametistacore.models.AmetistaUser.Role.VIEWER] information
//     */
//    @Composable
//    @NonRestartableComposable
//    private fun ViewerForm() {
//        val keyboardOptions = KeyboardOptions(
//            imeAction = ImeAction.Next
//        )
//        Column(
//            modifier = Modifier
//                .padding(
//                    horizontal = 16.dp
//                )
//        ) {
//            Row(
//                horizontalArrangement = Arrangement.spacedBy(10.dp)
//            ) {
//                EquinoxOutlinedTextField(
//                    modifier = Modifier
//                        .weight(1f),
//                    value = viewModel.viewerName,
//                    label = stringResource(string.name),
//                    keyboardOptions = keyboardOptions,
//                    errorText = stringResource(string.wrong_name),
//                    isError = viewModel.viewerNameError,
//                    validator = { isNameValid(it) }
//                )
//                EquinoxOutlinedTextField(
//                    modifier = Modifier
//                        .weight(1f),
//                    value = viewModel.viewerSurname,
//                    label = stringResource(string.surname),
//                    keyboardOptions = keyboardOptions,
//                    errorText = stringResource(string.wrong_surname),
//                    isError = viewModel.viewerSurnameError,
//                    validator = { isSurnameValid(it) }
//                )
//            }
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.spacedBy(10.dp)
//            ) {
//                EquinoxOutlinedTextField(
//                    modifier = Modifier
//                        .weight(1f),
//                    value = viewModel.viewerEmail,
//                    label = string.email,
//                    mustBeInLowerCase = true,
//                    allowsBlankSpaces = false,
//                    keyboardOptions = KeyboardOptions(
//                        imeAction = ImeAction.None,
//                        keyboardType = KeyboardType.Email
//                    ),
//                    errorText = string.wrong_email,
//                    isError = viewModel.viewerEmailError,
//                    validator = { isEmailValid(it) }
//                )
//                Column(
//                    modifier = Modifier
//                        .weight(1f),
//                    horizontalAlignment = Alignment.End
//                ) {
//                    FloatingActionButton(
//                        modifier = Modifier
//                            .padding(
//                                top = 5.dp
//                            ),
//                        shape = CircleShape,
//                        onClick = {
//                            viewModel.addViewer(
//                                onSuccess = { addViewer.value = false }
//                            )
//                        }
//                    ) {
//                        Icon(
//                            imageVector = Icons.Default.Done,
//                            contentDescription = null
//                        )
//                    }
//                }
//            }
//        }
//    }
//
//    /**
//     * Method to collect or instantiate the states of the screen
//     */
//    @Composable
//    override fun CollectStates() {
//        viewModel.sessionScreenSection = remember { mutableStateOf(ABOUT_ME) }
//        addViewer = remember { mutableStateOf(false) }
//    }
//
//}