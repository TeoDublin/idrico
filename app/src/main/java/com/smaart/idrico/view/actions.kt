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
import com.smaart.idrico.model.setOnSwipeListener
import org.json.JSONObject

class Actions:Base() {
    private var layout:String=""
    private lateinit var parentView:LinearLayout
    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actions)
        parentView=findViewById(R.id.view)
//        layout=intent.getStringExtra("layout")!!
        layout = "{\n" +
                "        \"common\": {\n" +
                "            \"label\": \"Common\",\n" +
                "            \"items\": {\n" +
                "                \"open\": {\n" +
                "                    \"label\": \"Open\",\n" +
                "                    \"payload\": \"6810FFFFFFFF0011110404A0170055AA16\"\n" +
                "                },\n" +
                "                \"close\": {\n" +
                "                    \"label\": \"Close\",\n" +
                "                    \"payload\": \"6810FFFFFFFF0011110404A0170055AA16\"\n" +
                "                }\n" +
                "            }\n" +
                "        },\n" +
                "        \"network\": {\n" +
                "            \"label\": \"Network\",\n" +
                "            \"items\": {\n" +
                "                \"setIP\": {\n" +
                "                    \"label\": \"Set IP/Port\",\n" +
                "                    \"payload\": \"6810{MeterID}001111D216D00101070004{IP1}{PORT1}{IP2}{PORT2}{CHK}16\",\n" +
                "                    \"parameters\": {\n" +
                "                        \"MeterID\": {\n" +
                "                            \"label\": \"S/N\",\n" +
                "                            \"type\": \"text\",\n" +
                "                            \"value\": \"equal\",\n" +
                "                            \"required\": \"^[0-9]{8}\$\"\n" +
                "                        },\n" +
                "                        \"CHK\": {\n" +
                "                            \"type\": \"checksum\"\n" +
                "                        },\n" +
                "                        \"IP1\": {\n" +
                "                            \"label\": \"IP 1\",\n" +
                "                            \"type\": \"text\",\n" +
                "                            \"value\": \"IP\",\n" +
                "                            \"required\": \"^[0-9]{1,3}\\\\.[0-9]{1,3}\\\\.[0-9]{1,3}\\\\.[0-9]{1,3}\$\"\n" +
                "                        },\n" +
                "                        \"PORT1\": {\n" +
                "                            \"label\": \"Port 1\",\n" +
                "                            \"type\": \"int\",\n" +
                "                            \"min\": 1,\n" +
                "                            \"max\": 65535,\n" +
                "                            \"value\": \"int4\",\n" +
                "                            \"required\": \"^[0-9]+\$\"\n" +
                "                        },\n" +
                "                        \"IP2\": {\n" +
                "                            \"label\": \"IP 2\",\n" +
                "                            \"type\": \"text\",\n" +
                "                            \"value\": \"IP\",\n" +
                "                            \"required\": \"^[0-9]{1,3}\\\\.[0-9]{1,3}\\\\.[0-9]{1,3}\\\\.[0-9]{1,3}\$\"\n" +
                "                        },\n" +
                "                        \"PORT2\": {\n" +
                "                            \"label\": \"Port 2\",\n" +
                "                            \"type\": \"int\",\n" +
                "                            \"min\": 1,\n" +
                "                            \"max\": 65535,\n" +
                "                            \"value\": \"int4\",\n" +
                "                            \"required\": \"^[0-9]+\$\"\n" +
                "                        }\n" +
                "                    }\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "}"
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
            val swipeListener = {
                Log.e("TEST","TEST")
                btn?.setBackgroundResource(R.drawable.btn_clicked)
                formLayout?.visibility = View.VISIBLE
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
            btn?.setOnSwipeListener(swipeListener)
            btn?.setOnClickListener {
                btn.setBackgroundResource(R.drawable.btn_clicked)
                formLayout?.visibility = View.VISIBLE
                formLayouts.keys.forEach { formKey ->
                    if (key != formKey) {
                        val otherFormLayout = formLayouts[formKey]
                        otherFormLayout?.visibility = View.GONE
                    }else{
                        formLayout?.setOnSwipeListener(swipeListener)
                        swipeListener.invoke()
                    }
                }
                formBtns.keys.forEach { k ->
                    if (k != key) {
                        val b = formBtns[k]
                        b?.setBackgroundResource(R.drawable.btn_not_clicked)
                    }
                }
            }
            parentLayout.addView(btn)
        }
        parentView.addView(parentLayout)
        parentView.addView(createBorder())

        formLayouts.keys.forEach{key-> parentView.addView(formLayouts[key])}
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
