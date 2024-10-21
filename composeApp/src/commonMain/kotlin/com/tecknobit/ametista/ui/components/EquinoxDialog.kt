package com.tecknobit.ametista.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.tecknobit.equinoxcompose.helpers.viewmodels.EquinoxViewModel
import org.jetbrains.annotations.TestOnly

@Composable
@TestOnly
@Deprecated(
    "JUST FOR TESTING WILL BE INTEGRATED IN THE OFFICIAL EQUINOX-COMPOSE LIBRARY",
    ReplaceWith("The official component")
)
fun EquinoxDialog(
    show: MutableState<Boolean>,
    viewModel: EquinoxViewModel? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    if (show.value) {
        viewModel?.suspendRefresher()
        Dialog(
            onDismissRequest = {
                show.value = false
                viewModel?.restartRefresher()
            },
            properties = DialogProperties(
                dismissOnBackPress = true
            )
        ) {
            Surface(
                modifier = Modifier
                    .width(400.dp)
                    .heightIn(
                        max = 550.dp
                    ),
                shape = RoundedCornerShape(
                    size = 15.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(
                            horizontal = 16.dp
                        )
                        .padding(
                            top = 16.dp,
                            bottom = 16.dp
                        ),
                    content = content
                )
            }
        }
    }
}