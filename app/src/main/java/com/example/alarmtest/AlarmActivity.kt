package com.example.alarmtest

import android.content.Intent
import android.os.Bundle
import android.os.PowerManager
import androidx.appcompat.app.AppCompatActivity

class AlarmActivity : AppCompatActivity() {
    private lateinit var wakeLock: PowerManager.WakeLock

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 화면을 켜기 위해 Wake Lock 설정
        val powerManager = getSystemService(POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(
            PowerManager.FULL_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP or PowerManager.ON_AFTER_RELEASE,
            "AlarmApp:WakeLock"
        )
        wakeLock.acquire(10*60*1000L /*10 minutes*/)

    }
    override fun onDestroy() {
        super.onDestroy()
        // 화면 Wake Lock 해제
        wakeLock.release()
    }

    // 알람을 해제하는 메서드
    private fun dismissAlarm() {
        // 여기에 알람을 해제하는 코드 작성
        // 예를 들어, 서비스를 시작하여 알람을 중지할 수 있습니다.
        val serviceIntent = Intent(this, AlarmService::class.java)
        stopService(serviceIntent)

        // 액티비티를 종료하여 화면을 닫음
        finish()
    }

    // 알람을 무시하는 메서드
    private fun snoozeAlarm() {
        // 여기에 알람을 일시 중지하는 코드 작성
        // 예를 들어, 일시 중지된 알람을 설정하여 나중에 다시 울릴 수 있도록 할 수 있습니다.

        // 액티비티를 종료하여 화면을 닫음
        finish()
    }
}