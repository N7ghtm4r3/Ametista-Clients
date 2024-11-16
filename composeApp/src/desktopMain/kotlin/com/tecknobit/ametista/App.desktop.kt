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

/**
 * Function to check whether are available any updates for each platform and then launch the application
 * which the correct first screen to display
 *
 */
@Composable
@NonRestartableComposable
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
 */
@Composable
@NonRestartableComposable
actual fun CloseApplicationOnNavBack() {
}

/**
 * Function to get the current screen dimension of the device where the application is running
 *
 *
 * @return the width size class based on the current dimension of the screen as [WindowWidthSizeClass]
 */
@Composable
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
actual fun getCurrentWidthSizeClass(): WindowWidthSizeClass {
    return calculateWindowSizeClass().widthSizeClass
}

/**
 * Function to get the image picture's path
 *
 * @param imagePic: the asset from fetch its path
 *
 * @return the asset path as [String]
 */
actual fun getImagePath(
    imagePic: PlatformFile?
): String? {
    return imagePic?.path
}

/**
 * Function to set locale language for the application
 *
 */
actual fun setUserLanguage() {
    var tag = localUser.language
    if (tag == null)
        tag = AmetistaValidator.DEFAULT_LANGUAGE
    Locale.setDefault(Locale.forLanguageTag(tag))
}