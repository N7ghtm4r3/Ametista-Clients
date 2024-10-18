package com.tecknobit.ametista.helpers

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

actual fun tileSize(): Dp {
    return 250.dp
}

@Composable
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
actual fun getCurrentWidthSizeClass(): WindowWidthSizeClass {
    return calculateWindowSizeClass().widthSizeClass
}