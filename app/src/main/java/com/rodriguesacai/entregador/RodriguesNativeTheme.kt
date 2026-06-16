package com.rodriguesacai.entregador

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density

@Composable
fun RodriguesNativeTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val appFont = RodriguesFonts.App
    val base = Typography()
    val typography = base.copy(
        displayLarge = base.displayLarge.copy(fontFamily = appFont),
        displayMedium = base.displayMedium.copy(fontFamily = appFont),
        displaySmall = base.displaySmall.copy(fontFamily = appFont),
        headlineLarge = base.headlineLarge.copy(fontFamily = appFont),
        headlineMedium = base.headlineMedium.copy(fontFamily = appFont),
        headlineSmall = base.headlineSmall.copy(fontFamily = appFont),
        titleLarge = base.titleLarge.copy(fontFamily = appFont),
        titleMedium = base.titleMedium.copy(fontFamily = appFont),
        titleSmall = base.titleSmall.copy(fontFamily = appFont),
        bodyLarge = base.bodyLarge.copy(fontFamily = appFont),
        bodyMedium = base.bodyMedium.copy(fontFamily = appFont),
        bodySmall = base.bodySmall.copy(fontFamily = appFont),
        labelLarge = base.labelLarge.copy(fontFamily = appFont),
        labelMedium = base.labelMedium.copy(fontFamily = appFont),
        labelSmall = base.labelSmall.copy(fontFamily = appFont)
    )

    val dark = darkColorScheme(
        primary = Color(0xFFB7E51E),
        onPrimary = Color(0xFF08164A),
        secondary = Color(0xFF1E4FFF),
        background = Color(0xFF08164A),
        surface = Color(0xFF101E5F),
        onSurface = Color.White,
        onBackground = Color.White,
        outline = Color(0xFF2A1E8A)
    )

    val light = lightColorScheme(
        primary = Color(0xFFB7E51E),
        onPrimary = Color(0xFF08164A),
        secondary = Color(0xFF1E4FFF),
        background = Color(0xFFF7F9FF),
        surface = Color(0xFFFFFFFF),
        surfaceVariant = Color(0xFFEFF4FF),
        onSurface = Color(0xFF08164A),
        onBackground = Color(0xFF08164A),
        outline = Color(0xFFDDE6FF)
    )

    val density = LocalDensity.current
    CompositionLocalProvider(LocalDensity provides Density(density.density, fontScale = 1.08f)) {
        MaterialTheme(colorScheme = if (darkTheme) dark else light, typography = typography, content = content)
    }
}
