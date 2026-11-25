package com.rodriguesacai.entregador

import android.os.Bundle
import android.content.Intent
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.rodriguesacai.entregador.data.DriverRepository
import com.rodriguesacai.entregador.ui.UrgentRideScreen

class UrgentRideActivity : ComponentActivity() {
    private var action by mutableStateOf("")
    private var actionError by mutableStateOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        )
        val rideId = intent.getStringExtra("rideId") ?: "sem-id"
        val value = intent.getStringExtra("value").orEmpty()
        val distance = intent.getStringExtra("distance").orEmpty()
        val duration = intent.getStringExtra("duration").orEmpty()
        val pickup = intent.getStringExtra("pickup").orEmpty()
        val dropoff = intent.getStringExtra("dropoff").orEmpty()
        val paymentMethod = intent.getStringExtra("paymentMethod").orEmpty()
        val paymentStatus = intent.getStringExtra("paymentStatus").orEmpty()
        val amountToCollect = intent.getStringExtra("amountToCollect").orEmpty()
        val changeFor = intent.getStringExtra("changeFor").orEmpty()
        val requiresMachine = intent.getStringExtra("requiresMachine").orEmpty()
        setContent {
            RodriguesNativeTheme(darkTheme = AppSettings.isDarkTheme(this)) {
                UrgentRideScreen(
                    rideId = rideId,
                    value = value,
                    distance = distance,
                    duration = duration,
                    pickup = pickup,
                    dropoff = dropoff,
                    paymentMethod = paymentMethod,
                    paymentStatus = paymentStatus,
                    amountToCollect = amountToCollect,
                    changeFor = changeFor,
                    requiresMachine = requiresMachine,
                    action = action,
                    actionError = actionError,
                    onAccept = {
                        if (action.isBlank()) {
                            action = "Aceitando..."
                            actionError = ""
                            DriverRepository.acceptRide(this, rideId, onDone = {
                                com.rodriguesacai.entregador.service.AppAlertPlayer.playSuccess(this)
                                startActivity(Intent(this, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP))
                                finish()
                            }, onError = { action = ""; actionError = it })
                        }
                    },
                    onReject = {
                        if (action.isBlank()) {
                            action = "Recusando..."
                            actionError = ""
                            DriverRepository.rejectRide(this, rideId, onDone = { finish() }, onError = { action = ""; actionError = it })
                        }
                    },
                    onExpired = {
                        if (action.isBlank()) {
                            action = "Encerrando..."
                            DriverRepository.expireRide(this, rideId, onDone = { finish() }, onError = { action = ""; actionError = it })
                        }
                    }
                )
            }
        }
    }
}
