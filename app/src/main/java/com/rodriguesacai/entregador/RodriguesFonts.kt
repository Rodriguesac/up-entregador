package com.rodriguesacai.entregador

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont

object RodriguesFonts {
    /**
     * Fonte oficial: Montserrat via Google Fonts Provider.
     * Não embute arquivo .ttf/.otf no pacote; o Android baixa/cacheia pelo provedor.
     * Se o provedor não estiver disponível, o Compose usa fallback sem quebrar o app.
     */
    private val provider = GoogleFont.Provider(
        providerAuthority = "com.google.android.gms.fonts",
        providerPackage = "com.google.android.gms",
        certificates = R.array.com_google_android_gms_fonts_certs
    )

    private val montserrat = GoogleFont("Montserrat")

    val App: FontFamily = FontFamily(
        Font(googleFont = montserrat, fontProvider = provider, weight = FontWeight.Normal),
        Font(googleFont = montserrat, fontProvider = provider, weight = FontWeight.Medium),
        Font(googleFont = montserrat, fontProvider = provider, weight = FontWeight.SemiBold),
        Font(googleFont = montserrat, fontProvider = provider, weight = FontWeight.Bold),
        Font(googleFont = montserrat, fontProvider = provider, weight = FontWeight.ExtraBold)
    )

    val Montserrat: FontFamily = App
}
