package com.rodriguesacai.entregador.ui

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Route
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.TwoWheeler
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
import com.rodriguesacai.entregador.AppVersion
import com.rodriguesacai.entregador.R
import com.rodriguesacai.entregador.RodriguesFonts
import kotlinx.coroutines.delay

private val Font = RodriguesFonts.App
private val DarkBg = Color(0xFF08164A)
private val Purple = Color(0xFF2A1E8A)
private val Lime = Color(0xFFB7E51E)
private val Yellow = Color(0xFFE8E61A)
private val ElectricBlue = Color(0xFF1E4FFF)
private val Surface = Color(0xFFFFFFFF)
private val SurfaceSoft = Color(0xFFF1F5FF)
private val Ink = Color(0xFF08164A)
private val Muted = Color(0xFF596482)
private val Border = Color(0xFFDDE6FF)
private val Red = Color(0xFFE53935)

@Composable
fun UrgentRideScreen(
    rideId: String,
    value: String,
    distance: String,
    duration: String,
    pickup: String,
    dropoff: String,
    pickupDistance: String = "",
    deliveryDistance: String = "",
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
    var details by remember(rideId) { mutableStateOf(false) }
    LaunchedEffect(rideId) {
        seconds = 60
        while (seconds > 0) {
            delay(1000)
            seconds -= 1
        }
        onExpired()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(DarkBg, Purple, DarkBg)))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            UrgentHeader(seconds)
            OfferMainCard(
                value = value,
                distance = distance,
                duration = duration,
                pickupDistance = pickupDistance,
                deliveryDistance = deliveryDistance,
                pickup = pickup,
                dropoff = dropoff,
                details = details,
                onDetails = { details = !details }
            )
            AnimatedVisibility(visible = details) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    RealDeliveryMap(
                        title = "Detalhes da corrida",
                        subtitle = listOf(distance, duration).filter { it.isNotBlank() }.joinToString(" • ").ifBlank { "Você → loja → cliente" },
                        pickupAddress = pickup,
                        dropoffAddress = dropoff,
                        mode = DeliveryMapMode.PICKUP_TO_DROPOFF,
                        modifier = Modifier.height(294.dp)
                    )
                    UrgentPaymentLine(paymentMethod, paymentStatus, amountToCollect, changeFor, requiresMachine)
                    ReferenceLine(rideId)
                }
            }
            ActionButtons(onReject = onReject, onAccept = onAccept)
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun UrgentHeader(seconds: Int) {
    val infinite = rememberInfiniteTransition(label = "urgentPulse")
    val pulse by infinite.animateFloat(
        initialValue = .96f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(animation = tween(650), repeatMode = RepeatMode.Reverse),
        label = "pulse"
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(30.dp))
            .background(Color.White.copy(alpha = .10f))
            .border(1.dp, Color.White.copy(alpha = .14f), RoundedCornerShape(30.dp))
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(Modifier.size(54.dp).scale(pulse).clip(RoundedCornerShape(18.dp)).background(Lime), contentAlignment = Alignment.Center) {
            Image(painter = painterResource(R.drawable.up_app_icon), contentDescription = "UP Entregador", modifier = Modifier.size(44.dp), contentScale = ContentScale.Fit)
        }
        Column(Modifier.weight(1f)) {
            Text("NOVA CORRIDA", color = Color.White, fontFamily = Font, fontSize = 18.sp, lineHeight = 20.sp, fontWeight = FontWeight.Bold)
            Text("Tela cheia urgente", color = Color.White.copy(alpha = .76f), fontFamily = Font, fontSize = 12.sp, lineHeight = 15.sp, fontWeight = FontWeight.SemiBold)
            Text(AppVersion.LOGIN_LABEL, color = Color.White.copy(alpha = .60f), fontFamily = Font, fontSize = 9.sp, fontWeight = FontWeight.Bold)
        }
        Column(horizontalAlignment = Alignment.End) {
            Text("ACEITAR EM", color = Yellow, fontFamily = Font, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            Text("00:${seconds.coerceIn(0, 59).toString().padStart(2, '0')}", color = Color.White, fontFamily = Font, fontSize = 21.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun OfferMainCard(
    value: String,
    distance: String,
    duration: String,
    pickupDistance: String,
    deliveryDistance: String,
    pickup: String,
    dropoff: String,
    details: Boolean,
    onDetails: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(18.dp, RoundedCornerShape(34.dp), clip = false)
            .border(1.dp, Color.White.copy(alpha = .36f), RoundedCornerShape(34.dp)),
        shape = RoundedCornerShape(34.dp),
        colors = CardDefaults.cardColors(containerColor = Surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                Text("1 ENTREGA", color = Purple, fontFamily = Font, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.clip(RoundedCornerShape(999.dp)).background(SurfaceSoft).padding(horizontal = 12.dp, vertical = 6.dp))
            }
            Text("VALOR DA CORRIDA", color = Muted, fontFamily = Font, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            Text(value.ifBlank { "R$ 12,50" }, color = Ink, fontFamily = Font, fontSize = 42.sp, lineHeight = 44.sp, fontWeight = FontWeight.Bold)
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                OfferMetric("Até loja", pickupDistance.ifBlank { distance.ifBlank { "calcular" } }, Icons.Filled.Storefront, Lime, Modifier.weight(1f))
                OfferMetric("Loja → cliente", deliveryDistance.ifBlank { "ver mapa" }, Icons.Filled.Route, ElectricBlue, Modifier.weight(1f))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                OfferMetric("Total", distance.ifBlank { "Calculando" }, Icons.Filled.Route, Yellow, Modifier.weight(1f))
                OfferMetric("Tempo", duration.ifBlank { "Calculando" }, Icons.Filled.Schedule, Purple, Modifier.weight(1f))
            }
            RouteLine(Icons.Filled.Storefront, "Coleta", pickup.ifBlank { "Rodrigues Açaí e Cia." }, Lime)
            RouteLine(Icons.Filled.Place, "Entrega", dropoff.ifBlank { "Endereço protegido até aceitar" }, ElectricBlue)
            OutlinedButton(onClick = onDetails, modifier = Modifier.fillMaxWidth().height(50.dp), shape = RoundedCornerShape(18.dp)) {
                Icon(if (details) Icons.Filled.CheckCircle else Icons.Filled.Map, null, tint = ElectricBlue, modifier = Modifier.size(19.dp))
                Spacer(Modifier.width(8.dp))
                Text(if (details) "Ocultar detalhes" else "Ver detalhes no mapa", color = ElectricBlue, fontFamily = Font, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.width(4.dp))
                Icon(Icons.Filled.KeyboardArrowRight, null, tint = ElectricBlue, modifier = Modifier.size(18.dp))
            }
        }
    }
}

@Composable
private fun OfferMetric(label: String, value: String, icon: ImageVector, color: Color, modifier: Modifier) {
    Column(
        modifier
            .clip(RoundedCornerShape(22.dp))
            .background(color.copy(alpha = .13f))
            .border(1.dp, color.copy(alpha = .24f), RoundedCornerShape(22.dp))
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Icon(icon, null, tint = color, modifier = Modifier.size(17.dp))
            Text(label, color = Muted, fontFamily = Font, fontSize = 10.sp, fontWeight = FontWeight.Bold, maxLines = 1)
        }
        Text(value, color = Ink, fontFamily = Font, fontSize = 16.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
private fun RouteLine(icon: ImageVector, label: String, value: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(11.dp), modifier = Modifier.fillMaxWidth()) {
        Box(Modifier.size(40.dp).clip(CircleShape).background(color.copy(alpha = .14f)), contentAlignment = Alignment.Center) { Icon(icon, null, tint = color, modifier = Modifier.size(21.dp)) }
        Column(Modifier.weight(1f)) {
            Text(label, color = Muted, fontFamily = Font, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            Text(value.ifBlank { "Não informado" }, color = Ink, fontFamily = Font, fontSize = 14.sp, lineHeight = 18.sp, fontWeight = FontWeight.Bold, maxLines = 2, overflow = TextOverflow.Ellipsis)
        }
    }
}

@Composable
private fun UrgentPaymentLine(method: String, status: String, amount: String, changeFor: String, requiresMachine: String) {
    val raw = method.ifBlank { status }.ifBlank { "Pagamento não informado" }
    val machine = requiresMachine.equals("true", ignoreCase = true) || raw.contains("cart", ignoreCase = true) || raw.contains("maquin", ignoreCase = true)
    val paid = raw.contains("pago", ignoreCase = true) || raw.contains("online", ignoreCase = true)
    Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(24.dp)).background(Surface).border(1.dp, Border, RoundedCornerShape(24.dp)).padding(14.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(if (machine) Icons.Filled.CreditCard else Icons.Filled.Payments, null, tint = if (machine) Yellow else Lime, modifier = Modifier.size(21.dp))
            Spacer(Modifier.width(8.dp))
            Text(if (paid) "Pago online" else raw, color = Ink, fontFamily = Font, fontSize = 15.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        Text(if (paid) "Nada a cobrar do cliente" else amount.ifBlank { "Confirmar cobrança com a operação" }, color = Muted, fontFamily = Font, fontSize = 12.sp, lineHeight = 16.sp, fontWeight = FontWeight.Bold)
        if (machine) Text("Maquininha necessária", color = Ink, fontFamily = Font, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        if (changeFor.isNotBlank()) Text("Troco para $changeFor", color = Red, fontFamily = Font, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun ReferenceLine(rideId: String) {
    Text(
        "Ref. #${rideId.takeLast(6).uppercase()}",
        color = Color.White.copy(alpha = .68f),
        fontFamily = Font,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun ActionButtons(onReject: () -> Unit, onAccept: () -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
        OutlinedButton(
            onClick = onReject,
            modifier = Modifier.weight(1f).height(58.dp),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
        ) {
            Text("Recusar", color = Color.White, fontFamily = Font, fontWeight = FontWeight.Bold, fontSize = 15.sp)
        }
        Button(
            onClick = onAccept,
            modifier = Modifier.weight(1.35f).height(58.dp),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Lime, contentColor = Ink),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 0.dp)
        ) {
            Icon(Icons.Filled.TwoWheeler, null, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
            Text("Aceitar", fontFamily = Font, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}
