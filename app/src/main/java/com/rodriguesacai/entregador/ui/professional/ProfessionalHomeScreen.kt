package com.rodriguesacai.entregador.ui.professional

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun ProfessionalHomeScreen(
    state: ProfessionalHomeUiState,
    selectedTab: String = "Início",
    onOpenDetails: (CorridaUiModel) -> Unit = {},
    onAcceptRide: (CorridaUiModel) -> Unit = {},
    onRejectRide: (CorridaUiModel) -> Unit = {},
    onNavigate: (String) -> Unit = {}
) {
    Scaffold(
        containerColor = RodriguesColors.SurfaceSoft,
        bottomBar = {
            RodriguesBottomNavigation(
                selectedTab = selectedTab,
                onNavigate = onNavigate
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(RodriguesColors.Navy950)
                .padding(padding),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item { HeaderSection(state) }
            item { MainStatusCard(state, onOpenDetails) }
            item { PerformanceCard(state.resumo) }
            item { QuickCards(state) }
            item { WalletCard(state.resumo) }
            item {
                val corrida = state.corridaAtual
                if (corrida != null && corrida.status != CorridaUiStatus.SEM_CORRIDA) {
                    ActiveRideCard(
                        corrida = corrida,
                        onOpenDetails = { onOpenDetails(corrida) },
                        onAcceptRide = { onAcceptRide(corrida) },
                        onRejectRide = { onRejectRide(corrida) }
                    )
                } else {
                    AvailableCard()
                }
            }
            item { Spacer(Modifier.height(10.dp)) }
        }
    }
}

@Composable
private fun HeaderSection(state: ProfessionalHomeUiState) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    listOf(RodriguesColors.Navy900, RodriguesColors.Navy950)
                )
            )
            .padding(start = 20.dp, end = 20.dp, top = 26.dp, bottom = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(RodriguesColors.Lime100)
                        .border(2.dp, Color.White.copy(alpha = 0.62f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.inicialEntregador.take(1).ifBlank { "D" },
                        color = RodriguesColors.Lime500,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Black
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(14.dp)
                            .clip(CircleShape)
                            .background(if (state.online) RodriguesColors.Lime500 else RodriguesColors.TextSecondary)
                            .border(2.dp, RodriguesColors.Navy900, CircleShape)
                    )
                }

                Spacer(Modifier.width(12.dp))

                Column {
                    Text(
                        text = "Olá, ${state.nomeEntregador}",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = state.mensagemStatus,
                        color = Color.White.copy(alpha = 0.74f),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                HeaderAction(icon = "🔔", hasDot = true)
                HeaderAction(icon = "💬", hasDot = true)
            }
        }
    }
}

@Composable
private fun HeaderAction(icon: String, hasDot: Boolean) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.copy(alpha = 0.10f))
            .border(1.dp, Color.White.copy(alpha = 0.13f), RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(text = icon, fontSize = 21.sp)
        if (hasDot) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(9.dp)
                    .clip(CircleShape)
                    .background(RodriguesColors.Lime500)
            )
        }
    }
}

@Composable
private fun MainStatusCard(
    state: ProfessionalHomeUiState,
    onOpenDetails: (CorridaUiModel) -> Unit
) {
    val corrida = state.corridaAtual
    val title = corrida?.titulo ?: "Disponível para corridas"
    val subtitle = corrida?.subtitulo ?: "Você está online e pronto para receber pedidos."
    val badge = corrida?.let { "${it.quantidadeEntregas} entrega" } ?: "Online"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .shadow(12.dp, RoundedCornerShape(24.dp))
            .clickable(enabled = corrida != null) { corrida?.let(onOpenDetails) },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        listOf(RodriguesColors.Lime500, RodriguesColors.Lime400)
                    )
                )
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircleIcon(text = if (corrida?.status == CorridaUiStatus.OFERTA_RECEBIDA) "⚡" else "📦")
            Spacer(Modifier.width(14.dp))
            androidx.compose.foundation.layout.Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = RodriguesColors.TextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    color = RodriguesColors.TextPrimary.copy(alpha = 0.76f),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(Modifier.width(10.dp))
            Surface(
                color = Color.White.copy(alpha = 0.58f),
                shape = RoundedCornerShape(100.dp)
            ) {
                Text(
                    text = badge,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                    color = RodriguesColors.TextPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black
                )
            }
            Text(
                text = "›",
                color = RodriguesColors.TextPrimary,
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 6.dp)
            )
        }
    }
}

@Composable
private fun PerformanceCard(resumo: ResumoGanhosUiModel) {
    WhiteCard {
        Text(
            text = "Seu desempenho hoje",
            color = RodriguesColors.TextPrimary,
            fontSize = 15.sp,
            fontWeight = FontWeight.Black
        )
        Spacer(Modifier.height(18.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            StatColumn("💵", "Ganhos de hoje", money(resumo.ganhosHoje), Modifier.weight(1.25f))
            VerticalDivider()
            StatColumn("🔁", "Corridas", resumo.corridasHoje.toString(), Modifier.weight(1f))
            VerticalDivider()
            StatColumn("✅", "Finalizadas", resumo.finalizadasHoje.toString(), Modifier.weight(1f))
        }
    }
}

@Composable
private fun StatColumn(icon: String, label: String, value: String, modifier: Modifier = Modifier) {
    androidx.compose.foundation.layout.Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(RodriguesColors.Lime100),
            contentAlignment = Alignment.Center
        ) {
            Text(text = icon, fontSize = 18.sp)
        }
        Spacer(Modifier.height(10.dp))
        Text(
            text = label,
            color = RodriguesColors.TextSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = value,
            color = RodriguesColors.TextPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Black,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun VerticalDivider() {
    Box(
        modifier = Modifier
            .height(74.dp)
            .width(1.dp)
            .background(RodriguesColors.Border)
    )
}

@Composable
private fun QuickCards(state: ProfessionalHomeUiState) {
    val resumo = state.resumo
    val progress = if (resumo.metaDiaria <= 0.0) 0f else (resumo.ganhosHoje / resumo.metaDiaria).toFloat().coerceIn(0f, 1f)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SmallInfoCard(
            modifier = Modifier.weight(1f),
            icon = "⚡",
            title = if (state.demandaAlta) "Demanda alta" else "Demanda normal",
            subtitle = if (state.demandaAlta) "Mais corridas na sua região agora." else "Você será avisado quando aumentar."
        )
        Card(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = RodriguesColors.Surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircleIcon(text = "🎯", small = true, color = RodriguesColors.Blue100)
                Spacer(Modifier.width(12.dp))
                androidx.compose.foundation.layout.Column(modifier = Modifier.weight(1f)) {
                    Text("Meta diária", color = RodriguesColors.TextPrimary, fontWeight = FontWeight.Black, fontSize = 14.sp)
                    Text(money(resumo.metaDiaria), color = RodriguesColors.Blue500, fontWeight = FontWeight.Black, fontSize = 17.sp)
                    Spacer(Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(5.dp)
                            .clip(RoundedCornerShape(10.dp)),
                        color = RodriguesColors.Blue500,
                        trackColor = RodriguesColors.Blue100
                    )
                    Text("${(progress * 100).roundToInt()}% concluída", color = RodriguesColors.TextSecondary, fontSize = 11.sp)
                }
            }
        }
    }
}

@Composable
private fun SmallInfoCard(modifier: Modifier, icon: String, title: String, subtitle: String) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = RodriguesColors.Surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircleIcon(text = icon, small = true)
            Spacer(Modifier.width(12.dp))
            Column {
                Text(title, color = RodriguesColors.TextPrimary, fontWeight = FontWeight.Black, fontSize = 14.sp)
                Text(subtitle, color = RodriguesColors.TextSecondary, fontSize = 12.sp, lineHeight = 15.sp)
            }
        }
    }
}

@Composable
private fun WalletCard(resumo: ResumoGanhosUiModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = RodriguesColors.Navy850),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.White.copy(alpha = 0.10f), RoundedCornerShape(24.dp))
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircleIcon(text = "💳", color = Color.White.copy(alpha = 0.12f))
            Spacer(Modifier.width(14.dp))
            androidx.compose.foundation.layout.Column(modifier = Modifier.weight(1f)) {
                Text("Carteira", color = Color.White, fontWeight = FontWeight.Black, fontSize = 15.sp)
                Text(money(resumo.saldoCarteira), color = RodriguesColors.Lime400, fontWeight = FontWeight.Black, fontSize = 24.sp)
                Text("Saldo disponível via Pix", color = Color.White.copy(alpha = 0.68f), fontSize = 12.sp)
            }
            androidx.compose.foundation.layout.Column(horizontalAlignment = Alignment.End) {
                Text("Próximo repasse", color = Color.White.copy(alpha = 0.66f), fontSize = 12.sp)
                Text(resumo.proximoRepasse, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
            Text("›", color = Color.White, fontSize = 30.sp, modifier = Modifier.padding(start = 8.dp))
        }
    }
}

@Composable
private fun ActiveRideCard(
    corrida: CorridaUiModel,
    onOpenDetails: () -> Unit,
    onAcceptRide: () -> Unit,
    onRejectRide: () -> Unit
) {
    WhiteCard(padding = 0.dp) {
        androidx.compose.foundation.layout.Column(modifier = Modifier.padding(18.dp)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                androidx.compose.foundation.layout.Column(modifier = Modifier.weight(1f)) {
                    StopRow(
                        icon = "🏪",
                        tag = "COLETA",
                        title = corrida.lojaNome,
                        subtitle = corrida.lojaEndereco,
                        tagColor = RodriguesColors.Lime100,
                        tagTextColor = RodriguesColors.Success
                    )
                    Spacer(Modifier.height(16.dp))
                    StopRow(
                        icon = "📍",
                        tag = "ENTREGA 1 DE ${corrida.quantidadeEntregas}",
                        title = corrida.clienteNome,
                        subtitle = corrida.entregaEndereco,
                        tagColor = RodriguesColors.Blue100,
                        tagTextColor = RodriguesColors.Blue500
                    )
                }
                Spacer(Modifier.width(12.dp))
                androidx.compose.foundation.layout.Column(
                    modifier = Modifier
                        .width(128.dp)
                        .border(1.dp, RodriguesColors.Border, RoundedCornerShape(18.dp))
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    RideMetric("⏱", "Distância", corrida.distanciaKm?.let { oneDecimal(it) + " km" } ?: "A calcular")
                    RideMetric("🕒", "Tempo", corrida.tempoEstimadoMin?.let { "$it min" } ?: "A calcular")
                    RideMetric("💳", "Pagamento", corrida.pagamentoLabel)
                }
            }

            Spacer(Modifier.height(16.dp))

            if (corrida.status == CorridaUiStatus.OFERTA_RECEBIDA) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(
                        onClick = onRejectRide,
                        modifier = Modifier.weight(1f).height(52.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = RodriguesColors.SurfaceSoft, contentColor = RodriguesColors.Danger),
                        shape = RoundedCornerShape(18.dp)
                    ) { Text("Recusar", fontWeight = FontWeight.Black) }
                    Button(
                        onClick = onAcceptRide,
                        modifier = Modifier.weight(1.4f).height(52.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = RodriguesColors.Lime500, contentColor = RodriguesColors.TextPrimary),
                        shape = RoundedCornerShape(18.dp)
                    ) { Text("Aceitar ${money(corrida.valorCorrida)}", fontWeight = FontWeight.Black) }
                }
            } else {
                Button(
                    onClick = onOpenDetails,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = RodriguesColors.Lime500, contentColor = RodriguesColors.TextPrimary),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Text(
                        text = if (corrida.ocorrenciaAtiva) "Ver ocorrência" else "Ver detalhes da corrida  ›",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }
        }
    }
}

@Composable
private fun StopRow(icon: String, tag: String, title: String, subtitle: String, tagColor: Color, tagTextColor: Color) {
    Row(verticalAlignment = Alignment.Top) {
        CircleIcon(text = icon, small = true)
        Spacer(Modifier.width(12.dp))
        androidx.compose.foundation.layout.Column(modifier = Modifier.weight(1f)) {
            Surface(color = tagColor, shape = RoundedCornerShape(100.dp)) {
                Text(
                    text = tag,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    color = tagTextColor,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black
                )
            }
            Spacer(Modifier.height(6.dp))
            Text(
                title,
                color = RodriguesColors.TextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                subtitle,
                color = RodriguesColors.TextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun RideMetric(icon: String, label: String, value: String) {
    Row(verticalAlignment = Alignment.Top) {
        Text(text = icon, fontSize = 15.sp)
        Spacer(Modifier.width(7.dp))
        Column {
            Text(label, color = RodriguesColors.TextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            Text(value, color = RodriguesColors.TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Black, maxLines = 2)
        }
    }
}

@Composable
private fun AvailableCard() {
    WhiteCard {
        androidx.compose.foundation.layout.Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            CircleIcon(text = "🛵", color = RodriguesColors.Lime100)
            Spacer(Modifier.height(12.dp))
            Text("Você está disponível", color = RodriguesColors.TextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Black)
            Text(
                "Assim que uma corrida chegar, ela aparecerá aqui com valor, rota, tempo e pagamento.",
                color = RodriguesColors.TextSecondary,
                textAlign = TextAlign.Center,
                fontSize = 13.sp,
                lineHeight = 18.sp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
}

@Composable
private fun RodriguesBottomNavigation(selectedTab: String, onNavigate: (String) -> Unit) {
    val items = listOf(
        "Início" to "⌂",
        "Corridas" to "⇆",
        "Carteira" to "▣",
        "Histórico" to "◷",
        "Mais" to "•••"
    )

    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 10.dp
    ) {
        items.forEach { (label, icon) ->
            val selected = selectedTab == label
            NavigationBarItem(
                selected = selected,
                onClick = { onNavigate(label) },
                icon = {
                    Text(
                        text = icon,
                        color = if (selected) RodriguesColors.Navy900 else RodriguesColors.TextSecondary,
                        fontSize = if (selected) 25.sp else 22.sp,
                        fontWeight = FontWeight.Black
                    )
                },
                label = {
                    Text(
                        label,
                        color = if (selected) RodriguesColors.Lime500 else RodriguesColors.TextSecondary,
                        fontWeight = FontWeight.Black,
                        fontSize = 11.sp
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = RodriguesColors.Lime100,
                    selectedIconColor = RodriguesColors.Navy900,
                    selectedTextColor = RodriguesColors.Lime500,
                    unselectedIconColor = RodriguesColors.TextSecondary,
                    unselectedTextColor = RodriguesColors.TextSecondary
                )
            )
        }
    }
}

@Composable
private fun WhiteCard(
    padding: androidx.compose.ui.unit.Dp = 18.dp,
    content: @Composable Column.() -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = RodriguesColors.Surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        androidx.compose.foundation.layout.Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding),
            content = content
        )
    }
}

@Composable
private fun CircleIcon(text: String, small: Boolean = false, color: Color = RodriguesColors.Lime100) {
    Box(
        modifier = Modifier
            .size(if (small) 42.dp else 56.dp)
            .clip(CircleShape)
            .background(color),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, fontSize = if (small) 19.sp else 24.sp)
    }
}

private fun money(value: Double): String =
    String.format(Locale("pt", "BR"), "R$ %.2f", value).replace('.', ',')

private fun oneDecimal(value: Double): String =
    String.format(Locale("pt", "BR"), "%.1f", value).replace('.', ',')

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun ProfessionalHomeScreenPreview() {
    ProfessionalHomeScreen(state = ProfessionalHomeUiState.preview())
}
