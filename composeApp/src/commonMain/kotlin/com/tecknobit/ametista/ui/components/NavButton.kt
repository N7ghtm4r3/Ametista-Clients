package com.tecknobit.ametista.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.graphics.Color
import com.tecknobit.ametista.navigator

/**
 * Back navigation button
 */
@Composable
@NonRestartableComposable
fun NavButton() {
    IconButton(
        onClick = { navigator.goBack() }
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBackIosNew,
            contentDescription = null,
            tint = Color.White
        )
    }
}