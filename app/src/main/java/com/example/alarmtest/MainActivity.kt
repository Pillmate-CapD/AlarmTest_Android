package com.example.alarmtest

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.alarmtest.databinding.ActivityMainBinding
import java.util.Calendar

import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


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
        alarmCalendar.add(Calendar.SECOND, 30) // Set alarm 30 seconds from now

        val alarmIntent = Intent(applicationContext, AlarmReceiver::class.java)
        alarmIntent.action = AlarmReceiver.ACTION_RESTART_SERVICE
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmCallPendingIntent = PendingIntent.getBroadcast(
            this@MainActivity, 0, alarmIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val canScheduleExactAlarms = alarmManager.canScheduleExactAlarms()
            if (!canScheduleExactAlarms) {
                Log.w("MainActivity", "Device doesn't support scheduling exact alarms.")
                return
            }
        }

        try {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                alarmCalendar.timeInMillis,
                alarmCallPendingIntent
            )
            // Log the alarm time
            val formattedTime = "%02d:%02d:%02d".format(
                alarmCalendar.get(Calendar.HOUR_OF_DAY),
                alarmCalendar.get(Calendar.MINUTE),
                alarmCalendar.get(Calendar.SECOND)
            )
            Log.d("MainActivity", "Alarm set for: $formattedTime")
        } catch (e: SecurityException) {
            Log.e("MainActivity", "SecurityException: ${e.message}")
            // Handle SecurityException gracefully, perhaps by requesting necessary permissions
        }
    }
}