package com.tlw.vymo.assignment.designsystem.tokens

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class VymoSpacing(
    val xs: Dp = 4.dp,
    val sm: Dp = 8.dp,
    val md: Dp = 12.dp,
    val lg: Dp = 16.dp,
    val xl: Dp = 24.dp,
    val xxl: Dp = 32.dp,
)

@Immutable
data class VymoShapes(
    val cornerSmall: Dp = 4.dp,
    val cornerMedium: Dp = 8.dp,
    val borderThin: Dp = 1.dp,
    val borderThick: Dp = 2.dp,
)
