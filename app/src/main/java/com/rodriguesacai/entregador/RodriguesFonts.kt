package com.rodriguesacai.entregador

import androidx.compose.ui.text.font.FontFamily

object RodriguesFonts {
    /**
     * Tipografia oficial do app entregador.
     * Mantém fonte nativa Android/Sans para ficar legível em celular comum.
     * A escala foi aumentada no RodriguesNativeTheme para resolver letras pequenas.
     */
    val App: FontFamily = FontFamily.Default

    // Alias mantido para compatibilidade interna.
    val Montserrat: FontFamily = App
}
