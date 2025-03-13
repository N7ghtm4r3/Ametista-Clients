package com.tecknobit.ametista

import android.os.Bundle
import android.os.StrictMode
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts.StartIntentSenderForResult
import androidx.annotation.CallSuper
import androidx.annotation.ContentView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.tecknobit.ametista.helpers.ContextProvider
import com.tecknobit.ametista.ui.screens.shared.presenters.AmetistaScreen.Companion.AUTH_SCREEN
import com.tecknobit.equinoxcompose.session.setUpSession
import io.github.vinceglb.filekit.core.FileKit

/**
 * The [MainActivity] is used as entry point of Ametista's application for Android
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see ComponentActivity
 *
 */
class MainActivity : ComponentActivity() {

    companion object {

        /**
         * **appUpdateManager** the manager to check if there is an update available
         */
        lateinit var appUpdateManager: AppUpdateManager

        /**
         * **launcher** the result registered for [appUpdateManager] and the action to execute if fails
         */
        lateinit var launcher: ActivityResultLauncher<IntentSenderRequest>

    }

    init {
        launcher = registerForActivityResult(StartIntentSenderForResult()) { result ->
            if (result.resultCode != RESULT_OK)
                startSession()
        }
    }

    /**
     * {@inheritDoc}
     *
     * If your ComponentActivity is annotated with [ContentView], this will
     * call [setContentView] for you.
     */
    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ContextProvider.setCurrentActivity(this)
        enableEdgeToEdge()
        setContent {
            InitSession()
            App()
        }
    }

    /**
     * Function to init the instances for the session
     *
     * No-any params required
     */
    @Composable
    @NonRestartableComposable
    private fun InitSession() {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        FileKit.init(this)
        appUpdateManager = AppUpdateManagerFactory.create(applicationContext)
        setUpSession(
            hasBeenDisconnectedAction = {
                localUser.clear()
                requester.setUserCredentials(
                    userId = null,
                    userToken = null
                )
                navigator.navigate(AUTH_SCREEN)
            }
        )
    }

}
