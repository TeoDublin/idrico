package com.smaart.idrico.view
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
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
import org.json.JSONObject
import kotlin.math.abs
class Actions:Base(),View.OnTouchListener {
    private var layout:String=""
    private var currentKey:String=""
    private lateinit var parentView:LinearLayout
    private lateinit var gestureDetector: GestureDetector
    private var isFirst=true
    private var formLayouts:MutableMap<String,LinearLayout> = mutableMapOf()
    private var formBtns:MutableMap<String,Button> = mutableMapOf()
    private var keyList = mutableListOf<String>()
    override fun onTouch(view: View, event: MotionEvent): Boolean {
        view.performClick()
        return gestureDetector.onTouchEvent(event)
    }
    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actions)
        parentView=findViewById(R.id.view)
        layout=intent.getStringExtra("layout")!!
//        layout="{\n" +
//                "\t\"common\": {\n" +
//                "\t\t\"label\": \"Common\",\n" +
//                "\t\t\"items\": {\n" +
//                "\t\t\t\"open\": {\n" +
//                "\t\t\t\t\"label\": \"Open\",\n" +
//                "\t\t\t\t\"payload\": \"6810FFFFFFFF0011110404A0170055AA16\"\n" +
//                "\t\t\t}\n" +
//                "\t\t}\n" +
//                "\t},\n" +
//                "\t\"test\": {\n" +
//                "\t\t\"label\": \"test\",\n" +
//                "\t\t\"items\": {\n" +
//                "\t\t\t\"open\": {\n" +
//                "\t\t\t\t\"label\": \"Open\",\n" +
//                "\t\t\t\t\"payload\": \"6810FFFFFFFF0011110404A0170055AA16\"\n" +
//                "\t\t\t}\n" +
//                "\t\t}\n" +
//                "\t},\t\n" +
//                "\t\"test2\": {\n" +
//                "\t\t\"label\": \"test2\",\n" +
//                "\t\t\"items\": {\n" +
//                "\t\t\t\"open\": {\n" +
//                "\t\t\t\t\"label\": \"Open\",\n" +
//                "\t\t\t\t\"payload\": \"6810FFFFFFFF0011110404A0170055AA16\"\n" +
//                "\t\t\t}\n" +
//                "\t\t}\n" +
//                "\t},\t\n" +
//                "\t\"network\": {\n" +
//                "\t\t\"label\": \"Network\",\n" +
//                "\t\t\"items\": {\n" +
//                "\t\t\t\"setIP\": {\n" +
//                "\t\t\t\t\"label\": \"Set IP/Port\",\n" +
//                "\t\t\t\t\"payload\": \"6810{MeterID}001111D216D00101070004{IP1}{PORT1}{IP2}{PORT2}{CHK}16\",\n" +
//                "\t\t\t\t\"parameters\": {\n" +
//                "\t\t\t\t\t\"MeterID\": {\n" +
//                "\t\t\t\t\t\t\"label\": \"S/N\",\n" +
//                "\t\t\t\t\t\t\"type\": \"text\",\n" +
//                "\t\t\t\t\t\t\"value\": \"equal\",\n" +
//                "\t\t\t\t\t\t\"required\": \"^[0-9]{8}\$\"\n" +
//                "\t\t\t\t\t},\n" +
//                "\t\t\t\t\t\"CHK\": {\n" +
//                "\t\t\t\t\t\t\"type\": \"checksum\"\n" +
//                "\t\t\t\t\t},\n" +
//                "\t\t\t\t\t\"IP1\": {\n" +
//                "\t\t\t\t\t\t\"label\": \"IP 1\",\n" +
//                "\t\t\t\t\t\t\"type\": \"text\",\n" +
//                "\t\t\t\t\t\t\"value\": \"IP\",\n" +
//                "\t\t\t\t\t\t\"required\": \"^[0-9]{1,3}\\\\.[0-9]{1,3}\\\\.[0-9]{1,3}\\\\.[0-9]{1,3}\$\"\n" +
//                "\t\t\t\t\t},\n" +
//                "\t\t\t\t\t\"PORT1\": {\n" +
//                "\t\t\t\t\t\t\"label\": \"Port 1\",\n" +
//                "\t\t\t\t\t\t\"type\": \"int\",\n" +
//                "\t\t\t\t\t\t\"min\": 1,\n" +
//                "\t\t\t\t\t\t\"max\": 65535,\n" +
//                "\t\t\t\t\t\t\"value\": \"int4\",\n" +
//                "\t\t\t\t\t\t\"required\": \"^[0-9]+\$\"\n" +
//                "\t\t\t\t\t},\n" +
//                "\t\t\t\t\t\"IP2\": {\n" +
//                "\t\t\t\t\t\t\"label\": \"IP 2\",\n" +
//                "\t\t\t\t\t\t\"type\": \"text\",\n" +
//                "\t\t\t\t\t\t\"value\": \"IP\",\n" +
//                "\t\t\t\t\t\t\"required\": \"^[0-9]{1,3}\\\\.[0-9]{1,3}\\\\.[0-9]{1,3}\\\\.[0-9]{1,3}\$\"\n" +
//                "\t\t\t\t\t},\n" +
//                "\t\t\t\t\t\"PORT2\": {\n" +
//                "\t\t\t\t\t\t\"label\": \"Port 2\",\n" +
//                "\t\t\t\t\t\t\"type\": \"int\",\n" +
//                "\t\t\t\t\t\t\"min\": 1,\n" +
//                "\t\t\t\t\t\t\"max\": 65535,\n" +
//                "\t\t\t\t\t\t\"value\": \"int4\",\n" +
//                "\t\t\t\t\t\t\"required\": \"^[0-9]+\$\"\n" +
//                "\t\t\t\t\t}\n" +
//                "\t\t\t\t}\n" +
//                "\t\t\t}\n" +
//                "\t\t}\n" +
//                "\t}\n" +
//                "}"
        gestureDetector = GestureDetector(this, GestureListener())
        val jsonLayout=JSONObject(layout)
        val parentLayout=createLayout("horizontal",true)
        jsonLayout.keys().forEach { key->
            val formObj=jsonLayout.getJSONObject(key)
            try {
                val formLayout=createLayout("vertical",isFirst)
                formLayouts[key]=createActions(formObj,formLayout)
                keyList.add(key)
                val btn=createButton(key,"horizontal")
                if(isFirst){
                    currentKey=key
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
                if (formLayout != null) btnLogic(key)
            }
            parentLayout.addView(btn)
        }
        parentView.addView(parentLayout)
        parentView.addView(createBorder())
        parentView.setOnTouchListener(this)
        formLayouts.keys.forEach{key-> parentView.addView(formLayouts[key])}
    }
    private fun btnLogic(key:String){
        currentKey=key
        val btn:Button?=formBtns[key]
        val formLayout:View?=formLayouts[key]
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
    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        private val SWIPE_THRESHOLD = 100
        private val SWIPE_VELOCITY_THRESHOLD = 100
        override fun onDown(e: MotionEvent): Boolean {
            return true
        }
        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            val diffX = e2.x - e1?.x!!
            val diffY = e2.y - e1.y
            parentView.performClick()
            if (abs(diffX) > abs(diffY)) {
                if (abs(diffX) > SWIPE_THRESHOLD && abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) toTheLeft() else toTheRight()
                    return true
                }
            }
            return false
        }
    }
    private fun toTheLeft(){
        val index=keyList.indexOf(currentKey)
        val next=if(index-1<0)index else index-1
        btnLogic(keyList[next])
    }
    private fun toTheRight(){
        val index=keyList.indexOf(currentKey)
        val next=if(index+1>=keyList.size)index else index+1
        btnLogic(keyList[next])
    }
}
