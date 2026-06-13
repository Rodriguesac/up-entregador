package com.rodriguesacai.entregador.service

import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import com.rodriguesacai.entregador.R

object AppAlertPlayer {
    private var lastUrgentPlayedAt = 0L
    private var lastSoftPlayedAt = 0L

    fun playNewRide(context: Context) {
        val now = System.currentTimeMillis()
        if (now - lastUrgentPlayedAt < 2500L) return
        lastUrgentPlayedAt = now
        playRaw(context, R.raw.nova_corrida)
        vibrate(context, longArrayOf(0, 450, 140, 450, 140, 720))
    }

    fun playSuccess(context: Context) {
        playSoft(context, R.raw.sucesso, longArrayOf(0, 80, 60, 120))
    }

    fun playNotice(context: Context) {
        playSoft(context, R.raw.aviso, longArrayOf(0, 120))
    }

    fun playTap(context: Context) {
        playSoft(context, R.raw.toque, longArrayOf(0, 35))
    }

    private fun playSoft(context: Context, rawId: Int, pattern: LongArray) {
        val now = System.currentTimeMillis()
        if (now - lastSoftPlayedAt < 350L) return
        lastSoftPlayedAt = now
        playRaw(context, rawId)
        vibrate(context, pattern)
    }

    private fun playRaw(context: Context, rawId: Int) {
        runCatching {
            val appContext = context.applicationContext
            MediaPlayer.create(appContext, rawId)?.apply {
                setOnCompletionListener { player -> runCatching { player.release() } }
                start()
            }
        }.recoverCatching {
            MediaPlayer.create(context.applicationContext, R.raw.alerta)?.apply {
                setOnCompletionListener { player -> runCatching { player.release() } }
                start()
            }
        }
    }

    private fun vibrate(context: Context, pattern: LongArray) {
        runCatching {
            val vibrator: Vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val manager = context.getSystemService(VibratorManager::class.java)
                manager.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createWaveform(pattern, -1))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(pattern, -1)
            }
        }
    }
}
