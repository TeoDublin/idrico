package com.smaart.idrico.service

import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import com.smaart.idrico.model.DAO
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class tokenExpiration : Service() {
    private val dao = DAO(this@tokenExpiration)

    override fun onCreate() {
        super.onCreate()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if(!tokenIsValid()) restartApplication()
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun tokenIsValid():Boolean {
        return !(isBeforeNow(dao.get("token_expiry")!!))
    }
    @RequiresApi(Build.VERSION_CODES.O)
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
