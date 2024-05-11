package com.example.alarmtest

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.app.Service.START_STICKY
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import java.security.Provider
import java.util.Calendar

class AlarmService : Service() {
    private val TAG = "TAG+Service"

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "AlarmService")
        val alarmIntent = Intent(applicationContext, AlarmActivity::class.java)
        startActivity(alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        return super.onStartCommand(intent, flags, startId)
    }
}