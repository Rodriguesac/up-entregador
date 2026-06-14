package com.rodriguesacai.entregador

import androidx.compose.ui.text.font.FontFamily

object RodriguesFonts {
    /**
     * Tipografia propria do app: a familia fica centralizada aqui e a escala/
     * fontScale ficam travados no RodriguesNativeTheme para nao variar conforme
     * Xiaomi, Samsung, Motorola ou configuracao de fonte do aparelho.
     */
    val App: FontFamily = FontFamily.SansSerif

    // Alias mantido para compatibilidade interna. Nao usa mais Montserrat.
    val Montserrat: FontFamily = App
}
