package com.tecknobit.ametista.ui.screens.session.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import com.tecknobit.equinoxcompose.utilities.responsiveAssignment

/**
 * Custom [HorizontalDivider] for the [com.tecknobit.ametista.ui.screens.session.presenter.SessionScreen] to divide
 * each section
 */
@Composable
@NonRestartableComposable
fun SectionDivider() {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth(
                    responsiveAssignment(
                        onExpandedSizeClass = { 0.5f },
                        onMediumSizeClass = { 1f },
                        onCompactSizeClass = { 1f },
                    )
                ),
            color = MaterialTheme.colorScheme.primary
        )
    }
}