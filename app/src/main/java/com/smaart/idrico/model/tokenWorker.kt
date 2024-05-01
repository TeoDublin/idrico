package com.smaart.idrico.model
import android.content.Context
import android.content.Intent
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.smaart.idrico.service.TokenService

class TokenWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        return try {
            applicationContext.startService(Intent(applicationContext, TokenService::class.java))
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}