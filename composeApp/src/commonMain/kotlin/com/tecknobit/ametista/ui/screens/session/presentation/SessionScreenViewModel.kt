package com.tecknobit.ametista.ui.screens.session.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.lifecycle.viewModelScope
import com.tecknobit.ametista.localUser
import com.tecknobit.ametista.requester
import com.tecknobit.ametista.ui.screens.session.data.AmetistaMember
import com.tecknobit.equinoxcompose.session.setHasBeenDisconnectedValue
import com.tecknobit.equinoxcompose.session.setServerOfflineValue
import com.tecknobit.equinoxcompose.viewmodels.EquinoxProfileViewModel
import com.tecknobit.equinoxcore.helpers.InputsValidator.Companion.isEmailValid
import com.tecknobit.equinoxcore.helpers.InputsValidator.Companion.isNameValid
import com.tecknobit.equinoxcore.helpers.InputsValidator.Companion.isSurnameValid
import com.tecknobit.equinoxcore.network.sendPaginatedRequest
import com.tecknobit.equinoxcore.network.sendRequest
import com.tecknobit.equinoxcore.pagination.PaginatedResponse.Companion.DEFAULT_PAGE
import io.github.ahmad_hamwi.compose.pagination.PaginationState
import kotlinx.coroutines.launch

/**
 * The **SessionScreenViewModel** class is the support class used to execute the requests related
 * to the [com.tecknobit.ametista.ui.screens.session.presenter.SessionScreen]
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see EquinoxViewModel
 * @see EquinoxProfileViewModel
 * @see androidx.lifecycle.ViewModel
 * @see com.tecknobit.equinoxcompose.session.Retriever
 */
class SessionScreenViewModel : EquinoxProfileViewModel(
    snackbarHostState = SnackbarHostState(),
    requester = requester,
    localUser = localUser
) {

    /**
     * `SessionScreenSection` -> session sections available
     */
    enum class SessionScreenSection {

        /**
         * `ABOUT_ME` -> section with the [localUser] details
         */
        ABOUT_ME,

        /**
         * `MEMBERS` -> section with the current members registered in the system
         */
        MEMBERS

    }

    /**
     * `sessionScreenSection` -> the container state to display the specific session section
     */
    lateinit var sessionScreenSection: MutableState<SessionScreenSection>

    /**
     * `viewerName` -> the value of the name of the viewer
     */
    lateinit var viewerName: MutableState<String>

    /**
     * `viewerNameError` -> whether the [viewerName] field is not valid
     */
    lateinit var viewerNameError: MutableState<Boolean>

    /**
     * `viewerSurname` -> the value of the surname of the viewer
     */
    lateinit var viewerSurname: MutableState<String>

    /**
     * `viewerSurnameError` -> whether the [viewerSurname] field is not valid
     */
    lateinit var viewerSurnameError: MutableState<Boolean>

    /**
     * `viewerEmail` -> the value of the email of the viewer
     */
    lateinit var viewerEmail: MutableState<String>

    /**
     * `viewerEmailError` -> whether the [viewerEmail] field is not valid
     */
    lateinit var viewerEmailError: MutableState<Boolean>

    /**
     * `membersState` -> the state used to manage the pagination for the [loadMembers] method
     */
    val membersState = PaginationState<Int, AmetistaMember>(
        initialPageKey = DEFAULT_PAGE,
        onRequestPage = { pageNumber ->
            loadMembers(
                pageNumber = pageNumber
            )
        }
    )

    /**
     * Method to load members
     *
     * @param pageNumber The number of the page to request to the server
     */
    private fun loadMembers(
        pageNumber: Int,
    ) {
        viewModelScope.launch {
            requester.sendPaginatedRequest(
                request = {
                    getSessionMembers(
                        page = pageNumber
                    )
                },
                serializer = AmetistaMember.serializer(),
                onSuccess = { page ->
                    setServerOfflineValue(false)
                    membersState.appendPage(
                        items = page.data,
                        nextPageKey = page.nextPage,
                        isLastPage = page.isLastPage
                    )
                },
                onFailure = { setHasBeenDisconnectedValue(true) },
                onConnectionError = { setServerOfflineValue(true) }
            )
        }
    }

    /**
     * Method to add a new [com.tecknobit.ametistacore.models.AmetistaUser.Role.VIEWER] in the system
     *
     * @param onSuccess The action to execute when the user has been added
     */
    fun addViewer(
        onSuccess: () -> Unit,
    ) {
        if (!isNameValid(viewerName.value)) {
            viewerNameError.value = true
            return
        }
        if (!isSurnameValid(viewerSurname.value)) {
            viewerSurnameError.value = true
            return
        }
        if (!isEmailValid(viewerEmail.value)) {
            viewerEmailError.value = true
            return
        }
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    requester.addViewer(
                        name = viewerName.value,
                        surname = viewerSurname.value,
                        email = viewerEmail.value,
                    )
                },
                onSuccess = {
                    membersState.refresh()
                    onSuccess.invoke()
                },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

    /**
     * Method to remove a member from the system
     *
     * @param member The member to remove
     */
    fun removeMember(
        member: AmetistaMember,
    ) {
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    requester.removeMember(
                        member = member
                    )
                },
                onSuccess = { membersState.refresh() },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

    /**
     * Method to logout from the current session
     *
     * @param onLogout The action to execute when the logout has been executed
     */
    fun logout(
        onLogout: () -> Unit,
    ) {
        clearSession {
            requester.setUserCredentials(
                userId = null,
                userToken = null
            )
            onLogout.invoke()
        }
    }

}