package com.tecknobit.ametista.helpers

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

actual fun tileSize(): Dp {
    return 250.dp
}

actual fun copyToClipboard(
    content: String
) {
    val stringSelection = StringSelection(content)
    Toolkit.getDefaultToolkit().systemClipboard.setContents(stringSelection, null)
}