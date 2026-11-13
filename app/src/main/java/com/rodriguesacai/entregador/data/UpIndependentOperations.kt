package com.rodriguesacai.entregador.data

import android.os.Handler
import android.os.Looper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
import org.json.JSONObject
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
