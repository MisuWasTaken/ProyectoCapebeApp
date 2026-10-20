package com.example.proyectocapebeapp.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object NotificacionHelper {
    fun programarRecordatorio(
        context: Context,
        inscripcionId: Int,
        fecha: String,
        hora: String
    ) {
        val formato = SimpleDateFormat(
            "d/M/yyyy H:mm",
            Locale.getDefault()
        )
        formato.isLenient = false
        val fechaVuelo =
            formato.parse("$fecha $hora")
                ?: return
        val ahora = System.currentTimeMillis()
        if (fechaVuelo.time <= ahora) {
            return
        }
        val calendario =
            Calendar.getInstance()
        calendario.time = fechaVuelo
        calendario.add(
            Calendar.HOUR_OF_DAY,
            -24
        )
        var momentoNotificacion =
            calendario.timeInMillis
        if (momentoNotificacion <= ahora) {
            momentoNotificacion = ahora + 1000
        }
        val intent = Intent(
            context,
            NotificacionReceiver::class.java
        )
        intent.putExtra(
            "inscripcionId",
            inscripcionId
        )
        intent.putExtra(
            "fecha",
            fecha
        )
        intent.putExtra(
            "hora",
            hora
        )
        val pendingIntent =
            PendingIntent.getBroadcast(
                context,
                inscripcionId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or
                        PendingIntent.FLAG_IMMUTABLE
            )
        val alarmManager =
            context.getSystemService(
                Context.ALARM_SERVICE
            ) as AlarmManager
        alarmManager.cancel(pendingIntent)
        alarmManager.setAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            momentoNotificacion,
            pendingIntent
        )
    }
}