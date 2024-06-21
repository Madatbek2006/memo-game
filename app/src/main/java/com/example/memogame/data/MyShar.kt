package com.example.memogame.data

import android.content.Context
import android.content.SharedPreferences

object MyShar {
    private lateinit var sharedPreferences:SharedPreferences
    fun init(context: Context){
        sharedPreferences=context.getSharedPreferences("Memo",Context.MODE_PRIVATE)
    }

    fun saveLVL(category:String, lvl:Int){
        sharedPreferences.edit().putInt(category,lvl).apply()
    }
    fun getLvl(category: String):Int{
        return sharedPreferences.getInt(category,1)
    }
    fun getMedia():Boolean{
        return sharedPreferences.getBoolean("media",true)
    }

    fun setMedia(boolean: Boolean){
        sharedPreferences.edit().putBoolean("media",boolean)
    }
}