package com.smaart.idrico.model

object Payload {
    lateinit var serial:String
    fun set(serial:String){
        this.serial=serial
    }
    fun get():String{
        return this.serial
    }
}