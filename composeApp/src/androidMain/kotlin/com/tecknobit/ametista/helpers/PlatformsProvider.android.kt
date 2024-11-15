package com.tecknobit.ametista.helpers

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.helpers.utils.AppContext

actual fun tileSize(): Dp {
    return 150.dp
}

actual fun copyToClipboard(
    content: String
) {
    val context = AppContext.get()
    val clipboard = context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("application_id", content)
    clipboard.setPrimaryClip(clip)
}