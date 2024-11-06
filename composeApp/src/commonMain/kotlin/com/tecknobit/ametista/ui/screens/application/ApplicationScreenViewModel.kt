package com.tecknobit.ametista.ui.screens.application

import com.tecknobit.ametista.requester
import com.tecknobit.ametista.ui.sharedviewmodels.ApplicationViewModel
import com.tecknobit.ametistacore.models.AmetistaApplication
import com.tecknobit.equinox.Requester.Companion.responseData
import com.tecknobit.equinoxcompose.helpers.session.setHasBeenDisconnectedValue
import com.tecknobit.equinoxcompose.helpers.session.setServerOfflineValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.json.JSONObject

open class ApplicationScreenViewModel(
    val applicationId: String
) : ApplicationViewModel() {

    private val _application = MutableStateFlow<AmetistaApplication?>(
        value = null
    )
    val application: StateFlow<AmetistaApplication?> = _application

    private var applicationDeleted = false

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