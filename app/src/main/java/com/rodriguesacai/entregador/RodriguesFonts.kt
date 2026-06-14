package com.rodriguesacai.entregador

import androidx.compose.ui.text.font.FontFamily

object RodriguesFonts {
    /**
     * Fonte final do app: usa a fonte nativa do Android (Roboto/Sans), que fica mais limpa,
     * mais leve e mais profissional em campos, menus e telas operacionais.
     */
    val App: FontFamily = FontFamily.Default

    // Alias mantido para compatibilidade interna. Nao usa mais Montserrat.
    val Montserrat: FontFamily = App
}
