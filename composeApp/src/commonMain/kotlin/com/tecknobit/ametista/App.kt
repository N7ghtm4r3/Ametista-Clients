package com.tecknobit.ametista

import ametista.composeapp.generated.resources.Res
import ametista.composeapp.generated.resources.dm_sans
import ametista.composeapp.generated.resources.kanit
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.text.font.FontFamily
import coil3.ImageLoader
import coil3.addLastModifiedToFileCacheKey
import coil3.compose.LocalPlatformContext
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import coil3.request.CachePolicy
import com.tecknobit.ametista.helpers.AmetistaLocalUser
import com.tecknobit.ametista.helpers.AmetistaRequester
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
import com.tecknobit.ametistacore.models.AmetistaApplication.IDENTIFIER_KEY
import com.tecknobit.ametistacore.models.AmetistaUser.DEFAULT_VIEWER_PASSWORD
import com.tecknobit.ametistacore.models.Platform
import com.tecknobit.ametistacore.models.analytics.AmetistaAnalytic.PLATFORM_KEY
import com.tecknobit.equinox.environment.records.EquinoxUser.NAME_KEY
import io.github.vinceglb.filekit.core.PlatformFile
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.path
import moe.tlaster.precompose.navigation.rememberNavigator
import okhttp3.OkHttpClient
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.ui.tooling.preview.Preview
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSession
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

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
 * **sslContext** -> the context helper to TLS protocols
 */
private val sslContext = SSLContext.getInstance("TLS")

/**
 * **imageLoader** -> the image loader used by coil library to load the image and by-passing the https self-signed certificates
 */
lateinit var imageLoader: ImageLoader

val localUser = AmetistaLocalUser()

lateinit var requester: AmetistaRequester

@Composable
@Preview
fun App() {
    bodyFontFamily = FontFamily(Font(Res.font.dm_sans))
    displayFontFamily = FontFamily(Font(Res.font.kanit))
    sslContext.init(null, validateSelfSignedCertificate(), SecureRandom())
    imageLoader = ImageLoader.Builder(LocalPlatformContext.current)
        .components {
            add(
                OkHttpNetworkFetcherFactory {
                    OkHttpClient.Builder()
                        .sslSocketFactory(sslContext.socketFactory,
                            validateSelfSignedCertificate()[0] as X509TrustManager
                        )
                        .hostnameVerifier { _: String?, _: SSLSession? -> true }
                        .connectTimeout(2, TimeUnit.SECONDS)
                        .build()
                }
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
 * Method to validate a self-signed SLL certificate and bypass the checks of its validity<br></br>
 * No-any params required
 *
 * @return list of trust managers as [Array] of [TrustManager]
 * @apiNote this method disable all checks on the SLL certificate validity, so is recommended to
 * use for test only or in a private distribution on own infrastructure
 */
private fun validateSelfSignedCertificate(): Array<TrustManager> {
    return arrayOf(object : X509TrustManager {
        override fun getAcceptedIssuers(): Array<X509Certificate> {
            return arrayOf()
        }

        override fun checkClientTrusted(certs: Array<X509Certificate>, authType: String) {}
        override fun checkServerTrusted(certs: Array<X509Certificate>, authType: String) {}
    })
}

@Composable
@NonRestartableComposable
expect fun CheckForUpdatesAndLaunch()

fun startSession() {
    MainScope().launch { // TODO: TO REMOVE 
        delay(250)
        requester = AmetistaRequester(
            host = localUser.hostAddress,
            userId = localUser.userId,
            userToken = localUser.userToken
        )
        val route = if (localUser.userId == null)
            AUTH_SCREEN
        else if (localUser.password == DEFAULT_VIEWER_PASSWORD)
            CHANGE_VIEWER_PASSWORD_SCREEN
        else
            APPLICATIONS_SCREEN
        navigator.navigate(route)
    }
}

/**
 * Function to manage correctly the back navigation from the current screen
 *
 * No-any params required
 */
@Composable
@NonRestartableComposable
expect fun CloseApplicationOnNavBack()

@Composable
expect fun getCurrentWidthSizeClass(): WindowWidthSizeClass

expect fun getImagePath(
    imagePic: PlatformFile?
): String?