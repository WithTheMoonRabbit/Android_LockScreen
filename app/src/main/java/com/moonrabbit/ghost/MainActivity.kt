package com.moonrabbit.ghost

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private val TAG = "SY"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. 버튼 연동
        val startService = findViewById<Button>(R.id.button)
        val stopService = findViewById<Button>(R.id.button2)

        // 2. 서비스실행 버튼 클릭 이벤트
        startService.setOnClickListener {
            if (checkPermission()) {
                val intent = Intent(applicationContext, LockService::class.java)
                startService(intent)
            } else {
                Log.i(TAG, "다른앱 위에 그리기 권한 필요. 서비스실행 불가")
            }
        }

        // 2. 서비스종료 버튼 클릭 이벤트
        stopService.setOnClickListener {
            val intent = Intent(applicationContext, LockService::class.java)
            stopService(intent)
        }
    }

    // 3. 권한체크 및 요청
    private fun checkPermission(): Boolean {
        if (!Settings.canDrawOverlays(this)) {              // 다른앱 위에 그리기 체크
            Log.i(TAG, "다른앱 위에 그리기 권한 없음")
            Toast.makeText(this, "다른앱 위에 표시 권한을 허용해 주세요", Toast.LENGTH_LONG).show()
            val uri = Uri.fromParts("package", packageName, null)
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, uri)
            startActivityForResult(intent, 1)
            return false
        } else {
            Log.i(TAG, "다른앱 위에 그리기 권한이 있음")
            return true
        }
    }
}