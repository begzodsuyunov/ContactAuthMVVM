package com.example.contactauth.data.local.shp

import android.content.Context
import android.content.SharedPreferences

class MySharedPreference private constructor(context: Context) {

    var token: String
        get() = sharedPreferences.getString("token", "").toString()
        set(token) {
            editor.putString("token", token).apply()
        }

    var users: String
        get() = sharedPreferences.getString("users", "").toString()
        set(users) {
            editor.putString("users", users).apply()
        }

    var isFirst: Boolean
        get() = sharedPreferences.getBoolean("is_first", false)
        set(isFirst) {
            editor.putBoolean("is_first", isFirst).apply()
        }

    companion object {
        var mySharedPreference: MySharedPreference? = null
        lateinit var sharedPreferences: SharedPreferences
        lateinit var editor: SharedPreferences.Editor
        fun init(context: Context): MySharedPreference? {
            if (mySharedPreference == null) {
                mySharedPreference = MySharedPreference(context)
            }
            return mySharedPreference
        }

        fun getInstance() = mySharedPreference!!
    }

    init {
        sharedPreferences = context.getSharedPreferences("app_name", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }

}