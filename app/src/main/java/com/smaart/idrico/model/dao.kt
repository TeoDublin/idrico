package com.smaart.idrico.model
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap

class DAO(applicationContext:Context) {
    private val gson=Gson()
    private val prefix="SHARED_PREFERENCES"
    private val sharedPrefs: SharedPreferences=applicationContext.getSharedPreferences(prefix,Context.MODE_PRIVATE)
    fun save(key:String,value:String) { sharedPrefs.edit().putString(key, value).apply() }
    fun save(save:Map<String,String>){ save.forEach{(key,value)->this.save(key,value)} }
    fun save(key:String,value:LinkedTreeMap<String,Any>?) { this.save(key,gson.toJson(value)) }
    fun get(key:String):String?{ return sharedPrefs.getString(key, null) }
    fun clearAll(){ sharedPrefs.edit().clear().apply() }
}
