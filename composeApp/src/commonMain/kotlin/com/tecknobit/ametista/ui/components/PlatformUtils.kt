package com.tecknobit.ametista.ui.components

import ametista.composeapp.generated.resources.Res
import ametista.composeapp.generated.resources.no_connected_platform
import ametista.composeapp.generated.resources.no_issues_android
import ametista.composeapp.generated.resources.no_issues_desktop
import ametista.composeapp.generated.resources.no_issues_ios
import ametista.composeapp.generated.resources.no_issues_web
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
import com.tecknobit.ametista.PLATFORM_SCREEN
import com.tecknobit.ametista.navigator
import com.tecknobit.ametista.ui.icons.Globe
import com.tecknobit.ametista.ui.icons.Ios
import com.tecknobit.ametista.ui.screens.application.presentation.ApplicationScreenViewModel
import com.tecknobit.ametista.ui.theme.platforms.android.AndroidPlatformTheme
import com.tecknobit.ametista.ui.theme.platforms.desktop.DesktopPlatformTheme
import com.tecknobit.ametista.ui.theme.platforms.ios.IosPlatformTheme
import com.tecknobit.ametista.ui.theme.platforms.web.WebPlatformTheme
import com.tecknobit.ametistacore.enums.Platform
import com.tecknobit.ametistacore.enums.Platform.ANDROID
import com.tecknobit.ametistacore.enums.Platform.DESKTOP
import com.tecknobit.ametistacore.enums.Platform.IOS
import com.tecknobit.ametistacore.enums.Platform.WEB
import com.tecknobit.equinoxcompose.components.Tile
import com.tecknobit.equinoxcompose.utilities.responsiveAssignment
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.getString

/**
 * Custom grid to display the platforms attached to an application
 *
 * @param viewModel The viewmodel of the [com.tecknobit.ametista.ui.screens.application.presenter.ApplicationScreen]
 * @param applicationPlatforms The platforms attached to the application
 */
@Composable
@NonRestartableComposable
fun PlatformsCustomGrid(
    viewModel: ApplicationScreenViewModel,
    applicationPlatforms: Set<Platform>,
) {
    val tileDimension = responsiveAssignment(
        onExpandedSizeClass = { 250.dp },
        onMediumSizeClass = { 200.dp },
        onCompactSizeClass = { 150.dp }
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                bottom = 16.dp
            )
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
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

/**
 * Custom [Row] to display the platforms
 *
 * @param viewModel The viewmodel of the [com.tecknobit.ametista.ui.screens.application.presenter.ApplicationScreen]
 * @param applicationPlatforms The platforms attached to the application
 * @param platforms The platforms to display in the [Row]
 * @param size The size of the [Tile]
 *
 */
@Composable
@NonRestartableComposable
private fun PlatformsRow(
    viewModel: ApplicationScreenViewModel,
    applicationPlatforms: Set<Platform>,
    platforms: List<Platform>,
    size: Dp,
) {
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

/**
 * Method to arrange the tiles in the [Row]
 *
 * @param viewModel The viewmodel of the [com.tecknobit.ametista.ui.screens.application.presenter.ApplicationScreen]
 * @param applicationPlatforms The platforms attached to the application
 * @param platforms The platforms to display in the [Row]
 * @param size The size of the [Tile]
 *
 */
@Composable
@NonRestartableComposable
private fun DrawTiles(
    viewModel: ApplicationScreenViewModel,
    applicationPlatforms: Set<Platform>,
    platforms: List<Platform>,
    size: Dp,
) {
    platforms.forEach { platform ->
        if (applicationPlatforms.contains(platform)) {
            platform.Theme {
                Tile(
                    size = size,
                    icon = platform.icon(),
                    text = platform.name,
                    onClick = {
                        val applicationId = viewModel.application.value!!.id
                        val applicationName = viewModel.application.value!!.name
                        val route = "$PLATFORM_SCREEN/$applicationId/$applicationName/$platform"
                        navigator.navigate(route)
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

/**
 * Method to get the related icon based on the [Platform] value
 *
 * @return platform icon as [ImageVector]
 */
fun Platform.icon(): ImageVector {
    return when (this) {
        ANDROID -> Icons.Default.Android
        IOS -> Ios
        DESKTOP -> Icons.Default.DesktopWindows
        WEB -> Globe
    }
}

/**
 * Method to get the related theme based on the [Platform] value
 *
 * @param content The UI content to display with the correct theme
 */
@Composable
fun Platform.Theme(
    content: @Composable () -> Unit,
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

/**
 * Method used to get the empty state resource to display when there aren't no issues to display
 */
@Composable
fun Platform.noIssues(): DrawableResource {
    return when (this) {
        ANDROID -> Res.drawable.no_issues_android
        IOS -> Res.drawable.no_issues_ios
        DESKTOP -> Res.drawable.no_issues_desktop
        WEB -> Res.drawable.no_issues_web
    }
}

