package com.smaart.idrico.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.smaart.idrico.R
import com.smaart.idrico.model.Functions
import com.smaart.idrico.model.DAO

class Login : AppCompatActivity() {
    private val loginView=R.layout.login
    private val fn = Functions(this)
    private val dao = DAO(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(loginView)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        btnLogin.setOnClickListener{
            val email = findViewById<EditText>(R.id.txtEmail).text.toString()
            val pass = findViewById<EditText>(R.id.txtPassword).text.toString()
            dao.save("email",email)
            dao.save("pass",pass)
            fn.login(email,pass)
        }
    }
}
