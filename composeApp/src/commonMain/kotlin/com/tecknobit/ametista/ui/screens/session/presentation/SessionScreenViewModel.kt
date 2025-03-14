// TODO: TO SET

//package com.tecknobit.ametista.ui.screens.session.presentation
//
//import androidx.compose.material3.SnackbarHostState
//import androidx.compose.runtime.MutableState
//import androidx.lifecycle.viewModelScope
//import com.tecknobit.ametista.localUser
//import com.tecknobit.ametista.requester
//import com.tecknobit.ametistacore.helpers.AmetistaValidator.isEmailValid
//import com.tecknobit.ametistacore.helpers.AmetistaValidator.isNameValid
//import com.tecknobit.ametistacore.helpers.AmetistaValidator.isSurnameValid
//import com.tecknobit.ametistacore.helpers.pagination.PaginatedResponse.Companion.DEFAULT_PAGE
//import com.tecknobit.ametistacore.models.AmetistaMember
//import com.tecknobit.equinoxcompose.helpers.session.setHasBeenDisconnectedValue
//import com.tecknobit.equinoxcompose.helpers.session.setServerOfflineValue
//import com.tecknobit.equinoxcompose.helpers.viewmodels.EquinoxProfileViewModel
//import com.tecknobit.equinoxcompose.helpers.viewmodels.EquinoxViewModel
//import io.github.ahmad_hamwi.compose.pagination.PaginationState
//import kotlinx.coroutines.launch
//
///**
// * The **SessionScreenViewModel** class is the support class used to execute the requests related
// * to the [SessionScreen]
// *
// * @author N7ghtm4r3 - Tecknobit
// * @see EquinoxProfileViewModel
// * @see androidx.lifecycle.ViewModel
// * @see com.tecknobit.equinoxcompose.session.Retriever
// * @see EquinoxViewModel
// */
//class SessionScreenViewModel : EquinoxProfileViewModel(
//    snackbarHostState = SnackbarHostState(),
//    requester = requester,
//    localUser = localUser
//) {
//
//    /**
//     * `SessionScreenSection` -> session sections available
//     */
//    enum class SessionScreenSection {
//
//        /**
//         * `ABOUT_ME` -> section with the [localUser] details
//         */
//        ABOUT_ME,
//
//        /**
//         * `MEMBERS` -> section with the current members registered in the system
//         */
//        MEMBERS
//
//    }
//
//    /**
//     * `sessionScreenSection` -> the container state to display the specific session section
//     */
//    lateinit var sessionScreenSection: MutableState<SessionScreenSection>
//
//    /**
//     * `viewerName` -> the value of the name of the viewer
//     */
//    lateinit var viewerName: MutableState<String>
//
//    /**
//     * `viewerNameError` -> whether the [viewerName] field is not valid
//     */
//    lateinit var viewerNameError: MutableState<Boolean>
//
//    /**
//     * `viewerSurname` -> the value of the surname of the viewer
//     */
//    lateinit var viewerSurname: MutableState<String>
//
//    /**
//     * `viewerSurnameError` -> whether the [viewerSurname] field is not valid
//     */
//    lateinit var viewerSurnameError: MutableState<Boolean>
//
//    /**
//     * `viewerEmail` -> the value of the email of the viewer
//     */
//    lateinit var viewerEmail: MutableState<String>
//
//    /**
//     * `viewerEmailError` -> whether the [viewerEmail] field is not valid
//     */
//    lateinit var viewerEmailError: MutableState<Boolean>
//
//    /**
//     * `applicationsState` -> the state used to manage the pagination for the [loadMembers] method
//     */
//    val applicationsState = PaginationState<Int, AmetistaMember>(
//        initialPageKey = DEFAULT_PAGE,
//        onRequestPage = { pageNumber ->
//            loadMembers(
//                pageNumber = pageNumber
//            )
//        }
//    )
//
//    /**
//     * Method to load members
//     *
//     * @param pageNumber The number of the page to request to the server
//     */
//    private fun loadMembers(
//        pageNumber: Int
//    ) {
//        viewModelScope.launch {
//            requester.sendPaginatedRequest(
//                request = {
//                    getSessionMembers(
//                        page = pageNumber
//                    )
//                },
//                supplier = { jMember -> AmetistaMember(jMember) },
//                onSuccess = { page ->
//                    setServerOfflineValue(false)
//                    applicationsState.appendPage(
//                        items = page.data,
//                        nextPageKey = page.nextPage,
//                        isLastPage = page.isLastPage
//                    )
//                },
//                onFailure = { setHasBeenDisconnectedValue(true) },
//                onConnectionError = { setServerOfflineValue(true) }
//            )
//        }
//    }
//
//    /**
//     * Method to add a new [com.tecknobit.ametistacore.models.AmetistaUser.Role.VIEWER] in the system
//     *
//     * @param onSuccess The action to execute when the user has been added
//     */
//    fun addViewer(
//        onSuccess: () -> Unit
//    ) {
//        if (!isNameValid(viewerName.value)) {
//            viewerNameError.value = true
//            return
//        }
//        if (!isSurnameValid(viewerSurname.value)) {
//            viewerSurnameError.value = true
//            return
//        }
//        if (!isEmailValid(viewerEmail.value)) {
//            viewerEmailError.value = true
//            return
//        }
//        requester.sendRequest(
//            request = {
//                requester.addViewer(
//                    name = viewerName.value,
//                    surname = viewerSurname.value,
//                    email = viewerEmail.value,
//                )
//            },
//            onSuccess = {
//                applicationsState.refresh()
//                onSuccess.invoke()
//            },
//            onFailure = { showSnackbarMessage(it) }
//        )
//    }
//
//    /**
//     * Method to remove a member from the system
//     *
//     * @param member The member to remove
//     */
//    fun removeMember(
//        member: AmetistaMember
//    ) {
//        requester.sendRequest(
//            request = {
//                requester.removeMember(
//                    member = member
//                )
//            },
//            onSuccess = { applicationsState.refresh() },
//            onFailure = { showSnackbarMessage(it) }
//        )
//    }
//
//    /**
//     * Method to logout from the current session
//     *
//     * @param onLogout The action to execute when the logout has been executed
//     */
//    fun logout(
//        onLogout: () -> Unit
//    ) {
//        clearSession {
//            requester.setUserCredentials(
//                userId = null,
//                userToken = null
//            )
//            onLogout.invoke()
//        }
//    }
//
//}