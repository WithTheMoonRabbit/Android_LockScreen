package com.moonrabbit.ghost

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log

class LockReceiver : BroadcastReceiver() {

    private val TAG = "SY"

    override fun onReceive(context: Context, intent: Intent) {

        // 1. SCREEN_OFF 이벤트 수신
        if (intent.action == Intent.ACTION_SCREEN_OFF) {
            Log.i(TAG, "화면OFF 이벤트수신")
            val i = Intent(context, LockActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or     // 새로운 Activity로 실행
                    Intent.FLAG_ACTIVITY_SINGLE_TOP or      // 최상위 Activity로 실행
                    Intent.FLAG_ACTIVITY_CLEAR_TASK         // 현재 Activity 외 다른 Activity 기록삭제
            context.startActivity(i)
        }

        // 2. BOOT_COMPLETED 이벤트 수신
        else if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            Log.i(TAG, "부팅 이벤트수신")
            val i = Intent(context, LockService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(i)
            } else {
                context.startService(i)
            }
        }

        // 3. PACKAGE_REPLACED 이벤트 수신
        else if (intent.action == Intent.ACTION_MY_PACKAGE_REPLACED) {
            Log.i(TAG, "앱 재 설치")
            val i = Intent(context, LockService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(i)
            } else {
                context.startService(i)
            }
        }
    }
}