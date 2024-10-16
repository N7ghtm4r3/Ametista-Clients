package com.tecknobit.ametista.helpers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.DesktopWindows
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tecknobit.ametista.ui.icons.Globe
import com.tecknobit.ametista.ui.icons.Ios
import com.tecknobit.ametista.ui.theme.platforms.android.AndroidPlatformTheme
import com.tecknobit.ametista.ui.theme.platforms.desktop.DesktopPlatformTheme
import com.tecknobit.ametista.ui.theme.platforms.ios.IosPlatformTheme
import com.tecknobit.ametista.ui.theme.platforms.web.WebPlatformTheme
import com.tecknobit.ametistacore.models.AmetistaApplication.Platform
import com.tecknobit.ametistacore.models.AmetistaApplication.Platform.ANDROID
import com.tecknobit.ametistacore.models.AmetistaApplication.Platform.DESKTOP
import com.tecknobit.ametistacore.models.AmetistaApplication.Platform.IOS
import com.tecknobit.ametistacore.models.AmetistaApplication.Platform.WEB
import com.tecknobit.equinoxcompose.components.Tile

fun Platform.icon(): ImageVector {
    return when (this) {
        ANDROID -> Icons.Default.Android
        IOS -> Ios
        DESKTOP -> Icons.Default.DesktopWindows
        WEB -> Globe
    }
}

@Composable
@NonRestartableComposable
fun PlatformsCustomGrid(
    paddingValues: PaddingValues,
    applicationPlatforms: Set<Platform>
) {
    val tileDimension = tileDimension()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = paddingValues.calculateTopPadding() + 16.dp,
                bottom = paddingValues.calculateBottomPadding() + 16.dp
            )
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        PlatformsRow(
            applicationPlatforms = applicationPlatforms,
            platforms = listOf(ANDROID, IOS),
            size = tileDimension
        )
        Spacer(Modifier.height(10.dp))
        PlatformsRow(
            applicationPlatforms = applicationPlatforms,
            platforms = listOf(DESKTOP, WEB),
            size = tileDimension
        )
    }
}

expect fun tileDimension(): Dp

@Composable
@NonRestartableComposable
private fun PlatformsRow(
    applicationPlatforms: Set<Platform>,
    platforms: List<Platform>,
    size: Dp
) {
    val width = getCurrentWidthSizeClass()
    when (width) {
        WindowWidthSizeClass.Compact -> {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                DrawTiles(
                    applicationPlatforms = applicationPlatforms,
                    platforms = platforms,
                    size = size
                )
            }
        }

        else -> {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                DrawTiles(
                    applicationPlatforms = applicationPlatforms,
                    platforms = platforms,
                    size = size
                )
            }
        }
    }
}

@Composable
@NonRestartableComposable
private fun DrawTiles(
    applicationPlatforms: Set<Platform>,
    platforms: List<Platform>,
    size: Dp
) {
    platforms.forEach { platform ->
        if (applicationPlatforms.contains(platform)) {
            platform.Theme {
                Tile(
                    size = size,
                    icon = platform.icon(),
                    text = platform.name,
                    onClick = {
                        // TODO: NAV TO PLATFORM PAGE
                    }
                )
            }
        } else {
            val stroke = Stroke(
                width = 4f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
            )
            val color = MaterialTheme.colorScheme.primary
            Tile(
                modifier = Modifier
                    .drawBehind {
                        drawRoundRect(
                            color = color,
                            style = stroke,
                            cornerRadius = CornerRadius(15.dp.toPx())
                        )
                    },
                size = size,
                containerColor = Color.Transparent,
                contentColor = color,
                icon = platform.icon(),
                text = platform.name,
                onClick = {
                    // TODO: WARN OF THE NOT CONNECTED YET  
                }
            )
        }
    }
}

@Composable
expect fun getCurrentWidthSizeClass(): WindowWidthSizeClass

@Composable
fun Platform.Theme(
    content: @Composable () -> Unit
) {
    when (this) {
        ANDROID -> {
            AndroidPlatformTheme {
                content.invoke()
            }
        }

        IOS -> {
            IosPlatformTheme {
                content.invoke()
            }
        }

        DESKTOP -> {
            DesktopPlatformTheme {
                content.invoke()
            }
        }

        WEB -> {
            WebPlatformTheme {
                content.invoke()
            }
        }
    }
}