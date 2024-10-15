package com.tecknobit.ametista.helpers

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.tecknobit.equinoxcompose.helpers.utils.AppContext
import io.github.vinceglb.filekit.core.PlatformFile
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import kotlin.math.min

actual fun getAppIconPath(
    appIcon: PlatformFile?
): String? {
    if (appIcon != null) {
        return getFilePath(
            context = AppContext.get(),
            uri = appIcon.uri
        )
    }
    return null
}

/**
 * Function to get the complete file path of an file
 *
 * @param context: the context where the file is needed
 * @param uri: the uri of the file
 * @return the path of the file as [String]
 */
private fun getFilePath(
    context: Context,
    uri: Uri
): String {
    val returnCursor = context.contentResolver.query(uri, null, null, null, null)
    val nameIndex = returnCursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
    val sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE)
    returnCursor.moveToFirst()
    val name = returnCursor.getString(nameIndex)
    returnCursor.getLong(sizeIndex).toString()
    val file = File(context.filesDir, name)
    try {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        var read = 0
        val maxBufferSize = 1 * 1024 * 1024
        val bytesAvailable: Int = inputStream?.available() ?: 0
        val bufferSize = min(bytesAvailable, maxBufferSize)
        val buffers = ByteArray(bufferSize)
        while (inputStream?.read(buffers).also {
                if (it != null) {
                    read = it
                }
            } != -1) {
            outputStream.write(buffers, 0, read)
        }
        inputStream?.close()
        outputStream.close()
    } catch (_: Exception) {
    } finally {
        returnCursor.close()
    }
    return file.path
}