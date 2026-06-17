package com.rodriguesacai.entregador

import android.Manifest
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.rodriguesacai.entregador.service.OnlineDriverService
import com.rodriguesacai.entregador.ui.UpEntregadorShell

class MainActivity : ComponentActivity() {
    private var pendingOnlineStart: Boolean = false
    private var runningPermissionWizard: Boolean = false
    private var permissionRefreshTick by mutableStateOf(0)
    private var themeMode by mutableStateOf(AppSettings.THEME_LIGHT)

    private val notificationLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        refreshPermissions()
        if (runningPermissionWizard) continuePermissionWizardAfterNotifications()
    }

    private val locationLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        val fine = result[Manifest.permission.ACCESS_FINE_LOCATION] == true
        val coarse = result[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (pendingOnlineStart && (fine || coarse)) startOnlineService()
        pendingOnlineStart = false
        refreshPermissions()
        if (runningPermissionWizard) continuePermissionWizardAfterLocation()
    }

    private val settingsLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        refreshPermissions()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        themeMode = AppSettings.getThemeMode(this)
        setContent {
            RodriguesNativeTheme(darkTheme = themeMode == AppSettings.THEME_DARK) {
                UpEntregadorShell(
                    permissionRefreshTick = permissionRefreshTick,
                    onGoOnline = { requestLocationAndStartOnline() },
                    onGoOffline = { stopService(Intent(this, OnlineDriverService::class.java)) },
                    onOpenNavigator = { pickup, dropoff -> openNavigator(pickup, dropoff) },
                    onOpenNotificationSettings = { openNotificationSettings() },
                    onOpenLocationSettings = { openLocationSettings() },
                    onOpenFullScreenSettings = { openFullScreenSettings() },
                    onOpenBatterySettings = { requestBatteryOptimizationDialog() },
                    onRequestNotificationPermission = { askNotificationPermissionOnly() },
                    onRequestLocationPermission = { requestLocationOnly() },
                    onRequestEssentialPermissions = { startPermissionWizard() },
                    onThemeModeChanged = { mode ->
                        themeMode = mode
                        AppSettings.setThemeMode(this, mode)
                    }
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        refreshPermissions()
    }

    private fun refreshPermissions() {
        permissionRefreshTick++
    }

    private fun startPermissionWizard() {
        runningPermissionWizard = true
        if (Build.VERSION.SDK_INT >= 33 && ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            notificationLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            continuePermissionWizardAfterNotifications()
        }
    }

    private fun continuePermissionWizardAfterNotifications() {
        val fine = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val coarse = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        if (!fine && !coarse) {
            pendingOnlineStart = false
            locationLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
        } else {
            continuePermissionWizardAfterLocation()
        }
    }

    private fun continuePermissionWizardAfterLocation() {
        runningPermissionWizard = false
        val status = PermissionStatusReader.read(this)
        when {
            !status.batteryUnrestricted -> requestBatteryOptimizationDialog()
            !status.fullScreenIntent -> openFullScreenSettings()
            else -> refreshPermissions()
        }
    }

    private fun askNotificationPermissionOnly() {
        if (Build.VERSION.SDK_INT >= 33) {
            notificationLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            openNotificationSettings()
        }
    }

    private fun requestLocationAndStartOnline() {
        val fine = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val coarse = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        if (fine || coarse) {
            startOnlineService()
            return
        }
        pendingOnlineStart = true
        locationLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
    }

    private fun requestLocationOnly() {
        pendingOnlineStart = false
        locationLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
    }

    private fun startOnlineService() {
        val intent = Intent(this, OnlineDriverService::class.java)
        if (Build.VERSION.SDK_INT >= 26) startForegroundService(intent) else startService(intent)
    }

    private fun openNavigator(pickup: String, dropoff: String) {
        val destination = dropoff.ifBlank { pickup }
        if (destination.isBlank()) return
        val encoded = Uri.encode(destination)
        val preference = AppSettings.getNavigationApp(this)

        val intents = when (preference) {
            AppSettings.NAV_GOOGLE -> listOf(
                Intent(Intent.ACTION_VIEW, "google.navigation:q=$encoded".toUri()).apply { setPackage("com.google.android.apps.maps") },
                Intent(Intent.ACTION_VIEW, "geo:0,0?q=$encoded".toUri())
            )
            AppSettings.NAV_WAZE -> listOf(
                Intent(Intent.ACTION_VIEW, "waze://?q=$encoded&navigate=yes".toUri()).apply { setPackage("com.waze") },
                Intent(Intent.ACTION_VIEW, "https://waze.com/ul?q=$encoded&navigate=yes".toUri())
            )
            else -> listOf(
                Intent(Intent.ACTION_VIEW, "google.navigation:q=$encoded".toUri()).apply { setPackage("com.google.android.apps.maps") },
                Intent(Intent.ACTION_VIEW, "waze://?q=$encoded&navigate=yes".toUri()).apply { setPackage("com.waze") },
                Intent(Intent.ACTION_VIEW, "geo:0,0?q=$encoded".toUri())
            )
        }

        for (intent in intents) {
            if (runCatching { startActivity(intent) }.isSuccess) return
        }
    }

    private fun openNotificationSettings() {
        val intent = if (Build.VERSION.SDK_INT >= 26) {
            Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
            }
        } else {
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply { data = Uri.fromParts("package", packageName, null) }
        }
        openWithFallback(intent)
    }

    private fun openFullScreenSettings() {
        val intent = if (Build.VERSION.SDK_INT >= 34) {
            Intent(Settings.ACTION_MANAGE_APP_USE_FULL_SCREEN_INTENT).apply {
                data = Uri.fromParts("package", packageName, null)
            }
        } else {
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply { data = Uri.fromParts("package", packageName, null) }
        }
        openWithFallback(intent)
    }

    private fun requestBatteryOptimizationDialog() {
        val ignored = runCatching {
            val pm = getSystemService(POWER_SERVICE) as PowerManager
            pm.isIgnoringBatteryOptimizations(packageName)
        }.getOrDefault(true)
        if (!ignored && Build.VERSION.SDK_INT >= 23) {
            val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
                data = Uri.parse("package:$packageName")
            }
            openWithFallback(intent)
        } else {
            openBatterySettings()
        }
    }

    private fun openBatterySettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", packageName, null)
        }
        openWithFallback(intent)
    }

    private fun openLocationSettings() {
        val status = PermissionStatusReader.read(this)
        if (!status.location) {
            requestLocationOnly()
            return
        }
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        openWithFallback(intent)
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", packageName, null)
        }
        openWithFallback(intent)
    }

    private fun openWithFallback(intent: Intent) {
        runCatching { settingsLauncher.launch(intent) }.onFailure { startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply { data = Uri.fromParts("package", packageName, null) }) }
    }
}
