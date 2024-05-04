package com.example.alarmtest

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val setAlarmButton: Button = findViewById(R.id.set_alarm_button)
        setAlarmButton.setOnClickListener {
            setAlarm()
        }
    }

    private fun setAlarm() {
        Log.d(TAG, "setAlarm: Setting alarm")

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            alarmIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        // 알람을 10초 후로 설정 (테스트를 위해)
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.SECOND, 10)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val exactAlarmSupported = alarmManager.canScheduleExactAlarms()
            Log.d(TAG, "setAlarm: Exact alarm supported: $exactAlarmSupported")

            if (exactAlarmSupported) {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
                Log.d(TAG, "setAlarm: Exact alarm scheduled for: ${calendar.time}")
            } else {
                // 대체 로직을 구현할 수 있습니다.
                Log.d(TAG, "setAlarm: Exact alarm not supported. Alternative logic may be needed.")
            }
        } else {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
            Log.d(TAG, "setAlarm: Exact alarm scheduled for: ${calendar.time}")
        }

        // Wakelock 획득
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        val wakeLock = powerManager.newWakeLock(
            PowerManager.PARTIAL_WAKE_LOCK,
            "MyApp::MyWakelockTag"
        )
        wakeLock.acquire(10*60*1000L /*10 minutes*/)

        // 알람 설정 후 WakeLock 해제
        wakeLock.release()
    }
}