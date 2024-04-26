package com.smaart.idrico.view
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.smaart.idrico.R
import com.smaart.idrico.model.Base
import com.smaart.idrico.model.Swipe
import com.smaart.idrico.model.SwipeActions
import org.json.JSONObject

class Actions:Base(), SwipeActions {
    private var layout:String=""
    private var curentLayoutKey:String=""
    private lateinit var parentView:LinearLayout
    private lateinit var swipe: Swipe
    override fun onSwipeRight() {
        Log.e("TEST", "onSwipeRight LOG FROM THE INTERFACE")
        // Add your logic for right swipe
    }

    override fun onSwipeLeft() {
        Log.e("TEST", "onSwipeLeft LOG FROM THE INTERFACE")
    }

    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actions)
        parentView=findViewById(R.id.view)
        layout=intent.getStringExtra("layout")!!
        val jsonLayout=JSONObject(layout)
        var isFirst=true
        val parentLayout=createLayout("horizontal",true)
        val formLayouts:MutableMap<String,LinearLayout> = mutableMapOf()
        val formBtns:MutableMap<String,Button> = mutableMapOf()
        jsonLayout.keys().forEach { key->
            val formObj=jsonLayout.getJSONObject(key)
            try {
                val formLayout=createLayout("vertical",isFirst)
                formLayouts[key]=createActions(formObj,formLayout)
                val btn=createButton(key,"horizontal")
                if(isFirst){
                    isFirst=false
                    btn.setBackgroundResource(R.drawable.btn_clicked)
                }else{
                    btn.setBackgroundResource(R.drawable.btn_not_clicked)
                }
                formBtns[key]=btn
            } catch (e:Exception) {
                Log.e("actions", "no items", e)
            }
        }
        formBtns.keys.forEach { key ->
            val btn=formBtns[key]
            val formLayout=formLayouts[key]
            btn?.setOnClickListener {
                if (formLayout != null) btnLogic(btn,formBtns,formLayout,formLayouts,key)
            }
            parentLayout.addView(btn)
        }
        parentView.addView(parentLayout)
        parentView.addView(createBorder())
        parentView.setOnTouchListener(SwipeActions(this))
        formLayouts.keys.forEach{key-> parentView.addView(formLayouts[key])}
    }
    private fun btnLogic(
        btn:Button,
        formBtns:MutableMap<String,Button>,
        formLayout:View,
        formLayouts: MutableMap<String, LinearLayout>,
        key:String){
        btn.setBackgroundResource(R.drawable.btn_clicked)
        formLayout.visibility = View.VISIBLE
        formLayouts.keys.forEach { formKey ->
            if (key != formKey) {
                val otherFormLayout = formLayouts[formKey]
                otherFormLayout?.visibility = View.GONE
            }
        }
        formBtns.keys.forEach { k ->
            if (k != key) {
                val b = formBtns[k]
                b?.setBackgroundResource(R.drawable.btn_not_clicked)
            }
        }
    }
    private fun createActions(parentObj:JSONObject,formLayout:LinearLayout):LinearLayout{
        try {
            val items=parentObj.getJSONObject("items")
            items.keys().forEach{ itemKey ->
                val obj=items.getJSONObject(itemKey)
                val label=obj.getString("label")
                val btn=createButton(label,"actions")
                btn.setBackgroundResource(R.drawable.btn)
                btn.setTextColor(Color.WHITE)
                val payload=obj.getString("payload")
                btn.setOnClickListener{
                    if(obj.has("parameters")){
                        parameterDialog(obj,payload)
                    }else{
                        payload(payload)
                    }
                }
                formLayout.addView(btn)
            }
        } catch (e:Exception){
            Log.e("actions", "no items", e)
        }
        return formLayout
    }
    private fun parameterDialog(obj:JSONObject,payload:String){
        val layout=createLayout("vertical",true)
        val parameters=obj.getJSONObject("parameters")
        val parameterValues=mutableMapOf<String,Any>()
        parameters.keys().forEach { paramKey ->
            val keyTextView=TextView(this@Actions)
            keyTextView.text=paramKey
            layout.addView(keyTextView)
            val paramObj=parameters.getJSONObject(paramKey)
            val input:View = when (val paramType=paramObj.getString("type")) {
                "text" -> EditText(this@Actions)
                "int" -> EditText(this@Actions)
                "checksum" -> CheckBox(this@Actions)
                else -> throw IllegalArgumentException("Unsupported parameter type: $paramType")
            }
            layout.addView(input)
            if (input is EditText) {
                input.addTextChangedListener(object:TextWatcher {
                    override fun beforeTextChanged(c:CharSequence?,s:Int,b:Int,cn:Int) {}
                    override fun onTextChanged(c:CharSequence?,s:Int,b:Int,cn:Int) {}
                    override fun afterTextChanged(s:Editable?) {
                        parameterValues[paramKey]=s.toString()
                    }
                })
            } else if (input is CheckBox) {
                input.setOnCheckedChangeListener { _,isChecked ->
                    parameterValues[paramKey]=isChecked
                }
            }
        }
        val dialog=AlertDialog.Builder(this@Actions)
        dialog.setPositiveButton("OK") { _, _ ->
            var updatedPayload=payload
            parameterValues.forEach { (key,value) ->
                updatedPayload=updatedPayload.replace("{$key}",value.toString())
            }
            payload(updatedPayload)
        }
        dialog.setNegativeButton("Cancel") { d, _ ->
            d.cancel()
        }
        dialog.setView(layout)
        dialog.show()
    }
    private  fun payload(payload:String){
        Toast.makeText(this,payload,Toast.LENGTH_SHORT).show()
    }

}
