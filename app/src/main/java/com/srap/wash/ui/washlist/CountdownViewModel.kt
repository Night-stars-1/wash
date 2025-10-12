package com.srap.wash.ui.washlist

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@HiltViewModel
class CountdownViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    var residualTime by mutableLongStateOf(0)

    fun startCountdown(id: Int, timeStamp: Long, allTimeStamp: Long) {
        residualTime = timeStamp

        // 通过 Intent 启动服务
        val intent = Intent(context, CountdownService::class.java).apply {
            putExtra("timeStamp", timeStamp)
            putExtra("allTimeStamp", allTimeStamp)
            putExtra("id", id)
        }
        context.startService(intent)
    }
}
