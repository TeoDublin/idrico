package com.smaart.idrico.view
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.smaart.idrico.R
import com.smaart.idrico.model.Base

class LoginView:Base() {
    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        val loginView=R.layout.login
        setContentView(loginView)
        val btnLogin=findViewById<Button>(R.id.btnLogin)
        btnLogin.setOnClickListener{
            val email=findViewById<EditText>(R.id.txtEmail).text.toString()
            val pass=findViewById<EditText>(R.id.txtPassword).text.toString()
            login(this,email,pass)
        }
    }
}
