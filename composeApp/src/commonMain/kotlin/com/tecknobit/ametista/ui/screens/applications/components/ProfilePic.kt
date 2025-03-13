package com.tecknobit.ametista.ui.screens.applications.components

import ametista.composeapp.generated.resources.Res
import ametista.composeapp.generated.resources.logo
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.tecknobit.ametista.SESSION_SCREEN
import com.tecknobit.ametista.imageLoader
import com.tecknobit.ametista.localUser
import com.tecknobit.ametista.navigator
import com.tecknobit.equinoxcompose.utilities.responsiveAssignment
import org.jetbrains.compose.resources.painterResource

@Composable
@NonRestartableComposable
fun ProfilePic() {
    AsyncImage(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = Color.White,
                shape = CircleShape
            )
            .size(
                size = responsiveAssignment(
                    onExpandedSizeClass = { 55.dp },
                    onMediumSizeClass = { 65.dp },
                    onCompactSizeClass = { 65.dp }
                )
            )
            .clip(CircleShape)
            .clickable { navigator.navigate(SESSION_SCREEN) },
        model = ImageRequest.Builder(LocalPlatformContext.current)
            .data(localUser.profilePic)
            .crossfade(true)
            .crossfade(500)
            .build(),
        imageLoader = imageLoader,
        contentDescription = "Application icon",
        contentScale = ContentScale.Crop,
        error = painterResource(Res.drawable.logo)
    )
}