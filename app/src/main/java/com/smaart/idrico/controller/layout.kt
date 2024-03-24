package com.smaart.idrico.controller

import android.content.Context
import com.google.gson.internal.LinkedTreeMap
import com.smaart.idrico.model.Api
import com.smaart.idrico.model.DAO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Layout(){
    fun get(activity:String,context: Context):String?{
        val api = Api()
        val dao = DAO(context)
        var layout = dao.get(activity)
        if(!layout.isNullOrEmpty()){
            return layout
        }else{
            when(activity){
                "com.smaart.idrico.view.Actions"->{
                    if(!layout.isNullOrEmpty()){
                        return layout
                    }else{
                        CoroutineScope(Dispatchers.IO).launch {
                            val operations=api.actions(dao.get("Token"))
                            layout = ""
                            operations?.forEach { (key, value) ->
                                if (value is LinkedTreeMap<*, *>) {
                                    val label = value["label"] as? String
                                    if (!label.isNullOrEmpty()) {
                                        layout+= "Key: $key, Label: $label\n"
                                    }
                                }
                            }
                            dao.save(activity, layout!!)
                        }
                    }
                }
                else->{
                    return "none"
                }
            }
        }
        return null
    }
}