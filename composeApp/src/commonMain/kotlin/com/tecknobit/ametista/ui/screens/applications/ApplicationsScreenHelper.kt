// TODO: TO SET
//@file:OptIn(ExperimentalMaterial3Api::class)
//
//package com.tecknobit.ametista.ui.screens.applications
//
//import ametista.composeapp.generated.resources.Res.string
//import ametista.composeapp.generated.resources.no_applications
//import androidx.compose.animation.AnimatedVisibility
//import androidx.compose.animation.fadeIn
//import androidx.compose.animation.fadeOut
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.HorizontalDivider
//import androidx.compose.material3.ModalBottomSheet
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.MutableState
//import androidx.compose.runtime.NonRestartableComposable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.tecknobit.ametista.bodyFontFamily
//import com.tecknobit.ametista.displayFontFamily
//import com.tecknobit.ametista.localUser
//import com.tecknobit.ametista.navigator
//import com.tecknobit.ametista.ui.icons.Boxes
//import com.tecknobit.ametista.ui.screens.applications.data.AmetistaApplication
//import com.tecknobit.ametista.ui.screens.applications.presentation.ApplicationsScreenViewModel
//import com.tecknobit.ametistacore.models.AmetistaApplication
//import com.tecknobit.apimanager.annotations.Wrapper
//import com.tecknobit.equinoxcompose.components.EmptyListUI
//import com.tecknobit.equinoxcore.annotations.Wrapper
//
///**
// * **ICONS_REGEX** -> the regex to determine whether the application icon is selected one or provided
// * by the server
// */
//private const val ICONS_REGEX = "icons"
//
///**
// * The component to display the applications list
// *
// * @param viewModel The viewmodel related to the [com.tecknobit.ametista.ui.screens.applications.ApplicationsScreen]
// */
//@Composable
//@NonRestartableComposable
//expect fun Applications(
//    viewModel: ApplicationsScreenViewModel
//)
//
///**
// * The layout to display when the applications list is empty
// *
// * @param noApplications Whether the applications list is empty or not
// */
//@Composable
//@NonRestartableComposable
//fun NoApplications(
//    noApplications: Boolean
//) {
//    AnimatedVisibility(
//        visible = noApplications,
//        enter = fadeIn(),
//        exit = fadeOut()
//    ) {
//        EmptyListUI(
//            imageModifier = Modifier
//                .size(150.dp),
//            icon = Boxes,
//            subText = string.no_applications,
//            textStyle = TextStyle(
//                fontFamily = displayFontFamily,
//                fontSize = 20.sp
//            )
//        )
//    }
//}
//
///**
// * The component to the details of an [AmetistaApplication]
// *
// * @param isTheFirst Whether is the first application of the list displayed
// * @param application The application to display
// * @param viewModel The viewmodel related to the [com.tecknobit.ametista.ui.screens.applications.ApplicationsScreen]
// */
//@Composable
//@NonRestartableComposable
//expect fun ApplicationItem(
//    isTheFirst: Boolean = false,
//    application: AmetistaApplication,
//    viewModel: ApplicationsScreenViewModel
//)
//
///**
// * Method to get the complete url to display the application icon
// *
// * @param application The application from get the icon url
// *
// * @return the complete icon url as [String]
// */
//@Wrapper
//fun getApplicationIconCompleteUrl(
//    application: AmetistaApplication
//): String {
//    return getApplicationIconCompleteUrl(
//        url = application.icon
//    )
//}
//
///**
// * Method to get the complete url to display the application icon
// *
// * @param url The slice of the icon url
// *
// * @return the complete icon url as [String]
// */
//fun getApplicationIconCompleteUrl(
//    url: String
//): String {
//    if (!url.startsWith(ICONS_REGEX))
//        return url
//    return localUser.hostAddress + "/" + url
//}
//
///**
// * Method to navigate to the [com.tecknobit.ametista.ui.screens.application.ApplicationScreen] screen
// *
// * @param application The application related to the screen to show
// */
//fun navToApplicationScreen(
//    application: AmetistaApplication
//) {
//    navigator.navigate("$APPLICATION_SCREEN/${application.id}")
//}
//
///**
// * The component to display the icon of the application
// *
// * @param modifier The [Modifier] to apply to the component
// * @param application The application to display its icon
// */
//@Composable
//@NonRestartableComposable
//expect fun ApplicationIcon(
//    modifier: Modifier = Modifier,
//    application: AmetistaApplication
//)
//
///**
// * The component to display the description of the application
// *
// * @param expand Whether the layout is visible or not
// * @param application The application to display its description
// */
//@Composable
//@NonRestartableComposable
//fun ExpandApplicationDescription(
//    expand: MutableState<Boolean>,
//    application: AmetistaApplication
//) {
//    if(expand.value) {
//        ModalBottomSheet(
//            onDismissRequest = { expand.value = false }
//        ) {
//            Column (
//                verticalArrangement = Arrangement.spacedBy(10.dp)
//            ) {
//                Text(
//                    modifier = Modifier
//                        .padding(
//                            start = 16.dp
//                        ),
//                    text = application.name,
//                    fontFamily = displayFontFamily,
//                    fontSize = 20.sp
//                )
//                HorizontalDivider()
//                Text(
//                    modifier = Modifier
//                        .padding(
//                            horizontal = 16.dp
//                        )
//                        .verticalScroll(rememberScrollState()),
//                    text = application.description,
//                    textAlign = TextAlign.Justify,
//                    fontFamily = bodyFontFamily,
//                    fontSize = 14.sp
//                )
//            }
//        }
//    }
//}