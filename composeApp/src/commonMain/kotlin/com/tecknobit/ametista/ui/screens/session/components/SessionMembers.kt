// TODO: TO SET

//package com.tecknobit.ametista.ui.screens.session.components
//
//import ametista.composeapp.generated.resources.Res
//import ametista.composeapp.generated.resources.Res.string
//import ametista.composeapp.generated.resources.logo
//import ametista.composeapp.generated.resources.no_members
//import androidx.compose.animation.AnimatedVisibility
//import androidx.compose.animation.fadeIn
//import androidx.compose.animation.fadeOut
//import androidx.compose.foundation.border
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.widthIn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Groups3
//import androidx.compose.material.icons.filled.PersonRemove
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.ListItem
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.NonRestartableComposable
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import coil3.compose.AsyncImage
//import coil3.compose.LocalPlatformContext
//import coil3.request.ImageRequest
//import coil3.request.crossfade
//import com.tecknobit.ametista.displayFontFamily
//import com.tecknobit.ametista.imageLoader
//import com.tecknobit.ametista.localUser
//import com.tecknobit.ametista.ui.components.FirstPageProgressIndicator
//import com.tecknobit.ametista.ui.components.NewPageProgressIndicator
//import com.tecknobit.ametista.ui.components.RoleBadge
//import com.tecknobit.ametista.ui.components.SectionDivider
//import com.tecknobit.ametista.ui.screens.session.presentation.SessionScreenViewModel
//import com.tecknobit.ametistacore.models.AmetistaMember
//import com.tecknobit.equinoxcompose.components.EmptyListUI
//import com.tecknobit.equinoxcompose.helpers.session.ManagedContent
//import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyColumn
//import org.jetbrains.compose.resources.painterResource
//
///**
// * **viewModel** -> The viewmodel related to the [com.tecknobit.ametista.ui.screens.session.SessionScreen]
// */
//private lateinit var viewModel: SessionScreenViewModel
//
///**
// * Section with the account details of the current [localUser]
// *
// * @param screenViewModel The viewmodel related to the [com.tecknobit.ametista.ui.screens.session.SessionScreen]
// */
//@Composable
//@NonRestartableComposable
//fun SessionMembers(
//    screenViewModel: SessionScreenViewModel
//) {
//    ManagedContent(
//        viewModel = screenViewModel,
//        content = {
//            Column(
//                modifier = Modifier
//                    .fillMaxSize(),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                val membersIsEmpty = remember { mutableStateOf(false) }
//                viewModel = screenViewModel
//                PaginatedLazyColumn(
//                    modifier = Modifier
//                        .widthIn(
//                            max = 800.dp
//                        ),
//                    paginationState = viewModel.paginationState,
//                    firstPageProgressIndicator = { FirstPageProgressIndicator() },
//                    newPageProgressIndicator = { NewPageProgressIndicator() },
//                    verticalArrangement = Arrangement.spacedBy(10.dp),
//                    contentPadding = PaddingValues(
//                        bottom = 16.dp
//                    )
//                ) {
//                    val members = viewModel.paginationState.allItems!!
//                    membersIsEmpty.value = members.isEmpty()
//                    if (members.isNotEmpty()) {
//                        items(
//                            items = members,
//                            key = { issue -> issue.id }
//                        ) { member ->
//                            Member(
//                                member = member
//                            )
//                        }
//                    }
//                }
//                NoMembers(
//                    noMembers = membersIsEmpty.value
//                )
//            }
//        }
//    )
//}
//
///**
// * The component to display the details of an [AmetistaMember]
// *
// * @param member The member to display
// */
//@Composable
//@NonRestartableComposable
//private fun Member(
//    member: AmetistaMember
//) {
//    ListItem(
//        leadingContent = {
//            AsyncImage(
//                modifier = Modifier
//                    .size(80.dp)
//                    .border(
//                        width = 1.dp,
//                        color = MaterialTheme.colorScheme.primary,
//                        shape = CircleShape
//                    )
//                    .clip(CircleShape),
//                model = ImageRequest.Builder(LocalPlatformContext.current)
//                    .data(localUser.hostAddress + "/" + member.profilePic)
//                    .crossfade(true)
//                    .crossfade(500)
//                    .build(),
//                imageLoader = imageLoader,
//                contentDescription = "Member profile picture",
//                contentScale = ContentScale.Crop,
//                error = painterResource(Res.drawable.logo)
//            )
//        },
//        overlineContent = {
//            RoleBadge(
//                role = member.role
//            )
//        },
//        headlineContent = {
//            Text(
//                text = member.completeName
//            )
//        },
//        supportingContent = {
//            Text(
//                text = member.email
//            )
//        },
//        trailingContent = {
//            if (localUser.isAdmin()) {
//                IconButton(
//                    onClick = {
//                        viewModel.removeMember(
//                            member = member
//                        )
//                    }
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.PersonRemove,
//                        contentDescription = null
//                    )
//                }
//            }
//        }
//    )
//    SectionDivider()
//}
//
///**
// * The layout to display when no member (apart the [localUser]) are registered in the system
// *
// * @param noMembers Whether some member are registered in the system
// */
//@Composable
//@NonRestartableComposable
//private fun NoMembers(
//    noMembers: Boolean,
//) {
//    AnimatedVisibility(
//        visible = noMembers,
//        enter = fadeIn(),
//        exit = fadeOut()
//    ) {
//        EmptyListUI(
//            imageModifier = Modifier
//                .size(150.dp),
//            icon = Icons.Default.Groups3,
//            subText = string.no_members,
//            textStyle = TextStyle(
//                fontFamily = displayFontFamily,
//                fontSize = 20.sp
//            )
//        )
//    }
//}