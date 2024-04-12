package com.smaart.idrico.model

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.IBinder
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class TokenExpirationService : Service() {
    private val dao = DAO(this@TokenExpirationService)

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        checkTokenValidity()
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun tokenIsValid():Boolean {
        return !(isBeforeNow(dao.get("token_expiry")!!))
    }
    fun isBeforeNow(dateTimeString: String): Boolean {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val dateTime = LocalDateTime.parse(dateTimeString, formatter)
        val now = LocalDateTime.now()
        return dateTime.isBefore(now)
    }
    private fun restartApplication() {
        val packageManager = applicationContext.packageManager
        val intent = packageManager.getLaunchIntentForPackage(applicationContext.packageName)
        val componentName = intent?.component
        val mainIntent = Intent.makeRestartActivityTask(componentName)
        applicationContext.startActivity(mainIntent)
        Runtime.getRuntime().exit(0)
    }
}
