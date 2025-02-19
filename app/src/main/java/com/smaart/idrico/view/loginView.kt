package com.smaart.idrico.view
import android.os.Bundle
import android.text.InputType
import android.view.Menu
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import com.smaart.idrico.R
import com.smaart.idrico.model.Base

class LoginView:Base(R.style.login) {
    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        val loginView=R.layout.login
        setContentView(loginView)
        val btnEye = findViewById<ImageButton>(R.id.btnTogglePasswordVisibility)
        btnEye.setOnClickListener{
            findViewById<EditText>(R.id.txtPassword).inputType = InputType.TYPE_CLASS_TEXT
        }
        val btnLogin=findViewById<Button>(R.id.btnLogin)
        btnLogin.setOnClickListener{
            val email=findViewById<EditText>(R.id.txtEmail).text.toString()
            val pass=findViewById<EditText>(R.id.txtPassword).text.toString()
            login(this,email,pass)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean { return false }
}
