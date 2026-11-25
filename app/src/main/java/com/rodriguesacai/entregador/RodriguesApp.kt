package com.rodriguesacai.entregador

import android.app.Application
import android.app.Activity
import android.os.Bundle
import java.util.concurrent.atomic.AtomicInteger
import com.google.firebase.FirebaseApp
import com.rodriguesacai.entregador.service.NotificationHelper

class RodriguesApp : Application() {
    companion object {
        private val visibleActivities = AtomicInteger(0)
        fun isVisible(): Boolean = visibleActivities.get() > 0
    }
    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, state: Bundle?) = Unit
            override fun onActivityStarted(activity: Activity) { visibleActivities.incrementAndGet() }
            override fun onActivityResumed(activity: Activity) = Unit
            override fun onActivityPaused(activity: Activity) = Unit
            override fun onActivityStopped(activity: Activity) { visibleActivities.decrementAndGet() }
            override fun onActivitySaveInstanceState(activity: Activity, state: Bundle) = Unit
            override fun onActivityDestroyed(activity: Activity) = Unit
        })
        FirebaseApp.initializeApp(this)
        NotificationHelper.createChannels(this)
    }
}
