package com.example.alarmtest

import android.app.Service
import android.app.Service.START_STICKY
import android.content.Intent
import android.os.IBinder
import java.security.Provider

class AlarmService : Service() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // AlarmActivity 실행
        val alarmActivityIntent = Intent(this, AlarmActivity::class.java)
        alarmActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(alarmActivityIntent)

        // 서비스를 종료하여 메모리에서 제거
        stopSelf()

        // START_STICKY를 반환하여 시스템이 강제로 서비스를 종료한 경우 재시작 요청
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}