package com.tecknobit.ametista.ui.screens.applications

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.tecknobit.ametista.bodyFontFamily
import com.tecknobit.ametista.displayFontFamily
import com.tecknobit.ametista.imageLoader
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyVerticalGrid

@Composable
@NonRestartableComposable
actual fun Applications(
    paddingValues: PaddingValues,
    viewModel: ApplicationsScreenViewModel
) {
    Column (
        modifier = Modifier
            .padding(
                top = paddingValues.calculateTopPadding() + 16.dp,
                bottom = paddingValues.calculateBottomPadding() + 16.dp
            )
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PaginatedLazyVerticalGrid(
            modifier = Modifier
                .widthIn(
                    max = 1200.dp
                )
                .fillMaxHeight(),
            paginationState = viewModel.paginationState,
            columns = GridCells.Adaptive(
                minSize = 400.dp
            ),
            firstPageProgressIndicator = {
                CircularProgressIndicator()
            },
            newPageProgressIndicator = {
                LinearProgressIndicator()
            }
        ) {
            items(
                items = viewModel.paginationState.allItems!!,
                key = { it }
            ) {
                ApplicationItem()
            }
        }
    }
}

@Composable
@NonRestartableComposable
actual fun ApplicationItem() {
    Card (
        modifier = Modifier
            .padding(
                all = 10.dp
            )
            .size(
                width = 400.dp,
                height = 450.dp
            ),
        shape = RoundedCornerShape(
            size = 15.dp
        ),
        onClick = {
            // TODO: NAV TO APPLICATION
        }
    ) {
        ApplicationIcon(
            modifier = Modifier
                .weight(1f)
        )
        HorizontalDivider(
            color = MaterialTheme.colorScheme.primaryContainer
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(
                    all = 16.dp
                ),
        ) {
            Text(
                text = "Application name",
                fontFamily = displayFontFamily,
                fontSize = 20.sp
            )
            Text(
                text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce consequat imperdiet accumsan. Quisque nisl mi, dignissim et mauris pharetra, laoreet dictum leo. Aenean efficitur lorem a nibh ullamcorper, a commodo lorem tincidunt. Integer cursus posuere tempor. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Maecenas ullamcorper elit a varius placerat. Vivamus nec ex ultricies, accumsan est sed, facilisis ipsum. Vivamus condimentum lacinia mi, at ultrices purus tincidunt eget. Ut dictum mi augue, vitae maximus mi feugiat sit amet. Mauris ipsum arcu, fermentum non orci non, blandit bibendum dui. Sed nulla justo, posuere at lectus venenatis, ullamcorper porttitor odio. Cras sed dolor turpis. Duis eu varius mauris, at euismod enim. Nulla facilisi. Pellentesque consequat venenatis tortor id aliquam.",
                maxLines = 6,
                textAlign = TextAlign.Justify,
                overflow = TextOverflow.Ellipsis,
                fontFamily = bodyFontFamily,
                fontSize = 14.sp
            )
        }
    }
}

@NonRestartableComposable
@Composable
actual fun ApplicationIcon(
    modifier: Modifier
) {
    AsyncImage(
        modifier = modifier
            .fillMaxWidth(),
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