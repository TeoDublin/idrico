package com.smaart.idrico.model

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class Broadcast : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.e("TEST","BROADCAST ON")
        if (intent != null) {
            Toast.makeText(context, "Action: " + intent.action, Toast.LENGTH_SHORT).show()
        };
    }
}
