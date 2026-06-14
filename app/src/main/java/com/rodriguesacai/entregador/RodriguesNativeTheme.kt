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
        primary = Color(0xFF82C91E),
        onPrimary = Color(0xFF10200A),
        secondary = Color(0xFF9B6DFF),
        background = Color(0xFF050507),
        surface = Color(0xFF15151C),
        onSurface = Color.White,
        onBackground = Color.White,
        outline = Color(0xFF3B3644)
    )

    val light = lightColorScheme(
        primary = Color(0xFF04A957),
        onPrimary = Color.White,
        secondary = Color(0xFFFF7A00),
        background = Color(0xFFFAFBFC),
        surface = Color(0xFFFFFFFF),
        surfaceVariant = Color(0xFFF4F7F6),
        onSurface = Color(0xFF101216),
        onBackground = Color(0xFF101216),
        outline = Color(0xFFE3E8EF)
    )

    val density = LocalDensity.current
    CompositionLocalProvider(LocalDensity provides Density(density.density, fontScale = 1f)) {
        MaterialTheme(colorScheme = light, typography = typography, content = content)
    }
}
