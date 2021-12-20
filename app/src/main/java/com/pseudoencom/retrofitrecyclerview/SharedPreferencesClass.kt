package com.pseudoencom.retrofitrecyclerview

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesClass{
    companion object{
        fun setValue(context: Context, value: Int){
            val sharedPreferences: SharedPreferences = context.getSharedPreferences("setValue", Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putInt("saveValue",value); editor.apply()
        }
        fun getValue(context: Context): Int{
            val sharedPreferences: SharedPreferences = context.getSharedPreferences("getValue", Context.MODE_PRIVATE)
            return sharedPreferences.getInt("saveValue", 0)
        }
    }
}