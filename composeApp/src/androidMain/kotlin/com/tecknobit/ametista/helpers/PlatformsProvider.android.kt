package com.tecknobit.ametista.helpers

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tecknobit.ametistacore.models.AmetistaApplication.APPLICATION_IDENTIFIER_KEY
import com.tecknobit.equinoxcompose.components.Tile
import com.tecknobit.equinoxcompose.helpers.utils.AppContext

/**
 * Method to get the correct size for a [Tile] component
 *
 * @return the size as [Dp]
 */
actual fun tileSize(): Dp {
    return 150.dp
}

/**
 * Method to copy to the clipboard a content value
 *
 * @param content The content to copy
 */
actual fun copyToClipboard(
    content: String
) {
    val context = AppContext.get()
    val clipboard = context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText(APPLICATION_IDENTIFIER_KEY, content)
    clipboard.setPrimaryClip(clip)
}