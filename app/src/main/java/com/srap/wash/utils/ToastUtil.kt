package com.srap.wash.utils

import android.widget.Toast
import com.srap.wash.myApplication

object ToastUtil {
    fun show(msg: String) {
        Toast.makeText(myApplication, msg, Toast.LENGTH_SHORT).show()
    }
}