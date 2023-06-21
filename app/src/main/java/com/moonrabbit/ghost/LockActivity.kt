package com.moonrabbit.ghost

import android.app.ActivityManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button

class LockActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lock)   // LockAvtivity의 화면 연동
        window.addFlags(
            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                WindowManager.LayoutParams.FLAG_FULLSCREEN or
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)   // 잠금화면 위에 표시

        val exit = findViewById<Button>(R.id.button3)
        exit.setOnClickListener(View.OnClickListener {
            // 버튼이벤트 동작 시, Activity 종료
            finish()
        })
    }

    override fun onPause() {
        // 홈 버튼을 클릭하면, 다시 LockActivity를 활성화 시켜 준다.
        super.onPause()
        val activityManager = applicationContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        activityManager.moveTaskToFront(taskId, 0)
    }

    override fun onBackPressed() {
        // 기존 뒤로 가기 버튼의 기능을 막기 위해 주석 처리 또는 삭제 (위 주석 삭제 시 앱종료됨)
    }
}