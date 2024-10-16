package com.tecknobit.ametista.helpers

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

actual fun tileDimension(): Dp {
    return 150.dp
}

@Composable
actual fun getCurrentWidthSizeClass(): WindowWidthSizeClass {
    return WindowWidthSizeClass.Medium
}