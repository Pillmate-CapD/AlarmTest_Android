package com.example.alarmtest

import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.PowerManager
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.alarmtest.databinding.ActivityAlarmBinding
import java.util.Calendar

class AlarmActivity : AppCompatActivity() {
    private lateinit var calendar: Calendar
    private lateinit var dismissButton: Button
    private lateinit var timeText: TextView
    private var flag = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)

        calendar = Calendar.getInstance()
        dismissButton = findViewById(R.id.dismiss_button)
        timeText = findViewById(R.id.time)

        window.addFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    or WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                    or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                    or WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
        )

        Thread {
            while (flag) {
                try {
                    calendar = Calendar.getInstance()
                    runOnUiThread {
                        updateTimeText()
                    }
                    Thread.sleep(1000)
                } catch (ex: InterruptedException) {
                    ex.printStackTrace()
                }
            }
        }.start()

        dismissButton.setOnClickListener {
            flag = false
            finish()
        }
    }

    private fun updateTimeText() {
        val hourOfDay = calendar[Calendar.HOUR_OF_DAY]
        val minute = calendar[Calendar.MINUTE]
        val second = calendar[Calendar.SECOND]
        val timeString = if (hourOfDay in 1..11) {
            "AM $hourOfDay:$minute:$second"
        } else if (hourOfDay == 12) {
            "PM $hourOfDay:$minute:$second"
        } else if (hourOfDay in 13..23) {
            "PM ${hourOfDay - 12}:$minute:$second"
        } else { // hourOfDay == 0
            "AM 0:$minute:$second"
        }
        timeText.text = timeString
    }
}