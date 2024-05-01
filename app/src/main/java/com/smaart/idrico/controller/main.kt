package com.smaart.idrico.controller
import android.os.Bundle
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.smaart.idrico.model.APP_VERSION
import com.smaart.idrico.model.Base
import com.smaart.idrico.model.FIRST_VIEW
import com.smaart.idrico.model.TokenWorker
import com.smaart.idrico.view.LoginView
import java.util.concurrent.TimeUnit

class Main: Base() {
    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        val periodicWorkRequest = PeriodicWorkRequestBuilder<TokenWorker>(
            repeatInterval = 5,
            repeatIntervalTimeUnit = TimeUnit.SECONDS
        ).build()
        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            "TokenWork",
            ExistingPeriodicWorkPolicy.REPLACE,
            periodicWorkRequest
        )
        val version=dao.get("appVersion")
        if(version.isNullOrEmpty()||version!=APP_VERSION)dao.clearAll()
        if(needsLogin()){
            startActivity(this, LoginView::class.java)
        }else{
            startActivityFromApi(this,FIRST_VIEW,"actions")
        }
    }
}