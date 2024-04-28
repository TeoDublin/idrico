package com.smaart.idrico.model
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.smaart.idrico.R
import com.smaart.idrico.controller.Layout
import com.smaart.idrico.service.tokenExpiration
import com.smaart.idrico.view.LoginView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

open class Base(private val themeId:Int?=null): AppCompatActivity(){
    lateinit var dao:DAO
    lateinit var fn:Functions
    private val Int.dp:Int get()=(this * Resources.getSystem().displayMetrics.density).toInt()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dao=DAO(applicationContext)
        fn=Functions(applicationContext)
        setTheme(themeId?:R.style.base)
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_base, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                dao.clearAll()
                val intent= Intent(this, LoginView::class.java)
                this.startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    fun createLayout(
        orientation: String,
        visible: Boolean
    ): LinearLayout {
        val linearLayout = LinearLayout(this)
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(0, 15.dp, 0, 1.dp)
        linearLayout.id = View.generateViewId()
        linearLayout.isVisible = visible
        when (orientation) {
            "vertical" -> {
                linearLayout.orientation = LinearLayout.VERTICAL
                linearLayout.gravity = Gravity.CENTER_HORIZONTAL
            }
            else -> {
                linearLayout.orientation = LinearLayout.HORIZONTAL
                linearLayout.gravity = Gravity.CENTER_VERTICAL
            }
        }
        linearLayout.layoutParams = layoutParams
        return linearLayout
    }
    fun createButton(label:String,orientation:String): Button {
        val button= Button(this)
        button.id=View.generateViewId()
        when(orientation){
            "vertical"->{
                val layoutParams=LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    0,
                    1f
                )
                button.textSize= 8.dp.toFloat()
                button.layoutParams=layoutParams
                button.setPadding(2, 8, 2, 0)
            }
            "horizontal"->{
                button.layoutParams=LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f
                )
                button.textSize= 8.dp.toFloat()
                button.setPadding(2, 8, 2, 0)
            }
            "actions"->{
                val screenWidth = this.resources.displayMetrics.widthPixels
                val layoutParams = LinearLayout.LayoutParams(
                    (screenWidth * 0.6).toInt(),
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                button.textSize= 10.dp.toFloat()
                layoutParams.gravity = Gravity.CENTER
                layoutParams.setMargins(0, 10.dp, 0, 0)
                button.layoutParams = layoutParams
                button.setPadding(2.dp, 40.dp, 2.dp, 40.dp)
            }
        }
        button.text=label
        button.isAllCaps=false
        return button
    }
    fun createBorder():View{
        val border = View(this)
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            resources.getDimensionPixelSize(R.dimen.dp05)
        )
        params.setMargins(0,5.dp,0,30.dp)
        border.layoutParams = params
        border.setBackgroundColor(ContextCompat.getColor(this, R.color.gray))
        return border
    }
    fun Context.theme(attrName: String, attType: String, themeResId: Int): Int {
        val typedValue = TypedValue()
        theme.obtainStyledAttributes(themeResId, intArrayOf(resources.getIdentifier(attrName, attType, packageName)))
            .getValue(0, typedValue)
        return typedValue.data
    }
    fun login(context: Context,email:String?,pass:String?) {
        CoroutineScope(Dispatchers.IO).launch  {
            try {
                val response=Api().login(email,pass)
                if(response.code()!=200){
                    Toast.makeText(context,response.message(), Toast.LENGTH_SHORT).show()
                }else{
                    val token=response.body()?.token?:throw Exception("Token not found")
                    val tokenExpiry= LocalDateTime.now().plusHours(TOKEN_EXPIRY_HOURS)
                    val formatter= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    dao.save(mapOf(
                        "email" to email!!,
                        "pass" to pass!!,
                        "token" to token,
                        "tokenExpiry" to tokenExpiry.format(formatter),
                        "appVersion" to APP_VERSION
                    ))
                    startActivityFromApi(context,FIRST_VIEW,"actions")
                }
            }catch(e:Exception){
                Toast.makeText(context,"Error $e", Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun startActivityFromApi(context: Context, activity:String, useApi:String) {
        val buffedLayout= Layout().get(activity,useApi,context)
        val layout:String=if(!buffedLayout.isNullOrEmpty()) buffedLayout else waitLayout(activity)
        try {
            val intent=Intent(context,Class.forName(activity))
            intent.putExtra("layout",layout)
            context.startActivity(intent)
        } catch (e:Exception) {
            Log.e("runActivity","Error starting $activity activity: ${e.message}")
            Toast.makeText(context,"Error starting $activity activity", Toast.LENGTH_SHORT).show()
        }
    }
    fun startActivity(context: Context,javaClass: Class<*>){
        val intent= Intent(context, javaClass)
        this.startActivity(intent)
    }
    fun needsLogin():Boolean{
        val token=dao.get("token")
        return token.isNullOrEmpty()
    }
    private fun waitLayout(activity:String):String {
        return runBlocking {
            var layout:String?=dao.get(activity)
            val delayMillis=1000L
            val timeoutSeconds=60
            var elapsedTimeSeconds=0
            while(layout.isNullOrEmpty()&&elapsedTimeSeconds<timeoutSeconds){
                println("Waiting...")
                delay(delayMillis)
                elapsedTimeSeconds++
                layout=dao.get(activity)
            }
            if (!layout.isNullOrEmpty())layout else throw IllegalStateException("Timeout waiting for buffed data")
        }
    }
    private fun startTokenExpirationService() {
        val serviceIntent=Intent(applicationContext, tokenExpiration::class.java)
        ContextCompat.startForegroundService(applicationContext,serviceIntent)
    }

}