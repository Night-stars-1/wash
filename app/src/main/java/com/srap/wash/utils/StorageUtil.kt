package com.srap.wash.utils

import android.content.Context
import android.content.SharedPreferences
import com.srap.wash.myApplication

object StorageUtil {
    private val pref: SharedPreferences = myApplication.baseContext.getSharedPreferences("washPrefs", Context.MODE_PRIVATE)
    private const val TOKEN = "Token"

//    var Token: String
//        get() = pref.getString(TOKEN, "").toString()
//        set(value) = pref.edit().putString(TOKEN, value).apply()

    private var _token = pref.getString(TOKEN, "").toString()

    var Token: String
        get() = _token
        set(value) {
            _token = value
            pref.edit().putString(TOKEN, value).apply()
        }
}