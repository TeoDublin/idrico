package com.smaart.idrico.view

import android.app.ActionBar.LayoutParams
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.smaart.idrico.R
import org.json.JSONObject
import androidx.appcompat.widget.AppCompatButton
import android.graphics.drawable.RippleDrawable


class Actions : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val intent = intent
//        val layout = intent.getStringExtra("layout")!!
//        val jsonLayout = JSONObject(layout)
//        val keys = jsonLayout.keys()
        setContentView(R.layout.actions)
        val parentLayout = findViewById<LinearLayout>(R.id.view)
        val linearLayout=createLayout()
        linearLayout.addView(createButton())
        parentLayout.addView(linearLayout)
    }
    private fun createButton():Button{
        val button = Button(this)
        button.id = View.generateViewId()
        button.layoutParams = LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1f
        )
        button.text = "Common"
        button.setTextColor(Color.WHITE)
        button.isAllCaps = false
        val rippleDrawableCommon = RippleDrawable(
            ColorStateList.valueOf(Color.GRAY),
            null,
            null
        )
        button.background = rippleDrawableCommon
        button.setPadding(8, 8, 8, 8)
        return button
    }
    private fun createLayout():LinearLayout{
        val typedValue = TypedValue()
        theme.resolveAttribute(android.R.attr.colorPrimary, typedValue, true)
        val colorPrimary = typedValue.data
        val linearLayout = LinearLayout(this)
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        linearLayout.id = View.generateViewId()
        linearLayout.layoutParams = layoutParams
        linearLayout.orientation=LinearLayout.HORIZONTAL
        linearLayout.setBackgroundColor(colorPrimary)
        linearLayout.gravity=Gravity.CENTER_VERTICAL
        return linearLayout
    }
}
