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
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup


class Actions : AppCompatActivity() {
    private var layout: String = ""
    private var jsonLayout: JSONObject = JSONObject()
    private lateinit var keys: MutableIterator<String>
    private lateinit var parentView: LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actions)
        layout = intent.getStringExtra("layout")!!
        jsonLayout = JSONObject(layout)
        keys = jsonLayout.keys()
        parentView = findViewById<LinearLayout>(R.id.view)
        var isFirst = true
        val parentLayout=createLayout()
        val formLayout = mutableMapOf<String,LinearLayout>()
        keys.forEach { key->
            val obj = jsonLayout.getJSONObject(key);
            val btnLabel = obj.getString("label");
            parentLayout.addView(createButton(btnLabel))
            if(isFirst){
                isFirst=false
                formLayout[key] = createLayout()
            }
        }
        parentView.addView(parentLayout)
        parentView=createItems(formLayout,parentView)
    }
    private fun createButton(label:String):Button{
        val button = Button(this)
        button.id = View.generateViewId()
        button.layoutParams = LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1f
        )
        button.text = label
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
    private fun createItems(formLayout: MutableMap<String, LinearLayout>,parentView:LinearLayout):LinearLayout {
        formLayout.forEach{ (key,form)->
            val items = jsonLayout.getJSONObject(key).getJSONObject("items")
            var fragment: View = View(this)
            items.keys().forEach { itemKey ->
                when (itemKey) {
                    "open" -> {
                        val inflater = LayoutInflater.from(this)
                        fragment = inflater.inflate(R.layout.fragment_open, form, false)
                        val button = fragment.findViewById<Button>(R.id.open)
                        button.text = "test1"
                    }
                    "setIP" -> {
                        val inflater = LayoutInflater.from(this)
                        fragment = inflater.inflate(R.layout.fragment_set_ip, form, false)
                        val button = fragment.findViewById<Button>(R.id.set_ip)
                        button.text = "test2"
                    }
                }
                form.addView(fragment)
            }
            parentView.addView(form)
        }
        return parentView
    }
}
