package com.tecknobit.ametista.ui.screens.applications

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.tecknobit.ametista.imageLoader

@NonRestartableComposable
@Composable
actual fun ApplicationItem() {
    ListItem(
        leadingContent = {
            Column {
                AsyncImage(
                    modifier = Modifier
                        .size(100.dp)
                        .border(
                            width = 1.5.dp,
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = CircleShape
                        )
                        .clip(CircleShape),
                    model = ImageRequest.Builder(LocalPlatformContext.current)
                        .data("https://www.euroschoolindia.com/wp-content/uploads/2023/06/facts-about-space.jpg")
                        .crossfade(true)
                        .crossfade(500)
                        .build(),
                    imageLoader = imageLoader,
                    contentDescription = "Application icon",
                    contentScale = ContentScale.Crop
                    // TODO: TO SET ERROR
                )
            }
        },
        headlineContent = {
            Text(
                text = "Application name"
            )
        },
        supportingContent = {
            Text(
                text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce consequat imperdiet accumsan. Quisque nisl mi, dignissim et mauris pharetra, laoreet dictum leo. Aenean efficitur lorem a nibh ullamcorper, a commodo lorem tincidunt. Integer cursus posuere tempor. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Maecenas ullamcorper elit a varius placerat. Vivamus nec ex ultricies, accumsan est sed, facilisis ipsum. Vivamus condimentum lacinia mi, at ultrices purus tincidunt eget. Ut dictum mi augue, vitae maximus mi feugiat sit amet. Mauris ipsum arcu, fermentum non orci non, blandit bibendum dui. Sed nulla justo, posuere at lectus venenatis, ullamcorper porttitor odio. Cras sed dolor turpis. Duis eu varius mauris, at euismod enim. Nulla facilisi. Pellentesque consequat venenatis tortor id aliquam.",
                maxLines = 4,
                textAlign = TextAlign.Justify,
                overflow = TextOverflow.Ellipsis
            )
        },
        trailingContent = {
            Column(
                modifier = Modifier
                    .height(110.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = null
                )
            }
        }
    )
    HorizontalDivider()
}