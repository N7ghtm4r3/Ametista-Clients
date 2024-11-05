package com.tecknobit.ametista.ui.screens.account

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.lifecycle.viewModelScope
import com.tecknobit.ametista.localUser
import com.tecknobit.ametista.requester
import com.tecknobit.ametistacore.helpers.AmetistaValidator.isEmailValid
import com.tecknobit.ametistacore.helpers.AmetistaValidator.isNameValid
import com.tecknobit.ametistacore.helpers.AmetistaValidator.isSurnameValid
import com.tecknobit.ametistacore.helpers.pagination.PaginatedResponse.Companion.DEFAULT_PAGE
import com.tecknobit.ametistacore.models.AmetistaMember
import com.tecknobit.equinoxcompose.helpers.session.setHasBeenDisconnectedValue
import com.tecknobit.equinoxcompose.helpers.session.setServerOfflineValue
import com.tecknobit.equinoxcompose.helpers.viewmodels.EquinoxProfileViewModel
import io.github.ahmad_hamwi.compose.pagination.PaginationState
import kotlinx.coroutines.launch

class SessionScreenViewModel : EquinoxProfileViewModel(
    snackbarHostState = SnackbarHostState(),
    requester = requester,
    localUser = localUser
) {

    enum class SessionScreenSection {

        ABOUT_ME,

        MEMBERS

    }

    lateinit var sessionScreenSection: MutableState<SessionScreenSection>

    lateinit var viewerName: MutableState<String>

    lateinit var viewerNameError: MutableState<Boolean>

    lateinit var viewerSurname: MutableState<String>

    lateinit var viewerSurnameError: MutableState<Boolean>

    lateinit var viewerEmail: MutableState<String>

    lateinit var viewerEmailError: MutableState<Boolean>

    val paginationState = PaginationState(
        initialPageKey = DEFAULT_PAGE,
        onRequestPage = { pageNumber ->
            viewModelScope.launch {
                requester.sendPaginatedRequest(
                    request = {
                        requester.getSessionMembers(
                            page = pageNumber
                        )
                    },
                    supplier = { jMember -> AmetistaMember(jMember) },
                    onSuccess = { page ->
                        setServerOfflineValue(false)
                        appendPage(
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
    )

    fun addViewer(
        onSuccess: () -> Unit
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
        requester.sendRequest(
            request = {
                requester.addViewer(
                    name = viewerName.value,
                    surname = viewerSurname.value,
                    email = viewerEmail.value,
                )
            },
            onSuccess = {
                paginationState.refresh()
                onSuccess.invoke()
            },
            onFailure = { showSnackbarMessage(it) }
        )
    }

    fun removeMember(
        member: AmetistaMember
    ) {
        requester.sendRequest(
            request = {
                requester.removeMember(
                    member = member
                )
            },
            onSuccess = { paginationState.refresh() },
            onFailure = { showSnackbarMessage(it) }
        )
    }

    fun logout(
        onLogout: () -> Unit
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