package com.tecknobit.ametista.helpers

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.components.Tile
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

/**
 * Method to get the correct size for a [Tile] component
 *
 * @return the size as [Dp]
 */
actual fun tileSize(): Dp {
    return 250.dp
}

/**
 * Method to copy to the clipboard a content value
 *
 * @param content The content to copy
 */
actual fun copyToClipboard(
    content: String
) {
    val stringSelection = StringSelection(content)
    Toolkit.getDefaultToolkit().systemClipboard.setContents(stringSelection, null)
}