package com.tecknobit.ametista.ui.screens.account

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import com.tecknobit.ametista.helpers.AmetistaLocalUser
import com.tecknobit.ametista.helpers.AmetistaRequester
import com.tecknobit.ametistacore.helpers.AmetistaValidator.isEmailValid
import com.tecknobit.ametistacore.helpers.AmetistaValidator.isNameValid
import com.tecknobit.ametistacore.helpers.AmetistaValidator.isSurnameValid
import com.tecknobit.ametistacore.models.AmetistaUser
import com.tecknobit.equinoxcompose.helpers.viewmodels.EquinoxProfileViewModel
import io.github.ahmad_hamwi.compose.pagination.PaginationState

class SessionScreenViewModel : EquinoxProfileViewModel(
    snackbarHostState = SnackbarHostState(),
    requester = AmetistaRequester(""), // TODO: USE THE REAL ONE
    localUser = AmetistaLocalUser()// TODO: USE THE REAL ONE
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

    val paginationState = PaginationState<Int, AmetistaUser>(
        initialPageKey = 1,
        onRequestPage = { loadMembers() }
    )

    private fun loadMembers() {
        paginationState.appendPage(
            items = listOf(AmetistaUser("don_joe@gmail.com"), AmetistaUser("prova@gmail.com")),
            nextPageKey = 1,
            isLastPage = true
        )
    }

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
        // TODO: MAKE THE REQUEST THEN
        onSuccess.invoke()
    }

    fun removeMember(
        member: AmetistaUser
    ) {
        // TODO MAKE THE REAL REQUEST THEN
    }

    fun logout(
        onLogout: () -> Unit
    ) {
        // TODO: MAKE THE REAL PROCEDURE THEN
        onLogout.invoke()
    }

}