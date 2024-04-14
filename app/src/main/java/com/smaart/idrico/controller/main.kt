package com.smaart.idrico.controller
import android.os.Bundle
import com.smaart.idrico.model.APP_VERSION
import com.smaart.idrico.model.Base
import com.smaart.idrico.model.FIRST_VIEW
import com.smaart.idrico.view.LoginView

class Main: Base() {
    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        val version=dao.get("appVersion")
        if(version.isNullOrEmpty()||version!=APP_VERSION)dao.clearAll()
        if(needsLogin()){
            startActivity(this, LoginView::class.java)
        }else{
            startActivityFromApi(this,FIRST_VIEW,"actions")
        }
    }
}