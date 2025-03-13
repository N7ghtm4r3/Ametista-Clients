package com.tecknobit.ametista.ui.screens.application.presentation

import com.tecknobit.ametista.requester
import com.tecknobit.ametista.ui.screens.application.presenter.ApplicationScreen
import com.tecknobit.ametista.ui.screens.shared.presentations.ApplicationViewModel
import com.tecknobit.ametistacore.models.AmetistaApplication
import com.tecknobit.equinox.Requester.Companion.responseData
import com.tecknobit.equinoxcompose.helpers.session.setHasBeenDisconnectedValue
import com.tecknobit.equinoxcompose.helpers.session.setServerOfflineValue
import com.tecknobit.equinoxcompose.helpers.viewmodels.EquinoxViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.json.JSONObject

/**
 * The **ApplicationScreenViewModel** class is the support class used to execute the requests related
 * to the [AmetistaApplication] displayed by the [ApplicationScreen]
 *
 * @param applicationId The identifier of the application displayed
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see ApplicationViewModel
 * @see EquinoxViewModel
 * @see ViewModel
 * @see FetcherManagerWrapper
 */
open class ApplicationScreenViewModel(
    val applicationId: String
) : ApplicationViewModel() {

    /**
     * **_application** -> container state of the application
     */
    private val _application = MutableStateFlow<AmetistaApplication?>(
        value = null
    )
    val application: StateFlow<AmetistaApplication?> = _application

    /**
     * **applicationDeleted** -> whether the application has been deleted
     */
    private var applicationDeleted = false

    /**
     * Method to refresh the application details
     */
    fun refreshApplication() {
        execRefreshingRoutine(
            currentContext = ApplicationScreen::class.java,
            routine = {
                if (!applicationDeleted) {
                    requester.sendRequest(
                        request = {
                            requester.getApplication(
                                applicationId = applicationId
                            )
                        },
                        onSuccess = { response ->
                            setServerOfflineValue(false)
                            val jApplication: JSONObject = response.responseData()
                            _application.value = AmetistaApplication(jApplication)
                        },
                        onFailure = { setHasBeenDisconnectedValue(true) },
                        onConnectionError = { setServerOfflineValue(true) }
                    )
                }
            }
        )
    }

    /**
     * Method to delete an application
     *
     * @param application The application to delete
     * @param onDelete The action to execute when the application has been deleted
     */
    override fun deleteApplication(application: AmetistaApplication, onDelete: () -> Unit) {
        super.deleteApplication(
            application = application,
            onDelete = {
                applicationDeleted = true
                onDelete.invoke()
            }
        )
    }

}