package com.example.animeproject.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.animeproject.util.NotificationHelper

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val name = intent.getStringExtra("extra_name") ?: "Студент"
        val time = intent.getStringExtra("extra_time") ?: ""

        Log.d("NotificationReceiver", "Получен BROADCAST")
        Log.d("NotificationReceiver", "Имя: $name, Время: $time")

        NotificationHelper.showNotification(
            context,
            "Сработало напоминание",
            "Пора на пару, $name! ($time)"
        )

        Log.d("NotificationReceiver", "Уведомление показано")
    }
}