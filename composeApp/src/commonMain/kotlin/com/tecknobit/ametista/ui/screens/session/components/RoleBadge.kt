package com.tecknobit.ametista.ui.screens.session.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecknobit.ametistacore.enums.Role
import com.tecknobit.ametistacore.enums.Role.VIEWER

/**
 * The badge of a [Role]
 *
 * @param modifier The modifier to apply to the component
 * @param role The role to create the related badge
 */
@Composable
@NonRestartableComposable
fun RoleBadge(
    modifier: Modifier = Modifier,
    role: Role,
) {
    val color = when (role) {
        VIEWER -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.error
    }
    OutlinedCard(
        modifier = modifier,
        colors = CardDefaults.outlinedCardColors(
            contentColor = color
        ),
        border = BorderStroke(
            width = 1.dp,
            color = color
        ),
        shape = RoundedCornerShape(
            size = 5.dp
        )
    ) {
        Text(
            modifier = Modifier
                .padding(
                    all = 2.dp
                ),
            text = role.name
        )
    }
}