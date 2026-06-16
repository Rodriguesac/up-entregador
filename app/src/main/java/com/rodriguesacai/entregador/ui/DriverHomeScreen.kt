package com.rodriguesacai.entregador.ui

import android.content.Context
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.BatteryManager
import android.widget.Toast
import java.util.Calendar
import java.util.Locale
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Route
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.rodriguesacai.entregador.AppSettings
import com.rodriguesacai.entregador.AppVersion
import com.rodriguesacai.entregador.PermissionStatus
import com.rodriguesacai.entregador.PermissionStatusReader
import com.rodriguesacai.entregador.R
import com.rodriguesacai.entregador.RodriguesFonts
import com.rodriguesacai.entregador.data.AppCarouselBanner
import com.rodriguesacai.entregador.data.AppNotice
import com.rodriguesacai.entregador.data.AppRuntimeConfig
import com.rodriguesacai.entregador.data.DriverHistory
import com.rodriguesacai.entregador.data.DriverOperationalPreferences
import com.rodriguesacai.entregador.data.DriverPayout
import com.rodriguesacai.entregador.data.DriverProfile
import com.rodriguesacai.entregador.data.DriverRegistrationRequest
import com.rodriguesacai.entregador.data.DriverRepository
import com.rodriguesacai.entregador.data.DriverRide
import com.rodriguesacai.entregador.data.DriverStats
import com.rodriguesacai.entregador.data.PaymentMachine
import com.rodriguesacai.entregador.data.PaymentSettlementInput
import com.rodriguesacai.entregador.data.RouteOrder
import com.rodriguesacai.entregador.service.AppAlertPlayer
import com.rodriguesacai.entregador.service.NotificationHelper
import kotlinx.coroutines.delay

private enum class AppTab { Inicio, Corridas, Mapa, Ganhos, Historico, Conta, Notificacoes }
private enum class AvailabilityKind { Disponivel, Indisponivel, Restricao, EmEntrega }

private val AppFont = RodriguesFonts.App

private object ThemeRuntime {
    var dark by mutableStateOf(false)
}

private val Bg: Color get() = if (ThemeRuntime.dark) Color(0xFF08164A) else Color(0xFFF7F9FF)
private val Surface: Color get() = if (ThemeRuntime.dark) Color(0xFF101B55) else Color(0xFFFFFFFF)
private val SurfaceSoft: Color get() = if (ThemeRuntime.dark) Color(0xFF16246B) else Color(0xFFEFF4FF)
private val Border: Color get() = if (ThemeRuntime.dark) Color(0xFF2A3A82) else Color(0xFFDDE6FF)
private val Ink: Color get() = if (ThemeRuntime.dark) Color(0xFFFFFFFF) else Color(0xFF08164A)
private val Muted: Color get() = if (ThemeRuntime.dark) Color(0xFFC8D2FF) else Color(0xFF4B587C)
private val Muted2: Color get() = if (ThemeRuntime.dark) Color(0xFF9EAADE) else Color(0xFF647092)
private val Navy: Color get() = Color(0xFFB7E51E)
private val NavyDark: Color get() = if (ThemeRuntime.dark) Color(0xFF1E4FFF) else Color(0xFF2A1E8A)
private val NavySoft: Color get() = if (ThemeRuntime.dark) Color(0xFF25356F) else Color(0xFFEFF8B8)
private val Green: Color get() = Color(0xFFB7E51E)
private val GreenDark: Color get() = if (ThemeRuntime.dark) Color(0xFFE8E61A) else Color(0xFF7FA40B)
private val GreenSoft: Color get() = if (ThemeRuntime.dark) Color(0xFF203C40) else Color(0xFFF3FFD0)
private val Orange: Color get() = Color(0xFFE8E61A)
private val OrangeSoft: Color get() = if (ThemeRuntime.dark) Color(0xFF3D3B16) else Color(0xFFFFFCE0)
private val Red: Color get() = Color(0xFFE53935)
private val RedSoft: Color get() = if (ThemeRuntime.dark) Color(0xFF3A1F2B) else Color(0xFFFFEBEE)
private val Blue: Color get() = Color(0xFF1E4FFF)
private val BlueSoft: Color get() = if (ThemeRuntime.dark) Color(0xFF182B78) else Color(0xFFEAF0FF)
private val CardShape = RoundedCornerShape(26.dp)
private val ButtonShape = RoundedCornerShape(18.dp)

private data class OperationalStatus(
    val kind: AvailabilityKind,
    val label: String,
    val message: String,
    val buttonColor: Color,
    val textColor: Color,
    val canGoOnline: Boolean
)

private data class PaymentUi(
    val label: String,
    val detail: String,
    val color: Color,
    val amountLabel: String,
    val requiresMachine: Boolean,
    val requiresChange: Boolean
)

private data class RouteAction(
    val title: String,
    val message: String,
    val button: String,
    val enabled: Boolean,
    val statusToSend: String,
    val accent: Color,
    val needsPaymentConfirmation: Boolean = false
)

@Composable
fun DriverHomeScreen(
    permissionRefreshTick: Int = 0,
    onGoOnline: () -> Unit,
    onGoOffline: () -> Unit,
    onOpenNavigator: (pickup: String, dropoff: String) -> Unit,
    onOpenNotificationSettings: () -> Unit,
    onOpenLocationSettings: () -> Unit,
    onOpenFullScreenSettings: () -> Unit,
    onOpenBatterySettings: () -> Unit,
    onRequestNotificationPermission: () -> Unit,
    onRequestLocationPermission: () -> Unit,
    onRequestEssentialPermissions: () -> Unit,
    onThemeModeChanged: (String) -> Unit = {}
) {
    val context = LocalContext.current
    var profile by remember { mutableStateOf(DriverRepository.currentSession(context)) }
    var tab by remember { mutableStateOf(AppTab.Inicio) }
    var online by remember { mutableStateOf(false) }
    var pendingRide by remember { mutableStateOf<DriverRide?>(null) }
    var activeRide by remember { mutableStateOf<DriverRide?>(null) }
    var history by remember { mutableStateOf<List<DriverHistory>>(emptyList()) }
    var stats by remember { mutableStateOf(DriverStats()) }
    var appBanners by remember { mutableStateOf<List<AppCarouselBanner>>(emptyList()) }
    var appNotices by remember { mutableStateOf<List<AppNotice>>(emptyList()) }
    var paymentMachines by remember { mutableStateOf<List<PaymentMachine>>(emptyList()) }
    var appRuntimeConfig by remember { mutableStateOf(AppRuntimeConfig()) }
    var error by remember { mutableStateOf("") }
    var notice by remember { mutableStateOf("") }
    var hideValues by remember { mutableStateOf(AppSettings.getHideValues(context)) }
    var themeMode by remember { mutableStateOf(AppSettings.getThemeMode(context)) }
    var welcomeDone by remember { mutableStateOf(AppSettings.isWelcomeDone(context)) }
    ThemeRuntime.dark = themeMode == AppSettings.THEME_DARK
    var bootingSession by remember { mutableStateOf(profile != null) }
    var lastActiveId by remember { mutableStateOf("") }

    DisposableEffect(profile?.id, online) {
        val pendingListener = if (profile != null && online) {
            DriverRepository.listenPendingRide(context, onRide = { ride ->
                pendingRide = if (activeRide != null && ride != null && !ride.isRouteAddition) null else ride
            }, onError = { error = it })
        } else null
        val activeListener = if (profile != null) {
            DriverRepository.listenMyActiveRide(context, onRide = { ride ->
                if (ride == null && lastActiveId.isNotBlank()) notice = "Corrida encerrada pela operação. Você está livre para nova corrida."
                activeRide = ride
                if (ride != null) {
                    lastActiveId = ride.id
                    if (pendingRide != null && !pendingRide!!.isRouteAddition) pendingRide = null
                }
            }, onError = { error = it })
        } else null
        val historyListener = if (profile != null) DriverRepository.listenMyHistory(context, { history = it }, { error = it }) else null
        val statsListener = if (profile != null) DriverRepository.listenDailyStats(context, { stats = it }, { error = it }) else null
        val carouselListener = if (profile != null) DriverRepository.listenAppCarousel({ appBanners = it }, {}) else null
        val noticeListener = if (profile != null) DriverRepository.listenAppNotifications(context, { appNotices = it }, {}) else null
        val profileListener = if (profile != null) DriverRepository.listenDriverProfile(context, { profile = it }, {}) else null
        val machinesListener = if (profile != null) DriverRepository.listenMachineOptions({ paymentMachines = it }, {}) else null
        val appConfigListener = if (profile != null) DriverRepository.listenAppRuntimeConfig({ appRuntimeConfig = it }, {}) else null
        onDispose {
            pendingListener?.remove(); activeListener?.remove(); historyListener?.remove(); statsListener?.remove()
            carouselListener?.remove(); noticeListener?.remove(); profileListener?.remove(); machinesListener?.remove(); appConfigListener?.remove()
        }
    }

    LaunchedEffect(profile?.id) {
        if (profile != null) {
            bootingSession = true
            delay(650)
            bootingSession = false
        }
    }

    LaunchedEffect(pendingRide?.id, online, activeRide?.id) {
        val ride = pendingRide
        if (online && ride != null && activeRide == null) {
            NotificationHelper.urgentRideNotification(
                context = context,
                rideId = ride.id,
                value = ride.value,
                distance = ride.distance,
                duration = ride.duration,
                pickup = ride.pickup,
                dropoff = ride.dropoff,
                pickupDistance = ride.pickupDistance,
                deliveryDistance = ride.deliveryDistance,
                paymentMethod = ride.paymentMethod,
                paymentStatus = ride.paymentStatus,
                amountToCollect = DriverRepository.formatCurrency(ride.amountToCollectNumber).takeIf { ride.amountToCollectNumber > 0.0 } ?: "",
                changeFor = DriverRepository.formatCurrency(ride.changeForNumber).takeIf { ride.changeForNumber > 0.0 } ?: "",
                requiresMachine = ride.requiresMachine.toString()
            )
        }
    }

    var lastSystemNoticeId by remember { mutableStateOf("") }
    LaunchedEffect(appNotices.map { it.id to it.read }) {
        val visible = appNotices.firstOrNull { it.isVisible() && !it.read }
        if (visible != null && visible.id != lastSystemNoticeId) {
            lastSystemNoticeId = visible.id
            NotificationHelper.appNoticeNotification(context, visible.id, visible.title, visible.message, visible.category)
        }
    }

    LaunchedEffect(profile?.id, welcomeDone) {
        if (profile != null && !welcomeDone) {
            AppSettings.setWelcomeDone(context, true)
            welcomeDone = true
        }
    }

    if (profile == null && !welcomeDone) {
        WelcomeAnimatedScreen(
            onEnter = {
                AppSettings.setWelcomeDone(context, true)
                welcomeDone = true
            },
            onCreateAccount = {
                AppSettings.setWelcomeDone(context, true)
                welcomeDone = true
            }
        )
        return
    }

    if (profile == null) {
        LoginScreen(
            error = error,
            notice = notice,
            onLogin = { value, password, setLoading ->
                error = ""; notice = ""; setLoading(true)
                DriverRepository.login(
                    context = context,
                    documentOrPhone = value,
                    password = password,
                    onSuccess = { profile = it; setLoading(false) },
                    onError = { error = it; setLoading(false) }
                )
            },
            onRegister = { request, setLoading ->
                error = ""; notice = ""; setLoading(true)
                DriverRepository.registerDriver(
                    request = request,
                    onSuccess = { notice = "Cadastro enviado. Aguarde aprovação do gestor."; setLoading(false) },
                    onError = { error = it; setLoading(false) }
                )
            }
        )
        return
    }

    if (profile?.needsPasswordSetup == true) {
        CreatePasswordScreen(profile = profile!!, onSaved = { profile = profile!!.copy(needsPasswordSetup = false) }, onLogout = { DriverRepository.logout(context) { profile = null } })
        return
    }

    if (bootingSession) {
        LoadingSessionSplash(profile = profile!!)
        return
    }

    if (appRuntimeConfig.maintenanceActive || appRuntimeConfig.forceUpdate) {
        SystemStateScreen(
            title = if (appRuntimeConfig.forceUpdate) "Atualização necessária" else "App em manutenção",
            message = if (appRuntimeConfig.forceUpdate) "Atualize o app para continuar operando com segurança." else appRuntimeConfig.maintenanceMessage.ifBlank { "A operação pausou o app temporariamente." },
            icon = if (appRuntimeConfig.forceUpdate) Icons.Filled.Bolt else Icons.Filled.Shield,
            color = if (appRuntimeConfig.forceUpdate) Orange else Blue
        )
        return
    }

    Scaffold(
        containerColor = Bg,
        bottomBar = {
            PremiumBottomBar(tab = tab, onTab = { tab = it })
        }
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding).background(Bg)) {
            Column(Modifier.fillMaxSize()) {
                if (notice.isNotBlank()) InlineAppMessage(text = notice, color = Green, onClose = { notice = "" })
                if (error.isNotBlank()) InlineAppMessage(text = error, color = Red, onClose = { error = "" })
                when (tab) {
                    AppTab.Inicio -> HomeContent(
                        profile = profile!!,
                        online = online,
                        pendingRide = pendingRide,
                        activeRide = activeRide,
                        stats = stats,
                        appBanners = appBanners,
                        appNotices = appNotices,
                        hideValues = hideValues,
                        onToggleValues = { hideValues = !hideValues; AppSettings.setHideValues(context, hideValues) },
                        onToggleOnline = { checked ->
                            AppAlertPlayer.playTap(context)
                            online = checked
                            DriverRepository.setOnline(context, checked)
                            if (checked) onGoOnline() else { pendingRide = null; onGoOffline() }
                        },
                        onAccept = { ride -> AppAlertPlayer.playSuccess(context); DriverRepository.acceptRide(context, ride.id, onDone = { pendingRide = null; tab = AppTab.Corridas }, onError = { error = it }) },
                        onReject = { ride, reason -> AppAlertPlayer.playTap(context); DriverRepository.rejectRide(context, ride.id, reason, onDone = { pendingRide = null }, onError = { error = it }) },
                        onExpire = { ride -> DriverRepository.expireRide(context, ride.id, onDone = { pendingRide = null }, onError = { error = it }) },
                        onOpenRides = { tab = AppTab.Corridas },
                        onOpenHistory = { tab = AppTab.Historico },
                        onOpenWallet = { tab = AppTab.Ganhos },
                        onOpenMap = { tab = AppTab.Mapa },
                        onOpenNotifications = { tab = AppTab.Notificacoes },
                        onOpenSupport = { tab = AppTab.Conta },
                        onFixPermissions = onRequestEssentialPermissions
                    )
                    AppTab.Corridas -> RidesContent(
                        pendingRide = pendingRide,
                        activeRide = activeRide,
                        paymentMachines = paymentMachines,
                        online = online,
                        onAccept = { ride -> AppAlertPlayer.playSuccess(context); DriverRepository.acceptRide(context, ride.id, onDone = { pendingRide = null }, onError = { error = it }) },
                        onReject = { ride, reason -> AppAlertPlayer.playTap(context); DriverRepository.rejectRide(context, ride.id, reason, onDone = { pendingRide = null }, onError = { error = it }) },
                        onExpire = { ride -> DriverRepository.expireRide(context, ride.id, onDone = { pendingRide = null }, onError = { error = it }) },
                        onUpdateRide = { ride, status -> DriverRepository.updateRideStatus(context, ride.id, status, onDone = { AppAlertPlayer.playSuccess(context); error = ""; notice = "Corrida atualizada." }, onError = { error = it }) },
                        onOpenNavigator = onOpenNavigator,
                        onOccurrence = { ride, reason, details ->
                            DriverRepository.reportRideOccurrence(context, ride.id, reason, details, onDone = { AppAlertPlayer.playNotice(context); notice = "Ocorrência enviada. Aguarde o gestor." }, onError = { error = it })
                        },
                        onSettleAndFinish = { ride, input ->
                            DriverRepository.savePaymentSettlementForRide(context, ride.id, input, onDone = {
                                DriverRepository.updateRideStatus(context, ride.id, "FINALIZADA", onDone = { AppAlertPlayer.playSuccess(context); error = ""; notice = "Entrega finalizada." }, onError = { error = it })
                            }, onError = { error = it })
                        }
                    )
                    AppTab.Mapa -> RouteMapContent(
                        activeRide = activeRide,
                        online = online,
                        onBackHome = { tab = AppTab.Inicio },
                        onOpenNavigator = onOpenNavigator
                    )
                    AppTab.Ganhos -> WalletContent(profile = profile!!, stats = stats, history = history, hideValues = hideValues, onToggleValues = { hideValues = !hideValues; AppSettings.setHideValues(context, hideValues) })
                    AppTab.Historico -> HistoryContent(history = history)
                    AppTab.Notificacoes -> NotificationsScreen(notices = appNotices, onBack = { tab = AppTab.Inicio })
                    AppTab.Conta -> MoreContent(
                        profile = profile!!,
                        online = online,
                        themeMode = themeMode,
                        hideValues = hideValues,
                        notices = appNotices,
                        onThemeChanged = { themeMode = it; onThemeModeChanged(it) },
                        onToggleValues = { hideValues = !hideValues; AppSettings.setHideValues(context, hideValues) },
                        onProfileChanged = { profile = DriverRepository.currentSession(context) ?: profile },
                        onOpenNotificationSettings = onOpenNotificationSettings,
                        onOpenLocationSettings = onOpenLocationSettings,
                        onOpenFullScreenSettings = onOpenFullScreenSettings,
                        onOpenBatterySettings = onOpenBatterySettings,
                        onRequestNotificationPermission = onRequestNotificationPermission,
                        onRequestLocationPermission = onRequestLocationPermission,
                        onRequestEssentialPermissions = onRequestEssentialPermissions,
                        permissionRefreshTick = permissionRefreshTick,
                        onForceUnlock = {
                            DriverRepository.forceClearActiveMission(context, onDone = { notice = "Operação destravada."; activeRide = null; tab = AppTab.Inicio }, onError = { error = it })
                        },
                        onLogout = {
                            onGoOffline()
                            DriverRepository.logout(context) { profile = null; online = false; pendingRide = null; activeRide = null; tab = AppTab.Inicio }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun PremiumBottomBar(tab: AppTab, onTab: (AppTab) -> Unit) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 0.dp,
        modifier = Modifier.border(1.dp, Border)
    ) {
        BottomItem(AppTab.Inicio, tab, "Início", Icons.Filled.Home, onTab)
        BottomItem(AppTab.Corridas, tab, "Corridas", Icons.Filled.Route, onTab)
        BottomItem(AppTab.Ganhos, tab, "Carteira", Icons.Filled.AccountBalanceWallet, onTab)
        BottomItem(AppTab.Historico, tab, "Histórico", Icons.Filled.History, onTab)
        BottomItem(AppTab.Conta, tab, "Mais", Icons.Filled.MoreHoriz, onTab)
    }
}

@Composable
private fun RowScope.BottomItem(item: AppTab, selected: AppTab, label: String, icon: ImageVector, onTab: (AppTab) -> Unit) {
    val isSelected = item == selected
    val scale by animateFloatAsState(targetValue = if (isSelected) 1.15f else .96f, animationSpec = tween(220), label = "bottomScale")
    NavigationBarItem(
        selected = isSelected,
        onClick = { onTab(item) },
        icon = {
            Box(Modifier.size(30.dp), contentAlignment = Alignment.Center) {
                Icon(icon, label, modifier = Modifier.size(23.dp).scale(scale))
                if (isSelected) Box(Modifier.align(Alignment.BottomCenter).size(5.dp).clip(CircleShape).background(Navy))
            }
        },
        label = { Text(label, fontSize = 10.sp, fontFamily = AppFont, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold) },
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = Navy,
            selectedTextColor = Navy,
            indicatorColor = NavySoft,
            unselectedIconColor = Muted2,
            unselectedTextColor = Muted2
        )
    )
}

@Composable
private fun ScreenScroll(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Bg)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        content = content
    )
}

@Composable
private fun PremiumCard(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(5.dp, CardShape, clip = false, ambientColor = Color(0x160F2742), spotColor = Color(0x12000000))
            .border(1.dp, Border, CardShape),
        colors = CardDefaults.cardColors(containerColor = Surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        shape = CardShape
    ) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp), content = content)
    }
}

@Composable
private fun MiniCard(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = modifier.shadow(2.dp, RoundedCornerShape(18.dp), clip = false).clip(RoundedCornerShape(18.dp)).background(SurfaceSoft).border(1.dp, Border, RoundedCornerShape(18.dp)).padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        content = content
    )
}

@Composable
private fun PrimaryButton(text: String, icon: ImageVector? = null, enabled: Boolean = true, color: Color = Navy, modifier: Modifier = Modifier, onClick: () -> Unit) {
    val contentColor = if (color == Navy || color == Green || color == Orange) Ink else Color.White
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = ButtonShape,
        colors = ButtonDefaults.buttonColors(containerColor = color, contentColor = contentColor, disabledContainerColor = Color(0xFFE6EBEF), disabledContentColor = Muted2),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp, pressedElevation = 0.dp),
        modifier = modifier.fillMaxWidth().height(50.dp)
    ) {
        if (icon != null) { Icon(icon, null, modifier = Modifier.size(20.dp)); Spacer(Modifier.width(8.dp)) }
        Text(text, fontFamily = AppFont, fontSize = 15.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun SecondaryButton(text: String, icon: ImageVector? = null, color: Color = Ink, modifier: Modifier = Modifier, onClick: () -> Unit) {
    OutlinedButton(onClick = onClick, modifier = modifier.fillMaxWidth().height(50.dp), shape = ButtonShape, colors = ButtonDefaults.outlinedButtonColors(contentColor = color)) {
        if (icon != null) { Icon(icon, null, modifier = Modifier.size(19.dp)); Spacer(Modifier.width(8.dp)) }
        Text(text, fontFamily = AppFont, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun StatusChip(text: String, color: Color, icon: ImageVector? = null) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clip(RoundedCornerShape(999.dp)).background(color.copy(alpha = .12f)).border(1.dp, color.copy(alpha = .25f), RoundedCornerShape(999.dp)).padding(horizontal = 11.dp, vertical = 7.dp)
    ) {
        if (icon != null) { Icon(icon, null, tint = color, modifier = Modifier.size(15.dp)); Spacer(Modifier.width(5.dp)) }
        Text(text, color = color, fontFamily = AppFont, fontSize = 11.sp, fontWeight = FontWeight.Bold, maxLines = 1)
    }
}

@Composable
private fun Field(label: String, value: String, onChange: (String) -> Unit, placeholder: String = "", password: Boolean = false, keyboardType: KeyboardType = KeyboardType.Text, lines: Int = 1) {
    Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
        Text(label, color = Muted, fontFamily = AppFont, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        OutlinedTextField(
            value = value,
            onValueChange = onChange,
            placeholder = { Text(placeholder, color = Muted2, fontSize = 15.sp, fontFamily = AppFont, fontWeight = FontWeight.Medium) },
            visualTransformation = if (password) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            textStyle = TextStyle(fontFamily = AppFont, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Ink),
            maxLines = lines,
            minLines = lines,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp)
        )
    }
}

@Composable
private fun InlineAppMessage(text: String, color: Color, onClose: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().background(color.copy(alpha = .10f)).padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(if (color == Red) Icons.Filled.ErrorOutline else Icons.Filled.CheckCircle, null, tint = color, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(8.dp))
        Text(text, color = Ink, fontFamily = AppFont, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
        Text("OK", color = color, fontFamily = AppFont, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.clickable { onClose() })
    }
}

@Composable
private fun WelcomeAnimatedScreen(onEnter: () -> Unit, onCreateAccount: () -> Unit) {
    var phase by remember { mutableStateOf(0) }
    LaunchedEffect(Unit) {
        phase = 1
        delay(260)
        phase = 2
        delay(260)
        phase = 3
        delay(260)
        phase = 4
    }
    val uScale by animateFloatAsState(targetValue = if (phase >= 1) 1f else .72f, animationSpec = tween(420), label = "welcomeU")
    val pScale by animateFloatAsState(targetValue = if (phase >= 2) 1f else .72f, animationSpec = tween(420), label = "welcomeP")
    val arrowOffset by animateFloatAsState(targetValue = if (phase >= 3) 0f else 22f, animationSpec = tween(480), label = "welcomeArrow")
    val contentAlpha by animateFloatAsState(targetValue = if (phase >= 4) 1f else 0f, animationSpec = tween(520), label = "welcomeContent")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF08164A), Color(0xFF111F66), Color(0xFF2A1E8A))))
            .padding(22.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(Modifier.size(172.dp), contentAlignment = Alignment.Center) {
                Box(Modifier.size(150.dp).clip(RoundedCornerShape(42.dp)).background(Color.White.copy(alpha = .08f)).border(1.dp, Color.White.copy(alpha = .12f), RoundedCornerShape(42.dp)))
                Text("U", color = Green, fontFamily = AppFont, fontSize = 92.sp, lineHeight = 96.sp, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.CenterStart).padding(start = 30.dp).scale(uScale))
                Text("P", color = Color.White, fontFamily = AppFont, fontSize = 92.sp, lineHeight = 96.sp, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.CenterEnd).padding(end = 24.dp).scale(pScale))
                Icon(Icons.Filled.KeyboardArrowRight, null, tint = Orange, modifier = Modifier.align(Alignment.Center).size(44.dp).scale(if (phase >= 3) 1.1f else .7f).padding(top = arrowOffset.dp))
                MotionRail(Green)
            }
            Spacer(Modifier.height(18.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.alpha(contentAlpha)) {
                Text("UP Entregador", color = Color.White, fontFamily = AppFont, fontSize = 29.sp, lineHeight = 32.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                AppVersionBadge(onDark = true)
                Text("Corridas, entregas e ganhos em uma operação profissional.", color = Color.White.copy(alpha = .78f), fontFamily = AppFont, fontSize = 14.sp, lineHeight = 20.sp, textAlign = TextAlign.Center, fontWeight = FontWeight.Medium)
                Spacer(Modifier.height(12.dp))
                PrimaryButton("Entrar", icon = Icons.Filled.CheckCircle, color = Green, onClick = onEnter)
                SecondaryButton("Criar cadastro", icon = Icons.Filled.TwoWheeler, color = Color.White, onClick = onCreateAccount)
                Text("Splash nativo + boas-vindas animada", color = Color.White.copy(alpha = .58f), fontFamily = AppFont, fontSize = 11.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            }
        }
    }
}


@Composable
private fun PermissionsOnboardingScreen(
    permissionRefreshTick: Int,
    onRequestNotificationPermission: () -> Unit,
    onRequestLocationPermission: () -> Unit,
    onRequestEssentialPermissions: () -> Unit,
    onOpenFullScreenSettings: () -> Unit,
    onOpenBatterySettings: () -> Unit,
    onContinue: () -> Unit
) {
    val context = LocalContext.current
    var permissionStatus by remember { mutableStateOf(PermissionStatusReader.read(context)) }
    LaunchedEffect(permissionRefreshTick) { permissionStatus = PermissionStatusReader.read(context) }

    ScreenScroll {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            AppVersionBadge(onDark = false)
            Text("Permissões do app", color = Ink, fontFamily = AppFont, fontSize = 26.sp, lineHeight = 29.sp, fontWeight = FontWeight.Bold)
            Text(
                "Precisamos de algumas permissões para você receber corridas com segurança e eficiência.",
                color = Muted,
                fontFamily = AppFont,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Medium
            )
        }
        PermissionProgressHero(permissionStatus, context, onRequestEssentialPermissions)
        PermissionSetupCard("Notificações", "Receba alertas de novas corridas e atualizações.", permissionStatus.notifications, Icons.Filled.NotificationsActive, onClick = onRequestNotificationPermission)
        PermissionSetupCard("Localização", "Mapa, distância e acompanhamento da entrega.", permissionStatus.location, Icons.Filled.MyLocation, onClick = onRequestLocationPermission)
        PermissionSetupCard("Alerta urgente", "Tela cheia para oferta importante.", permissionStatus.fullScreenIntent, Icons.Filled.Bolt, onClick = onOpenFullScreenSettings)
        PermissionSetupCard("Bateria", "Evita o app morrer em segundo plano.", permissionStatus.batteryUnrestricted, Icons.Filled.Shield, onClick = onOpenBatterySettings)
        PermissionSetupCard("Internet/GPS", "Conexão e localização do aparelho.", hasInternet(context) && isGpsEnabled(context), Icons.Filled.Map, onClick = onRequestEssentialPermissions)
        Spacer(Modifier.height(10.dp))
        PrimaryButton("Liberar permissões essenciais", icon = Icons.Filled.CheckCircle, color = Navy, onClick = onRequestEssentialPermissions)
        SecondaryButton("Continuar mesmo assim", color = Muted, onClick = onContinue)
    }
}



@Composable
private fun PermissionProgressHero(status: PermissionStatus, context: Context, onFix: () -> Unit) {
    val missing = permissionMissingLabels(status, context)
    val released = 5 - missing.size.coerceIn(0, 5)
    val ready = missing.isEmpty()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(5.dp, RoundedCornerShape(22.dp), clip = false, ambientColor = Navy.copy(alpha = .10f), spotColor = Color(0x11000000))
            .clip(RoundedCornerShape(22.dp))
            .background(Color.White)
            .border(1.dp, Border, RoundedCornerShape(22.dp))
            .clickable { onFix() }
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(Modifier.size(52.dp).clip(RoundedCornerShape(16.dp)).background(if (ready) GreenSoft else NavySoft), contentAlignment = Alignment.Center) {
                Icon(if (ready) Icons.Filled.CheckCircle else Icons.Filled.Shield, null, tint = if (ready) Green else Navy, modifier = Modifier.size(29.dp))
            }
            Column(Modifier.weight(1f)) {
                Text("Permissões essenciais", color = Ink, fontFamily = AppFont, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text("$released de 5 liberadas", color = if (ready) Green else Muted, fontFamily = AppFont, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
            Icon(Icons.Filled.KeyboardArrowRight, null, tint = Muted2, modifier = Modifier.size(22.dp))
        }
        Box(Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(999.dp)).background(SurfaceSoft)) {
            Box(Modifier.fillMaxWidth((released / 5f).coerceIn(.04f, 1f)).height(8.dp).clip(RoundedCornerShape(999.dp)).background(if (ready) Green else Navy))
        }
        if (!ready) Text("Falta: ${missing.joinToString(" • ")}", color = Muted, fontFamily = AppFont, fontSize = 12.sp, lineHeight = 16.sp, fontWeight = FontWeight.SemiBold)
    }
}



@Composable
private fun PermissionSetupCard(title: String, message: String, ok: Boolean, icon: ImageVector, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(76.dp)
            .shadow(2.dp, RoundedCornerShape(18.dp), clip = false, ambientColor = Color(0x10000000), spotColor = Color(0x0C000000))
            .clip(RoundedCornerShape(18.dp))
            .background(Surface)
            .border(1.dp, if (ok) Green.copy(alpha = .18f) else Border, RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .padding(horizontal = 13.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(Modifier.size(46.dp).clip(RoundedCornerShape(14.dp)).background(if (ok) GreenSoft else NavySoft), contentAlignment = Alignment.Center) {
            Icon(icon, null, tint = if (ok) Green else Navy, modifier = Modifier.size(24.dp))
        }
        Column(Modifier.weight(1f)) {
            Text(title, color = Ink, fontFamily = AppFont, fontSize = 15.sp, lineHeight = 17.sp, fontWeight = FontWeight.Bold, maxLines = 1)
            Text(message, color = Muted, fontFamily = AppFont, fontSize = 11.sp, lineHeight = 14.sp, fontWeight = FontWeight.Medium, maxLines = 2, overflow = TextOverflow.Ellipsis)
        }
        if (ok) {
            StatusChip("OK", Green)
        } else {
            Icon(Icons.Filled.KeyboardArrowRight, null, tint = Muted2, modifier = Modifier.size(21.dp))
        }
    }
}



@Composable
private fun BrandHeader(title: String, subtitle: String, icon: ImageVector) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            RodriguesLogoMark(size = 50, pulse = true)
            Column {
                Text("UP Entregador", color = Navy, fontFamily = AppFont, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Text(title, color = Ink, fontFamily = AppFont, fontSize = 22.sp, lineHeight = 25.sp, fontWeight = FontWeight.Bold)
            }
        }
        Text(subtitle, color = Muted, fontFamily = AppFont, fontSize = 13.sp, lineHeight = 18.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun AppVersionBadge(onDark: Boolean = false) {
    val bg = if (onDark) Color.White.copy(alpha = .12f) else NavySoft
    val fg = if (onDark) Color.White else Ink
    Text(
        AppVersion.LOGIN_LABEL,
        color = fg,
        fontFamily = AppFont,
        fontSize = 11.sp,
        lineHeight = 13.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(bg)
            .border(1.dp, if (onDark) Color.White.copy(alpha = .20f) else Border, RoundedCornerShape(999.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    )
}

@Composable
private fun LoginHeroHeader(mode: String) {
    if (mode == "login") {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 34.dp, bottomEnd = 34.dp))
                .background(Brush.linearGradient(listOf(Color(0xFF08164A), Color(0xFF2A1E8A), Color(0xFF1E4FFF))))
                .padding(horizontal = 20.dp, vertical = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.up_app_icon),
                contentDescription = "UP Entregador",
                modifier = Modifier.size(118.dp),
                contentScale = ContentScale.Fit
            )
            Text("UP Entregador", color = Color.White, fontFamily = AppFont, fontSize = 17.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            AppVersionBadge(onDark = true)
        }
        Column(Modifier.fillMaxWidth().padding(top = 16.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("Bem-vindo(a)!", color = Ink, fontFamily = AppFont, fontSize = 25.sp, lineHeight = 28.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            Text("Acesse sua conta para continuar fazendo entregas.", color = Muted, fontFamily = AppFont, fontSize = 14.sp, lineHeight = 20.sp, fontWeight = FontWeight.Medium, textAlign = TextAlign.Center)
        }
    } else {
        BrandHeader(title = "Solicitar cadastro", subtitle = "Envie seus dados para análise da operação.", icon = Icons.Filled.TwoWheeler)
    }
}

@Composable
private fun LoginScreen(
    error: String,
    notice: String,
    onLogin: (String, String, (Boolean) -> Unit) -> Unit,
    onRegister: (DriverRegistrationRequest, (Boolean) -> Unit) -> Unit
) {
    var mode by remember { mutableStateOf("login") }
    var loading by remember { mutableStateOf(false) }
    var doc by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var cpf by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var vehicle by remember { mutableStateOf("Moto") }
    var plate by remember { mutableStateOf("") }
    var pix by remember { mutableStateOf("") }
    var bank by remember { mutableStateOf("") }

    if (mode == "login") {
        Column(Modifier.fillMaxSize().background(Bg).verticalScroll(rememberScrollState())) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(238.dp)
                    .clip(RoundedCornerShape(bottomStart = 34.dp, bottomEnd = 34.dp))
                    .background(Brush.linearGradient(listOf(Color(0xFF08164A), Color(0xFF2A1E8A), Color(0xFF1E4FFF)))),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    RodriguesLogoMark(size = 112, pulse = false)
                    Text("UP ENTREGADOR", color = Color.White, fontFamily = AppFont, fontSize = 14.sp, letterSpacing = 4.sp, fontWeight = FontWeight.Bold)
                    AppVersionBadge(onDark = true)
                }
            }
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 22.dp, vertical = 22.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(13.dp)
            ) {
                Text("Bem-vindo(a)!", color = Ink, fontFamily = AppFont, fontSize = 27.sp, lineHeight = 31.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                Text("Acesse sua conta para continuar\nfazendo entregas.", color = Muted, fontFamily = AppFont, fontSize = 14.sp, lineHeight = 20.sp, fontWeight = FontWeight.Medium, textAlign = TextAlign.Center)
                if (notice.isNotBlank()) InlineNoticeCard(notice, Green)
                if (error.isNotBlank()) InlineNoticeCard(error, Red)
                Spacer(Modifier.height(4.dp))
                Field("", doc, { doc = onlyDigits(it).take(14) }, "CPF ou telefone", keyboardType = KeyboardType.Number)
                Field("", password, { password = it }, "Senha", password = true)
                PrimaryButton("Entrar", enabled = !loading, color = Navy, icon = null) { onLogin(doc, password) { loading = it } }
                SecondaryButton("Solicitar cadastro", color = Ink) { mode = "cadastro" }
                Text("Esqueci minha senha", color = Muted, fontFamily = AppFont, fontSize = 12.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                Text(AppVersion.LABEL, color = Muted2, fontFamily = AppFont, fontSize = 11.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            }
        }
    } else {
        ScreenScroll {
            BrandHeader(title = "Solicitar cadastro", subtitle = "Envie seus dados para análise da operação.", icon = Icons.Filled.Person)
            if (notice.isNotBlank()) InlineNoticeCard(notice, Green)
            if (error.isNotBlank()) InlineNoticeCard(error, Red)
            PremiumCard {
                Field("Nome completo", name, { name = it }, "Seu nome")
                Field("CPF", cpf, { cpf = onlyDigits(it).take(11) }, "Somente números", keyboardType = KeyboardType.Number)
                Field("Telefone / WhatsApp", phone, { phone = onlyDigits(it).take(11) }, "67999999999", keyboardType = KeyboardType.Phone)
                Field("E-mail", email, { email = it }, "seu@email.com", keyboardType = KeyboardType.Email)
                Field("Cidade", city, { city = it }, "Campo Grande - MS")
                Field("Senha", password, { password = it }, "Mínimo 6 caracteres", password = true)
                Field("Veículo", vehicle, { vehicle = it }, "Moto")
                Field("Placa", plate, { plate = it.uppercase(Locale.ROOT) }, "Opcional")
                Field("Chave Pix", pix, { pix = it }, "Opcional")
                Field("Banco", bank, { bank = it }, "Opcional")
                PrimaryButton("Enviar cadastro", enabled = !loading, icon = Icons.Filled.CheckCircle) { onRegister(DriverRegistrationRequest(name, cpf, phone, password, vehicle, plate, pix, bank, email, city)) { loading = it } }
                SecondaryButton("Voltar ao login", icon = Icons.Filled.ArrowBack) { mode = "login" }
                Text(AppVersion.LABEL, color = Muted2, fontFamily = AppFont, fontSize = 11.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            }
        }
    }
}



@Composable
private fun RodriguesLogoMark(size: Int = 56, pulse: Boolean = false) {
    val infinite = rememberInfiniteTransition(label = "logoPulse")
    val pulseScale by infinite.animateFloat(
        initialValue = 1f,
        targetValue = if (pulse) 1.06f else 1f,
        animationSpec = infiniteRepeatable(animation = tween(1350), repeatMode = RepeatMode.Reverse),
        label = "logoScale"
    )
    Box(
        modifier = Modifier
            .size(size.dp)
            .scale(pulseScale)
            .shadow(10.dp, RoundedCornerShape((size / 3).dp), clip = false, ambientColor = Navy.copy(alpha = .18f), spotColor = Navy.copy(alpha = .10f))
            .clip(RoundedCornerShape((size / 3).dp))
            .background(Color.White)
            .border(1.dp, Navy.copy(alpha = .18f), RoundedCornerShape((size / 3).dp)),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.up_app_icon),
            contentDescription = "Logo UP Entregador",
            modifier = Modifier.size((size * .82f).dp),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
private fun MotionRail(color: Color = Green) {
    val infinite = rememberInfiniteTransition(label = "motionRail")
    val offset by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(animation = tween(1600), repeatMode = RepeatMode.Restart),
        label = "railOffset"
    )
    Box(Modifier.fillMaxWidth().height(7.dp).clip(RoundedCornerShape(999.dp)).background(color.copy(alpha = .13f))) {
        Box(
            Modifier
                .fillMaxWidth(offset.coerceIn(.18f, 1f))
                .height(7.dp)
                .clip(RoundedCornerShape(999.dp))
                .background(Brush.horizontalGradient(listOf(color.copy(alpha = .35f), color, Color.White.copy(alpha = .55f))))
        )
    }
}

@Composable
private fun AnimatedAlertStrip(text: String, color: Color) {
    val infinite = rememberInfiniteTransition(label = "alertStrip")
    val pulse by infinite.animateFloat(
        initialValue = .55f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(animation = tween(700), repeatMode = RepeatMode.Reverse),
        label = "alertAlpha"
    )
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(9.dp),
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)).background(color.copy(alpha = .10f)).border(1.dp, color.copy(alpha = .22f), RoundedCornerShape(20.dp)).padding(12.dp)
    ) {
        Box(Modifier.size(11.dp).clip(CircleShape).background(color.copy(alpha = pulse)))
        Text(text, color = color, fontFamily = AppFont, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun RouteProgressRibbon(ride: DriverRide) {
    val stage = ride.stageShort()
    val color = ride.stageColor()
    PremiumCard {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Box(Modifier.size(42.dp).clip(CircleShape).background(color.copy(alpha = .12f)), contentAlignment = Alignment.Center) {
                Icon(Icons.Filled.Route, null, tint = color, modifier = Modifier.size(22.dp))
            }
            Column(Modifier.weight(1f)) {
                Text("Fluxo da corrida", color = Muted, fontFamily = AppFont, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Text(stage, color = Ink, fontFamily = AppFont, fontSize = 17.sp, fontWeight = FontWeight.Bold)
            }
            StatusChip(ride.deliveryCountLabel(), color)
        }
        MotionRail(color)
    }
}

private fun modifierWeightPlaceholder(): String = "Moto"

@Composable
private fun InlineNoticeCard(text: String, color: Color) {
    Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(22.dp)).background(color.copy(alpha = .10f)).border(1.dp, color.copy(alpha = .25f), RoundedCornerShape(22.dp)).padding(14.dp)) {
        Text(text, color = if (color == Red) Red else GreenDark, fontFamily = AppFont, fontSize = 13.sp, lineHeight = 18.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun CreatePasswordScreen(profile: DriverProfile, onSaved: () -> Unit, onLogout: () -> Unit) {
    val context = LocalContext.current
    var password by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    ScreenScroll {
        BrandHeader(title = "Crie sua senha", subtitle = "Esse acesso será usado nas próximas entradas no app.", icon = Icons.Filled.Shield)
        if (message.isNotBlank()) InlineNoticeCard(message, Red)
        PremiumCard {
            Text("Olá, ${profile.firstName()}", color = Ink, fontFamily = AppFont, fontSize = 19.sp, fontWeight = FontWeight.Bold)
            Field("Nova senha", password, { password = it }, password = true)
            Field("Confirmar senha", confirm, { confirm = it }, password = true)
            PrimaryButton("Salvar senha", enabled = !loading, icon = Icons.Filled.CheckCircle) {
                when {
                    password.length < 6 -> message = "Use pelo menos 6 caracteres."
                    password != confirm -> message = "As senhas não conferem."
                    else -> {
                        loading = true
                        DriverRepository.updateAccessPassword(context, password, onSuccess = { loading = false; onSaved() }, onError = { loading = false; message = it })
                    }
                }
            }
            SecondaryButton("Sair", icon = Icons.Filled.ArrowBack, color = Red, onClick = onLogout)
        }
    }
}

@Composable
private fun LoadingSessionSplash(profile: DriverProfile) {
    Box(Modifier.fillMaxSize().background(Bg), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(14.dp)) {
            RodriguesLogoMark(size = 92, pulse = true)
            CircularProgressIndicator(color = Green)
            Text("Preparando sua operação, ${profile.firstName()}...", color = Muted, fontFamily = AppFont, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun SystemStateScreen(title: String, message: String, icon: ImageVector, color: Color) {
    ScreenScroll {
        Spacer(Modifier.height(80.dp))
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Box(Modifier.size(82.dp).clip(CircleShape).background(color.copy(alpha = .12f)), contentAlignment = Alignment.Center) { Icon(icon, null, tint = color, modifier = Modifier.size(42.dp)) }
            Text(title, color = Ink, fontFamily = AppFont, fontSize = 23.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            Text(message, color = Muted, fontFamily = AppFont, fontSize = 14.sp, lineHeight = 20.sp, textAlign = TextAlign.Center)
        }
    }
}

@Composable
private fun HomeContent(
    profile: DriverProfile,
    online: Boolean,
    pendingRide: DriverRide?,
    activeRide: DriverRide?,
    stats: DriverStats,
    appBanners: List<AppCarouselBanner>,
    appNotices: List<AppNotice>,
    hideValues: Boolean,
    onToggleValues: () -> Unit,
    onToggleOnline: (Boolean) -> Unit,
    onAccept: (DriverRide) -> Unit,
    onReject: (DriverRide, String) -> Unit,
    onExpire: (DriverRide) -> Unit,
    onOpenRides: () -> Unit,
    onOpenHistory: () -> Unit,
    onOpenWallet: () -> Unit,
    onOpenMap: () -> Unit,
    onOpenNotifications: () -> Unit,
    onOpenSupport: () -> Unit,
    onFixPermissions: () -> Unit
) {
    val context = LocalContext.current
    val permissions = PermissionStatusReader.read(context)
    val status = operationalStatus(online, activeRide, permissions, context)
    ScreenScroll {
        ContractHomeHeader(
            profile = profile,
            status = status,
            unread = appNotices.count { !it.read && it.isVisible() },
            onOpenNotifications = onOpenNotifications,
            onOpenSupport = onOpenSupport
        )
        ContractStatusPill(status = status, online = online, onToggleOnline = onToggleOnline, onFixPermissions = onFixPermissions)
        ContractTodayCard(stats = stats, hideValues = hideValues, onToggleValues = onToggleValues)
        if (activeRide == null) {
            ContractOperationalInsights(stats = stats, online = online)
            ContractSmartCarousel(appBanners)
        }
        when {
            activeRide != null -> ContractActiveRideCard(activeRide, onOpenRides)
            pendingRide != null -> ContractPendingOfferCompactCard(pendingRide, onOpenRides)
            !permissions.ready -> ContractPermissionStateCard(permissions, context, onFixPermissions)
            else -> ContractEmptyRideCard(online = online, onToggleOnline = onToggleOnline)
        }
        ContractQuickActions(onOpenHistory, onOpenWallet, onOpenMap, onOpenSupport)
    }
}

@Composable
private fun ContractHomeHeader(
    profile: DriverProfile,
    status: OperationalStatus,
    unread: Int,
    onOpenNotifications: () -> Unit,
    onOpenSupport: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
    ) {
        Avatar(profile.name, profile.photoUrl, 58)
        Column(Modifier.weight(1f)) {
            Text("Olá, ${profile.firstName()}", color = Ink, fontFamily = AppFont, fontSize = 24.sp, lineHeight = 27.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(
                when (status.kind) {
                    AvailabilityKind.Disponivel -> "Pronto para receber corridas"
                    AvailabilityKind.EmEntrega -> status.label
                    AvailabilityKind.Restricao -> status.label
                    AvailabilityKind.Indisponivel -> "Você está indisponível"
                },
                color = status.buttonColor.takeIf { status.kind != AvailabilityKind.Indisponivel } ?: Muted,
                fontFamily = AppFont,
                fontSize = 14.sp,
                lineHeight = 17.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        RoundIconButton(Icons.Filled.Notifications, onOpenNotifications, badge = unread > 0)
        RoundIconButton(Icons.Filled.ChatBubbleOutline, onOpenSupport)
    }
}

@Composable
private fun RoundIconButton(icon: ImageVector, onClick: () -> Unit, badge: Boolean = false) {
    Box(
        Modifier
            .size(46.dp)
            .shadow(2.dp, CircleShape, clip = false)
            .clip(CircleShape)
            .background(Surface)
            .border(1.dp, Border, CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, null, tint = Navy, modifier = Modifier.size(22.dp))
        if (badge) Box(Modifier.align(Alignment.TopEnd).size(12.dp).clip(CircleShape).background(Green).border(2.dp, Color.White, CircleShape))
    }
}

@Composable
private fun ContractStatusPill(status: OperationalStatus, online: Boolean, onToggleOnline: (Boolean) -> Unit, onFixPermissions: () -> Unit) {
    val isOccurrenceHero = status.label.contains("Ocorr", ignoreCase = true) || status.message.contains("gestor", ignoreCase = true)
    val color = when (status.kind) {
        AvailabilityKind.Disponivel -> Green
        AvailabilityKind.EmEntrega -> if (isOccurrenceHero) Orange else Green
        AvailabilityKind.Restricao -> if (status.label.contains("Sem", true)) Color(0xFF50647A) else Red
        AvailabilityKind.Indisponivel -> Color(0xFF5B6778)
    }
    val endColor = when (status.kind) {
        AvailabilityKind.Disponivel -> Color(0xFF0E9F6E)
        AvailabilityKind.EmEntrega -> if (isOccurrenceHero) Color(0xFFD97706) else Color(0xFF047857)
        AvailabilityKind.Restricao -> if (status.label.contains("Sem", true)) Color(0xFF334155) else Color(0xFFB91C1C)
        AvailabilityKind.Indisponivel -> Color(0xFF374151)
    }
    val title = when {
        status.kind == AvailabilityKind.EmEntrega -> "Corrida em andamento"
        !status.canGoOnline && status.kind == AvailabilityKind.Restricao -> status.label.ifBlank { "Liberar permissões" }
        else -> status.label
    }
    val subtitle = when {
        status.kind == AvailabilityKind.EmEntrega -> status.message.ifBlank { "Toque em Corridas para continuar" }
        status.kind == AvailabilityKind.Disponivel -> "Aguardando corridas"
        status.kind == AvailabilityKind.Indisponivel -> "Ative para receber corridas"
        else -> status.message
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(76.dp)
            .shadow(10.dp, RoundedCornerShape(24.dp), clip = false, ambientColor = color.copy(alpha = .20f), spotColor = color.copy(alpha = .13f))
            .clip(RoundedCornerShape(24.dp))
            .background(Brush.horizontalGradient(listOf(color, endColor)))
            .clickable {
                when {
                    status.kind == AvailabilityKind.Restricao -> onFixPermissions()
                    status.kind == AvailabilityKind.EmEntrega -> Unit
                    status.canGoOnline || online -> onToggleOnline(!online)
                }
            }
            .padding(horizontal = 16.dp)
    ) {
        Box(Modifier.size(48.dp).clip(CircleShape).background(Color.White.copy(alpha = .16f)), contentAlignment = Alignment.Center) {
            Icon(
                when (status.kind) {
                    AvailabilityKind.Disponivel -> Icons.Filled.CheckCircle
                    AvailabilityKind.EmEntrega -> Icons.Filled.Route
                    AvailabilityKind.Restricao -> Icons.Filled.ErrorOutline
                    AvailabilityKind.Indisponivel -> Icons.Filled.Cancel
                },
                null,
                tint = Color.White,
                modifier = Modifier.size(25.dp)
            )
        }
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(title, color = Color.White, fontFamily = AppFont, fontSize = 21.sp, lineHeight = 23.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(subtitle, color = Color.White.copy(alpha = .90f), fontFamily = AppFont, fontSize = 13.sp, lineHeight = 16.sp, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        Icon(Icons.Filled.KeyboardArrowRight, null, tint = Color.White, modifier = Modifier.size(26.dp))
    }
}

@Composable
private fun ContractTodayCard(stats: DriverStats, hideValues: Boolean, onToggleValues: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(7.dp, RoundedCornerShape(24.dp), clip = false, ambientColor = Color(0x14000000), spotColor = Color(0x10000000))
            .border(1.dp, Border, RoundedCornerShape(24.dp)),
        colors = CardDefaults.cardColors(containerColor = Surface),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1.2f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text("Ganhos de hoje", color = Muted, fontFamily = AppFont, fontSize = 14.sp, lineHeight = 16.sp, fontWeight = FontWeight.SemiBold)
                Text(if (hideValues) "••••" else DriverRepository.formatCurrency(stats.totalToday), color = Ink, fontFamily = AppFont, fontSize = 31.sp, lineHeight = 34.sp, fontWeight = FontWeight.Bold)
            }
            Box(Modifier.width(1.dp).height(52.dp).background(Border))
            Spacer(Modifier.width(14.dp))
            Column(Modifier.width(86.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                MetricLine(stats.ridesTodayCount.toString(), "Corridas")
                MetricLine(stats.finishedTodayCount.toString(), "Finalizadas")
            }
            Box(Modifier.size(42.dp).clip(CircleShape).background(SurfaceSoft).clickable { onToggleValues() }, contentAlignment = Alignment.Center) {
                Icon(if (hideValues) Icons.Filled.VisibilityOff else Icons.Filled.Visibility, null, tint = Muted, modifier = Modifier.size(22.dp))
            }
        }
    }
}

@Composable
private fun MetricLine(value: String, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(7.dp)) {
        Text(value, color = Red, fontFamily = AppFont, fontSize = 20.sp, lineHeight = 22.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(24.dp))
        Text(label, color = Muted, fontFamily = AppFont, fontSize = 13.sp, lineHeight = 15.sp, fontWeight = FontWeight.Bold, maxLines = 1)
    }
}

@Composable
private fun ContractOperationalInsights(stats: DriverStats, online: Boolean) {
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    val demand = when (hour) {
        in 11..13, in 18..22 -> "Demanda alta"
        in 14..17 -> "Demanda média"
        else -> if (online) "Região ativa" else "Fique online"
    }
    val target = 150.0
    val done = stats.totalToday.coerceAtLeast(0.0)
    val missing = (target - done).coerceAtLeast(0.0)
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
        PremiumInsightTile(
            title = demand,
            subtitle = if (online) "Região monitorada" else "Fique disponível para receber",
            icon = Icons.Filled.MyLocation,
            color = Orange,
            modifier = Modifier.weight(1f)
        )
        PremiumInsightTile(
            title = if (missing <= 0.0) "Meta batida" else "Faltam ${DriverRepository.formatCurrency(missing)}",
            subtitle = "Meta diária R$ 150",
            icon = Icons.Filled.Bolt,
            color = Green,
            modifier = Modifier.weight(1f)
        )
    }
}



@Composable
private fun PremiumInsightTile(title: String, subtitle: String, icon: ImageVector, color: Color, modifier: Modifier) {
    Row(
        modifier = modifier
            .height(80.dp)
            .shadow(3.dp, RoundedCornerShape(21.dp), clip = false, ambientColor = color.copy(alpha = .10f), spotColor = Color(0x08000000))
            .clip(RoundedCornerShape(21.dp))
            .background(Surface)
            .border(1.dp, Border, RoundedCornerShape(21.dp))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(Modifier.size(42.dp).clip(CircleShape).background(color.copy(alpha = .12f)), contentAlignment = Alignment.Center) {
            Icon(icon, null, tint = color, modifier = Modifier.size(22.dp))
        }
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(title, color = Ink, fontFamily = AppFont, fontSize = 14.sp, lineHeight = 16.sp, fontWeight = FontWeight.Bold, maxLines = 2, overflow = TextOverflow.Ellipsis)
            Text(subtitle, color = Muted, fontFamily = AppFont, fontSize = 11.5.sp, lineHeight = 14.sp, fontWeight = FontWeight.SemiBold, maxLines = 2, overflow = TextOverflow.Ellipsis)
        }
    }
}

@Composable
private fun ContractSmartCarousel(appBanners: List<AppCarouselBanner>) {
    val banners = remember(appBanners) {
        val remote = appBanners.filter { it.isVisible() }.sortedBy { it.order }
        (remote + defaultPremiumBanners()).distinctBy { it.id }.take(5)
    }
    val pagerState = rememberPagerState(pageCount = { banners.size })
    LaunchedEffect(banners.size) {
        if (banners.size > 1) {
            while (true) {
                delay(4200)
                val next = (pagerState.currentPage + 1) % banners.size
                pagerState.animateScrollToPage(next)
            }
        }
    }
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxWidth()) { page ->
            ContractNewsBanner(banners[page])
        }
        Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            banners.forEachIndexed { index, _ ->
                val selected = pagerState.currentPage == index
                Box(
                    Modifier
                        .padding(horizontal = 4.dp)
                        .width(if (selected) 22.dp else 8.dp)
                        .height(8.dp)
                        .clip(CircleShape)
                        .background(if (selected) Navy else Border)
                )
            }
        }
    }
}

private fun defaultPremiumBanners(): List<AppCarouselBanner> = listOf(
    AppCarouselBanner(
        id = "local_meta_dia",
        badge = "META",
        title = "Meta do dia",
        description = "Acompanhe seus ganhos e mantenha o ritmo nos horários de pico.",
        buttonText = "Ver ganhos"
    ),
    AppCarouselBanner(
        id = "local_operacao",
        badge = "OPERAÇÃO",
        title = "Rode com segurança",
        description = "Mantenha GPS, notificações e bateria liberados durante as entregas.",
        buttonText = "Conferir"
    ),
    AppCarouselBanner(
        id = "local_repasse",
        badge = "REPASSE",
        title = "Carteira organizada",
        description = "Veja saldo, Pix e próximos repasses em uma visão mais limpa.",
        buttonText = "Abrir carteira"
    )
)

@Composable
private fun ContractNewsBanner(banner: AppCarouselBanner?) {
    val title = banner?.title?.takeIf { it.isNotBlank() } ?: "Indique e ganhe"
    val badge = banner?.badge?.takeIf { it.isNotBlank() } ?: "BÔNUS"
    val desc = banner?.description?.takeIf { it.isNotBlank() } ?: "Convide amigos e ganhe bônus por indicação aprovada."
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(116.dp)
            .shadow(6.dp, RoundedCornerShape(22.dp), clip = false, ambientColor = Navy.copy(alpha = .12f), spotColor = Color(0x10000000))
            .clip(RoundedCornerShape(22.dp))
            .background(Brush.horizontalGradient(listOf(Color(0xFFF0F7FF), Color(0xFFEAFBF2), Color(0xFFFFF7ED))))
            .border(1.dp, Color(0xFFDCEBFA), RoundedCornerShape(22.dp))
            .padding(14.dp)
    ) {
        Column(Modifier.align(Alignment.CenterStart).fillMaxWidth(.68f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Text(badge.uppercase(Locale.ROOT).take(14), color = Navy, fontFamily = AppFont, fontSize = 11.sp, lineHeight = 13.sp, fontWeight = FontWeight.Bold)
            Text(title, color = Ink, fontFamily = AppFont, fontSize = 18.sp, lineHeight = 20.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(desc, color = Muted, fontFamily = AppFont, fontSize = 12.5.sp, lineHeight = 15.sp, fontWeight = FontWeight.SemiBold, maxLines = 2, overflow = TextOverflow.Ellipsis)
        }
        Box(Modifier.align(Alignment.CenterEnd).size(72.dp).clip(RoundedCornerShape(22.dp)).background(Color.White.copy(alpha = .75f)), contentAlignment = Alignment.Center) {
            Icon(Icons.Filled.TwoWheeler, null, tint = Navy, modifier = Modifier.size(44.dp))
        }
    }
}

@Composable
private fun ContractPagerDots() {
    Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Box(Modifier.size(8.dp).clip(CircleShape).background(Navy))
        Spacer(Modifier.width(8.dp))
        Box(Modifier.size(8.dp).clip(CircleShape).background(Border))
        Spacer(Modifier.width(8.dp))
        Box(Modifier.size(8.dp).clip(CircleShape).background(Border))
    }
}

@Composable
private fun ContractQuickActions(onHistory: () -> Unit, onWallet: () -> Unit, onMap: () -> Unit, onSupport: () -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
        ContractQuickTile("Histórico", "Corridas", Icons.Filled.History, Modifier.weight(1f), onHistory)
        ContractQuickTile("Ganhos", "Carteira", Icons.Filled.AccountBalanceWallet, Modifier.weight(1f), onWallet)
        ContractQuickTile("Mapa", "Região", Icons.Filled.Map, Modifier.weight(1f), onMap)
        ContractQuickTile("Suporte", "Ajuda", Icons.Filled.SupportAgent, Modifier.weight(1f), onSupport)
    }
}

@Composable
private fun ContractQuickTile(title: String, subtitle: String, icon: ImageVector, modifier: Modifier, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.dp),
        modifier = modifier
            .height(82.dp)
            .shadow(3.dp, RoundedCornerShape(20.dp), clip = false, ambientColor = Color(0x10000000), spotColor = Color(0x08000000))
            .clip(RoundedCornerShape(20.dp))
            .background(Surface)
            .border(1.dp, Border, RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(vertical = 9.dp, horizontal = 4.dp)
    ) {
        Box(Modifier.size(36.dp).clip(CircleShape).background(NavySoft), contentAlignment = Alignment.Center) {
            Icon(icon, null, tint = Navy, modifier = Modifier.size(20.dp))
        }
        Text(title, color = Ink, fontFamily = AppFont, fontSize = 12.sp, lineHeight = 13.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
        Text(subtitle, color = Muted, fontFamily = AppFont, fontSize = 9.5.sp, lineHeight = 10.sp, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
private fun ContractPendingOfferCompactCard(ride: DriverRide, onOpenRides: () -> Unit) {
    PremiumCard(modifier = Modifier.clickable { onOpenRides() }) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            StatusChip("Nova oferta", Navy, Icons.Filled.Bolt)
            Spacer(Modifier.weight(1f))
            Text(ride.distance.ifBlank { "—" }, color = Muted, fontFamily = AppFont, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
        Text(ride.pickup.ifBlank { "Local de coleta" }, color = Ink, fontFamily = AppFont, fontSize = 18.sp, lineHeight = 21.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
        Text(ride.dropoff.ifBlank { ride.neighborhood.ifBlank { "Endereço de entrega" } }, color = Muted, fontFamily = AppFont, fontSize = 13.sp, lineHeight = 16.sp, fontWeight = FontWeight.SemiBold, maxLines = 2, overflow = TextOverflow.Ellipsis)
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(ride.value.ifBlank { DriverRepository.formatCurrency(ride.valueNumber) }, color = Ink, fontFamily = AppFont, fontSize = 22.sp, lineHeight = 25.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            Text("Toque para ver oferta", color = Navy, fontFamily = AppFont, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Icon(Icons.Filled.KeyboardArrowRight, null, tint = Navy, modifier = Modifier.size(22.dp))
        }
    }
}

@Composable
private fun ContractPermissionStateCard(status: PermissionStatus, context: Context, onFix: () -> Unit) {
    val missing = permissionMissingLabels(status, context).take(3)
    PremiumCard(modifier = Modifier.clickable { onFix() }) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(Modifier.size(52.dp).clip(CircleShape).background(RedSoft), contentAlignment = Alignment.Center) {
                Icon(Icons.Filled.Shield, null, tint = Red, modifier = Modifier.size(27.dp))
            }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text("Permissões pendentes", color = Red, fontFamily = AppFont, fontSize = 13.sp, lineHeight = 15.sp, fontWeight = FontWeight.Bold)
                Text(if (missing.isEmpty()) "Libere as permissões do Android" else missing.joinToString(" • "), color = Ink, fontFamily = AppFont, fontSize = 17.sp, lineHeight = 20.sp, fontWeight = FontWeight.Bold, maxLines = 2, overflow = TextOverflow.Ellipsis)
                Text("Ative para receber corridas sem travar a operação.", color = Muted, fontFamily = AppFont, fontSize = 13.sp, lineHeight = 16.sp, fontWeight = FontWeight.SemiBold)
            }
            Icon(Icons.Filled.KeyboardArrowRight, null, tint = Red)
        }
    }
}

@Composable
private fun ContractEmptyRideCard(online: Boolean, onToggleOnline: (Boolean) -> Unit) {
    PremiumCard(modifier = Modifier.clickable { if (!online) onToggleOnline(true) }) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(Modifier.size(52.dp).clip(CircleShape).background(if (online) GreenSoft else SurfaceSoft), contentAlignment = Alignment.Center) {
                Icon(if (online) Icons.Filled.CheckCircle else Icons.Filled.Cancel, null, tint = if (online) Green else Muted, modifier = Modifier.size(27.dp))
            }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text("Sem corrida ativa", color = Ink, fontFamily = AppFont, fontSize = 18.sp, lineHeight = 21.sp, fontWeight = FontWeight.Bold)
                Text(if (online) "Aguardando nova solicitação." else "Ative para começar a receber corridas.", color = Muted, fontFamily = AppFont, fontSize = 13.sp, lineHeight = 16.sp, fontWeight = FontWeight.SemiBold)
            }
            Icon(Icons.Filled.KeyboardArrowRight, null, tint = Muted)
        }
    }
}


@Composable
private fun ContractActiveRideCard(ride: DriverRide, onOpenRides: () -> Unit) {
    val isOccurrence = ride.stageLabel().contains("ocorr", ignoreCase = true) || ride.status.contains("OCOR", ignoreCase = true) || ride.rawStatus.contains("OCOR", ignoreCase = true)
    val accent = if (isOccurrence) Orange else Green
    PremiumCard(modifier = Modifier.clickable { onOpenRides() }) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(Modifier.size(52.dp).clip(CircleShape).background(accent.copy(alpha = .12f)), contentAlignment = Alignment.Center) {
                Icon(if (isOccurrence) Icons.Filled.ErrorOutline else Icons.Filled.Route, null, tint = accent, modifier = Modifier.size(27.dp))
            }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(if (isOccurrence) "Ocorrência na corrida" else ride.missionTitle("Corrida"), color = accent, fontFamily = AppFont, fontSize = 13.sp, lineHeight = 15.sp, fontWeight = FontWeight.Bold)
                Text("${ride.deliveryCountLabel()} • ${ride.stageShort()}", color = Ink, fontFamily = AppFont, fontSize = 19.sp, lineHeight = 22.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(
                    if (isOccurrence) "Aguardando orientação do gestor" else ride.dropoff.ifBlank { ride.neighborhood.ifBlank { "Toque para continuar a operação" } },
                    color = Muted,
                    fontFamily = AppFont,
                    fontSize = 13.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        val hasRouteMetrics = ride.distance.isNotBlank() || ride.duration.isNotBlank()
        if (hasRouteMetrics) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                SmallMetric("Distância", ride.distance.ifBlank { "Calculando" }, Modifier.weight(1f))
                SmallMetric("Tempo", ride.duration.ifBlank { "Calculando" }, Modifier.weight(1f))
            }
        } else {
            InlineNoticeCard("Calculando rota e tempo da corrida...", Blue)
        }
        PrimaryButton(if (isOccurrence) "Ver detalhes" else "Continuar corrida", icon = Icons.Filled.KeyboardArrowRight, color = accent, onClick = onOpenRides)
    }
}

@Composable
private fun ContractPermissionMini(status: PermissionStatus, context: Context, onFix: () -> Unit) {
    val missing = permissionMissingLabels(status, context)
    if (missing.isEmpty()) return
    Column(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)).background(OrangeSoft).border(1.dp, Orange.copy(alpha = .22f), RoundedCornerShape(20.dp)).padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Permissões pendentes", color = Ink, fontFamily = AppFont, fontSize = 15.sp, fontWeight = FontWeight.Bold)
        Text(missing.joinToString(" • "), color = Muted, fontFamily = AppFont, fontSize = 12.sp, lineHeight = 15.sp, fontWeight = FontWeight.Bold)
        PrimaryButton("Liberar agora", icon = Icons.Filled.CheckCircle, color = Orange, onClick = onFix)
    }
}

@Composable
private fun DriverTopHeader(profile: DriverProfile, status: OperationalStatus, unread: Int, onOpenNotifications: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
        Avatar(profile.name, profile.photoUrl, 54)
        Column(Modifier.weight(1f)) {
            Text("Olá, ${profile.firstName()}", color = Ink, fontFamily = AppFont, fontSize = 22.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(status.label, color = status.buttonColor, fontFamily = AppFont, fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }
        Box(Modifier.size(48.dp).clip(CircleShape).background(SurfaceSoft).border(1.dp, Border, CircleShape).clickable { onOpenNotifications() }, contentAlignment = Alignment.Center) {
            Icon(Icons.Filled.Notifications, null, tint = Ink, modifier = Modifier.size(22.dp))
            if (unread > 0) Box(Modifier.align(Alignment.TopEnd).size(13.dp).clip(CircleShape).background(Red).border(2.dp, Color.White, CircleShape))
        }
    }
}

@Composable
private fun StatusHeroCard(status: OperationalStatus, online: Boolean, onToggleOnline: (Boolean) -> Unit, onFixPermissions: () -> Unit) {
    val infinite = rememberInfiniteTransition(label = "heroPulse")
    val pulse by infinite.animateFloat(
        initialValue = .92f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(animation = tween(1150), repeatMode = RepeatMode.Reverse),
        label = "pulse"
    )
    val heroBrush = Brush.linearGradient(
        colors = when (status.kind) {
            AvailabilityKind.Disponivel -> listOf(Navy, NavyDark)
            AvailabilityKind.Restricao -> listOf(Red, Color(0xFFB9142B))
            AvailabilityKind.EmEntrega -> listOf(Navy, Blue)
            AvailabilityKind.Indisponivel -> listOf(Color(0xFF171C22), Color(0xFF2D3440))
        }
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(34.dp))
            .background(heroBrush)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(66.dp), contentAlignment = Alignment.Center) {
                Box(Modifier.size(64.dp).scale(pulse).alpha(.28f).clip(CircleShape).background(Color.White))
                Box(Modifier.size(58.dp).clip(CircleShape).background(Color.White.copy(alpha = .18f)).border(1.dp, Color.White.copy(alpha = .24f), CircleShape), contentAlignment = Alignment.Center) {
                    if (status.kind == AvailabilityKind.Restricao) Icon(Icons.Filled.ErrorOutline, null, tint = Color.White, modifier = Modifier.size(31.dp)) else RodriguesLogoMark(size = 44, pulse = false)
                }
            }
            Spacer(Modifier.width(13.dp))
            Column(Modifier.weight(1f)) {
                Text(status.label, color = Color.White, fontFamily = AppFont, fontSize = 23.sp, lineHeight = 25.sp, fontWeight = FontWeight.Bold)
                Text(status.message, color = Color.White.copy(alpha = .86f), fontFamily = AppFont, fontSize = 13.sp, lineHeight = 18.sp, fontWeight = FontWeight.SemiBold)
            }
        }
        Button(
            onClick = { if (!status.canGoOnline && !online) onFixPermissions() else onToggleOnline(!online) },
            enabled = true,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = ButtonShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = if (online) Red else Navy,
                disabledContainerColor = Color.White.copy(alpha = .45f),
                disabledContentColor = Color.White
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 0.dp)
        ) {
            Icon(if (online) Icons.Filled.Cancel else Icons.Filled.CheckCircle, null, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
            Text(if (online) "Ficar indisponível" else if (!status.canGoOnline) "Liberar permissões" else "Ficar disponível", fontFamily = AppFont, fontSize = 15.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun EarningsSummaryCard(stats: DriverStats, hideValues: Boolean, onToggleValues: () -> Unit) {
    PremiumCard {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1.25f)) {
                Text("Ganhos de hoje", color = Muted, fontFamily = AppFont, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Text(if (hideValues) "••••" else DriverRepository.formatCurrency(stats.totalToday), color = Ink, fontFamily = AppFont, fontSize = 28.sp, lineHeight = 30.sp, fontWeight = FontWeight.Bold)
                Text("Próximo repasse e acertos ficam na Carteira", color = Muted2, fontFamily = AppFont, fontSize = 11.sp, lineHeight = 15.sp, fontWeight = FontWeight.SemiBold)
            }
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(.9f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                MiniCard { SmallMetricText("Corridas", stats.ridesTodayCount.toString()) }
                MiniCard { SmallMetricText("Finalizadas", stats.finishedTodayCount.toString()) }
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
            StatusChip("Semana ${if (hideValues) "••••" else DriverRepository.formatCurrency(stats.totalWeek)}", Blue)
            Spacer(Modifier.weight(1f))
            Box(Modifier.size(42.dp).clip(CircleShape).background(SurfaceSoft).clickable { onToggleValues() }, contentAlignment = Alignment.Center) {
                Icon(if (hideValues) Icons.Filled.VisibilityOff else Icons.Filled.Visibility, null, tint = Muted, modifier = Modifier.size(21.dp))
            }
        }
    }
}

@Composable
private fun SmallMetric(label: String, value: String, modifier: Modifier = Modifier) {
    MiniCard(modifier = modifier) { SmallMetricText(label, value) }
}

@Composable
private fun SmallMetricText(label: String, value: String) {
    Text(label, color = Muted, fontFamily = AppFont, fontSize = 12.sp, lineHeight = 14.sp, fontWeight = FontWeight.Bold, maxLines = 1)
    Text(value, color = Ink, fontFamily = AppFont, fontSize = 17.sp, lineHeight = 20.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
}

@Composable
private fun ActiveRouteShortcut(ride: DriverRide, onOpenRides: () -> Unit) {
    val payment = ride.paymentUi()
    PremiumCard(modifier = Modifier.clickable { onOpenRides() }) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(52.dp).clip(CircleShape).background(GreenSoft), contentAlignment = Alignment.Center) {
                Icon(Icons.Filled.Route, null, tint = Green, modifier = Modifier.size(27.dp))
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(ride.missionTitle("Corrida"), color = Ink, fontFamily = AppFont, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text("${ride.deliveryCountLabel()} • ${ride.stageLabel()}", color = Muted, fontFamily = AppFont, fontSize = 12.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            Icon(Icons.Filled.KeyboardArrowRight, null, tint = Muted)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            StatusChip(payment.label, payment.color, if (payment.requiresMachine) Icons.Filled.CreditCard else Icons.Filled.Payments)
            StatusChip(ride.distance.ifBlank { "Calculando rota" }, Blue)
        }
    }
}

@Composable
private fun OperationBanner(banner: AppCarouselBanner?, status: OperationalStatus) {
    val title = banner?.title?.takeIf { it.isNotBlank() } ?: "Pronto para rodar"
    val desc = banner?.description?.takeIf { it.isNotBlank() } ?: "Fique disponível e aguarde uma corrida da operação."
    PremiumCard {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            if (banner?.imageUrl?.isNotBlank() == true) {
                AsyncImage(model = banner.imageUrl, contentDescription = title, contentScale = ContentScale.Crop, modifier = Modifier.size(72.dp).clip(RoundedCornerShape(22.dp)))
            } else {
                Box(Modifier.size(72.dp).clip(RoundedCornerShape(22.dp)).background(status.buttonColor.copy(alpha = .12f)), contentAlignment = Alignment.Center) { Icon(Icons.Filled.Bolt, null, tint = status.buttonColor, modifier = Modifier.size(35.dp)) }
            }
            Column(Modifier.weight(1f)) {
                Text(title, color = Ink, fontFamily = AppFont, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(desc, color = Muted, fontFamily = AppFont, fontSize = 13.sp, lineHeight = 17.sp)
            }
        }
    }
}

@Composable
private fun QuickActions(onHistory: () -> Unit, onWallet: () -> Unit, onMap: () -> Unit, onSupport: () -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
        QuickTile("Histórico", Icons.Filled.History, Green, Modifier.weight(1f), onHistory)
        QuickTile("Ganhos", Icons.Filled.Payments, Blue, Modifier.weight(1f), onWallet)
        QuickTile("Mapa", Icons.Filled.Map, Orange, Modifier.weight(1f), onMap)
        QuickTile("Suporte", Icons.Filled.SupportAgent, Ink, Modifier.weight(1f), onSupport)
    }
}

@Composable
private fun QuickTile(title: String, icon: ImageVector, color: Color, modifier: Modifier, onClick: () -> Unit) {
    Column(modifier.shadow(7.dp, RoundedCornerShape(24.dp), clip = false).clip(RoundedCornerShape(24.dp)).background(Surface).border(1.dp, Border, RoundedCornerShape(24.dp)).clickable { onClick() }.padding(vertical = 15.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(7.dp)) {
        Icon(icon, null, tint = color, modifier = Modifier.size(24.dp))
        Text(title, color = Ink, fontFamily = AppFont, fontSize = 11.sp, fontWeight = FontWeight.Bold)
    }
}

private fun permissionMissingLabels(status: PermissionStatus, context: Context): List<String> = buildList {
    if (!status.notifications) add("notificações")
    if (!status.location) add("localização")
    if (!status.fullScreenIntent) add("alerta urgente")
    if (!status.batteryUnrestricted) add("bateria")
    if (!hasInternet(context)) add("internet")
    if (!isGpsEnabled(context)) add("GPS")
}

@Composable
private fun PermissionRestrictionMini(status: PermissionStatus, context: Context, onFix: () -> Unit) {
    val missing = permissionMissingLabels(status, context)
    if (missing.isNotEmpty()) {
        Column(
            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(26.dp)).background(OrangeSoft).border(1.dp, Orange.copy(alpha = .25f), RoundedCornerShape(26.dp)).padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Box(Modifier.size(44.dp).clip(CircleShape).background(Color.White), contentAlignment = Alignment.Center) {
                    Icon(Icons.Filled.ErrorOutline, null, tint = Orange, modifier = Modifier.size(24.dp))
                }
                Column(Modifier.weight(1f)) {
                    Text("Operação em restrição", color = Ink, fontFamily = AppFont, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text("Falta: ${missing.joinToString(", ")}", color = Muted, fontFamily = AppFont, fontSize = 12.sp, lineHeight = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
            PrimaryButton("Liberar permissões", icon = Icons.Filled.CheckCircle, color = Orange, onClick = onFix)
        }
    }
}

@Composable
private fun IncomingOfferPanel(ride: DriverRide, onAccept: (DriverRide) -> Unit, onReject: (DriverRide, String) -> Unit, onExpire: (DriverRide) -> Unit) {
    var rejectOpen by remember(ride.id) { mutableStateOf(false) }
    var now by remember(ride.id) { mutableStateOf(System.currentTimeMillis()) }
    val payment = ride.paymentUi()
    LaunchedEffect(ride.id, ride.offerExpiresAtMillis) {
        while (ride.offerExpiresAtMillis > 0L && System.currentTimeMillis() <= ride.offerExpiresAtMillis) {
            now = System.currentTimeMillis()
            delay(1000)
        }
        now = System.currentTimeMillis()
        if (ride.offerExpiresAtMillis > 0L && now > ride.offerExpiresAtMillis) onExpire(ride)
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(Surface)
            .border(1.dp, Border, RoundedCornerShape(28.dp))
            .shadow(7.dp, RoundedCornerShape(28.dp), clip = false),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().background(Red).padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(if (ride.deliveryCount() > 1) "NOVA CORRIDA AGRUPADA" else "NOVA CORRIDA URGENTE", color = Color.White, fontFamily = AppFont, fontSize = 13.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            Text(offerCountdownText(ride, now), color = Color.White, fontFamily = AppFont, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(ride.value.ifBlank { DriverRepository.formatCurrency(ride.valueNumber) }, color = Green, fontFamily = AppFont, fontSize = 32.sp, lineHeight = 36.sp, fontWeight = FontWeight.Bold)
            PremiumCard {
                RoutePointLine(Icons.Filled.Storefront, "Coleta", ride.pickup.ifBlank { "Rodrigues Açaí e Cia. - Av. Gury Marques, Campo Grande - MS" }, Green)
                RoutePointLine(Icons.Filled.Place, "Entrega", if (ride.dropoff.isNotBlank()) ride.dropoff else ride.neighborhood.ifBlank { "Rua das Flores, 245 - Jardim dos Estados" }, Red)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                OfferMetric("Distância", ride.distance.ifBlank { "6,2 km" }, Icons.Filled.Route, Modifier.weight(1f))
                OfferMetric("Tempo", ride.duration.ifBlank { "18 min" }, Icons.Filled.Schedule, Modifier.weight(1f))
            }
            RealDeliveryMap(
                title = "Nova corrida",
                subtitle = listOf(ride.distance, ride.duration).filter { it.isNotBlank() }.joinToString(" • ").ifBlank { ride.neighborhood.ifBlank { "Área da entrega" } },
                pickupAddress = ride.pickup,
                dropoffAddress = ride.dropoff,
                pickupLat = ride.pickupLat,
                pickupLng = ride.pickupLng,
                dropoffLat = ride.dropoffLat,
                dropoffLng = ride.dropoffLng,
                mode = DeliveryMapMode.PICKUP_TO_DROPOFF,
                modifier = Modifier.height(218.dp)
            )
            PaymentChips(payment)
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                SecondaryButton("Recusar", icon = null, color = Red, modifier = Modifier.weight(1f)) { rejectOpen = true }
                PrimaryButton("Aceitar", icon = null, modifier = Modifier.weight(1.35f)) { onAccept(ride) }
            }
        }
    }
    if (rejectOpen) RejectDialog(onClose = { rejectOpen = false }, onConfirm = { reason -> rejectOpen = false; onReject(ride, reason) })
}



private fun offerCountdownText(ride: DriverRide, now: Long): String {
    if (ride.offerExpiresAtMillis <= 0L) return "00:--"
    val remaining = ((ride.offerExpiresAtMillis - now) / 1000L).coerceAtLeast(0L)
    val min = remaining / 60
    val sec = remaining % 60
    return "%02d:%02d".format(Locale.ROOT, min, sec)
}

@Composable
private fun OfferMetric(label: String, value: String, icon: ImageVector, modifier: Modifier = Modifier) {
    Row(modifier.clip(RoundedCornerShape(22.dp)).background(SurfaceSoft).border(1.dp, Border, RoundedCornerShape(22.dp)).padding(12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(9.dp)) {
        Icon(icon, null, tint = Green, modifier = Modifier.size(20.dp))
        Column {
            Text(label, color = Muted, fontFamily = AppFont, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            Text(value, color = Ink, fontFamily = AppFont, fontSize = 14.sp, fontWeight = FontWeight.Bold, maxLines = 1)
        }
    }
}

@Composable
private fun RidesContent(
    pendingRide: DriverRide?,
    activeRide: DriverRide?,
    paymentMachines: List<PaymentMachine>,
    online: Boolean,
    onAccept: (DriverRide) -> Unit,
    onReject: (DriverRide, String) -> Unit,
    onExpire: (DriverRide) -> Unit,
    onUpdateRide: (DriverRide, String) -> Unit,
    onOpenNavigator: (String, String) -> Unit,
    onOccurrence: (DriverRide, String, String) -> Unit,
    onSettleAndFinish: (DriverRide, PaymentSettlementInput) -> Unit
) {
    when {
        activeRide != null -> CurrentRouteScreen(
            ride = activeRide,
            routeAddition = pendingRide?.takeIf { it.isRouteAddition },
            paymentMachines = paymentMachines,
            onAcceptAddition = onAccept,
            onRejectAddition = onReject,
            onUpdateRide = onUpdateRide,
            onOpenNavigator = onOpenNavigator,
            onOccurrence = onOccurrence,
            onSettleAndFinish = onSettleAndFinish
        )
        pendingRide != null -> ScreenScroll { IncomingOfferPanel(pendingRide, onAccept, onReject, onExpire) }
        else -> WaitingRideScreen(online)
    }
}


@Composable
private fun WaitingRideScreen(online: Boolean) {
    ScreenScroll {
        Spacer(Modifier.height(26.dp))
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Box(Modifier.size(132.dp).clip(CircleShape).background(NavySoft), contentAlignment = Alignment.Center) {
                Icon(Icons.Filled.Route, null, tint = Navy, modifier = Modifier.size(62.dp))
            }
            Text(if (online) "Aguardando corrida" else "Você está indisponível", color = Ink, fontFamily = AppFont, fontSize = 25.sp, lineHeight = 28.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            Text(
                if (online) "Você está disponível. Assim que surgir uma oportunidade, avisaremos com alerta urgente."
                else "Volte para a Home e fique disponível para receber ofertas.",
                color = Muted,
                fontFamily = AppFont,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                textAlign = TextAlign.Center
            )
            PremiumCard {
                RoutePointLine(Icons.Filled.MyLocation, "Região", "Mantenha o GPS ativo para receber chamadas próximas.", Blue)
                RoutePointLine(Icons.Filled.NotificationsActive, "Alertas", "Notificações liberadas aumentam a chance de atender rápido.", Orange)
                RoutePointLine(Icons.Filled.Bolt, "Horário de pico", "Fique atento principalmente no almoço e à noite.", Green)
            }
        }
    }
}



@Composable
private fun CurrentRouteScreen(
    ride: DriverRide,
    routeAddition: DriverRide?,
    paymentMachines: List<PaymentMachine>,
    onAcceptAddition: (DriverRide) -> Unit,
    onRejectAddition: (DriverRide, String) -> Unit,
    onUpdateRide: (DriverRide, String) -> Unit,
    onOpenNavigator: (String, String) -> Unit,
    onOccurrence: (DriverRide, String, String) -> Unit,
    onSettleAndFinish: (DriverRide, PaymentSettlementInput) -> Unit
) {
    var occurrenceOpen by remember(ride.id) { mutableStateOf(false) }
    var paymentConfirmOpen by remember(ride.id) { mutableStateOf(false) }
    val action = ride.nextAction()
    val payment = ride.paymentUi()
    val isDelivering = ride.isDeliveringStage()
    val mapMode = if (isDelivering) DeliveryMapMode.DRIVER_TO_DROPOFF else DeliveryMapMode.DRIVER_TO_PICKUP

    ScreenScroll {
        RouteTopHeader(ride)
        routeAddition?.let { addition ->
            RouteAdditionOfferCard(
                addition = addition,
                onAccept = { onAcceptAddition(addition) },
                onReject = { onRejectAddition(addition, "IGNORADA_PELO_ENTREGADOR") }
            )
        }
        RealDeliveryMap(
            title = ride.missionTitle("Corrida"),
            subtitle = "${ride.deliveryCountLabel()} • ${ride.stageLabel()}",
            pickupAddress = ride.pickup,
            dropoffAddress = ride.dropoff,
            pickupLat = ride.pickupLat,
            pickupLng = ride.pickupLng,
            dropoffLat = ride.dropoffLat,
            dropoffLng = ride.dropoffLng,
            mode = mapMode,
            modifier = Modifier.height(430.dp)
        )
        CurrentStageFocusCard(ride, action)
        CompactStopsCard(ride, isDelivering)
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
            SmallMetric("Distância", ride.distance.ifBlank { "—" }, Modifier.weight(1f))
            SmallMetric("Tempo", ride.duration.ifBlank { "—" }, Modifier.weight(1f))
        }
        PaymentChipsCard(payment)
        SecondaryButton("Abrir navegação", icon = Icons.Filled.Map, color = Green) { onOpenNavigator(ride.pickup, ride.dropoff) }
        NextActionCard(ride, action, onMainClick = {
            if (action.needsPaymentConfirmation) paymentConfirmOpen = true else onUpdateRide(ride, action.statusToSend)
        })
        SecondaryButton("Registrar ocorrência", icon = Icons.Filled.ErrorOutline, color = Orange) { occurrenceOpen = true }
    }
    if (occurrenceOpen) OccurrenceDialog(onClose = { occurrenceOpen = false }, onConfirm = { reason, details -> occurrenceOpen = false; onOccurrence(ride, reason, details) })
    if (paymentConfirmOpen) PaymentConfirmDialog(ride, paymentMachines, onClose = { paymentConfirmOpen = false }, onConfirm = { input -> paymentConfirmOpen = false; onSettleAndFinish(ride, input) })
}

@Composable
private fun CurrentStageFocusCard(ride: DriverRide, action: RouteAction) {
    val accent = action.accent
    PremiumCard {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            Box(Modifier.size(56.dp).clip(RoundedCornerShape(18.dp)).background(accent.copy(alpha = .13f)), contentAlignment = Alignment.Center) {
                Icon(
                    when {
                        ride.isOccurrence() -> Icons.Filled.ErrorOutline
                        ride.isDeliveringStage() -> Icons.Filled.Place
                        ride.isAtPickup() -> Icons.Filled.Storefront
                        else -> Icons.Filled.TwoWheeler
                    },
                    null,
                    tint = accent,
                    modifier = Modifier.size(29.dp)
                )
            }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text("Próxima parada", color = Muted, fontFamily = AppFont, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Text(ride.nextStopSummary(), color = Ink, fontFamily = AppFont, fontSize = 18.sp, lineHeight = 21.sp, fontWeight = FontWeight.Bold)
                Text(action.message, color = Muted, fontFamily = AppFont, fontSize = 12.sp, lineHeight = 16.sp, fontWeight = FontWeight.SemiBold)
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            StatusChip(ride.deliveryCountLabel(), accent, Icons.Filled.Route)
            StatusChip(ride.stageLabel(), accent, Icons.Filled.Schedule)
        }
    }
}

@Composable
private fun RouteAdditionOfferCard(addition: DriverRide, onAccept: () -> Unit, onReject: () -> Unit) {
    PremiumCard {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(Modifier.size(54.dp).clip(RoundedCornerShape(18.dp)).background(BlueSoft), contentAlignment = Alignment.Center) {
                Icon(Icons.Filled.Bolt, null, tint = Blue, modifier = Modifier.size(29.dp))
            }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text("Entrega adicional encontrada", color = Blue, fontFamily = AppFont, fontSize = 13.sp, lineHeight = 15.sp, fontWeight = FontWeight.Bold)
                Text("Adicionar à corrida atual", color = Ink, fontFamily = AppFont, fontSize = 19.sp, lineHeight = 22.sp, fontWeight = FontWeight.Bold)
                Text("Só aceite se ainda estiver na loja ou se o gestor liberou o encaixe.", color = Muted, fontFamily = AppFont, fontSize = 12.sp, lineHeight = 16.sp, fontWeight = FontWeight.SemiBold)
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
            SmallMetric("Adicional", addition.value.ifBlank { DriverRepository.formatCurrency(addition.valueNumber) }, Modifier.weight(1f))
            SmallMetric("Desvio", addition.distance.ifBlank { "baixo" }, Modifier.weight(1f))
            SmallMetric("Tempo", addition.duration.ifBlank { "compatível" }, Modifier.weight(1f))
        }
        RoutePointLine(Icons.Filled.Place, "Nova entrega", addition.dropoff.ifBlank { addition.neighborhood.ifBlank { "Endereço protegido" } }, Blue)
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
            SecondaryButton("Ignorar", color = Muted, modifier = Modifier.weight(1f)) { onReject() }
            PrimaryButton("Adicionar", color = Blue, modifier = Modifier.weight(1.35f)) { onAccept() }
        }
    }
}

@Composable
private fun RouteMicroSummary(ride: DriverRide, payment: PaymentUi) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
        MiniCard(Modifier.weight(1f)) { SmallMetricText("Etapa", ride.stageShort()) }
        MiniCard(Modifier.weight(1f)) { SmallMetricText("Entregas", ride.deliveryCountLabel()) }
        MiniCard(Modifier.weight(1f)) { SmallMetricText("Cobrança", payment.label) }
    }
}

@Composable
private fun RouteTopHeader(ride: DriverRide) {
    val countLabel = ride.deliveryCountLabel()
    val title = ride.missionTitle("Corrida em andamento")
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(Brush.linearGradient(listOf(Color(0xFF08164A), Color(0xFF2A1E8A), Color(0xFF1E4FFF))))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(title, color = Color.White, fontFamily = AppFont, fontSize = 23.sp, lineHeight = 26.sp, fontWeight = FontWeight.Bold)
                Text("$countLabel • ${ride.stageLabel()} • ${ride.referenceLabel()}", color = Color.White.copy(alpha = .78f), fontFamily = AppFont, fontSize = 12.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            Icon(Icons.Filled.MoreHoriz, null, tint = Color.White.copy(alpha = .86f), modifier = Modifier.size(25.dp))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            StatusChip(countLabel, Color.White, Icons.Filled.CheckCircle)
            StatusChip(ride.distance.ifBlank { "distância pendente" }, Color.White, Icons.Filled.Route)
            StatusChip(ride.duration.ifBlank { "tempo pendente" }, Color.White, Icons.Filled.Schedule)
        }
    }
}



@Composable
private fun NextActionCard(ride: DriverRide, action: RouteAction, onMainClick: () -> Unit) {
    PremiumCard {
        Text("Próxima ação", color = Muted, fontFamily = AppFont, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        Text(action.title, color = Ink, fontFamily = AppFont, fontSize = 20.sp, lineHeight = 23.sp, fontWeight = FontWeight.Bold)
        Text(action.message, color = Muted, fontFamily = AppFont, fontSize = 13.sp, lineHeight = 17.sp, fontWeight = FontWeight.SemiBold)
        if (ride.shouldShowReleaseCode()) ReleaseCodeCard(ride)
        PrimaryButton(action.button, icon = null, enabled = action.enabled, color = action.accent, onClick = onMainClick)
    }
}



@Composable
private fun ReleaseCodeCard(ride: DriverRide) {
    Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(26.dp)).background(GreenSoft).border(1.dp, Green.copy(alpha = .22f), RoundedCornerShape(26.dp)).padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Código de retirada", color = GreenDark, fontFamily = AppFont, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Text(ride.releaseCode(), color = Ink, fontFamily = AppFont, fontSize = 44.sp, fontWeight = FontWeight.Bold)
        Text("Mostre no balcão", color = Muted, fontFamily = AppFont, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun CompactStopsCard(ride: DriverRide, isDelivering: Boolean) {
    val orders = ride.displayOrders()
    val total = ride.deliveryCount()
    PremiumCard {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.weight(1f)) {
                Text("Paradas da corrida", color = Ink, fontFamily = AppFont, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(ride.nextStopSummary(), color = Muted, fontFamily = AppFont, fontSize = 12.sp, lineHeight = 15.sp, fontWeight = FontWeight.Bold)
            }
            StatusChip(ride.deliveryCountLabel(), if (total > 1) Navy else Green, Icons.Filled.Route)
        }
        RoutePointLine(Icons.Filled.Storefront, "Coleta na loja", ride.pickup.ifBlank { "Rodrigues Açaí e Cia." }, Green)
        if (total <= 1) {
            RoutePointLine(Icons.Filled.Place, "Entrega 1 de 1", if (isDelivering) ride.dropoff.ifBlank { ride.neighborhood.ifBlank { "Endereço não informado" } } else "Endereço protegido até a saída", Orange)
        } else {
            orders.take(total).forEachIndexed { index, order ->
                val label = "Entrega ${index + 1} de $total"
                val value = order.customerName.ifBlank { order.code.ifBlank { "Cliente ${index + 1}" } }
                RoutePointLine(Icons.Filled.Place, label, value, if (index == 0) Orange else Blue)
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
            SmallMetric("Distância", ride.distance.ifBlank { "pendente" }, Modifier.weight(1f))
            SmallMetric("Tempo", ride.duration.ifBlank { "pendente" }, Modifier.weight(1f))
        }
    }
}

@Composable
private fun RoutePointLine(icon: ImageVector, label: String, value: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(11.dp), modifier = Modifier.fillMaxWidth()) {
        Box(Modifier.size(36.dp).clip(CircleShape).background(color.copy(alpha = .12f)), contentAlignment = Alignment.Center) { Icon(icon, null, tint = color, modifier = Modifier.size(20.dp)) }
        Column(Modifier.weight(1f)) {
            Text(label, color = Muted, fontFamily = AppFont, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            Text(value.ifBlank { "Não informado" }, color = Ink, fontFamily = AppFont, fontSize = 14.sp, fontWeight = FontWeight.Bold, maxLines = 2, overflow = TextOverflow.Ellipsis)
        }
    }
}


@Composable
private fun PaymentChipsCard(payment: PaymentUi) {
    PremiumCard {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Box(Modifier.size(42.dp).clip(CircleShape).background(payment.color.copy(alpha = .12f)), contentAlignment = Alignment.Center) {
                Icon(if (payment.requiresMachine) Icons.Filled.CreditCard else Icons.Filled.Payments, null, tint = payment.color, modifier = Modifier.size(23.dp))
            }
            Column(Modifier.weight(1f)) {
                Text("Pagamento", color = Muted, fontFamily = AppFont, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Text(payment.label, color = Ink, fontFamily = AppFont, fontSize = 17.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            if (payment.amountLabel.isNotBlank()) {
                StatusChip(payment.amountLabel, if (payment.requiresChange) Orange else Green)
            }
        }
        PaymentChips(payment)
    }
}

@Composable
private fun PaymentChips(payment: PaymentUi) {
    Column(verticalArrangement = Arrangement.spacedBy(7.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            StatusChip(payment.label, payment.color, if (payment.requiresMachine) Icons.Filled.CreditCard else Icons.Filled.Payments)
            if (payment.amountLabel.isNotBlank()) StatusChip(payment.amountLabel, if (payment.requiresChange) Orange else Green)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            if (payment.requiresMachine) StatusChip("Levar maquininha", Blue, Icons.Filled.CreditCard)
            if (payment.requiresChange) StatusChip("Conferir troco", Orange, Icons.Filled.Payments)
        }
        if (payment.detail.isNotBlank()) Text(payment.detail, color = Muted, fontFamily = AppFont, fontSize = 12.sp, lineHeight = 16.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun OrdersCompactSection(ride: DriverRide, expanded: Boolean, onToggle: () -> Unit) {
    val waiting = ride.routeOrderCount > 1 && ride.routeReadyCount < ride.routeOrderCount
    PremiumCard {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().clickable { onToggle() }) {
            Column(Modifier.weight(1f)) {
                Text("Entregas da corrida", color = Ink, fontFamily = AppFont, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(if (waiting) "Aguardando loja • ${ride.ordersReadyLabel()}" else ride.deliveryCountLabel(), color = if (waiting) Orange else Muted, fontFamily = AppFont, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
            Icon(Icons.Filled.KeyboardArrowRight, null, tint = Muted)
        }
        val orders = ride.displayOrders()
        if (expanded) orders.forEach { RouteOrderLine(it, ride.paymentUi().label) } else orders.firstOrNull()?.let { RouteOrderLine(it, ride.paymentUi().label) }
        if (waiting) InlineNoticeCard("Saída bloqueada até todas as entregas ficarem prontas e o gestor liberar.", Orange)
    }
}

@Composable
private fun RouteOrderLine(order: RouteOrder, fallbackPayment: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(18.dp)).background(SurfaceSoft).padding(12.dp)) {
        Column(Modifier.weight(1f)) {
            Text("Ref. #${order.code.ifBlank { order.id.takeLast(4).uppercase(Locale.ROOT) }} • ${order.customerName.ifBlank { "Cliente" }}", color = Ink, fontFamily = AppFont, fontSize = 13.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(order.paymentSummary.ifBlank { fallbackPayment }, color = Muted, fontFamily = AppFont, fontSize = 11.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        StatusChip(if (order.terminal) "Cancelado" else if (order.ready) "Pronto" else "Preparo", if (order.terminal) Red else if (order.ready) Green else Orange)
    }
}

@Composable
private fun CollapsibleFinance(ride: DriverRide, expanded: Boolean, onToggle: () -> Unit) {
    PremiumCard {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().clickable { onToggle() }) {
            Text("Financeiro e repasse", color = Ink, fontFamily = AppFont, fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            Icon(Icons.Filled.KeyboardArrowRight, null, tint = Muted)
        }
        if (expanded) {
            FinanceLine("Corrida", ride.value.ifBlank { DriverRepository.formatCurrency(ride.valueNumber) })
            FinanceLine("Pedido", DriverRepository.formatCurrency(ride.clientTotalNumber))
            FinanceLine("Receber cliente", DriverRepository.formatCurrency(ride.amountToCollectNumber))
            FinanceLine("Repassar loja", DriverRepository.formatCurrency(ride.storeReturnNumber))
            FinanceLine("Taxa maquininha", DriverRepository.formatCurrency(ride.machineFeeNumber))
        }
    }
}

@Composable
private fun FinanceLine(label: String, value: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = Muted, fontFamily = AppFont, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Text(value, color = Ink, fontFamily = AppFont, fontSize = 13.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun RouteMapContent(activeRide: DriverRide?, online: Boolean, onBackHome: () -> Unit, onOpenNavigator: (String, String) -> Unit) {
    if (activeRide == null) {
        ScreenScroll {
            BrandHeader(title = "Mapa", subtitle = if (online) "Você está disponível. O mapa acompanha sua posição e a corrida quando houver missão." else "Fique disponível para usar mapa operacional em corrida.", icon = Icons.Filled.Map)
            RealDeliveryMap("Mapa do entregador", if (online) "Disponível" else "Indisponível", "Rodrigues Açaí e Cia Campo Grande MS", "Campo Grande MS", mode = DeliveryMapMode.DRIVER_TO_PICKUP, modifier = Modifier.height(360.dp))
            PrimaryButton("Voltar para início", icon = Icons.Filled.Home, onClick = onBackHome)
        }
        return
    }
    ScreenScroll {
        BrandHeader(title = "Mapa da corrida", subtitle = "${activeRide.deliveryCountLabel()} • ${activeRide.stageLabel()} • ${activeRide.distance.ifBlank { "distância pendente" }}", icon = Icons.Filled.Map)
        RealDeliveryMap(
            title = activeRide.missionTitle("Corrida"),
            subtitle = listOf(activeRide.distance, activeRide.duration).filter { it.isNotBlank() }.joinToString(" • "),
            pickupAddress = activeRide.pickup,
            dropoffAddress = activeRide.dropoff,
            pickupLat = activeRide.pickupLat,
            pickupLng = activeRide.pickupLng,
            dropoffLat = activeRide.dropoffLat,
            dropoffLng = activeRide.dropoffLng,
            mode = if (activeRide.isDeliveringStage()) DeliveryMapMode.DRIVER_TO_DROPOFF else DeliveryMapMode.DRIVER_TO_PICKUP,
            modifier = Modifier.height(380.dp)
        )
        PrimaryButton("Abrir navegação", icon = Icons.Filled.Map) { onOpenNavigator(activeRide.pickup, activeRide.dropoff) }
    }
}

@Composable
private fun EmptyOperationScreen(title: String, message: String, icon: ImageVector, color: Color) {
    ScreenScroll {
        Spacer(Modifier.height(50.dp))
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(15.dp)) {
            Box(Modifier.size(88.dp).clip(CircleShape).background(color.copy(alpha = .12f)), contentAlignment = Alignment.Center) { Icon(icon, null, tint = color, modifier = Modifier.size(44.dp)) }
            Text(title, color = Ink, fontFamily = AppFont, fontSize = 22.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            Text(message, color = Muted, fontFamily = AppFont, fontSize = 14.sp, lineHeight = 20.sp, textAlign = TextAlign.Center)
        }
    }
}

@Composable
private fun WalletContent(profile: DriverProfile, stats: DriverStats, history: List<DriverHistory>, hideValues: Boolean, onToggleValues: () -> Unit) {
    ScreenScroll {
        ScreenHeader("Carteira", "Saldo, ganhos e repasses.", Icons.Filled.AccountBalanceWallet, null)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(26.dp))
                .background(Brush.linearGradient(listOf(Navy, NavyDark)))
                .padding(20.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Saldo disponível", color = Color.White.copy(alpha = .86f), fontFamily = AppFont, fontSize = 13.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                    Box(Modifier.size(38.dp).clip(CircleShape).background(Color.White.copy(alpha = .15f)).clickable { onToggleValues() }, contentAlignment = Alignment.Center) { Icon(if (hideValues) Icons.Filled.VisibilityOff else Icons.Filled.Visibility, null, tint = Color.White) }
                }
                Text(if (hideValues) "••••" else DriverRepository.formatCurrency((stats.valorAReceber ?: 0.0).takeIf { it > 0.0 } ?: stats.totalWeek), color = Color.White, fontFamily = AppFont, fontSize = 39.sp, lineHeight = 42.sp, fontWeight = FontWeight.Bold)
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
            AmountCard("Saldo pendente", stats.valorARepassar ?: 0.0, hideValues, Modifier.weight(1f))
            AmountCard("Total a receber", stats.valorAReceber ?: 0.0, hideValues, Modifier.weight(1f))
        }
        PremiumFinanceDashboard(stats = stats, hideValues = hideValues)
        PremiumCard {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(Modifier.size(52.dp).clip(RoundedCornerShape(16.dp)).background(NavySoft), contentAlignment = Alignment.Center) { Icon(Icons.Filled.Schedule, null, tint = Navy, modifier = Modifier.size(28.dp)) }
                Column(Modifier.weight(1f)) {
                    Text("Próximo repasse", color = Muted, fontFamily = AppFont, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text(stats.proximoRepasseLabel.ifBlank { "A definir pela operação" }, color = Ink, fontFamily = AppFont, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text(stats.proximoRepasseDescricao.ifBlank { "Estimativa de crédito será atualizada." }, color = Muted, fontFamily = AppFont, fontSize = 12.sp, lineHeight = 16.sp)
                }
                Icon(Icons.Filled.KeyboardArrowRight, null, tint = Muted)
            }
        }
        PremiumCard {
            RoutePointLine(Icons.Filled.Payments, "Chave Pix cadastrada", stats.pixKey.ifBlank { profile.pixKey.ifBlank { "Não informada" } }, Green)
            RoutePointLine(Icons.Filled.AccountBalanceWallet, "Banco", stats.bankName.ifBlank { profile.bankName.ifBlank { "Não informado" } }, Blue)
        }
        PremiumCard {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Últimos repasses", color = Ink, fontFamily = AppFont, fontSize = 17.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                Text("Ver todos", color = Navy, fontFamily = AppFont, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
            history.filter { it.action.contains("FINAL", true) || it.action.contains("PAGO", true) }.take(3).forEach { item ->
                FinanceLine(item.createdLabel, item.value.ifBlank { "Pago" })
            }
            if (history.isEmpty()) Text("Nenhum repasse registrado ainda.", color = Muted, fontFamily = AppFont, fontSize = 12.sp)
        }
    }
}


@Composable
private fun PremiumFinanceDashboard(stats: DriverStats, hideValues: Boolean) {
    PremiumCard {
        Text("Resumo financeiro", color = Ink, fontFamily = AppFont, fontSize = 17.sp, fontWeight = FontWeight.Bold)
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
            FinanceMetric("Hoje", if (hideValues) "••••" else DriverRepository.formatCurrency(stats.totalToday), Green, Modifier.weight(1f))
            FinanceMetric("Semana", if (hideValues) "••••" else DriverRepository.formatCurrency(stats.totalWeek), Navy, Modifier.weight(1f))
            FinanceMetric("Corridas", stats.finishedTodayCount.toString(), Blue, Modifier.weight(1f))
        }
    }
}

@Composable
private fun FinanceMetric(label: String, value: String, color: Color, modifier: Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(color.copy(alpha = .09f))
            .border(1.dp, color.copy(alpha = .14f), RoundedCornerShape(18.dp))
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(label, color = Muted, fontFamily = AppFont, fontSize = 10.sp, fontWeight = FontWeight.Bold, maxLines = 1)
        Text(value, color = color, fontFamily = AppFont, fontSize = 13.sp, lineHeight = 15.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
private fun AmountCard(label: String, amount: Double, hide: Boolean, modifier: Modifier) {
    PremiumCard(modifier = modifier) {
        Text(label, color = Muted, fontFamily = AppFont, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        Text(if (hide) "••••" else DriverRepository.formatCurrency(amount), color = Ink, fontFamily = AppFont, fontSize = 19.sp, fontWeight = FontWeight.Bold, maxLines = 1)
    }
}

@Composable
private fun HistoryContent(history: List<DriverHistory>) {
    ScreenScroll {
        BrandHeader(title = "Histórico", subtitle = "Resumo limpo das corridas e ocorrências.", icon = Icons.Filled.History)
        HistoryFilterChips()
        if (history.isEmpty()) {
            EmptyInsideCard("Sem corridas ainda", "Corridas aceitas, recusadas, expiradas e canceladas aparecerão aqui.", Icons.Filled.History)
        } else {
            history.take(60).forEach { HistoryCard(it) }
        }
    }
}


@Composable
private fun HistoryFilterChips() {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
        StatusChip("Todas", Navy)
        StatusChip("Concluídas", Green)
        StatusChip("Ocorrências", Orange)
        StatusChip("Expiradas", Muted)
    }
}

@Composable
private fun HistoryCard(item: DriverHistory) {
    val color = when {
        item.action.contains("CANCEL", true) -> Red
        item.action.contains("RECUS", true) -> Orange
        item.action.contains("EXPIR", true) -> Muted
        item.action.contains("FINAL", true) || item.action.contains("ENTREG", true) -> Green
        item.action.contains("OCOR", true) -> Orange
        else -> Blue
    }
    PremiumCard {
        Row(verticalAlignment = Alignment.CenterVertically) {
            StatusChip(historyLabel(item.action), color)
            Spacer(Modifier.weight(1f))
            Text(item.value.ifBlank { "—" }, color = Ink, fontFamily = AppFont, fontSize = 15.sp, fontWeight = FontWeight.Bold)
        }
        Text("Ref. #${item.rideId.takeLast(6).uppercase(Locale.ROOT)} • ${item.createdLabel}", color = Ink, fontFamily = AppFont, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Text(historySubtitle(item), color = Muted, fontFamily = AppFont, fontSize = 12.sp, fontWeight = FontWeight.Medium, maxLines = 2, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
private fun NotificationsScreen(notices: List<AppNotice>, onBack: () -> Unit) {
    ScreenScroll {
        ScreenHeader("Notificações", "Avisos da operação e alertas do app.", Icons.Filled.Notifications, onBack)
        if (notices.isEmpty()) EmptyInsideCard("Nenhum aviso", "Quando o gestor enviar avisos, eles aparecem aqui.", Icons.Filled.Notifications)
        else notices.sortedBy { it.read }.take(50).forEach { notice ->
            PremiumCard {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    StatusChip(notice.category.ifBlank { "Operação" }, if (notice.priority.uppercase(Locale.ROOT).contains("ALTA")) Red else Green)
                    Spacer(Modifier.weight(1f))
                    Text(notice.createdLabel, color = Muted2, fontFamily = AppFont, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Text(notice.title.ifBlank { "Aviso" }, color = Ink, fontFamily = AppFont, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                Text(notice.message.ifBlank { "Sem descrição." }, color = Muted, fontFamily = AppFont, fontSize = 13.sp, lineHeight = 18.sp)
            }
        }
    }
}

@Composable
private fun MoreContent(
    profile: DriverProfile,
    online: Boolean,
    themeMode: String,
    hideValues: Boolean,
    notices: List<AppNotice>,
    onThemeChanged: (String) -> Unit,
    onToggleValues: () -> Unit,
    onProfileChanged: () -> Unit,
    onOpenNotificationSettings: () -> Unit,
    onOpenLocationSettings: () -> Unit,
    onOpenFullScreenSettings: () -> Unit,
    onOpenBatterySettings: () -> Unit,
    onRequestNotificationPermission: () -> Unit,
    onRequestLocationPermission: () -> Unit,
    onRequestEssentialPermissions: () -> Unit,
    permissionRefreshTick: Int,
    onForceUnlock: () -> Unit,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    var screen by remember { mutableStateOf("menu") }
    when (screen) {
        "perfil" -> ProfileScreen(profile, online, onBack = { screen = "menu" })
        "pix" -> PixBankScreen(profile, onBack = { screen = "menu" }, onSaved = onProfileChanged)
        "operacao" -> OperationPreferencesScreen(onBack = { screen = "menu" })
        "permissoes" -> PermissionsSettingsScreen(
            permissionRefreshTick = permissionRefreshTick,
            onBack = { screen = "menu" },
            onRequestNotificationPermission = onRequestNotificationPermission,
            onRequestLocationPermission = onRequestLocationPermission,
            onRequestEssentialPermissions = onRequestEssentialPermissions,
            onOpenNotificationSettings = onOpenNotificationSettings,
            onOpenLocationSettings = onOpenLocationSettings,
            onOpenFullScreenSettings = onOpenFullScreenSettings,
            onOpenBatterySettings = onOpenBatterySettings
        )
        "suporte" -> SupportScreen(onBack = { screen = "menu" }, onForceUnlock = onForceUnlock)
        else -> ScreenScroll {
            BrandHeader(title = "Mais", subtitle = "Conta, repasse, operação e suporte.", icon = Icons.Filled.MoreHoriz)
            PremiumCard {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Avatar(profile.name, profile.photoUrl, 58)
                    Column(Modifier.weight(1f)) {
                        Text(profile.name, color = Ink, fontFamily = AppFont, fontSize = 18.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Text(if (online) "Disponível" else "Indisponível", color = if (online) Green else Muted, fontFamily = AppFont, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    ProfileStat("4,9", "Avaliação", Green, Modifier.weight(1f))
                    ProfileStat("98%", "Aceitação", Navy, Modifier.weight(1f))
                    ProfileStat("Elite", "Nível", Orange, Modifier.weight(1f))
                }
            }
            MenuTile("Perfil", "Dados principais da conta", Icons.Filled.Person) { screen = "perfil" }
            MenuTile("Pix/Banco", "Pix, banco e repasse", Icons.Filled.Payments) { screen = "pix" }
            MenuTile("Preferências de operação", "Pagamento e disponibilidade", Icons.Filled.TwoWheeler) { screen = "operacao" }
            MenuTile("Permissões", "Alertas, GPS e bateria", Icons.Filled.Shield) { screen = "permissoes" }
            MenuTile("Suporte e destravar", "Destravar corrida e suporte", Icons.Filled.SupportAgent) { screen = "suporte" }
            PremiumCard {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text("Tema do app", color = Ink, fontFamily = AppFont, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        Text("Claro como padrão, escuro neon quando ativado", color = Muted, fontFamily = AppFont, fontSize = 12.sp)
                    }
                    Switch(
                        checked = themeMode == AppSettings.THEME_DARK,
                        onCheckedChange = { checked -> onThemeChanged(if (checked) AppSettings.THEME_DARK else AppSettings.THEME_LIGHT) }
                    )
                }
                StatusChip(AppSettings.themeLabel(themeMode), if (themeMode == AppSettings.THEME_DARK) Blue else Green)
            }
            PremiumCard {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text("Ocultar valores", color = Ink, fontFamily = AppFont, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        Text("Mostra ou esconde ganhos e repasses", color = Muted, fontFamily = AppFont, fontSize = 12.sp)
                    }
                    Switch(checked = hideValues, onCheckedChange = { onToggleValues() })
                }
                Text(AppVersion.LABEL, color = Muted2, fontFamily = AppFont, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
            SecondaryButton("Sair do app", icon = Icons.Filled.ArrowBack, color = Red, onClick = onLogout)
        }
    }
}


@Composable
private fun ProfileStat(value: String, label: String, color: Color, modifier: Modifier) {
    Column(
        modifier = modifier.clip(RoundedCornerShape(16.dp)).background(color.copy(alpha = .09f)).padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(value, color = color, fontFamily = AppFont, fontSize = 15.sp, fontWeight = FontWeight.Bold)
        Text(label, color = Muted, fontFamily = AppFont, fontSize = 10.sp, fontWeight = FontWeight.Medium, maxLines = 1)
    }
}

@Composable
private fun MenuTile(title: String, subtitle: String, icon: ImageVector, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .shadow(3.dp, RoundedCornerShape(22.dp), clip = false, ambientColor = Color(0x12000000), spotColor = Color(0x10000000))
            .clip(RoundedCornerShape(22.dp))
            .background(Surface)
            .border(1.dp, Border, RoundedCornerShape(22.dp))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 12.dp)
    ) {
        Box(Modifier.size(42.dp).clip(CircleShape).background(NavySoft), contentAlignment = Alignment.Center) { Icon(icon, null, tint = Navy, modifier = Modifier.size(22.dp)) }
        Column(Modifier.weight(1f)) {
            Text(title, color = Ink, fontFamily = AppFont, fontSize = 16.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(subtitle, color = Muted, fontFamily = AppFont, fontSize = 12.sp, fontWeight = FontWeight.Medium, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        Icon(Icons.Filled.KeyboardArrowRight, null, tint = Muted2, modifier = Modifier.size(21.dp))
    }
}

@Composable
private fun ScreenHeader(title: String, subtitle: String, icon: ImageVector, onBack: (() -> Unit)? = null) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        if (onBack != null) Box(Modifier.size(42.dp).clip(CircleShape).background(SurfaceSoft).border(1.dp, Border, CircleShape).clickable { onBack() }, contentAlignment = Alignment.Center) { Icon(Icons.Filled.ArrowBack, null, tint = Ink) }
        Column(Modifier.weight(1f)) { BrandHeader(title, subtitle, icon) }
    }
}

@Composable
private fun ProfileScreen(profile: DriverProfile, online: Boolean, onBack: () -> Unit) {
    ScreenScroll {
        ScreenHeader("Perfil", "Dados principais do entregador.", Icons.Filled.Person, onBack)
        PremiumCard {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Avatar(profile.name, profile.photoUrl, 94)
                Text(profile.name, color = Ink, fontFamily = AppFont, fontSize = 21.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                StatusChip(if (online) "Disponível" else "Indisponível", if (online) Green else Muted)
            }
        }
        PremiumCard {
            RoutePointLine(Icons.Filled.TwoWheeler, "Veículo", profile.vehicle.ifBlank { "Moto" }, Green)
            RoutePointLine(Icons.Filled.ChatBubbleOutline, "Telefone", profile.phone.ifBlank { "Não informado" }, Blue)
            RoutePointLine(Icons.Filled.Place, "Cidade", profile.city.ifBlank { "Campo Grande - MS" }, Orange)
        }
    }
}

@Composable
private fun PixBankScreen(profile: DriverProfile, onBack: () -> Unit, onSaved: () -> Unit) {
    val context = LocalContext.current
    var pix by remember { mutableStateOf(profile.pixKey) }
    var bank by remember { mutableStateOf(profile.bankName) }
    var payoutType by remember { mutableStateOf("Pix") }
    var message by remember { mutableStateOf("") }
    ScreenScroll {
        ScreenHeader("Pix/Banco", "Recebimento do entregador.", Icons.Filled.Payments, onBack)
        if (message.isNotBlank()) InlineNoticeCard(message, if (message.contains("salv", true)) Green else Red)
        PremiumCard {
            Field("Chave Pix", pix, { pix = it }, "CPF, telefone, e-mail ou aleatória")
            Field("Banco", bank, { bank = it }, "Nome do banco")
            Field("Tipo de repasse", payoutType, { payoutType = it }, "Pix")
            Text("A conta deve estar no nome do titular aprovado.", color = Muted, fontFamily = AppFont, fontSize = 12.sp, lineHeight = 17.sp)
            PrimaryButton("Salvar recebimento", icon = Icons.Filled.CheckCircle) {
                DriverRepository.updatePayoutData(context, pix, bank, payoutType, onSuccess = { message = "Dados salvos para conferência."; onSaved() }, onError = { message = it })
            }
        }
    }
}

@Composable
private fun OperationPreferencesScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    var prefs by remember { mutableStateOf(DriverRepository.loadOperationalPreferences(context)) }
    fun save(newPrefs: DriverOperationalPreferences) { prefs = newPrefs; DriverRepository.saveOperationalPreferences(context, newPrefs) }
    ScreenScroll {
        ScreenHeader("Operação", "Preferências usadas no despacho da corrida.", Icons.Filled.TwoWheeler, onBack)
        PreferenceSwitch("Tenho maquininha", "Permite receber pedido no cartão.", prefs.hasMachine) { save(prefs.copy(hasMachine = it)) }
        PreferenceSwitch("Aceito débito", "Disponível se você usa maquininha.", prefs.acceptsDebit) { save(prefs.copy(acceptsDebit = it)) }
        PreferenceSwitch("Aceito crédito", "Disponível se você usa maquininha.", prefs.acceptsCredit) { save(prefs.copy(acceptsCredit = it)) }
        PreferenceSwitch("Aceito parcelado/ticket", "Usado quando o gestor precisar filtrar corridas.", prefs.acceptsInstallment || prefs.acceptsTicket) { save(prefs.copy(acceptsInstallment = it, acceptsTicket = it)) }
        PreferenceSwitch("Tenho troco", "Informe se pode receber dinheiro.", prefs.hasCashChange) { save(prefs.copy(hasCashChange = it)) }
        PreferenceSwitch("Somente pago online", "Bloqueia dinheiro/maquininha para você.", prefs.onlyOnlinePaid) { save(prefs.copy(onlyOnlinePaid = it)) }
        PreferenceSwitch("Bloquear dinheiro à noite", "Evita pedidos em dinheiro no horário configurado.", prefs.blockCashAtNight) { save(prefs.copy(blockCashAtNight = it)) }
        PreferenceSwitch("Bloquear maquininha à noite", "Evita cartão/maquininha no horário configurado.", prefs.blockMachineAtNight) { save(prefs.copy(blockMachineAtNight = it)) }
    }
}

@Composable
private fun PreferenceSwitch(title: String, subtitle: String, checked: Boolean, onChecked: (Boolean) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(Surface)
            .border(1.dp, Border, RoundedCornerShape(22.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Column(Modifier.weight(1f)) {
            Text(title, color = Ink, fontFamily = AppFont, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text(subtitle, color = Muted, fontFamily = AppFont, fontSize = 12.sp, lineHeight = 16.sp, fontWeight = FontWeight.Medium)
        }
        Switch(checked = checked, onCheckedChange = onChecked)
    }
}

@Composable
private fun PermissionsSettingsScreen(
    permissionRefreshTick: Int,
    onBack: () -> Unit,
    onRequestNotificationPermission: () -> Unit,
    onRequestLocationPermission: () -> Unit,
    onRequestEssentialPermissions: () -> Unit,
    onOpenNotificationSettings: () -> Unit,
    onOpenLocationSettings: () -> Unit,
    onOpenFullScreenSettings: () -> Unit,
    onOpenBatterySettings: () -> Unit
) {
    val context = LocalContext.current
    var status by remember { mutableStateOf(PermissionStatusReader.read(context)) }
    LaunchedEffect(permissionRefreshTick) { status = PermissionStatusReader.read(context) }
    ScreenScroll {
        ScreenHeader("Permissões do app", "Toque nos itens para abrir a pergunta do Android quando existir.", Icons.Filled.Shield, onBack)
        PermissionProgressHero(status, context, onRequestEssentialPermissions)
        PermissionSetupCard("Notificações", "Receber corrida e avisos do gestor.", status.notifications, Icons.Filled.NotificationsActive, onRequestNotificationPermission)
        PermissionSetupCard("Localização", "Mapa e acompanhamento.", status.location, Icons.Filled.MyLocation, onRequestLocationPermission)
        PermissionSetupCard("Alerta urgente", "Tela cheia para oferta importante.", status.fullScreenIntent, Icons.Filled.Bolt, onOpenFullScreenSettings)
        PermissionSetupCard("Bateria", "Evita o app morrer em segundo plano.", status.batteryUnrestricted, Icons.Filled.Shield, onOpenBatterySettings)
        PermissionSetupCard("Internet/GPS", "Conexão e localização do aparelho.", hasInternet(context) && isGpsEnabled(context), Icons.Filled.Map, onOpenLocationSettings)
        SecondaryButton("Abrir configurações de notificações", icon = Icons.Filled.Notifications, color = Muted, onClick = onOpenNotificationSettings)
    }
}

@Composable
private fun SupportScreen(onBack: () -> Unit, onForceUnlock: () -> Unit) {
    ScreenScroll {
        ScreenHeader("Suporte", "Ações de segurança para operação real.", Icons.Filled.SupportAgent, onBack)
        PremiumCard {
            Text("Destravar operação", color = Ink, fontFamily = AppFont, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text("Use se o gestor cancelou/finalizou uma corrida e o app ficou preso nela.", color = Muted, fontFamily = AppFont, fontSize = 13.sp, lineHeight = 18.sp)
            SecondaryButton("Destravar operação", icon = Icons.Filled.Shield, color = Orange, onClick = onForceUnlock)
        }
        PremiumCard {
            Text("Versão", color = Ink, fontFamily = AppFont, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(AppVersion.LABEL, color = Muted, fontFamily = AppFont, fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun OccurrenceDialog(onClose: () -> Unit, onConfirm: (String, String) -> Unit) {
    var selected by remember { mutableStateOf("Loja demorando") }
    var details by remember { mutableStateOf("") }
    val reasons = listOf("Problema no veículo", "Loja demorando", "Pedido não encontrado", "Cliente não atende", "Endereço divergente", "Pagamento com problema", "Local inseguro", "Outro")
    AlertDialog(
        onDismissRequest = onClose,
        title = { Text("Problema na corrida", color = Ink, fontFamily = AppFont, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(9.dp)) {
                reasons.forEach { reason ->
                    Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(18.dp)).background(if (selected == reason) OrangeSoft else SurfaceSoft).clickable { selected = reason }.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(reason, color = if (selected == reason) Orange else Ink, fontFamily = AppFont, fontSize = 13.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                        if (selected == reason) Icon(Icons.Filled.CheckCircle, null, tint = Orange, modifier = Modifier.size(18.dp))
                    }
                }
                OutlinedTextField(value = details, onValueChange = { details = it }, label = { Text("Detalhes opcionais") }, shape = RoundedCornerShape(18.dp), modifier = Modifier.fillMaxWidth())
            }
        },
        confirmButton = { TextButton(onClick = { onConfirm(selected, details.ifBlank { "Ocorrência enviada pelo app do entregador." }) }) { Text("Enviar", color = Orange, fontFamily = AppFont, fontWeight = FontWeight.Bold) } },
        dismissButton = { TextButton(onClick = onClose) { Text("Cancelar", color = Muted, fontFamily = AppFont, fontWeight = FontWeight.Bold) } },
        containerColor = Color.White,
        shape = RoundedCornerShape(28.dp)
    )
}

@Composable
private fun RejectDialog(onClose: () -> Unit, onConfirm: (String) -> Unit) {
    var selected by remember { mutableStateOf("Não consigo atender agora") }
    val reasons = listOf("Não consigo atender agora", "Muito longe", "Sem maquininha", "Sem troco", "Problema no veículo", "Outro")
    AlertDialog(
        onDismissRequest = onClose,
        title = { Text("Recusar corrida", color = Ink, fontFamily = AppFont, fontWeight = FontWeight.Bold) },
        text = { Column(verticalArrangement = Arrangement.spacedBy(8.dp)) { reasons.forEach { Text(it, color = if (selected == it) Red else Ink, fontFamily = AppFont, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(if (selected == it) RedSoft else SurfaceSoft).clickable { selected = it }.padding(12.dp)) } } },
        confirmButton = { TextButton(onClick = { onConfirm(selected) }) { Text("Recusar", color = Red, fontFamily = AppFont, fontWeight = FontWeight.Bold) } },
        dismissButton = { TextButton(onClick = onClose) { Text("Voltar", color = Muted, fontFamily = AppFont, fontWeight = FontWeight.Bold) } },
        containerColor = Color.White,
        shape = RoundedCornerShape(28.dp)
    )
}

@Composable
private fun PaymentConfirmDialog(ride: DriverRide, machines: List<PaymentMachine>, onClose: () -> Unit, onConfirm: (PaymentSettlementInput) -> Unit) {
    var method by remember { mutableStateOf(ride.paymentUi().label) }
    var transaction by remember { mutableStateOf(if (ride.requiresMachine) "Débito" else "") }
    val machine = machines.firstOrNull { it.active }
    AlertDialog(
        onDismissRequest = onClose,
        title = { Text("Finalizar entrega", color = Ink, fontFamily = AppFont, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Confirme o recebimento antes de finalizar.", color = Muted, fontFamily = AppFont, fontSize = 13.sp)
                listOf("Pago online", "Dinheiro", "Pix", "Maquininha", "Não informado").forEach { opt ->
                    Text(opt, color = if (method == opt) Green else Ink, fontFamily = AppFont, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(if (method == opt) GreenSoft else SurfaceSoft).clickable { method = opt }.padding(12.dp))
                }
                if (method == "Maquininha") {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { listOf("Débito", "Crédito", "Parcelado", "Ticket").forEach { opt -> StatusChip(opt, if (transaction == opt) Green else Muted) } }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onConfirm(PaymentSettlementInput(
                    rideId = ride.id,
                    orderTotal = ride.clientTotalNumber.takeIf { it > 0.0 } ?: ride.amountToCollectNumber,
                    driverFee = ride.valueNumber,
                    paymentMethod = method,
                    transactionType = transaction,
                    machineId = machine?.id ?: "",
                    machineName = machine?.name ?: "",
                    receivedByDriver = method != "Pago online",
                    receivedBy = if (method == "Pago online") "SISTEMA" else "ENTREGADOR",
                    note = "Confirmado no app entregador"
                ))
            }) { Text("Confirmar", color = Green, fontFamily = AppFont, fontWeight = FontWeight.Bold) }
        },
        dismissButton = { TextButton(onClick = onClose) { Text("Voltar", color = Muted, fontFamily = AppFont, fontWeight = FontWeight.Bold) } },
        containerColor = Color.White,
        shape = RoundedCornerShape(28.dp)
    )
}

@Composable
private fun EmptyInsideCard(title: String, message: String, icon: ImageVector) {
    PremiumCard {
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(Modifier.size(72.dp).clip(CircleShape).background(GreenSoft), contentAlignment = Alignment.Center) { Icon(icon, null, tint = Green, modifier = Modifier.size(34.dp)) }
            Text(title, color = Ink, fontFamily = AppFont, fontSize = 19.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            Text(message, color = Muted, fontFamily = AppFont, fontSize = 13.sp, lineHeight = 18.sp, textAlign = TextAlign.Center)
        }
    }
}

@Composable
private fun Avatar(name: String, photoUrl: String = "", size: Int) {
    Box(Modifier.size(size.dp).clip(CircleShape).background(GreenSoft).border(2.dp, Color.White, CircleShape), contentAlignment = Alignment.Center) {
        if (photoUrl.isNotBlank()) AsyncImage(model = photoUrl, contentDescription = name, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
        else Text(name.trim().firstOrNull()?.uppercaseChar()?.toString() ?: "E", color = Green, fontFamily = AppFont, fontSize = (size / 2.4).sp, fontWeight = FontWeight.Bold)
    }
}

private fun operationalStatus(online: Boolean, activeRide: DriverRide?, permissions: PermissionStatus, context: Context): OperationalStatus {
    val batteryLow = currentBattery(context) in 1..9
    val netOk = hasInternet(context)
    val gpsOk = isGpsEnabled(context)
    return when {
        activeRide != null -> {
            val issue = activeRide.stageLabel().contains("ocorr", ignoreCase = true) || activeRide.status.contains("OCOR", ignoreCase = true) || activeRide.rawStatus.contains("OCOR", ignoreCase = true)
            if (issue) OperationalStatus(AvailabilityKind.EmEntrega, "Ocorrência na corrida", "Aguardando orientação do gestor", Orange, Color.White, false)
            else OperationalStatus(AvailabilityKind.EmEntrega, "Corrida em andamento", "${activeRide.deliveryCountLabel()} • ${activeRide.stageLabel()}", Green, Color.White, false)
        }
        !netOk -> OperationalStatus(AvailabilityKind.Restricao, "Sem conexão", "Operação limitada", Color(0xFF50647A), Color.White, false)
        batteryLow -> OperationalStatus(AvailabilityKind.Restricao, "Bateria baixa", "Carregue o aparelho para operar.", Orange, Color.White, false)
        !permissions.notifications -> OperationalStatus(AvailabilityKind.Restricao, "Liberar permissões", "Notificações precisam ser ativadas.", Red, Color.White, false)
        !permissions.location || !gpsOk -> OperationalStatus(AvailabilityKind.Restricao, "Liberar permissões", "GPS e localização precisam ser ativados.", Red, Color.White, false)
        !permissions.fullScreenIntent -> OperationalStatus(AvailabilityKind.Restricao, "Liberar alerta urgente", "Ative a tela cheia para novas ofertas.", Red, Color.White, false)
        !permissions.batteryUnrestricted -> OperationalStatus(AvailabilityKind.Restricao, "Liberar bateria", "Remova restrição de bateria do app.", Orange, Color.White, false)
        online -> OperationalStatus(AvailabilityKind.Disponivel, "Disponível", "Aguardando corridas", Green, Color.White, true)
        else -> OperationalStatus(AvailabilityKind.Indisponivel, "Ficar disponível", "Ative para receber corridas", Ink, Color.White, true)
    }
}

private fun DriverProfile.firstName(): String = name.trim().split(" ").firstOrNull().orEmpty().ifBlank { "Entregador" }
private fun DriverRide.routeCode(): String = routeReleaseCode.ifBlank { orderCode.ifBlank { routeId.ifBlank { id }.takeLast(4).uppercase(Locale.ROOT) } }
private fun DriverRide.releaseCode(): String = routeReleaseCode.ifBlank { orderCode.ifBlank { id.takeLast(4).uppercase(Locale.ROOT) } }
private fun DriverRide.stageText(): String = listOf(status, rawStatus, pickupReleaseStatus).joinToString(" ").uppercase(Locale.ROOT)
private fun DriverRide.isOccurrence(): Boolean = stageText().contains("OCORRENCIA") || stageText().contains("OCORRÊNCIA")
private fun DriverRide.isDeliveringStage(): Boolean = stageText().let { it.contains("ENTREGA") || it.contains("CLIENTE") || it.contains("ROTA") || it.contains("SAIU") || it.contains("COM_ENTREGADOR") }
private fun DriverRide.isAtPickup(): Boolean = stageText().let { it.contains("NA_COLETA") || it.contains("COLETA") || it.contains("AGUARDANDO_SAIDA") || it.contains("RETIRADA") }
private fun DriverRide.hasPickupRelease(): Boolean = pickupReleaseAllowed || pickupReleaseStatus.uppercase(Locale.ROOT).let { it.contains("LIBER") || it.contains("COM_ENTREGADOR") || it.contains("SAIU") }
private fun DriverRide.shouldShowReleaseCode(): Boolean = isAtPickup() && !isDeliveringStage()
private fun DriverRide.stageLabel(): String = when {
    isOccurrence() -> "Ocorrência"
    stageText().contains("ENTREGADOR_NO_LOCAL") -> "No cliente"
    isDeliveringStage() -> "Em entrega"
    isAtPickup() && !hasPickupRelease() -> "Aguardando saída"
    isAtPickup() -> "Na coleta"
    stageText().contains("ACEIT") -> "Indo para coleta"
    else -> "Indo para coleta"
}
private fun DriverRide.stageShort(): String = when {
    isOccurrence() -> "Aguardando gestor"
    stageText().contains("ENTREGADOR_NO_LOCAL") -> "No local"
    isDeliveringStage() -> "Em entrega"
    isAtPickup() && !hasPickupRelease() -> "Saída pendente"
    isAtPickup() -> "Na coleta"
    else -> "A caminho"
}
private fun DriverRide.stageColor(): Color = when {
    isOccurrence() -> Orange
    isDeliveringStage() -> Blue
    isAtPickup() -> Green
    else -> Green
}
private fun DriverRide.deliveryCount(): Int {
    return listOf(routeOrderCount, routeOrders.size, 1).filter { it > 0 }.maxOrNull() ?: 1
}
private fun DriverRide.deliveryCountLabel(): String {
    val count = deliveryCount()
    return "$count ${if (count == 1) "entrega" else "entregas"}"
}
private fun DriverRide.missionTitle(base: String = "Corrida"): String = when {
    deliveryCount() > 1 && base.contains("em andamento", ignoreCase = true) -> "Corrida agrupada"
    deliveryCount() > 1 -> "$base agrupada"
    else -> base
}
private fun DriverRide.referenceLabel(): String = "Ref. #${routeCode()}"
private fun DriverRide.ordersReadyLabel(): String = if (deliveryCount() > 1) "$routeReadyCount de ${deliveryCount()} prontos" else "1 pronto"
private fun DriverRide.nextStopSummary(): String = when {
    isOccurrence() -> "Ocorrência aberta • aguarde o gestor"
    stageText().contains("ENTREGADOR_NO_LOCAL") -> "Próxima ação: finalizar entrega"
    isDeliveringStage() -> "Próxima parada: Entrega 1 de ${deliveryCount()}"
    isAtPickup() && !hasPickupRelease() -> "Próxima parada: coleta na loja • saída aguardando gestor"
    isAtPickup() && hasPickupRelease() -> "Saída liberada • iniciar entrega"
    else -> "Próxima parada: coleta na loja"
}
private fun DriverRide.displayOrders(): List<RouteOrder> = routeOrders.takeIf { it.isNotEmpty() } ?: listOf(RouteOrder(id = id, code = orderCode, customerName = customerName, status = status, paymentSummary = paymentUi().label, ready = true, requiresMachine = requiresMachine, requiresChange = changeForNumber > 0.0))

private fun DriverRide.nextAction(): RouteAction {
    return when {
        isOccurrence() -> RouteAction("Ocorrência enviada", "Aguarde o gestor resolver antes de continuar.", "Aguardando gestor", false, "OCORRENCIA", Orange)
        stageText().contains("ENTREGADOR_NO_LOCAL") -> RouteAction("Finalizar entrega", "Confirme o recebimento/código antes de encerrar.", "Finalizar entrega", true, "FINALIZADA", Green, needsPaymentConfirmation = true)
        isDeliveringStage() -> RouteAction("Próxima ação", "Siga até o cliente e confirme quando chegar.", "Cheguei no cliente", true, "ENTREGADOR_NO_LOCAL", Blue)
        isAtPickup() && !hasPickupRelease() -> RouteAction("Retirada na loja", "Mostre o código e aguarde a saída do gestor.", "Aguardando saída", false, "AGUARDANDO_SAIDA_GESTOR", Orange)
        isAtPickup() && hasPickupRelease() -> RouteAction("Saída liberada", "A corrida foi liberada. Inicie a entrega.", "Iniciar entrega", true, "EM_ENTREGA", Green)
        else -> RouteAction("Próxima ação", "Vá até a loja e confirme sua chegada.", "Cheguei na coleta", true, "NA_COLETA", Green)
    }
}

private fun DriverRide.paymentUi(): PaymentUi {
    val method = paymentMethod.uppercase(Locale.ROOT)
    val statusUp = paymentStatus.uppercase(Locale.ROOT)
    val kind = paymentKind()
    return when {
        statusUp.contains("PAGO") || kind == "ONLINE" -> PaymentUi("Pago online", "Nada a cobrar do cliente.", Green, "Nada a cobrar", false, false)
        kind == "DINHEIRO" -> PaymentUi("Dinheiro", if (changeForNumber > 0.0) "Troco para ${DriverRepository.formatCurrency(changeForNumber)}." else "Receber na entrega.", Orange, DriverRepository.formatCurrency(amountToCollectNumber.takeIf { it > 0.0 } ?: clientTotalNumber), false, changeForNumber > 0.0)
        kind == "MAQUININHA" -> PaymentUi("Maquininha", "Receber no cartão/maquininha.", Blue, DriverRepository.formatCurrency(amountToCollectNumber.takeIf { it > 0.0 } ?: clientTotalNumber), true, false)
        kind == "PIX" -> PaymentUi("Pix na entrega", "Confirmar recebimento do Pix.", Green, DriverRepository.formatCurrency(amountToCollectNumber.takeIf { it > 0.0 } ?: clientTotalNumber), false, false)
        method.isBlank() && statusUp.isBlank() -> PaymentUi("Não informado", "Confirme com o gestor antes de finalizar.", Orange, "Confirmar", false, false)
        else -> PaymentUi(paymentMethod.ifBlank { paymentStatus.ifBlank { "Não informado" } }, "Confirme o pagamento com a operação.", Orange, DriverRepository.formatCurrency(amountToCollectNumber.takeIf { it > 0.0 } ?: clientTotalNumber), requiresMachine, changeForNumber > 0.0)
    }
}

private fun historyLabel(action: String): String {
    val raw = action.trim()
    val up = raw.uppercase(Locale.ROOT)
    return when {
        up.contains("CANCEL") -> "Cancelada"
        up.contains("RECUS") -> "Recusada"
        up.contains("EXPIR") -> "Expirada"
        up.contains("OCOR") -> "Ocorrência"
        up.contains("FINAL") || up.contains("ENTREG") -> "Finalizada"
        up.contains("COLET") || up.contains("NA_COLETA") -> "Na coleta"
        up.contains("ROTA") || up.contains("EM_ENTREGA") || up.contains("SAIU") -> "Em entrega"
        up.contains("ACEIT") -> "Aceita"
        up.isBlank() -> "Registro"
        else -> raw.replace("_", " ").lowercase(Locale.ROOT).replaceFirstChar { it.uppercase() }
    }
}

private fun historySubtitle(item: DriverHistory): String {
    val parts = listOf(item.pickup, item.neighborhood.ifBlank { item.dropoff }, item.reason, item.paymentMethod)
        .map { it.trim() }
        .filter { it.isNotBlank() }
        .filterNot { it.contains("android_native", true) || it.matches(Regex(""".*v\d+_\d+.*""", RegexOption.IGNORE_CASE)) }
        .distinct()
    return parts.take(2).joinToString(" → ").ifBlank { "Registro operacional da corrida" }
}

private fun onlyDigits(value: String): String = value.filter { it.isDigit() }
private fun moneyMaybe(value: Double?, hide: Boolean): String = if (hide) "••••" else DriverRepository.formatCurrency(value ?: 0.0)
private fun hasInternet(context: Context): Boolean = runCatching {
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val net = cm.activeNetwork ?: return false
    val caps = cm.getNetworkCapabilities(net) ?: return false
    caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}.getOrDefault(false)
private fun isGpsEnabled(context: Context): Boolean = runCatching { (context.getSystemService(Context.LOCATION_SERVICE) as LocationManager).isProviderEnabled(LocationManager.GPS_PROVIDER) }.getOrDefault(false)
private fun currentBattery(context: Context): Int = runCatching { (context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager).getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY) }.getOrDefault(100)
