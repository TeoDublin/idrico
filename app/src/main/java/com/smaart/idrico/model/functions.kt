package com.smaart.idrico.model
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.smaart.idrico.controller.Layout
import com.smaart.idrico.service.tokenExpiration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime.now
import java.time.format.DateTimeFormatter
class Functions(private val applicationContext:Context) {
    private val dao=DAO(applicationContext)
    private val api=Api()

}