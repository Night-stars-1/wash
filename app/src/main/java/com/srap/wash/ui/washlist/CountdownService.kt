package com.srap.wash.ui.washlist

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.app.Service.START_STICKY
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.srap.wash.utils.TimeUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CountdownService : Service() {

    private val channelId = "countdown_channel"
    private var timeStamp: Long = 0
    private var allTimeStamp: Long = 0
    private var id: Int = 0
    private var job: Job? = null

    override fun onCreate() {
        super.onCreate()
        initNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        timeStamp = intent?.getLongExtra("timeStamp", 0) ?: 0
        allTimeStamp = intent?.getLongExtra("allTimeStamp", 0) ?: 0
        id = intent?.getIntExtra("id", 0) ?: 0

        // 启动前台服务
        startForeground(1, createNotification(timeStamp, allTimeStamp))

        // 启动倒计时任务
        job = CoroutineScope(Dispatchers.Main).launch {
            startCountdown()
        }

        return START_STICKY // 如果服务被杀死，系统会自动重启服务
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel() // 取消倒计时任务
    }

    // 初始化通知渠道
    private fun initNotificationChannel() {
        val name = "洗衣倒计时"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, name, importance)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    // 创建通知内容
    private fun createNotification(timeStamp: Long, allTimeStamp: Long): Notification {
        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("洗衣倒计时")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentText("剩余洗衣时间：${TimeUtil.getDateTime(timeStamp)}")
            .setOngoing(true)
            .setProgress(100, (timeStamp.toFloat() / allTimeStamp.toFloat() * 100).toInt(), false) // 设置进度条
            .build()
    }

    // 更新通知内容
    private fun updateNotification(timeStamp: Long, allTimeStamp: Long) {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val notification = createNotification(timeStamp, allTimeStamp)
        notificationManager.notify(1, notification) // 更新通知
    }

    // 执行倒计时任务
    private suspend fun startCountdown() {
        while (timeStamp > 0) {
            sendTimeUpdateBroadcast(timeStamp)
            delay(1000) // 每秒减少
            timeStamp -= 1

            updateNotification(timeStamp, allTimeStamp)
        }

        // 停止前台服务
        stopForeground(STOP_FOREGROUND_DETACH)
    }

    private fun sendTimeUpdateBroadcast(timeLeft: Long) {
        val intent = Intent("com.srap.wash.UPDATE_TIME").apply {
            putExtra("residualTime", timeLeft)
            putExtra("id", id)
        }
        sendBroadcast(intent)
    }
}
