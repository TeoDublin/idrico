package com.smaart.idrico.controller
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.smaart.idrico.model.DAO
import com.smaart.idrico.model.FIRST_VIEW
import com.smaart.idrico.model.Functions
import com.smaart.idrico.model.LOGIN_VIEW
class Main : AppCompatActivity(){
    private val dao = DAO(this)
    private val fn = Functions(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val email = dao.get("email")
        val pass = dao.get("pass")
        //dao.clearAll()
        if(!email.isNullOrEmpty() && !pass.isNullOrEmpty()){
            fn.login(email,pass)
        }else{
            fn.runActivity(LOGIN_VIEW)
        }
        fn.runActivity(FIRST_VIEW)
    }
}