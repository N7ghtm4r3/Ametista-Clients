package com.tecknobit.ametista

import ametista.composeapp.generated.resources.Res
import ametista.composeapp.generated.resources.dm_sans
import ametista.composeapp.generated.resources.kanit
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.text.font.FontFamily
import coil3.ImageLoader
import coil3.compose.LocalPlatformContext
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.request.CachePolicy
import coil3.request.addLastModifiedToFileCacheKey
import com.tecknobit.ametista.helpers.AmetistaLocalUser
import com.tecknobit.ametista.helpers.AmetistaRequester
import com.tecknobit.ametista.helpers.customHttpClient
import com.tecknobit.ametista.ui.screens.AmetistaScreen.Companion.APPLICATIONS_SCREEN
import com.tecknobit.ametista.ui.screens.AmetistaScreen.Companion.APPLICATION_SCREEN
import com.tecknobit.ametista.ui.screens.AmetistaScreen.Companion.AUTH_SCREEN
import com.tecknobit.ametista.ui.screens.AmetistaScreen.Companion.CHANGE_VIEWER_PASSWORD_SCREEN
import com.tecknobit.ametista.ui.screens.AmetistaScreen.Companion.PLATFORM_SCREEN
import com.tecknobit.ametista.ui.screens.AmetistaScreen.Companion.SESSION_SCREEN
import com.tecknobit.ametista.ui.screens.AmetistaScreen.Companion.SPLASHSCREEN
import com.tecknobit.ametista.ui.screens.application.ApplicationScreen
import com.tecknobit.ametista.ui.screens.applications.ApplicationsScreen
import com.tecknobit.ametista.ui.screens.auth.AuthScreen
import com.tecknobit.ametista.ui.screens.changeviewerpassword.ChangeViewerPasswordScreen
import com.tecknobit.ametista.ui.screens.navigation.Splashscreen
import com.tecknobit.ametista.ui.screens.platform.PlatformScreen
import com.tecknobit.ametista.ui.screens.session.SessionScreen
import com.tecknobit.ametistacore.enums.Platform
import com.tecknobit.ametistacore.helpers.AmetistaValidator.DEFAULT_VIEWER_PASSWORD
import com.tecknobit.ametistacore.models.analytics.AmetistaAnalytic.PLATFORM_KEY
import com.tecknobit.equinoxcore.helpers.IDENTIFIER_KEY
import com.tecknobit.equinoxcore.helpers.NAME_KEY
import io.github.vinceglb.filekit.core.PlatformFile
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.path
import moe.tlaster.precompose.navigation.rememberNavigator
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * **bodyFontFamily** -> the Ametista's body font family
 */
lateinit var bodyFontFamily: FontFamily

/**
 * **displayFontFamily** -> the Ametista's font family
 */
lateinit var displayFontFamily: FontFamily

/**
 * **navigator** -> the navigator instance is useful to manage the navigation between the screens of the application
 */
lateinit var navigator: Navigator

/**
 * **imageLoader** -> the image loader used by coil library to load the image and by-passing the https self-signed certificates
 */
lateinit var imageLoader: ImageLoader

/**
 * **localUser** -> the helper to manage the local sessions stored locally in
 * the device
 */
val localUser = AmetistaLocalUser()

/**
 * **requester** -> the instance to manage the requests with the backend
 */
lateinit var requester: AmetistaRequester

/**
 * Common entry point of the **Ametista** application
 *
 */
@Composable
@Preview
fun App() {
    bodyFontFamily = FontFamily(Font(Res.font.dm_sans))
    displayFontFamily = FontFamily(Font(Res.font.kanit))
    imageLoader = ImageLoader.Builder(LocalPlatformContext.current)
        .components {
            add(
                KtorNetworkFetcherFactory(
                    httpClient = customHttpClient()
                )
            )
        }
        .addLastModifiedToFileCacheKey(true)
        .diskCachePolicy(CachePolicy.ENABLED)
        .networkCachePolicy(CachePolicy.ENABLED)
        .memoryCachePolicy(CachePolicy.ENABLED)
        .build()
    PreComposeApp {
        navigator = rememberNavigator()
        NavHost(
            navigator = navigator,
            initialRoute = SPLASHSCREEN
        ) {
            scene(
                route = SPLASHSCREEN
            ) {
                Splashscreen().ShowContent()
            }
            scene(
                route = AUTH_SCREEN
            ) {
                AuthScreen().ShowContent()
            }
            scene(
                route = CHANGE_VIEWER_PASSWORD_SCREEN
            ) {
                ChangeViewerPasswordScreen().ShowContent()
            }
            scene(
                route = SESSION_SCREEN
            ) {
                SessionScreen().ShowContent()
            }
            scene(
                route = APPLICATIONS_SCREEN
            ) {
                ApplicationsScreen().ShowContent()
            }
            scene(
                route = "$APPLICATION_SCREEN/{$IDENTIFIER_KEY}"
            ) { backstackEntry ->
                val applicationId = backstackEntry.path<String>(IDENTIFIER_KEY)!!
                ApplicationScreen(
                    applicationId = applicationId
                ).ShowContent()
            }
            scene(
                route = "$PLATFORM_SCREEN/{$IDENTIFIER_KEY}/{$NAME_KEY}/{$PLATFORM_KEY}"
            ) { backstackEntry ->
                val applicationId = backstackEntry.path<String>(IDENTIFIER_KEY)!!
                val applicationName = backstackEntry.path<String>(NAME_KEY)!!
                val platform = backstackEntry.path<String>(PLATFORM_KEY)!!
                PlatformScreen(
                    applicationId = applicationId,
                    applicationName = applicationName,
                    platform = Platform.valueOf(platform)
                ).ShowContent()
            }
        }
    }
}

/**
 * Function to check whether are available any updates for each platform and then launch the application
 * which the correct first screen to display
 *
 */
@Composable
@NonRestartableComposable
expect fun CheckForUpdatesAndLaunch()

/**
 * Function to init the local session and the related instances then start the user session
 *
 */
fun startSession() {
    requester = AmetistaRequester(
        host = localUser.hostAddress,
        userId = localUser.userId,
        userToken = localUser.userToken
    )
    val route = if (!localUser.isAuthenticated)
        AUTH_SCREEN
    else if (localUser.password == DEFAULT_VIEWER_PASSWORD)
        CHANGE_VIEWER_PASSWORD_SCREEN
    else
        APPLICATIONS_SCREEN
    setUserLanguage()
    navigator.navigate(route)
}

/**
 * Function to set locale language for the application
 *
 */
expect fun setUserLanguage()

/**
 * Function to manage correctly the back navigation from the current screen
 *
 */
@Composable
@NonRestartableComposable
expect fun CloseApplicationOnNavBack()

/**
 * Function to get the current screen dimension of the device where the application is running
 *
 *
 * @return the width size class based on the current dimension of the screen as [WindowWidthSizeClass]
 */
@Composable
expect fun getCurrentWidthSizeClass(): WindowWidthSizeClass

/**
 * Function to get the image picture's path
 *
 * @param imagePic: the asset from fetch its path
 *
 * @return the asset path as [String]
 */
expect fun getImagePath(
    imagePic: PlatformFile?
): String?