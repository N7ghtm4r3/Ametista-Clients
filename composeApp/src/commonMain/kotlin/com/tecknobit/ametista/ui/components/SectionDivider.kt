// TODO: TO SET
//package com.tecknobit.ametista.ui.components
//
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.material3.HorizontalDivider
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.NonRestartableComposable
//import androidx.compose.ui.Modifier
//import com.tecknobit.ametista.getCurrentWidthSizeClass
//
///**
// * Custom [HorizontalDivider] for the [com.tecknobit.ametista.ui.screens.session.SessionScreen] to divide
// * each section
// */
//@Composable
//@NonRestartableComposable
//fun SectionDivider() {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth(),
//        horizontalArrangement = Arrangement.Center
//    ) {
//        HorizontalDivider(
//            modifier = Modifier
//                .fillMaxWidth(
//                    when (getCurrentWidthSizeClass()) {
//                        WindowWidthSizeClass.Expanded -> 0.5f
//                        else -> 1f
//                    }
//                ),
//            color = MaterialTheme.colorScheme.primary
//        )
//    }
//}