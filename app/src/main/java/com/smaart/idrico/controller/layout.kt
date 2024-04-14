package com.smaart.idrico.controller
import android.content.Context
import com.smaart.idrico.model.Api
import com.smaart.idrico.model.DAO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
class Layout(){
    fun get(activity:String,useApi:String,context:Context):String? {
        val api=Api()
        val dao=DAO(context)
        val layout=dao.get(activity)
        if(!layout.isNullOrEmpty()){
            return layout
        }else{
            CoroutineScope(Dispatchers.IO).launch {
                when(useApi){
                    "actions"->{dao.save(activity,api.actions(dao.get("token")))}
                }
            }
        }
        return null
    }
}