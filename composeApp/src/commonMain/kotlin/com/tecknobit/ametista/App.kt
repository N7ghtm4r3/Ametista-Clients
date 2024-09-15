package com.tecknobit.ametista

import ametista.composeapp.generated.resources.Res
import ametista.composeapp.generated.resources.dm_sans
import ametista.composeapp.generated.resources.kanit
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import com.tecknobit.ametista.ui.theme.AmetistaTheme
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * **bodyFontFamily** -> the Refy's body font family
 */
lateinit var bodyFontFamily: FontFamily

/**
 * **displayFontFamily** -> the Refy's font family
 */
lateinit var displayFontFamily: FontFamily

@Composable
@Preview
fun App() {
    bodyFontFamily = FontFamily(Font(Res.font.dm_sans))
    displayFontFamily = FontFamily(Font(Res.font.kanit))
    AmetistaTheme {
    }
}