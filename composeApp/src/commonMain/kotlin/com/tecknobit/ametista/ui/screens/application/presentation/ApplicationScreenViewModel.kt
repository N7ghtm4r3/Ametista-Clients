package com.tecknobit.ametista.ui.screens.application.presentation

import com.tecknobit.ametista.navigator
import com.tecknobit.ametista.requester
import com.tecknobit.ametista.ui.screens.application.presenter.ApplicationScreen
import com.tecknobit.ametista.ui.screens.applications.data.AmetistaApplication
import com.tecknobit.ametista.ui.screens.shared.presentations.ApplicationViewModel
import com.tecknobit.equinoxcompose.session.setServerOfflineValue
import com.tecknobit.equinoxcore.network.Requester.Companion.sendRequest
import com.tecknobit.equinoxcore.network.Requester.Companion.toResponseData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement

/**
 * The **ApplicationScreenViewModel** class is the support class used to execute the requests related
 * to the [AmetistaApplication] displayed by the [ApplicationScreen]
 *
 * @param applicationId The identifier of the application displayed
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see ApplicationViewModel
 * @see androidx.lifecycle.ViewModel
 * @see com.tecknobit.equinoxcompose.session.Retriever
 * @see EquinoxViewModel
 */
open class ApplicationScreenViewModel(
    val applicationId: String,
) : ApplicationViewModel() {

    /**
     * `_application` -> container state of the application
     */
    private val _application = MutableStateFlow<AmetistaApplication?>(
        value = null
    )
    val application = _application.asStateFlow()

    /**
     * Method to refresh the application details
     */
    fun refreshApplication() {
        retrieve(
            currentContext = ApplicationScreen::class,
            routine = {
                requester.sendRequest(
                    request = {
                        getApplication(
                            applicationId = applicationId
                        )
                    },
                    onSuccess = {
                        setServerOfflineValue(false)
                        _application.value = Json.decodeFromJsonElement(it.toResponseData())
                    },
                    onFailure = { navigator.goBack() },
                    onConnectionError = { setServerOfflineValue(true) }
                )
            }
        )
    }

}