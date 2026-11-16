package com.rodriguesacai.entregador.data

import android.content.Context
import android.os.BatteryManager
import android.os.Handler
import android.os.Looper
import com.google.firebase.auth.FirebaseAuth
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Cliente Android da API independente do UP.
 *
 * NUNCA recebe service_role, chaves privadas ou IDs de entregadores como
 * credencial. A API confere o Firebase ID token e a atribuicao da corrida.
 * O Firebase Firestore antigo nao e fonte dos pontos deste servico.
 */
object UpIndependentApi {
    private const val ENDPOINT =
        "https://gkwmqwtktedarodvqqtn.supabase.co/functions/v1/driver-api-independent"
    private const val REGISTRATION_ENDPOINT =
        "https://gkwmqwtktedarodvqqtn.supabase.co/functions/v1/driver-registration-independent"
    private val worker = Executors.newSingleThreadExecutor()
    private val main = Handler(Looper.getMainLooper())
    private val gpsInFlight = AtomicBoolean(false)
    private val independentId = Regex(
        "^supabase:[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$",
        RegexOption.IGNORE_CASE
    )

    fun isIndependentRide(rideId: String): Boolean = independentId.matches(rideId)

    /**
     * Requer login Firebase Auth real. Sessao legada por CPF nao e uma
     * autenticacao aceita pelo novo servidor e nunca e usada como substituto.
     */
    fun post(
        action: String,
        body: JSONObject = JSONObject(),
        onSuccess: (JSONObject) -> Unit = {},
        onError: (String) -> Unit = {}
    ) = request(ENDPOINT, action, body, onSuccess, onError)

    fun registration(
        action: String,
        body: JSONObject = JSONObject(),
        onSuccess: (JSONObject) -> Unit = {},
        onError: (String) -> Unit = {}
    ) = request(REGISTRATION_ENDPOINT, action, body, onSuccess, onError)

    private fun request(
        endpoint: String,
        action: String,
        body: JSONObject,
        onSuccess: (JSONObject) -> Unit,
        onError: (String) -> Unit
    ) {
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            main.post { onError("Entre com sua conta UP verificada para usar a operacao independente.") }
            return
        }
        user.getIdToken(false)
            .addOnSuccessListener { result ->
                val idToken = result.token
                if (idToken.isNullOrBlank()) {
                    onError("Sessao UP expirada. Faca login novamente.")
                    return@addOnSuccessListener
                }
                worker.execute {
                    var conn: HttpURLConnection? = null
                    try {
                        val json = JSONObject(body.toString()).put("action", action)
                        val url = URL(endpoint)
                        conn = url.openConnection() as HttpURLConnection
                        conn.requestMethod = "POST"
                        conn.connectTimeout = 10000
                        conn.readTimeout = 12000
                        conn.doOutput = true
                        conn.instanceFollowRedirects = false
                        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8")
                        conn.setRequestProperty("Authorization", "Bearer " + idToken)
                        conn.setRequestProperty("Cache-Control", "no-store")
                        conn.outputStream.use { output ->
                            output.write(json.toString().toByteArray(Charsets.UTF_8))
                        }
                        val status = conn.responseCode
                        val responseText = (if (status in 200..299) conn.inputStream else conn.errorStream)
                            ?.bufferedReader(Charsets.UTF_8)?.use { it.readText() }.orEmpty()
                        val response = JSONObject(responseText.ifBlank { "{}" })
                        if (status !in 200..299 || !response.optBoolean("ok", false)) {
                            val code = response.optString("error", "http_" + status)
                            main.post { onError("UP: " + code) }
                        } else {
                            main.post { onSuccess(response) }
                        }
                    } catch (_: Exception) {
                        main.post { onError("Sem conexao segura com o UP Entregas.") }
                    } finally {
                        conn?.disconnect()
                    }
                }
            }
            .addOnFailureListener {
                onError("Nao foi possivel autenticar a sessao UP.")
            }
    }

    /**
     * Telemetria da bateria, sem CPF, foto ou localizacao.
     * O backend so autoriza novo aceite quando a bateria reportada permite.
     */
    fun sendTelemetry(context: Context) {
        val manager = context.getSystemService(Context.BATTERY_SERVICE) as? BatteryManager ?: return
        val level = manager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        if (level !in 0..100) return
        val payload = JSONObject()
            .put("battery_level", level)
            .put("charging", manager.isCharging)
            .put("app_version", "2.0.3-rc1")
        post("telemetry", payload)
    }

    /**
     * Envia GPS APENAS de corrida independente. Nao envia posicao sem corrida
     * vinculada, nem para Firestore legado. O servidor valida propriedade,
     * status e permissao antes de gravar.
     */
    fun sendLocation(
        rideId: String,
        lat: Double,
        lng: Double,
        accuracy: Float,
        onError: (String) -> Unit = {}
    ) {
        if (!isIndependentRide(rideId)) return
        if (!lat.isFinite() || !lng.isFinite() ||
            lat !in -90.0..90.0 || lng !in -180.0..180.0 ||
            (lat == 0.0 && lng == 0.0) ||
            !accuracy.isFinite() || accuracy < 0f
        ) return
        if (!gpsInFlight.compareAndSet(false, true)) return
        val payload = JSONObject()
            .put("mission_id", rideId)
            .put("lat", lat)
            .put("lng", lng)
            .put("accuracy", accuracy.toDouble())
        post(
            action = "location",
            body = payload,
            onSuccess = { gpsInFlight.set(false) },
            onError = { message ->
                gpsInFlight.set(false)
                onError(message)
            }
        )
    }
}
