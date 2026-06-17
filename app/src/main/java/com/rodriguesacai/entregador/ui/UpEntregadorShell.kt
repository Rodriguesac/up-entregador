package com.rodriguesacai.entregador.ui

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rodriguesacai.entregador.AppVersion
import com.rodriguesacai.entregador.R
import com.rodriguesacai.entregador.data.DriverProfile
import com.rodriguesacai.entregador.data.DriverRegistrationRequest
import com.rodriguesacai.entregador.data.DriverRepository

private enum class UpAuthStep { Welcome, Login, Register, Analysis }

@Composable
fun UpEntregadorShell(
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
    var step by remember { mutableStateOf(if (profile == null) UpAuthStep.Welcome else UpAuthStep.Login) }

    if (profile != null && profile!!.needsPasswordSetup) {
        UpCreatePasswordScreen(
            profile = profile!!,
            onSaved = { profile = profile!!.copy(needsPasswordSetup = false) },
            onLogout = { DriverRepository.logout(context) { profile = null; step = UpAuthStep.Login } }
        )
        return
    }

    if (profile != null) {
        DriverHomeScreen(
            permissionRefreshTick = permissionRefreshTick,
            onGoOnline = onGoOnline,
            onGoOffline = onGoOffline,
            onOpenNavigator = onOpenNavigator,
            onOpenNotificationSettings = onOpenNotificationSettings,
            onOpenLocationSettings = onOpenLocationSettings,
            onOpenFullScreenSettings = onOpenFullScreenSettings,
            onOpenBatterySettings = onOpenBatterySettings,
            onRequestNotificationPermission = onRequestNotificationPermission,
            onRequestLocationPermission = onRequestLocationPermission,
            onRequestEssentialPermissions = onRequestEssentialPermissions,
            onThemeModeChanged = onThemeModeChanged
        )
        return
    }

    when (step) {
        UpAuthStep.Welcome -> UpWelcomeScreen(
            onStart = { step = UpAuthStep.Login },
            onRegister = { step = UpAuthStep.Register }
        )
        UpAuthStep.Login -> UpLoginScreen(
            onLoginOk = { driver -> profile = driver },
            onRegister = { step = UpAuthStep.Register }
        )
        UpAuthStep.Register -> UpRegisterScreen(
            onBack = { step = UpAuthStep.Login },
            onSent = { step = UpAuthStep.Analysis }
        )
        UpAuthStep.Analysis -> UpAnalysisScreen(onBackLogin = { step = UpAuthStep.Login })
    }
}

@Composable
private fun UpWelcomeScreen(onStart: () -> Unit, onRegister: () -> Unit) {
    UpAuthSurface {
        UpBrandHeader(title = "UP Entregas", subtitle = "Entregador")
        Spacer(Modifier.height(18.dp))
        Text("Seja bem-vindo ao UP Entregas", color = Color.White, fontSize = 31.sp, fontWeight = FontWeight.Black, lineHeight = 35.sp)
        Spacer(Modifier.height(8.dp))
        Text("Entre com CPF e senha para continuar fazendo entregas.", color = Color(0xFFD9E2FF), fontSize = 16.sp, lineHeight = 22.sp)
        Spacer(Modifier.height(26.dp))
        UpPrimaryButton(text = "Começar", onClick = onStart)
        Spacer(Modifier.height(10.dp))
        UpSecondaryButton(text = "Novo por aqui? Solicitar cadastro", onClick = onRegister)
        Spacer(Modifier.height(18.dp))
        Text(AppVersion.LOGIN_LABEL, color = Color(0xFFB7E51E), fontSize = 13.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun UpLoginScreen(onLoginOk: (DriverProfile) -> Unit, onRegister: () -> Unit) {
    val context = LocalContext.current
    var cpf by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordTaps by remember { mutableStateOf(0) }
    var loading by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }

    UpAuthSurface {
        UpBrandHeader(title = "UP Entregas", subtitle = "Login do entregador")
        Spacer(Modifier.height(18.dp))
        UpAuthCard {
            Text("Acessar conta", color = UpInk, fontSize = 24.sp, fontWeight = FontWeight.Black)
            Text("Login somente com CPF e senha do app.", color = UpMuted, fontSize = 14.sp)
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = cpf,
                onValueChange = { cpf = maskCpf(it) },
                label = { Text("CPF") },
                placeholder = { Text("000.000.000-00") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp)
            )
            Spacer(Modifier.height(12.dp))
            UpPasswordBox(count = passwordTaps, label = "Senha do app")
            Spacer(Modifier.height(8.dp))
            UpPairKeyboard(
                onAppend = { token -> password += token; passwordTaps++ },
                onBackspace = { if (passwordTaps > 0) { password = password.dropLast(1); passwordTaps-- } },
                onClear = { password = ""; passwordTaps = 0 }
            )
            if (message.isNotBlank()) {
                Spacer(Modifier.height(10.dp))
                Text(message, color = Color(0xFFD32F2F), fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(16.dp))
            UpPrimaryButton(text = if (loading) "Entrando..." else "Entrar") {
                val cpfDigits = cpf.onlyDigitsLocal()
                when {
                    !isValidCpf(cpfDigits) -> message = "CPF inválido. Confira os números digitados."
                    passwordTaps < 7 -> message = "Senha precisa ter no mínimo 7 toques."
                    else -> {
                        loading = true
                        message = ""
                        DriverRepository.login(
                            context = context,
                            documentOrPhone = cpfDigits,
                            password = password,
                            onSuccess = { loading = false; onLoginOk(it) },
                            onError = { loading = false; message = it }
                        )
                    }
                }
            }
            Spacer(Modifier.height(10.dp))
            UpSecondaryButton(text = "Solicitar cadastro", onClick = onRegister)
            Spacer(Modifier.height(12.dp))
            Text(AppVersion.LOGIN_LABEL, color = Color(0xFF1E4FFF), fontSize = 12.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
private fun UpRegisterScreen(onBack: () -> Unit, onSent: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var cpf by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var vehicle by remember { mutableStateOf("Moto") }
    var plate by remember { mutableStateOf("") }
    var pix by remember { mutableStateOf("") }
    var bank by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordTaps by remember { mutableStateOf(0) }
    var loading by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }

    UpAuthSurface {
        UpBrandHeader(title = "UP Entregas", subtitle = "Solicitação de cadastro")
        Spacer(Modifier.height(14.dp))
        UpAuthCard {
            Text("Dados do entregador", color = UpInk, fontSize = 22.sp, fontWeight = FontWeight.Black)
            Text("O cadastro fica em análise até aprovação no gestor.", color = UpMuted, fontSize = 14.sp)
            Spacer(Modifier.height(14.dp))
            UpField(name, { name = it }, "Nome completo", "Ex: Diego Alves")
            UpField(cpf, { cpf = maskCpf(it) }, "CPF", "000.000.000-00", KeyboardType.Number)
            UpField(phone, { phone = maskPhone(it) }, "Telefone/WhatsApp", "(67) 99999-9999", KeyboardType.Phone)
            UpField(city, { city = it }, "Cidade", "Campo Grande")
            UpField(vehicle, { vehicle = it }, "Modalidade", "Moto")
            UpField(plate, { plate = it.uppercase().take(8) }, "Placa", "ABC1D23")
            UpField(pix, { pix = it }, "Chave Pix", "CPF, telefone, e-mail ou aleatória")
            UpField(bank, { bank = it }, "Banco", "Nome do banco")
            Spacer(Modifier.height(8.dp))
            UpPasswordBox(count = passwordTaps, label = "Criar senha do app")
            Spacer(Modifier.height(8.dp))
            UpPairKeyboard(
                onAppend = { token -> password += token; passwordTaps++ },
                onBackspace = { if (passwordTaps > 0) { password = password.dropLast(1); passwordTaps-- } },
                onClear = { password = ""; passwordTaps = 0 }
            )
            if (message.isNotBlank()) {
                Spacer(Modifier.height(10.dp))
                Text(message, color = Color(0xFFD32F2F), fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(16.dp))
            UpPrimaryButton(text = if (loading) "Enviando..." else "Enviar cadastro") {
                val cpfDigits = cpf.onlyDigitsLocal()
                val phoneDigits = phone.onlyDigitsLocal()
                when {
                    !isValidFullName(name) -> message = "Informe nome completo: mínimo 2 nomes, 2 letras em cada, sem números."
                    !isValidCpf(cpfDigits) -> message = "CPF inválido. Confira os números digitados."
                    phoneDigits.length < 10 -> message = "Informe um telefone válido com DDD."
                    passwordTaps < 7 -> message = "Crie uma senha com no mínimo 7 toques."
                    else -> {
                        loading = true
                        message = ""
                        DriverRepository.registerDriver(
                            request = DriverRegistrationRequest(
                                name = name.trim(),
                                cpf = cpf,
                                phone = phone,
                                password = password,
                                vehicle = vehicle.trim().ifBlank { "Moto" },
                                plate = plate.trim(),
                                pixKey = pix.trim(),
                                bankName = bank.trim(),
                                city = city.trim()
                            ),
                            onSuccess = { loading = false; onSent() },
                            onError = { loading = false; message = it }
                        )
                    }
                }
            }
            Spacer(Modifier.height(10.dp))
            TextButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text("Voltar para login") }
        }
    }
}

@Composable
private fun UpCreatePasswordScreen(profile: DriverProfile, onSaved: () -> Unit, onLogout: () -> Unit) {
    val context = LocalContext.current
    var password by remember { mutableStateOf("") }
    var count by remember { mutableStateOf(0) }
    var loading by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }
    UpAuthSurface {
        UpBrandHeader(title = "UP Entregas", subtitle = "Criar senha")
        Spacer(Modifier.height(16.dp))
        UpAuthCard {
            Text("Olá, ${profile.name}", color = UpInk, fontSize = 22.sp, fontWeight = FontWeight.Black)
            Text("Crie sua senha numérica do app antes de continuar.", color = UpMuted, fontSize = 14.sp)
            Spacer(Modifier.height(16.dp))
            UpPasswordBox(count = count, label = "Nova senha")
            Spacer(Modifier.height(8.dp))
            UpPairKeyboard(
                onAppend = { token -> password += token; count++ },
                onBackspace = { if (count > 0) { password = password.dropLast(1); count-- } },
                onClear = { password = ""; count = 0 }
            )
            if (message.isNotBlank()) {
                Spacer(Modifier.height(10.dp))
                Text(message, color = Color(0xFFD32F2F), fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(16.dp))
            UpPrimaryButton(text = if (loading) "Salvando..." else "Salvar senha") {
                if (count < 7) {
                    message = "Senha precisa ter no mínimo 7 toques."
                } else {
                    loading = true
                    DriverRepository.updateAccessPassword(
                        context = context,
                        newPassword = password,
                        onSuccess = { loading = false; onSaved() },
                        onError = { loading = false; message = it }
                    )
                }
            }
            TextButton(onClick = onLogout, modifier = Modifier.fillMaxWidth()) { Text("Sair") }
        }
    }
}

@Composable
private fun UpAnalysisScreen(onBackLogin: () -> Unit) {
    UpAuthSurface {
        UpBrandHeader(title = "UP Entregas", subtitle = "Cadastro enviado")
        Spacer(Modifier.height(22.dp))
        UpAuthCard {
            Text("Cadastro em análise", color = UpInk, fontSize = 25.sp, fontWeight = FontWeight.Black)
            Spacer(Modifier.height(8.dp))
            Text(
                "Recebemos sua solicitação. Agora o gestor precisa conferir e aprovar seu acesso. Depois da aprovação, entre usando CPF e senha.",
                color = UpMuted,
                fontSize = 15.sp,
                lineHeight = 22.sp
            )
            Spacer(Modifier.height(18.dp))
            UpPrimaryButton(text = "Voltar para login", onClick = onBackLogin)
            Spacer(Modifier.height(12.dp))
            Text(AppVersion.LOGIN_LABEL, color = Color(0xFF1E4FFF), fontSize = 12.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
private fun UpAuthSurface(content: @Composable () -> Unit) {
    Box(
        Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF08164A), Color(0xFF0E2368), Color(0xFF2A1E8A))))
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 22.dp, vertical = 28.dp),
            horizontalAlignment = Alignment.Start
        ) { content() }
    }
}

@Composable
private fun UpBrandHeader(title: String, subtitle: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(68.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White)
                .border(2.dp, Color(0xFFB7E51E), RoundedCornerShape(20.dp)),
            contentAlignment = Alignment.Center
        ) {
            Image(painter = painterResource(id = R.drawable.up_app_icon), contentDescription = "UP", modifier = Modifier.size(50.dp))
        }
        Spacer(Modifier.width(12.dp))
        Column {
            Text(title, color = Color.White, fontWeight = FontWeight.Black, fontSize = 24.sp)
            Text(subtitle, color = Color(0xFFE8E61A), fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
    }
}

@Composable
private fun UpAuthCard(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(28.dp)
    ) { Column(Modifier.padding(20.dp)) { content() } }
}

@Composable
private fun UpField(value: String, onChange: (String) -> Unit, label: String, placeholder: String, keyboard: KeyboardType = KeyboardType.Text) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboard),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        shape = RoundedCornerShape(18.dp)
    )
}

@Composable
private fun UpPasswordBox(count: Int, label: String) {
    Column {
        Text(label, color = UpInk, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Spacer(Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(Color(0xFFF4F7FF))
                .border(1.dp, Color(0xFFDCE5FF), RoundedCornerShape(18.dp))
                .padding(18.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(if (count == 0) "Toque no teclado seguro" else "•".repeat(count), color = if (count == 0) UpMuted else UpInk, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(4.dp))
        Text("Mínimo 7 toques. Os pares alternam a ordem ao abrir o app.", color = UpMuted, fontSize = 12.sp)
    }
}

@Composable
private fun UpPairKeyboard(onAppend: (String) -> Unit, onBackspace: () -> Unit, onClear: () -> Unit) {
    val pairs = remember {
        listOf(
            "1 ou 2" to "1",
            "3 ou 4" to "3",
            "5 ou 6" to "5",
            "7 ou 8" to "7",
            "9 ou 0" to "9"
        ).shuffled()
    }
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        pairs.chunked(2).forEach { rowItems ->
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                rowItems.forEach { item ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFF08164A))
                            .clickable { onAppend(item.second) },
                        contentAlignment = Alignment.Center
                    ) { Text(item.first, color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.Black) }
                }
                if (rowItems.size == 1) Spacer(Modifier.weight(1f))
            }
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = onBackspace, modifier = Modifier.weight(1f), shape = RoundedCornerShape(16.dp)) { Text("Apagar") }
            OutlinedButton(onClick = onClear, modifier = Modifier.weight(1f), shape = RoundedCornerShape(16.dp)) { Text("Limpar") }
        }
    }
}

@Composable
private fun UpPrimaryButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp),
        shape = RoundedCornerShape(18.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB7E51E), contentColor = Color(0xFF08164A))
    ) { Text(text, fontSize = 16.sp, fontWeight = FontWeight.Black) }
}

@Composable
private fun UpSecondaryButton(text: String, onClick: () -> Unit) {
    OutlinedButton(onClick = onClick, modifier = Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(18.dp)) {
        Text(text, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFFFFF))
    }
}

private val UpInk = Color(0xFF08164A)
private val UpMuted = Color(0xFF647092)

private fun String.onlyDigitsLocal(): String = filter { it.isDigit() }

private fun maskCpf(value: String): String {
    val d = value.onlyDigitsLocal().take(11)
    return buildString {
        d.forEachIndexed { i, c ->
            append(c)
            if (i == 2 || i == 5) append('.')
            if (i == 8) append('-')
        }
    }
}

private fun maskPhone(value: String): String {
    val d = value.onlyDigitsLocal().take(11)
    return when {
        d.length <= 2 -> d
        d.length <= 6 -> "(${d.substring(0, 2)}) ${d.substring(2)}"
        d.length <= 10 -> "(${d.substring(0, 2)}) ${d.substring(2, 6)}-${d.substring(6)}"
        else -> "(${d.substring(0, 2)}) ${d.substring(2, 7)}-${d.substring(7)}"
    }
}

private fun isValidFullName(name: String): Boolean {
    val parts = name.trim().split(Regex("\\s+")).filter { it.isNotBlank() }
    if (parts.size < 2) return false
    if (parts.any { it.length < 2 }) return false
    if (name.any { it.isDigit() }) return false
    return parts.all { part -> part.all { it.isLetter() || it == '\'' || it == '-' } }
}

private fun isValidCpf(cpf: String): Boolean {
    val numbers = cpf.onlyDigitsLocal()
    if (numbers.length != 11) return false
    if (numbers.toSet().size == 1) return false
    fun digit(size: Int): Int {
        var sum = 0
        var weight = size + 1
        for (i in 0 until size) sum += (numbers[i] - '0') * weight--
        val rest = (sum * 10) % 11
        return if (rest == 10) 0 else rest
    }
    return digit(9) == numbers[9] - '0' && digit(10) == numbers[10] - '0'
}
