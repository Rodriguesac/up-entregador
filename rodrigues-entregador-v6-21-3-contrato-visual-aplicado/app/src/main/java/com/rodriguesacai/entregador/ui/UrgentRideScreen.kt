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
private val Bg = Color(0xFFFAFBFC)
private val Ink = Color(0xFF101216)
private val Muted = Color(0xFF5B6673)
private val Border = Color(0xFFE3E8EF)
private val Green = Color(0xFF04A957)
private val Orange = Color(0xFFFF7A00)
private val Red = Color(0xFFEF233C)
private val SurfaceSoft = Color(0xFFF4F7F6)
private val GreenSoft = Color(0xFFEAF8EF)
private val RedSoft = Color(0xFFFFEAEE)

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
        modifier = Modifier.fillMaxSize().background(Bg).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        UrgentTop(seconds)
        Card(
            modifier = Modifier.fillMaxWidth().shadow(12.dp, RoundedCornerShape(24.dp), clip = false, ambientColor = Color(0x2204A957), spotColor = Color(0x11000000)).border(1.dp, Border, RoundedCornerShape(24.dp)),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(Modifier.padding(15.dp), verticalArrangement = Arrangement.spacedBy(9.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    UrgentLogoMark()
                    Column(Modifier.weight(1f)) {
                        Text("Rodrigues Entregador", color = Green, fontFamily = Font, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Text("Nova corrida", color = Ink, fontFamily = Font, fontSize = 25.sp, fontWeight = FontWeight.ExtraBold)
                    }
                }
                Text(value.ifBlank { "Valor a definir" }, color = Green, fontFamily = Font, fontSize = 40.sp, fontWeight = FontWeight.Black)
                Text(listOf(distance, duration).filter { it.isNotBlank() }.joinToString(" • ").ifBlank { "Dados da rota aguardando sincronização" }, color = Muted, fontFamily = Font, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                UrgentMotionRail(seconds)
            }
        }
        RealDeliveryMap(
            title = "Oferta #${rideId.takeLast(6).uppercase()}",
            subtitle = listOf(distance, duration).filter { it.isNotBlank() }.joinToString(" • "),
            pickupAddress = pickup,
            dropoffAddress = dropoff,
            mode = DeliveryMapMode.PICKUP_TO_DROPOFF,
            modifier = Modifier.height(272.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth().border(1.dp, Border, RoundedCornerShape(24.dp)),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                RouteLine(Icons.Filled.Storefront, "Coleta", pickup.ifBlank { "Rodrigues Açaí e Cia." }, Green)
                RouteLine(Icons.Filled.Place, "Entrega", dropoff.ifBlank { "Área da entrega" }, Orange)
                UrgentPaymentLine(paymentMethod, paymentStatus, amountToCollect, changeFor, requiresMachine)
            }
        }
        Spacer(Modifier.weight(1f))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(onClick = onReject, modifier = Modifier.weight(1f).height(54.dp), shape = RoundedCornerShape(22.dp)) {
                Text("Recusar", color = Red, fontFamily = Font, fontWeight = FontWeight.Black)
            }
            Button(
                onClick = onAccept,
                modifier = Modifier.weight(1.4f).height(54.dp),
                shape = RoundedCornerShape(22.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Green, contentColor = Color.White)
            ) {
                Icon(Icons.Filled.CheckCircle, null, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Text("Aceitar", fontFamily = Font, fontSize = 16.sp, fontWeight = FontWeight.Black)
            }
        }
        Text("Pedido #${rideId.takeLast(6).uppercase()}", color = Muted, fontFamily = Font, fontSize = 11.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
    }
}

@Composable
private fun UrgentTop(seconds: Int) {
    val infinite = rememberInfiniteTransition(label = "urgentPulse")
    val pulse by infinite.animateFloat(
        initialValue = .88f,
        targetValue = 1.10f,
        animationSpec = infiniteRepeatable(animation = tween(650), repeatMode = RepeatMode.Reverse),
        label = "urgentScale"
    )
    Row(verticalAlignment = Alignment.CenterVertically) {
        Row(Modifier.shadow(8.dp, RoundedCornerShape(999.dp), clip = false).clip(RoundedCornerShape(999.dp)).background(RedSoft).border(1.dp, Red.copy(alpha = .25f), RoundedCornerShape(999.dp)).padding(horizontal = 12.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(10.dp).scale(pulse).clip(CircleShape).background(Red.copy(alpha = .75f)))
            Spacer(Modifier.width(7.dp))
            Icon(Icons.Filled.Bolt, null, tint = Red, modifier = Modifier.size(17.dp))
            Spacer(Modifier.width(6.dp))
            Text("URGENTE", color = Red, fontFamily = Font, fontSize = 12.sp, fontWeight = FontWeight.Black)
        }
        Spacer(Modifier.weight(1f))
        Box(Modifier.size(68.dp), contentAlignment = Alignment.Center) {
            Box(Modifier.size(66.dp).scale(pulse).alpha(.22f).clip(CircleShape).background(if (seconds <= 10) Red else Green))
            Box(Modifier.size(62.dp).clip(CircleShape).background(if (seconds <= 10) Red else Green).border(4.dp, Color.White, CircleShape), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(seconds.toString(), color = Color.White, fontFamily = Font, fontSize = 21.sp, fontWeight = FontWeight.Black)
                    Text("seg", color = Color.White.copy(alpha = .85f), fontFamily = Font, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun UrgentLogoMark() {
    Box(Modifier.size(56.dp).shadow(10.dp, RoundedCornerShape(19.dp), clip = false).clip(RoundedCornerShape(19.dp)).background(Color.White).border(1.dp, Green.copy(alpha = .18f), RoundedCornerShape(19.dp)), contentAlignment = Alignment.Center) {
        Image(painter = painterResource(R.drawable.ic_rodrigues_logo), contentDescription = "Logo", modifier = Modifier.size(46.dp), contentScale = ContentScale.Fit)
    }
}

@Composable
private fun UrgentMotionRail(seconds: Int) {
    val fraction = (seconds / 60f).coerceIn(.03f, 1f)
    val color = if (seconds <= 10) Red else Green
    Box(Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(999.dp)).background(color.copy(alpha = .14f))) {
        Box(Modifier.fillMaxWidth(fraction).height(8.dp).clip(RoundedCornerShape(999.dp)).background(Brush.horizontalGradient(listOf(color, Color.White.copy(alpha = .45f)))))
    }
}

@Composable
private fun RouteLine(icon: ImageVector, label: String, value: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(11.dp), modifier = Modifier.fillMaxWidth()) {
        Box(Modifier.size(38.dp).clip(CircleShape).background(color.copy(alpha = .12f)), contentAlignment = Alignment.Center) { Icon(icon, null, tint = color, modifier = Modifier.size(20.dp)) }
        Column(Modifier.weight(1f)) {
            Text(label, color = Muted, fontFamily = Font, fontSize = 10.sp, fontWeight = FontWeight.Black)
            Text(value.ifBlank { "Não informado" }, color = Ink, fontFamily = Font, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, maxLines = 2, overflow = TextOverflow.Ellipsis)
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
            Icon(if (machine) Icons.Filled.CreditCard else Icons.Filled.Payments, null, tint = if (machine) Orange else Green, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
            Text(if (paid) "Pago online" else raw, color = Ink, fontFamily = Font, fontSize = 15.sp, fontWeight = FontWeight.Black, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        Text(if (paid) "Nada a cobrar" else amount.ifBlank { "Confirmar pagamento com a operação" }, color = Muted, fontFamily = Font, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        if (machine) Text("Maquininha necessária", color = Orange, fontFamily = Font, fontSize = 12.sp, fontWeight = FontWeight.Black)
        if (changeFor.isNotBlank()) Text("Troco para $changeFor", color = Red, fontFamily = Font, fontSize = 12.sp, fontWeight = FontWeight.Black)
    }
}
