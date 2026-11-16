package com.rodriguesacai.entregador.data

import android.os.Handler
import android.os.Looper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Date
import java.util.Locale
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Leitura da operacao independente sem acesso direto ao banco de dados.
 * Cada chamada passa pelo Firebase ID token validado no backend UP.
 */
object UpIndependentOperations {
    const val COLLECTION = "up_independent"
    private const val POLL_MS = 10000L

    fun watch(
        pendingOnly: Boolean,
        onRide: (DriverRide?) -> Unit,
        onError: (String) -> Unit
    ): ListenerRegistration {
        val handler = Handler(Looper.getMainLooper())
        val running = AtomicBoolean(true)
        var lastError = ""
        val poll = object : Runnable {
            override fun run() {
                if (!running.get()) return
                UpIndependentApi.post("offer",
                    onSuccess = { response ->
                        if (running.get()) {
                            lastError = ""
                            val ride = decodeRide(response.optJSONObject("document"))
                            onRide(ride?.takeIf {
                                if (pendingOnly) it.status == "pending" else it.status != "pending"
                            })
                        }
                    },
                    onError = { message ->
                        if (running.get() && message != lastError) {
                            lastError = message
                            onError(message)
                        }
                    }
                )
                handler.postDelayed(this, POLL_MS)
            }
        }
        handler.post(poll)
        return object : ListenerRegistration {
            override fun remove() {
                running.set(false)
                handler.removeCallbacks(poll)
            }
        }
    }

    private fun observeApi(
        action: String,
        onData: (JSONObject) -> Unit,
        onError: (String) -> Unit
    ): ListenerRegistration {
        val handler = Handler(Looper.getMainLooper())
        val running = AtomicBoolean(true)
        var lastError = ""
        val task = object : Runnable {
            override fun run() {
                if (!running.get()) return
                UpIndependentApi.post(action,
                    onSuccess = { if (running.get()) { lastError = ""; onData(it) } },
                    onError = { msg ->
                        if (running.get() && lastError != msg) {
                            lastError = msg
                            onError(msg)
                        }
                    }
                )
                handler.postDelayed(this, 45_000L)
            }
        }
        handler.post(task)
        return object : ListenerRegistration {
            override fun remove() {
                running.set(false)
                handler.removeCallbacks(task)
            }
        }
    }

    private fun millis(iso: String): Long =
        runCatching { Instant.parse(iso).toEpochMilli() }.getOrDefault(0L)

    private fun label(millis: Long): String =
        if (millis == 0L) "Data nao informada"
        else SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("pt", "BR")).format(Date(millis))

    fun watchHistory(
        onHistory: (List<DriverHistory>) -> Unit,
        onError: (String) -> Unit
    ): ListenerRegistration = observeApi("history", { json ->
        val items = json.optJSONArray("documents")
        val out = ArrayList<DriverHistory>()
        if (items != null) for (i in 0 until items.length()) {
            val document = items.optJSONObject(i) ?: continue
            val data = document.optJSONObject("data") ?: continue
            val id = document.optString("id")
            if (!UpIndependentApi.isIndependentRide(id)) continue
            val at = millis(data.optString("updatedAt"))
            out.add(DriverHistory(
                id = id,
                rideId = data.optString("codigoPedido").ifBlank { id.takeLast(6) },
                action = data.optString("upState"),
                value = "", // collection_amount NAO e pagamento ao entregador
                createdAtMillis = at,
                createdLabel = label(at),
                pickup = data.optString("enderecoLoja"),
                dropoff = data.optString("clienteEnderecoCompleto")
            ))
        }
        onHistory(out.sortedByDescending { it.createdAtMillis })
    }, onError)

    fun watchWallet(
        onStats: (DriverStats) -> Unit,
        onError: (String) -> Unit
    ): ListenerRegistration = observeApi("wallet", { json ->
        val transactions = json.optJSONArray("transactions")
        val payouts = json.optJSONArray("payouts")
        var credits = 0.0
        val now = System.currentTimeMillis()
        val dayStart = now - 86_400_000L
        val weekStart = now - 7 * 86_400_000L
        val monthStart = now - 30 * 86_400_000L
        var today = 0.0
        var week = 0.0
        var month = 0.0
        if (transactions != null) for (i in 0 until transactions.length()) {
            val row = transactions.optJSONObject(i) ?: continue
            val amount = row.optDouble("amount", 0.0)
            if (!amount.isFinite()) continue
            credits += amount
            val created = millis(row.optString("created_at"))
            if (amount > 0 && created > dayStart) today += amount
            if (amount > 0 && created > weekStart) week += amount
            if (amount > 0 && created > monthStart) month += amount
        }
        var reserved = 0.0
        val rows = ArrayList<DriverPayout>()
        if (payouts != null) for (i in 0 until payouts.length()) {
            val row = payouts.optJSONObject(i) ?: continue
            val amount = row.optDouble("amount", 0.0)
            if (!amount.isFinite()) continue
            val state = row.optString("status").uppercase(Locale.ROOT)
            if (state in setOf("REQUESTED", "APPROVED", "PAID"))
                reserved += amount
            val created = millis(row.optString("requested_at"))
            rows.add(DriverPayout(
                id = row.optString("id"),
                value = amount,
                valueLabel = DriverRepository.formatCurrency(amount),
                status = state,
                statusLabel = when (state) {
                    "REQUESTED" -> "Solicitado"
                    "APPROVED" -> "Aprovado"
                    "PAID" -> "Pago"
                    "REJECTED" -> "Recusado"
                    else -> state
                },
                createdAtMillis = created,
                createdLabel = label(created)
            ))
        }
        val balance = (credits - reserved).coerceAtLeast(0.0)
        onStats(DriverStats(
            totalToday = today,
            totalWeek = week,
            totalMonth = month,
            saldoDisponivel = balance,
            saldoPendente = reserved,
            totalAReceber = credits,
            payoutRows = rows.sortedByDescending { it.createdAtMillis }
        ))
    }, onError)

    private fun decodeRide(doc: JSONObject?): DriverRide? {
        if (doc == null) return null
        val id = doc.optString("id")
        if (!UpIndependentApi.isIndependentRide(id)) return null
        val data = doc.optJSONObject("data") ?: return null
        val state = data.optString("upState").uppercase(Locale.ROOT)
        val pending = state == "OFFER_PENDING"
        val localStatus = when (state) {
            "OFFER_PENDING" -> "pending"
            "ACCEPTED", "TO_STORE" -> "accepted"
            "AT_STORE" -> "pickup"
            "TO_CUSTOMER" -> "delivering"
            "AT_CUSTOMER" -> "arrived_client"
            else -> return null
        }
        val ownerId = FirebaseAuth.getInstance().currentUser?.uid ?: return null
        fun coord(key: String): Double? {
            val n = data.optDouble(key, Double.NaN)
            return n.takeIf { it.isFinite() && it != 0.0 }
        }
        val amount = data.optDouble("valorReceberCliente", 0.0).coerceAtLeast(0.0)
        return DriverRide(
            id = id,
            collectionName = COLLECTION,
            status = localStatus,
            rawStatus = state,
            value = "Tarifa a confirmar",
            valueNumber = 0.0,
            distance = "",
            duration = "",
            pickup = data.optString("enderecoLoja"),
            dropoff = data.optString("clienteEnderecoCompleto"),
            neighborhood = "",
            assignedDriverId = if (pending) "" else ownerId,
            targetDriverId = if (pending) ownerId else "",
            broadcast = false,
            customerName = data.optString("clienteNome"),
            orderCode = data.optString("codigoPedido"),
            stops = 1,
            rejectedDriverIds = emptyList(),
            expiredDriverIds = emptyList(),
            pickupLat = coord("lojaLat"),
            pickupLng = coord("lojaLng"),
            dropoffLat = coord("clienteLat"),
            dropoffLng = coord("clienteLng"),
            clientTotalNumber = amount,
            amountToCollectNumber = amount,
            pickupReleaseAllowed = state in setOf("TO_CUSTOMER", "AT_CUSTOMER"),
            pickupReleaseStatus = if (state == "AT_STORE") "AGUARDANDO_CONFIRMACAO" else "",
            offerType = "UP_INDEPENDENTE"
        )
    }
}
