package com.tecknobit.ametista.helpers

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.DesktopWindows
import androidx.compose.ui.graphics.vector.ImageVector
import com.tecknobit.ametista.ui.icons.Globe
import com.tecknobit.ametista.ui.icons.Ios
import com.tecknobit.ametistacore.models.AmetistaApplication.Platform
import com.tecknobit.ametistacore.models.AmetistaApplication.Platform.ANDROID
import com.tecknobit.ametistacore.models.AmetistaApplication.Platform.DESKTOP
import com.tecknobit.ametistacore.models.AmetistaApplication.Platform.IOS
import com.tecknobit.ametistacore.models.AmetistaApplication.Platform.WEB

fun Platform.icon(): ImageVector {
    return when (this) {
        ANDROID -> Icons.Default.Android
        IOS -> Ios
        DESKTOP -> Icons.Default.DesktopWindows
        WEB -> Globe
    }
}