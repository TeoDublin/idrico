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
import androidx.core.view.marginTop
import androidx.core.view.setMargins


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
        var hasFormElements = false
        val parentLayout=createLayout("horizontal")
        var formLayout=createLayout("vertical")
        actionsObject.keys().forEach { key->
            parentLayout.addView(createButton(key,"horizontal"))
            if(isFirst){
                isFirst=false
                val formObj=actionsObject.getJSONObject(key)
                try {
                    formLayout=createFormButton(formObj,formLayout)
                    hasFormElements=true
                } catch (e:Exception) {
                    hasFormElements=false
                    Log.e("actions", "no items", e)
                }
            }
        }
        parentView.addView(parentLayout)
        if(hasFormElements) parentView.addView(formLayout)
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
                val button = createButton(label,"vertical")
                button.setBackgroundColor(Color.DKGRAY)
                button.setTextColor(Color.WHITE)
                formLayout.addView(button)
            }
        } catch (e:Exception){
            Log.e("actions", "no items", e)
        }
        return formLayout
    }
    private fun createLayout(orientation:String):LinearLayout{
        val linearLayout = LinearLayout(this)
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(16.dp,16.dp,16.dp,16.dp)
        linearLayout.id = View.generateViewId()
        linearLayout.layoutParams = layoutParams
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
