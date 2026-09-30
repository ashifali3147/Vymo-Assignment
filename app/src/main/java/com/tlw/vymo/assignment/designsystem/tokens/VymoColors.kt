package com.tlw.vymo.assignment.designsystem.tokens

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class VymoColors(
    val primary: Color,
    val onPrimary: Color,
    val background: Color,
    val surface: Color,
    val onSurface: Color,
    val onSurfaceMuted: Color,
    val border: Color,
    val borderFocused: Color,
    val error: Color,
    val onError: Color,
    val disabled: Color,
    val onDisabled: Color,
)

private object Palette {
    val Indigo600 = Color(0xFF3F51B5)
    val Indigo300 = Color(0xFF9FA8FF)
    val Indigo900 = Color(0xFF1A237E)
    val Grey50 = Color(0xFFF7F8FA)
    val Grey200 = Color(0xFFDCDFE4)
    val Grey400 = Color(0xFF9AA1AC)
    val Grey600 = Color(0xFF5F6671)
    val Grey800 = Color(0xFF2B2F36)
    val Grey900 = Color(0xFF16181C)
    val Red600 = Color(0xFFC62828)
    val Red300 = Color(0xFFFF8A80)
    val White = Color(0xFFFFFFFF)
}

val LightVymoColors = VymoColors(
    primary = Palette.Indigo600,
    onPrimary = Palette.White,
    background = Palette.Grey50,
    surface = Palette.White,
    onSurface = Palette.Grey900,
    onSurfaceMuted = Palette.Grey600,
    border = Palette.Grey400,
    borderFocused = Palette.Indigo600,
    error = Palette.Red600,
    onError = Palette.White,
    disabled = Palette.Grey200,
    onDisabled = Palette.Grey600,
)

val DarkVymoColors = VymoColors(
    primary = Palette.Indigo300,
    onPrimary = Palette.Indigo900,
    background = Palette.Grey900,
    surface = Palette.Grey800,
    onSurface = Palette.Grey50,
    onSurfaceMuted = Palette.Grey400,
    border = Palette.Grey600,
    borderFocused = Palette.Indigo300,
    error = Palette.Red300,
    onError = Palette.Grey900,
    disabled = Palette.Grey800,
    onDisabled = Palette.Grey400,
)
