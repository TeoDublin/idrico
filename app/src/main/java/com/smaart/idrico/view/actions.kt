package com.smaart.idrico.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.smaart.idrico.R


class Actions : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = intent
        val layout = intent.getStringExtra("layout")

        setContentView(R.layout.actions)
//
//        val layoutCommon = findViewById<View>(R.id.layout_common)
//        val layoutNetwork = findViewById<View>(R.id.layout_network)
//
//        // Set up onClickListener for the "Common" tab button
//        val buttonCommon = findViewById<Button>(R.id.button_common)
//        buttonCommon.setOnClickListener {
//            layoutCommon.visibility = View.VISIBLE
//            layoutNetwork.visibility = View.GONE
//        }
//
//        // Set up onClickListener for the "Network" tab button
//        val buttonNetwork = findViewById<Button>(R.id.button_network)
//        buttonNetwork.setOnClickListener {
//            layoutCommon.visibility = View.GONE
//            layoutNetwork.visibility = View.VISIBLE
//        }
    }
}
