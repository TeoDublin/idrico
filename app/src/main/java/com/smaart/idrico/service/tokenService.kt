package com.smaart.idrico.service
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.smaart.idrico.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TokenService : Service() {
    companion object {
        private const val NOTIFICATION_CHANNEL_ID = "TokenServiceChannel"
        private const val NOTIFICATION_ID = 12345
        private var isServiceRunning = false
    }
    private var job: Job? = null
    private val wifiStateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == WifiManager.WIFI_STATE_CHANGED_ACTION) {
                val wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN)
                if (wifiState == WifiManager.WIFI_STATE_DISABLED) {
                    stopSelf()
                }
            }
        }
    }
    override fun onCreate() {
        super.onCreate()
        registerReceiver(wifiStateReceiver, IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION))
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (isServiceRunning) {
            return START_NOT_STICKY
        }
        val currentDateTime: String = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(
            Date()
        )
        Log.e("TEST", "SERVICE STARTED: $currentDateTime")
        startForeground(NOTIFICATION_ID, createNotification())
        job = CoroutineScope(Dispatchers.Default).launch {
            test()
            stopSelf()
        }
        isServiceRunning = true
        return START_STICKY
    }
    override fun onDestroy() {
        val currentDateTime: String = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(
            Date()
        )
        Log.e("TEST", "SERVICE DESTROYED: $currentDateTime")
        job?.cancel()
        isServiceRunning = false
        super.onDestroy()
    }
    private suspend fun test() {
        while (true){
            delay(7200000)
            val currentDateTime: String = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(
                Date()
            )
            Log.e("TEST", "RUNNING: $currentDateTime")
        }
    }
    private fun createNotification(): Notification {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            "Token Service Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)
        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Token Service")
            .setContentText("Running in background")
            .setSmallIcon(R.drawable.ic_eye)
            .build()
    }
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
