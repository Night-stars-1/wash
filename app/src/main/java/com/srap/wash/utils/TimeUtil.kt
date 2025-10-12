package com.srap.wash.utils

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

object TimeUtil {
    fun calculateTimeDifference(createTimeStr: String, finishTimeStr: String): Long {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

        // 解析时间字符串为 Date 对象
        val createTime = dateFormat.parse(createTimeStr)
        val finishTime = dateFormat.parse(finishTimeStr)

        // 如果时间解析成功，计算时间差
        return if (createTime != null && finishTime != null) {
            val timeDiffMillis = finishTime.time - createTime.time // 毫秒差
            val seconds = timeDiffMillis / 1000
            return seconds
        } else {
            -1
        }
    }

    fun getTimeDifferenceWithCurrent(finishTimeStr: String): Long {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

        // 解析 finishTimeStr 为 Date 对象
        val finishTime = dateFormat.parse(finishTimeStr)

        // 如果时间解析成功，计算与当前时间的时间差
        return if (finishTime != null) {
            val currentTimeMillis = System.currentTimeMillis() // 当前时间的毫秒时间戳
            val finishTimeMillis = finishTime.time // finishTimeStr 转换后的毫秒时间戳
            val timeDiffMillis = finishTimeMillis - currentTimeMillis // 时间差（毫秒）

            // 返回时间差（秒）
            val timeDiffSeconds = timeDiffMillis / 1000
            timeDiffSeconds
        } else {
            -1
        }
    }

    fun getDateTime(timestampInSeconds: Long): String {
        // 将秒级时间戳转换为 Instant 对象
        val instant = Instant.ofEpochSecond(timestampInSeconds)

        // 设置日期格式和时区
        val formatter = DateTimeFormatter.ofPattern("mm:ss").withZone(ZoneId.systemDefault())

        // 格式化为字符串并返回
        return formatter.format(instant)
    }
}