@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.ametista.ui.screens.account

import ametista.composeapp.generated.resources.Res
import ametista.composeapp.generated.resources.email
import ametista.composeapp.generated.resources.language
import ametista.composeapp.generated.resources.password
import ametista.composeapp.generated.resources.session
import ametista.composeapp.generated.resources.theme
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.tecknobit.ametista.displayFontFamily
import com.tecknobit.ametista.getImagePath
import com.tecknobit.ametista.imageLoader
import com.tecknobit.ametista.localUser
import com.tecknobit.ametista.ui.screens.AmetistaScreen.Companion.CONTAINER_MAX_WIDTH
import com.tecknobit.equinoxcompose.helpers.session.EquinoxScreen
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.core.PickerMode
import io.github.vinceglb.filekit.core.PickerType
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

class SessionScreen : EquinoxScreen<SessionScreenViewModel>(
    viewModel = SessionScreenViewModel()
) {

    /**
     * Function to arrange the content of the screen to display
     *
     * No-any params required
     */
    @Composable
    override fun ArrangeScreenContent() {
        Scaffold(
            topBar = {
                LargeTopAppBar(
                    colors = TopAppBarDefaults.largeTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = Color.White
                    ),
                    title = {
                        Text(
                            text = stringResource(Res.string.session)
                        )
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(
                        top = paddingValues.calculateTopPadding() + 16.dp,
                        bottom = paddingValues.calculateBottomPadding() + 16.dp
                    )
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                UserDetails()
            }
        }
    }

    @Composable
    @NonRestartableComposable
    private fun UserDetails() {
        Column(
            modifier = Modifier
                .widthIn(
                    max = CONTAINER_MAX_WIDTH
                )
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                val currentProfilePic = remember { mutableStateOf(localUser.profilePic) }
                val picker = rememberFilePickerLauncher(
                    type = PickerType.Image,
                    mode = PickerMode.Single
                ) { profilePic ->
                    val profilePicPath = getImagePath(
                        imagePic = profilePic
                    )
                    profilePicPath?.let {
                        viewModel!!.changeProfilePic(
                            imagePath = profilePicPath,
                            profilePic = currentProfilePic
                        )
                    }
                }
                AsyncImage(
                    modifier = Modifier
                        .size(125.dp)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = CircleShape
                        )
                        .clip(CircleShape)
                        .clickable { picker.launch() },
                    model = ImageRequest.Builder(LocalPlatformContext.current)
                        .data(currentProfilePic.value)
                        .crossfade(true)
                        .crossfade(500)
                        .build(),
                    imageLoader = imageLoader,
                    contentDescription = "Application icon",
                    contentScale = ContentScale.Crop
                    // TODO: TO SET ERROR
                )
                Text(
                    text = localUser.completeName
                )
            }
            HorizontalDivider(
                color = MaterialTheme.colorScheme.primary
            )
            UserData(
                header = Res.string.email,
                data = localUser.email
            )
            UserData(
                header = Res.string.password,
                data = "****"
            )
            UserData(
                header = Res.string.language,
                data = localUser.language
            )
            UserData(
                header = Res.string.theme,
                data = localUser.theme.name
            )
            UserActions()
        }
    }

    @Composable
    @NonRestartableComposable
    private fun UserData(
        header: StringResource,
        data: String
    ) {
        Column(
            modifier = Modifier
                .padding(
                    start = 16.dp
                )
        ) {
            Text(
                text = stringResource(header),
                fontFamily = displayFontFamily,
                fontSize = 14.sp
            )
            Text(
                text = data,
                fontSize = 20.sp
            )
        }
        HorizontalDivider(
            color = MaterialTheme.colorScheme.primary
        )
    }

    @Composable
    @NonRestartableComposable
    private fun UserActions() {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    end = 10.dp
                ),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                shape = RoundedCornerShape(
                    size = 10.dp
                ),
                onClick = {
                    viewModel!!.logout {

                    }
                }
            ) {
                Text(
                    text = "Logout"
                )
            }
            Button(
                modifier = Modifier
                    .padding(
                        start = 10.dp
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                ),
                shape = RoundedCornerShape(
                    size = 10.dp
                ),
                onClick = {
                    viewModel!!.deleteAccount {

                    }
                }
            ) {
                Text(
                    text = "Delete account"
                )
            }
        }
    }

    /**
     * Function to collect or instantiate the states of the screen
     *
     * No-any params required
     */
    @Composable
    override fun CollectStates() {
    }

}