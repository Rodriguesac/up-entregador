package com.rodriguesacai.entregador.ui

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rodriguesacai.entregador.R
import com.rodriguesacai.entregador.RodriguesFonts
import kotlinx.coroutines.delay

private val Font = RodriguesFonts.App
private val Bg = Color(0xFFF7F7F7)
private val Ink = Color(0xFF1F1F1F)
private val Muted = Color(0xFF667085)
private val Border = Color(0xFFE6E6E6)
private val Navy = Color(0xFFEA1D2C)
private val NavyDark = Color(0xFF9B111E)
private val NavySoft = Color(0xFFFFEBEE)
private val Green = Color(0xFF16A34A)
private val Orange = Color(0xFFD97706)
private val Red = Color(0xFFEA1D2C)
private val SurfaceSoft = Color(0xFFF2F2F2)
private val GreenSoft = Color(0xFFE8F7F1)
private val RedSoft = Color(0xFFFFEBEE)

@Composable
fun UrgentRideScreen(
    rideId: String,
    value: String,
    distance: String,
    duration: String,
    pickup: String,
    dropoff: String,
    paymentMethod: String = "",
    paymentStatus: String = "",
    amountToCollect: String = "",
    changeFor: String = "",
    requiresMachine: String = "",
    onAccept: () -> Unit,
    onReject: () -> Unit,
    onExpired: () -> Unit
) {
    var seconds by remember(rideId) { mutableStateOf(60) }
    LaunchedEffect(rideId) {
        seconds = 60
        while (seconds > 0) {
            delay(1000)
            seconds -= 1
        }
        onExpired()
    }

    Column(
        modifier = Modifier.fillMaxSize().background(Bg),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        UrgentTop(seconds)
        Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(value.ifBlank { "R$ 18,50" }, color = Green, fontFamily = Font, fontSize = 34.sp, lineHeight = 38.sp, fontWeight = FontWeight.Bold)
            Card(
                modifier = Modifier.fillMaxWidth().border(1.dp, Border, RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(Modifier.padding(15.dp), verticalArrangement = Arrangement.spacedBy(11.dp)) {
                    RouteLine(Icons.Filled.Storefront, "Coleta", pickup.ifBlank { "Rodrigues Açaí e Cia. - Av. Gury Marques, Campo Grande - MS" }, Green)
                    RouteLine(Icons.Filled.Place, "Entrega", dropoff.ifBlank { "Rua das Flores, 245 - Jardim dos Estados, Campo Grande - MS" }, Red)
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                        MiniUrgentMetric("Distância", distance.ifBlank { "6,2 km" }, Modifier.weight(1f))
                        MiniUrgentMetric("Tempo", duration.ifBlank { "18 min" }, Modifier.weight(1f))
                    }
                }
            }
            RealDeliveryMap(
                title = "Nova corrida",
                subtitle = listOf(distance, duration).filter { it.isNotBlank() }.joinToString(" • ").ifBlank { "Rota de coleta e entrega" },
                pickupAddress = pickup,
                dropoffAddress = dropoff,
                mode = DeliveryMapMode.PICKUP_TO_DROPOFF,
                modifier = Modifier.height(266.dp)
            )
            UrgentPaymentLine(paymentMethod, paymentStatus, amountToCollect, changeFor, requiresMachine)
            Spacer(Modifier.weight(1f))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(onClick = onReject, modifier = Modifier.weight(1f).height(56.dp), shape = RoundedCornerShape(18.dp)) {
                    Text("Recusar", color = Red, fontFamily = Font, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
                Button(
                    onClick = onAccept,
                    modifier = Modifier.weight(1.35f).height(56.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Navy, contentColor = Color.White)
                ) {
                    Text("Aceitar", fontFamily = Font, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
            Text("Pedido #${rideId.takeLast(6).uppercase()}", color = Muted, fontFamily = Font, fontSize = 11.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        }
    }
}



@Composable
private fun UrgentTop(seconds: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Brush.horizontalGradient(listOf(Red, NavyDark)))
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("NOVA CORRIDA URGENTE", color = Color.White, fontFamily = Font, fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
        Text("00:${seconds.coerceIn(0, 59).toString().padStart(2, '0')}", color = Color.White, fontFamily = Font, fontSize = 15.sp, fontWeight = FontWeight.Bold)
    }
}




@Composable
private fun MiniUrgentMetric(label: String, value: String, modifier: Modifier) {
    Column(modifier.clip(RoundedCornerShape(18.dp)).background(SurfaceSoft).padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(label, color = Muted, fontFamily = Font, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        Text(value, color = Ink, fontFamily = Font, fontSize = 15.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun UrgentLogoMark() {
    Box(Modifier.size(56.dp).shadow(10.dp, RoundedCornerShape(19.dp), clip = false).clip(RoundedCornerShape(19.dp)).background(Color.White).border(1.dp, Navy.copy(alpha = .18f), RoundedCornerShape(19.dp)), contentAlignment = Alignment.Center) {
        Image(painter = painterResource(R.drawable.up_app_icon), contentDescription = "UP Entregador", modifier = Modifier.size(46.dp), contentScale = ContentScale.Fit)
    }
}

@Composable
private fun UrgentMotionRail(seconds: Int) {
    val fraction = (seconds / 60f).coerceIn(.03f, 1f)
    val color = if (seconds <= 10) Red else Navy
    Box(Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(999.dp)).background(color.copy(alpha = .14f))) {
        Box(Modifier.fillMaxWidth(fraction).height(8.dp).clip(RoundedCornerShape(999.dp)).background(Brush.horizontalGradient(listOf(color, Color.White.copy(alpha = .45f)))))
    }
}

@Composable
private fun RouteLine(icon: ImageVector, label: String, value: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(11.dp), modifier = Modifier.fillMaxWidth()) {
        Box(Modifier.size(38.dp).clip(CircleShape).background(color.copy(alpha = .12f)), contentAlignment = Alignment.Center) { Icon(icon, null, tint = color, modifier = Modifier.size(20.dp)) }
        Column(Modifier.weight(1f)) {
            Text(label, color = Muted, fontFamily = Font, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            Text(value.ifBlank { "Não informado" }, color = Ink, fontFamily = Font, fontSize = 14.sp, fontWeight = FontWeight.Bold, maxLines = 2, overflow = TextOverflow.Ellipsis)
        }
    }
}

@Composable
private fun UrgentPaymentLine(method: String, status: String, amount: String, changeFor: String, requiresMachine: String) {
    val raw = method.ifBlank { status }.ifBlank { "Pagamento não informado" }
    val machine = requiresMachine.equals("true", ignoreCase = true) || raw.contains("cart", ignoreCase = true) || raw.contains("maquin", ignoreCase = true)
    val paid = raw.contains("pago", ignoreCase = true) || raw.contains("online", ignoreCase = true)
    Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(22.dp)).background(SurfaceSoft).padding(13.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(if (machine) Icons.Filled.CreditCard else Icons.Filled.Payments, null, tint = if (machine) Orange else Navy, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
            Text(if (paid) "Pago online" else raw, color = Ink, fontFamily = Font, fontSize = 15.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        Text(if (paid) "Nada a cobrar" else amount.ifBlank { "Confirmar pagamento com a operação" }, color = Muted, fontFamily = Font, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        if (machine) Text("Maquininha necessária", color = Orange, fontFamily = Font, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        if (changeFor.isNotBlank()) Text("Troco para $changeFor", color = Red, fontFamily = Font, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}
