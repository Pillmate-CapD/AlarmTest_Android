package com.example.alarmtest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


class AlarmReceiver : BroadcastReceiver() {
    companion object {
        const val ACTION_RESTART_SERVICE = "Restart"
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == ACTION_RESTART_SERVICE) {
            val inIntent = Intent(context, AlarmService::class.java)
            context.startService(inIntent)
        }
    }
}