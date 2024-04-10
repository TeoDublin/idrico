package com.smaart.idrico.model
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import com.google.gson.reflect.TypeToken
import com.smaart.idrico.controller.Layout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class Functions(private val context: Context) {
    val gson = Gson()
    private val dao = DAO(context)
    private val api = Api()
    fun login(email: String?, pass: String?) {
        CoroutineScope(Dispatchers.IO).launch  {
            try {
                val response = api.login(email,pass)
                if(response.code()!=200){
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                }else{
                    val token=response.body()?.token ?: throw Exception("Token not found")
                    dao.save("Token",token)
                    runActivity(FIRST_VIEW)
                }
            }catch (e:Exception){
                Toast.makeText(context, "Error $e", Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun runActivity(activity: String) {
        val buffedLayout = Layout().get(activity,context)
        val layout : String = if(!buffedLayout.isNullOrEmpty()){
            buffedLayout
        }else{
            waitLayout(activity)
        }
        try {
            val intent = Intent(context, Class.forName(activity))
            intent.putExtra("layout", layout)
            context.startActivity(intent)
        } catch (e: Exception) {
            Log.e("runActivity", "Error starting $activity activity: ${e.message}")
            Toast.makeText(context, "Error starting $activity activity", Toast.LENGTH_SHORT).show()
        }
    }
    private fun waitLayout(activity: String): String {
        return runBlocking {
            var layout: String? = dao.get(activity)
            val delayMillis = 1000L
            val timeoutSeconds = 60
            var elapsedTimeSeconds = 0

            while (layout.isNullOrEmpty() && elapsedTimeSeconds < timeoutSeconds) {
                println("Waiting...")
                delay(delayMillis)
                elapsedTimeSeconds++
                layout = dao.get(activity)
            }

            if (layout.isNullOrEmpty()) {
                throw IllegalStateException("Timeout waiting for buffed data")
            } else {
                layout
            }
        }
    }
    fun needsLogin():Boolean{
        return true
    }
}