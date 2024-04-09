package com.smaart.idrico.view

import android.app.ActionBar.LayoutParams
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
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
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.core.view.marginTop
import androidx.core.view.setMargins
import androidx.core.widget.addTextChangedListener
import com.smaart.idrico.model.Payload


class Actions : AppCompatActivity() {
    private var layout: String = ""
    private var jsonLayout = JSONObject(layout)
    private lateinit var keys: MutableIterator<String>
    private lateinit var parentView: LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actions)
        layout = intent.getStringExtra("layout")!!
        val jsonLayout = JSONObject(layout)
        val actionsObject = jsonLayout.getJSONObject("actions")
        parentView = findViewById<LinearLayout>(R.id.view)
        var isFirst = true
        val parentLayout=createLayout("horizontal",true)
        val formLayouts: MutableMap<String, LinearLayout> = mutableMapOf()
        val formBtns: MutableMap<String, Button> = mutableMapOf()
        actionsObject.keys().forEach { key->
            val formObj=actionsObject.getJSONObject(key)
            try {
                val formLayout=createLayout("vertical",isFirst)
                formLayouts[key]=createFormButton(formObj,formLayout)
                val btn=createButton(key,"horizontal")
                formBtns[key]=btn
            } catch (e:Exception) {
                Log.e("actions", "no items", e)
            }
            if(isFirst)isFirst=false
        }
        formBtns.keys.forEach { key ->
            val btn = formBtns[key]
            val formLayout = formLayouts[key]
            btn?.setOnClickListener {
                formLayout?.visibility = View.VISIBLE
                formLayouts.keys.forEach { formKey ->
                    if (key != formKey) {
                        val otherFormLayout = formLayouts[formKey]
                        otherFormLayout?.visibility = View.GONE
                    }
                }
            }
            parentLayout.addView(btn)
        }
        parentView.addView(parentLayout)
        formLayouts.keys.forEach{key-> parentView.addView(formLayouts[key])}
    }
    private fun createButton(label:String,orientation: String):Button{
        val button = Button(this)
        button.id = View.generateViewId()
        when(orientation){
            "vertical"->{
                val layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    0,
                    1f
                )
                layoutParams.setMargins(0,0,0,16.dp)
                button.layoutParams=layoutParams
            }
            else->{
                button.layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f
                )
            }
        }
        button.text = label
        button.setTextColor(Color.BLACK)
        button.isAllCaps = false
        button.setPadding(8, 8, 8, 8)
        return button
    }
    private fun createFormButton(parentObj:JSONObject,formLayout:LinearLayout):LinearLayout{
        try {
            val items = parentObj.getJSONObject("items")
            items.keys().forEach{ itemKey ->
                val obj = items.getJSONObject(itemKey);
                val label = obj.getString("label");
                val btn = createButton(label,"vertical")
                val payload = obj.getString("payload")
                btn.setOnClickListener{
                    if(obj.has("parameters")){
                        parameterDialog(obj, payload)
                    }else{
                        payload(payload)
                    }
                }
                btn.setBackgroundColor(Color.DKGRAY)
                btn.setTextColor(Color.WHITE)

                formLayout.addView(btn)
            }
        } catch (e:Exception){
            Log.e("actions", "no items", e)
        }
        return formLayout
    }
    private fun parameterDialog(obj:JSONObject,payload: String){
        val layout = createLayout("vertical",true)
        val parameters = obj.getJSONObject("parameters")
        val parameterValues = mutableMapOf<String, Any>()
        parameters.keys().forEach { paramKey ->
            val keyTextView = TextView(this@Actions)
            keyTextView.text = paramKey
            layout.addView(keyTextView)
            val paramObj = parameters.getJSONObject(paramKey)
            val input: View = when (val paramType = paramObj.getString("type")) {
                "text" -> EditText(this@Actions)
                "int" -> EditText(this@Actions)
                "checksum" -> CheckBox(this@Actions)
                else -> throw IllegalArgumentException("Unsupported parameter type: $paramType")
            }
            layout.addView(input)
            if (input is EditText) {
                input.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(c: CharSequence?, s: Int, b: Int, cn: Int) {}
                    override fun onTextChanged(c: CharSequence?, s: Int, b: Int, cn: Int) {}
                    override fun afterTextChanged(s: Editable?) {
                        parameterValues[paramKey] = s.toString()
                    }
                })
            } else if (input is CheckBox) {
                input.setOnCheckedChangeListener { _, isChecked ->
                    parameterValues[paramKey] = isChecked
                }
            }
        }
        val dialog = AlertDialog.Builder(this@Actions)
        dialog.setPositiveButton("OK") { _, _ ->
            var updatedPayload = payload
            parameterValues.forEach { (key, value) ->
                updatedPayload = updatedPayload.replace("{$key}", value.toString())
            }
            payload(updatedPayload)
        }
        dialog.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }
        dialog.setView(layout)
        dialog.show()
    }
    private  fun payload(payload:String){
        Toast.makeText(this, payload, Toast.LENGTH_SHORT).show()
    }
    private fun createLayout(orientation:String,visible:Boolean):LinearLayout{
        val linearLayout = LinearLayout(this)
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(16.dp,16.dp,16.dp,16.dp)
        linearLayout.id = View.generateViewId()
        linearLayout.layoutParams = layoutParams
        linearLayout.isVisible=visible
        when(orientation){
            "vertical"->{
                linearLayout.orientation=LinearLayout.VERTICAL
                linearLayout.gravity=Gravity.CENTER_HORIZONTAL
            }
            else->{
                linearLayout.orientation=LinearLayout.HORIZONTAL
                linearLayout.gravity=Gravity.CENTER_VERTICAL
            }
        }
        return linearLayout
    }
    private val Int.dp: Int
        get() = (this * Resources.getSystem().displayMetrics.density).toInt()
}
