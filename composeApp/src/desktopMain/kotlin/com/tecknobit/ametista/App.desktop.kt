package com.tecknobit.ametista

import OctocatKDUConfig
import UpdaterDialog
import ametista.composeapp.generated.resources.Res
import ametista.composeapp.generated.resources.app_name
import ametista.composeapp.generated.resources.app_version
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.tecknobit.ametistacore.helpers.AmetistaValidator
import io.github.vinceglb.filekit.core.PlatformFile
import org.jetbrains.compose.resources.stringResource
import java.util.Locale

@NonRestartableComposable
@Composable
actual fun CheckForUpdatesAndLaunch() {
    var launchApp by remember { mutableStateOf(true) }
    UpdaterDialog(
        config = OctocatKDUConfig(
            locale = Locale.getDefault(),
            appName = stringResource(Res.string.app_name),
            currentVersion = stringResource(Res.string.app_version),
            onUpdateAvailable = { launchApp = false },
            dismissAction = { launchApp = true }
        )
    )
    if (launchApp)
        startSession()
}

/**
 * Function to manage correctly the back navigation from the current screen
 *
 * No-any params required
 */
@Composable
@NonRestartableComposable
actual fun CloseApplicationOnNavBack() {
}

@Composable
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
actual fun getCurrentWidthSizeClass(): WindowWidthSizeClass {
    return calculateWindowSizeClass().widthSizeClass
}

actual fun getImagePath(
    imagePic: PlatformFile?
): String? {
    return imagePic?.path
}

actual fun setUserLanguage() {
    var tag = localUser.language
    if (tag == null)
        tag = AmetistaValidator.DEFAULT_LANGUAGE
    Locale.setDefault(Locale.forLanguageTag(tag))
}