package com.rodriguesacai.entregador.ui.design

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object EntregadorColors {
    val Background = Color(0xFFF7F9FF)
    val Surface = Color(0xFFFFFFFF)
    val SurfaceSoft = Color(0xFFEFF4FF)
    val Border = Color(0xFFDDE6FF)
    val Text = Color(0xFF08164A)
    val Muted = Color(0xFF4B587C)
    val MutedLight = Color(0xFF647092)
    val Navy = Color(0xFFB7E51E)
    val NavyDark = Color(0xFF2A1E8A)
    val NavySoft = Color(0xFFEFF8B8)
    val Green = Color(0xFFB7E51E)
    val GreenDark = Color(0xFF8CB80F)
    val Orange = Color(0xFFE8E61A)
    val Red = Color(0xFFE53935)
    val Blue = Color(0xFF1E4FFF)
}

object EntregadorShape {
    val Card = RoundedCornerShape(24.dp)
    val Button = RoundedCornerShape(18.dp)
    val Chip = RoundedCornerShape(999.dp)
}

@Composable
fun EntregadorCard(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Card(
        shape = EntregadorShape.Card,
        colors = CardDefaults.cardColors(containerColor = EntregadorColors.Surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = modifier.fillMaxWidth().border(1.dp, EntregadorColors.Border, EntregadorShape.Card)
    ) {
        Column(Modifier.padding(14.dp), content = content)
    }
}

@Composable
fun EntregadorPrimaryButton(text: String, modifier: Modifier = Modifier, enabled: Boolean = true, onClick: () -> Unit, content: @Composable RowScope.() -> Unit = { Text(text, fontWeight = FontWeight.Bold, fontSize = 15.sp) }) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.fillMaxWidth().height(56.dp),
        shape = EntregadorShape.Button,
        colors = ButtonDefaults.buttonColors(
            containerColor = EntregadorColors.Navy,
            contentColor = EntregadorColors.Text,
            disabledContainerColor = Color(0xFFE6EBEF),
            disabledContentColor = EntregadorColors.MutedLight
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp, pressedElevation = 0.dp),
        content = content
    )
}

@Composable
fun EntregadorTitle(title: String, subtitle: String = "") {
    Text(title, color = EntregadorColors.Text, fontSize = 26.sp, lineHeight = 29.sp, fontWeight = FontWeight.Bold)
    if (subtitle.isNotBlank()) Text(subtitle, color = EntregadorColors.Muted, fontSize = 14.sp, lineHeight = 20.sp, fontWeight = FontWeight.SemiBold)
}
