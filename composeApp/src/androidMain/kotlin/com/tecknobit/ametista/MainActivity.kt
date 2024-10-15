package com.tecknobit.ametista

import android.os.Bundle
import android.os.StrictMode
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.tecknobit.equinoxcompose.helpers.session.setUpSession
import io.github.vinceglb.filekit.core.FileKit

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InitSession()
            App()
        }
    }

    @Composable
    @NonRestartableComposable
    private fun InitSession() {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        setUpSession(
            serverOfflineMessage = stringResource(id = R.string.server_currently_offline),
            noInternetConnectionMessage = stringResource(id = R.string.no_internet_connection),
            noInternetConnectionIcon = ImageVector.vectorResource(id = R.drawable.no_internet),
            hasBeenDisconnectedAction = {
                // TODO: TO SET
            }
        )
        FileKit.init(this)
    }

}
