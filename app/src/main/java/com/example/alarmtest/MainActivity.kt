package com.example.alarmtest

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.alarmtest.databinding.ActivityMainBinding
import java.util.Calendar

import android.app.TimePickerDialog
import android.widget.TimePicker


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var alarmHour = 0
    private var alarmMinute = 0
    private lateinit var alarmCalendar: Calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.alarmButton.setOnClickListener {
            setAlarm()
        }
    }

    private fun setAlarm() {
        alarmCalendar = Calendar.getInstance()
        alarmCalendar.timeInMillis = System.currentTimeMillis()
        alarmCalendar.add(Calendar.SECOND, 30) // 30초 후로 설정

        // 시간, 분, 초 설정
        val alarmHour = alarmCalendar.get(Calendar.HOUR_OF_DAY)
        val alarmMinute = alarmCalendar.get(Calendar.MINUTE)
        val alarmSecond = alarmCalendar.get(Calendar.SECOND)

        // 알람 설정 시간을 로그로 표시
        val formattedTime = "%02d:%02d:%02d".format(alarmHour, alarmMinute, alarmSecond)
        println("알람이 설정되었습니다. 설정 시간: $formattedTime")

        val alarmIntent = Intent(applicationContext, AlarmReceiver::class.java)
        alarmIntent.action = AlarmReceiver.ACTION_RESTART_SERVICE
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmCallPendingIntent = PendingIntent.getBroadcast(
            this@MainActivity, 0, alarmIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE // FLAG_IMMUTABLE 추가
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                alarmCalendar.timeInMillis,
                alarmCallPendingIntent
            )
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                alarmCalendar.timeInMillis,
                alarmCallPendingIntent
            )
        }
    }
}