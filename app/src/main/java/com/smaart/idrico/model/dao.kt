package com.smaart.idrico.model
import android.content.Context
import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap

class DAO constructor(private val context: Context) {
    private val gson = Gson()
    private val prefix = "SHARED_PREFERENCES"
    fun save(key: String, value:String) {
        val sharedPrefs = context.getSharedPreferences(prefix, Context.MODE_PRIVATE)
        sharedPrefs.edit().putString(key, value).apply()
    }
    fun get(key:String): String? {
        val sharedPrefs = context.getSharedPreferences(prefix, Context.MODE_PRIVATE)
        return sharedPrefs.getString(key, null)
    }
    fun getAll(): Map<String, String> {
        val sharedPrefs = context.getSharedPreferences(prefix, Context.MODE_PRIVATE)
        val allValues = mutableMapOf<String, String>()
        sharedPrefs.all.forEach { (key, value) -> allValues[key] = value.toString() }
        return allValues
    }
    fun saveJson(key:String, value: LinkedTreeMap<String, Any>?) {
        val jsonString = gson.toJson(value)
        this.save(key,jsonString)
    }
    fun clearAll() {
        val sharedPrefs = context.getSharedPreferences(prefix, Context.MODE_PRIVATE)
        sharedPrefs.edit().clear().apply()
    }
}
