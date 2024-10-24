package com.tecknobit.ametista

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import io.github.vinceglb.filekit.core.PlatformFile

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