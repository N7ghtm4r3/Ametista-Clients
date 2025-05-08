@file:OptIn(ExperimentalMultiplatform::class)

package com.tecknobit.ametista.ui.screens.session.components

import ametista.composeapp.generated.resources.Res
import ametista.composeapp.generated.resources.logo
import ametista.composeapp.generated.resources.no_members
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonRemove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.tecknobit.ametista.imageLoader
import com.tecknobit.ametista.localUser
import com.tecknobit.ametista.ui.components.FirstPageProgressIndicator
import com.tecknobit.ametista.ui.components.NewPageProgressIndicator
import com.tecknobit.ametista.ui.screens.session.data.AmetistaMember
import com.tecknobit.ametista.ui.screens.session.presentation.SessionScreenViewModel
import com.tecknobit.ametista.ui.theme.AppTypography
import com.tecknobit.equinoxcompose.components.EmptyState
import com.tecknobit.equinoxcompose.session.ManagedContent
import com.tecknobit.equinoxcompose.utilities.responsiveAssignment
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyColumn
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/**
 * Section with the account details of the current [localUser]
 *
 * @param viewModel The viewmodel related to the [com.tecknobit.ametista.ui.screens.session.presenter.SessionScreen]
 */
@Composable
@NonRestartableComposable
fun SessionMembers(
    viewModel: SessionScreenViewModel,
) {
    ManagedContent(
        viewModel = viewModel,
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PaginatedLazyColumn(
                    modifier = Modifier
                        // TODO: TO SET
                        .widthIn(
                            max = 1280.dp
                        ),
                    paginationState = viewModel.membersState,
                    firstPageProgressIndicator = { FirstPageProgressIndicator() },
                    newPageProgressIndicator = { NewPageProgressIndicator() },
                    firstPageEmptyIndicator = {
                        EmptyState(
                            resource = Res.drawable.no_members,
                            contentDescription = "No members available",
                            resourceSize = responsiveAssignment(
                                onExpandedSizeClass = { 350.dp },
                                onMediumSizeClass = { 275.dp },
                                onCompactSizeClass = { 250.dp }
                            ),
                            title = stringResource(Res.string.no_members),
                            titleStyle = AppTypography.titleMedium
                        )
                    },
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(
                        bottom = 16.dp
                    )
                ) {
                    items(
                        items = viewModel.membersState.allItems!!,
                        key = { member -> member.id }
                    ) { member ->
                        Member(
                            member = member,
                            viewModel = viewModel
                        )
                    }
                }
            }
        }
    )
}

/**
 * The component to display the details of an [AmetistaMember]
 *
 * @param member The member to display
 * @param viewModel The viewmodel related to the [com.tecknobit.ametista.ui.screens.session.presenter.SessionScreen]
 */
@Composable
@NonRestartableComposable
private fun Member(
    member: AmetistaMember,
    viewModel: SessionScreenViewModel,
) {
    ListItem(
        leadingContent = {
            AsyncImage(
                modifier = Modifier
                    .size(80.dp)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    )
                    .clip(CircleShape),
                model = ImageRequest.Builder(LocalPlatformContext.current)
                    .data(localUser.hostAddress + "/" + member.profilePic)
                    .crossfade(true)
                    .crossfade(500)
                    .build(),
                imageLoader = imageLoader,
                contentDescription = "Member profile picture",
                contentScale = ContentScale.Crop,
                error = painterResource(Res.drawable.logo)
            )
        },
        overlineContent = {
            RoleBadge(
                role = member.role
            )
        },
        headlineContent = {
            Text(
                text = member.completeName
            )
        },
        supportingContent = {
            Text(
                text = member.email
            )
        },
        trailingContent = {
            if (localUser.isAdmin()) {
                IconButton(
                    onClick = {
                        viewModel.removeMember(
                            member = member
                        )
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.PersonRemove,
                        contentDescription = null
                    )
                }
            }
        }
    )
    SectionDivider()
}