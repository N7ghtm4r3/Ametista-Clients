package com.tecknobit.ametista.ui.screens.shared.presentations

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.viewModelScope
import com.tecknobit.ametista.requester
import com.tecknobit.ametista.ui.screens.applications.data.AmetistaApplication
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.annotations.Structure
import com.tecknobit.equinoxcore.network.Requester.Companion.sendRequest
import kotlinx.coroutines.launch

/**
 * The **ApplicationViewModel** class is the support class used to execute the managing requests related
 * to an [AmetistaApplication]
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see androidx.lifecycle.ViewModel
 * @see com.tecknobit.equinoxcompose.session.Retriever
 * @see EquinoxViewModel
 */
@Structure
abstract class ApplicationViewModel : EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
) {

    /**
     * Method to delete an application
     *
     * @param application The application to delete
     * @param onDelete The action to execute when the application has been deleted
     */
    open fun deleteApplication(
        application: AmetistaApplication,
        onDelete: () -> Unit
    ) {
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    deleteApplication(
                        application = application
                    )
                },
                onSuccess = { onDelete.invoke() },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

}