package com.tlw.vymo.assignment.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import com.tlw.vymo.assignment.designsystem.tokens.DarkVymoColors
import com.tlw.vymo.assignment.designsystem.tokens.LightVymoColors
import com.tlw.vymo.assignment.designsystem.tokens.VymoColors
import com.tlw.vymo.assignment.designsystem.tokens.VymoShapes
import com.tlw.vymo.assignment.designsystem.tokens.VymoSpacing
import com.tlw.vymo.assignment.designsystem.tokens.VymoTypography

private val LocalVymoColors = staticCompositionLocalOf { LightVymoColors }
private val LocalVymoSpacing = staticCompositionLocalOf { VymoSpacing() }
private val LocalVymoShapes = staticCompositionLocalOf { VymoShapes() }
private val LocalVymoTypography = staticCompositionLocalOf { VymoTypography() }

object VymoTheme {
    val colors: VymoColors
        @Composable @ReadOnlyComposable get() = LocalVymoColors.current

    val spacing: VymoSpacing
        @Composable @ReadOnlyComposable get() = LocalVymoSpacing.current

    val shapes: VymoShapes
        @Composable @ReadOnlyComposable get() = LocalVymoShapes.current

    val typography: VymoTypography
        @Composable @ReadOnlyComposable get() = LocalVymoTypography.current
}

@Composable
fun VymoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = if (darkTheme) DarkVymoColors else LightVymoColors

    CompositionLocalProvider(LocalVymoColors provides colors) {
        // Material components used inside the atoms (menus, ripples, checkbox)
        // pick up the same palette.
        MaterialTheme(
            colorScheme = colors.toMaterialColorScheme(darkTheme),
            content = content,
        )
    }
}

private fun VymoColors.toMaterialColorScheme(darkTheme: Boolean) =
    if (darkTheme) {
        darkColorScheme(
            primary = primary,
            onPrimary = onPrimary,
            background = background,
            onBackground = onSurface,
            surface = surface,
            onSurface = onSurface,
            onSurfaceVariant = onSurfaceMuted,
            outline = border,
            error = error,
            onError = onError,
        )
    } else {
        lightColorScheme(
            primary = primary,
            onPrimary = onPrimary,
            background = background,
            onBackground = onSurface,
            surface = surface,
            onSurface = onSurface,
            onSurfaceVariant = onSurfaceMuted,
            outline = border,
            error = error,
            onError = onError,
        )
    }
