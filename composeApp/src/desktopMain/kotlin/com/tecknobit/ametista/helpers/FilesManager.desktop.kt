package com.tecknobit.ametista.helpers

import io.github.vinceglb.filekit.core.PlatformFile

actual fun getAppIconPath(
    appIcon: PlatformFile?
): String? {
    return appIcon?.path
}