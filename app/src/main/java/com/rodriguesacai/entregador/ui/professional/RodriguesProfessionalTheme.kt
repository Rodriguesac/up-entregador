package com.rodriguesacai.entregador.ui.professional

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object RodriguesColors {
    val Navy950 = Color(0xFF06113D)
    val Navy900 = Color(0xFF08164A)
    val Navy850 = Color(0xFF0B1D5C)
    val Navy800 = Color(0xFF102466)
    val Lime500 = Color(0xFFB7E51E)
    val Lime400 = Color(0xFFD8F72F)
    val Lime100 = Color(0xFFF4FFD0)
    val Blue500 = Color(0xFF1E4FFF)
    val Blue100 = Color(0xFFEAF0FF)
    val Surface = Color(0xFFFFFFFF)
    val SurfaceSoft = Color(0xFFF6F8FD)
    val Border = Color(0xFFE1E7F5)
    val TextPrimary = Color(0xFF071445)
    val TextSecondary = Color(0xFF65708E)
    val Danger = Color(0xFFE5484D)
    val Success = Color(0xFF20B15A)
    val Warning = Color(0xFFFFB020)
}

private val RodriguesProfessionalColorScheme = lightColorScheme(
    primary = RodriguesColors.Lime500,
    onPrimary = RodriguesColors.Navy900,
    secondary = RodriguesColors.Blue500,
    onSecondary = Color.White,
    background = RodriguesColors.Navy950,
    onBackground = Color.White,
    surface = RodriguesColors.Surface,
    onSurface = RodriguesColors.TextPrimary,
    error = RodriguesColors.Danger
)

@Composable
fun RodriguesProfessionalTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = RodriguesProfessionalColorScheme,
        content = content
    )
}
