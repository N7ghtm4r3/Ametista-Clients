package com.tecknobit.ametista.ui.components

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
import com.tecknobit.ametistacore.models.AmetistaUser.Role
import com.tecknobit.ametistacore.models.AmetistaUser.Role.VIEWER

@Composable
@NonRestartableComposable
fun RoleBadge(
    role: Role
) {
    val color = when (role) {
        VIEWER -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.error
    }
    OutlinedCard(
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
                    all = 1.dp
                ),
            text = role.name
        )
    }
}