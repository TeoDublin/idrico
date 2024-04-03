package com.smaart.idrico.view

import android.app.ActionBar.LayoutParams
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
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.view.marginTop
import androidx.core.view.setMargins
import com.smaart.idrico.model.Payload


class Actions : AppCompatActivity() {
//    private var layout: String = ""
private val layout = "{\n" +
        "    \"actions\": {\n" +
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
        "    }\n" +
        "}"
    private var jsonLayout = JSONObject(layout)
    private lateinit var keys: MutableIterator<String>
    private lateinit var parentView: LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actions)
//        layout = intent.getStringExtra("layout")!!
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
                btn.tag = obj.getString("payload")
                btn.setBackgroundColor(Color.DKGRAY)
                btn.setTextColor(Color.WHITE)
                btn.setOnClickListener{
                    Toast.makeText(this, btn.tag.toString(), Toast.LENGTH_SHORT).show()
                }
                formLayout.addView(btn)
            }
        } catch (e:Exception){
            Log.e("actions", "no items", e)
        }
        return formLayout
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
    val Int.dp: Int
        get() = (this * Resources.getSystem().displayMetrics.density).toInt()
}
