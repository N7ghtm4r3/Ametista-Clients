package com.tecknobit.ametista.helpers

import ametista.composeapp.generated.resources.Res
import ametista.composeapp.generated.resources.no_connected_platform
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.lifecycle.viewModelScope
import com.tecknobit.ametista.navigator
import com.tecknobit.ametista.ui.icons.Globe
import com.tecknobit.ametista.ui.icons.Ios
import com.tecknobit.ametista.ui.screens.AmetistaScreen.Companion.PLATFORM_SCREEN
import com.tecknobit.ametista.ui.screens.application.ApplicationScreenViewModel
import com.tecknobit.ametista.ui.theme.platforms.android.AndroidPlatformTheme
import com.tecknobit.ametista.ui.theme.platforms.desktop.DesktopPlatformTheme
import com.tecknobit.ametista.ui.theme.platforms.ios.IosPlatformTheme
import com.tecknobit.ametista.ui.theme.platforms.web.WebPlatformTheme
import com.tecknobit.ametistacore.models.AmetistaApplication.APPLICATION_KEY
import com.tecknobit.ametistacore.models.AmetistaApplication.PLATFORM_KEY
import com.tecknobit.ametistacore.models.AmetistaApplication.Platform
import com.tecknobit.ametistacore.models.AmetistaApplication.Platform.ANDROID
import com.tecknobit.ametistacore.models.AmetistaApplication.Platform.DESKTOP
import com.tecknobit.ametistacore.models.AmetistaApplication.Platform.IOS
import com.tecknobit.ametistacore.models.AmetistaApplication.Platform.WEB
import com.tecknobit.equinoxcompose.components.Tile
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString

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
    viewModel: ApplicationScreenViewModel,
    applicationPlatforms: Set<Platform>
) {
    val tileDimension = tileDimension()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                bottom = 16.dp
            )
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PlatformsRow(
            viewModel = viewModel,
            applicationPlatforms = applicationPlatforms,
            platforms = listOf(ANDROID, IOS),
            size = tileDimension
        )
        Spacer(Modifier.height(10.dp))
        PlatformsRow(
            viewModel = viewModel,
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
    viewModel: ApplicationScreenViewModel,
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
                    viewModel = viewModel,
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
                    viewModel = viewModel,
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
    viewModel: ApplicationScreenViewModel,
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
                        MainScope().launch {
                            val currentEntry = navigator.currentEntry.first()
                            val stateHolder = currentEntry?.stateHolder
                            stateHolder?.set(APPLICATION_KEY, viewModel.application.value)
                            stateHolder?.set(PLATFORM_KEY, platform)
                        }
                        navigator.navigate(PLATFORM_SCREEN)
                    }
                )
            }
        } else {
            val stroke = Stroke(
                width = 5f,
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
                elevation = 0.dp,
                contentColor = color,
                icon = platform.icon(),
                text = platform.name,
                onClick = {
                    viewModel.viewModelScope.launch {
                        viewModel.snackbarHostState!!.showSnackbar(
                            message = getString(Res.string.no_connected_platform)
                        )
                    }
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