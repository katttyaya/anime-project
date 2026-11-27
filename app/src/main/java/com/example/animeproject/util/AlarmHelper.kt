package com.example.animeproject.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import java.util.Calendar
import com.example.animeproject.receiver.NotificationReceiver

object AlarmHelper {

    const val ALARM_REQUEST_CODE = 1001
    const val EXTRA_NAME = "extra_name"
    const val EXTRA_TIME = "extra_time"

    fun setAlarm(context: Context, timeString: String, name: String) {
        Log.d("AlarmHelper", "Установка будильника")
        Log.d("AlarmHelper", "Получено время: $timeString, Имя: $name")

        val parts = timeString.split(":")
        val hour = parts[0].toInt()
        val minute = parts[1].toInt()

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        Log.d("AlarmHelper", "Текущее время: ${Calendar.getInstance().time}")
        Log.d("AlarmHelper", "Время будильника до проверки: ${calendar.time}")

        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            Log.d("AlarmHelper", "Время уже прошло — переносим на завтра")
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        Log.d("AlarmHelper", "Финальное время срабатывания: ${calendar.time}")
        Log.d("AlarmHelper", "timeInMillis: ${calendar.timeInMillis}")

        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra(EXTRA_NAME, name)
            putExtra(EXTRA_TIME, timeString)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            ALARM_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        Log.d("AlarmHelper", "PendingIntent создан: $pendingIntent")

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
            Log.d("AlarmHelper", "Вызван setExactAndAllowWhileIdle (API 23+)")
        } else {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
            Log.d("AlarmHelper", "Вызван setExact (API < 23)")
        }

        Log.d("AlarmHelper", "Будильник установлен")
    }

    fun cancelAlarm(context: Context) {
        Log.d("AlarmHelper", "Отмена будильника")

        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            ALARM_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)

        Log.d("AlarmHelper", "Будильник отменён")
    }
}