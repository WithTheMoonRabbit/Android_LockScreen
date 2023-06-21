package com.moonrabbit.ghost

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat

class LockService : Service() {

    private val TAG = "SY"
    private var mReceiver: LockReceiver? = null

    @Nullable
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "onCreate 서비스 시작")

        mReceiver = LockReceiver()
        val filter = IntentFilter(Intent.ACTION_SCREEN_OFF)
        filter.addAction(Intent.ACTION_MY_PACKAGE_REPLACED)
        filter.addAction(Intent.ACTION_BOOT_COMPLETED)
        registerReceiver(mReceiver, filter)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        Log.i(TAG, "onStartCommand 서비스 시작")

        if (intent != null) {
            if (intent.action == null) {
                if (mReceiver == null) {
                    Log.i(TAG, "onStartCommand receiver null")

                    // 1. 리시버 생성
                    mReceiver = LockReceiver()
                    val filter = IntentFilter(Intent.ACTION_SCREEN_OFF)
                    filter.addAction(Intent.ACTION_MY_PACKAGE_REPLACED)
                    filter.addAction(Intent.ACTION_BOOT_COMPLETED)
                    registerReceiver(mReceiver, filter)
                }
            }
        }
        Log.i(TAG, "foreground start")
        initializeNotification()

        return START_NOT_STICKY
    }

    // 2. ForegroundService 실행
    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun initializeNotification() {
        val builder = NotificationCompat.Builder(this, "1")
        builder.setSmallIcon(R.mipmap.ic_launcher)
        val style = NotificationCompat.BigTextStyle()
        style.bigText("설정을 보려면 누르세요")
        style.setBigContentTitle(null)
        style.setSummaryText("서비스 동작중")
        builder.setContentText(null)
        builder.setContentTitle(null)
        builder.setOngoing(true)
        builder.setStyle(style)
        builder.setWhen(0)
        builder.setShowWhen(false)

        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)
        builder.setContentIntent(pendingIntent)
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager // getSystemService 메서드 사용 변경
        if (Build.VERSION_CODES.O <= Build.VERSION.SDK_INT) {
            manager.createNotificationChannel(NotificationChannel("1", "포그라운드 서비스", NotificationManager.IMPORTANCE_NONE))
        }
        val notification: Notification = builder.build()
        startForeground(1, notification)
    }

    // 3. 서비스 종료
    override fun onDestroy() {
        if (mReceiver != null) {
            unregisterReceiver(mReceiver)
            Log.i(TAG, "서비스 종료, 리시버 종료")
        }
        super.onDestroy()
    }
}