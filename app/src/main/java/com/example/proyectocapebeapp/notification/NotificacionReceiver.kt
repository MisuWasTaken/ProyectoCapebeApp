package com.example.proyectocapebeapp.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.proyectocapebeapp.MainActivity

class NotificacionReceiver : BroadcastReceiver() {

    override fun onReceive(
        context: Context,
        intent: Intent
    ) {
        val fecha = intent.getStringExtra("fecha") ?: ""
        val hora = intent.getStringExtra("hora") ?: ""
        val inscripcionId = intent.getIntExtra("inscripcionId", 0)
        val canalId = "recordatorios_vuelo"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canal = NotificationChannel(
                canalId,
                "Recordatorios de vuelos",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            canal.description =
                "Recordatorios de vuelos recreativos de CAPEBE"
            val notificationManager =
                context.getSystemService(
                    Context.NOTIFICATION_SERVICE
                ) as NotificationManager
            notificationManager.createNotificationChannel(canal)
        }
        val intentAbrirApp =
            Intent(context, MainActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(
                context,
                0,
                intentAbrirApp,
                PendingIntent.FLAG_UPDATE_CURRENT or
                        PendingIntent.FLAG_IMMUTABLE
            )
        val notificacion =
            NotificationCompat.Builder(
                context,
                canalId
            )
                .setSmallIcon(
                    android.R.drawable.ic_dialog_info
                )
                .setContentTitle(
                    "Tu vuelo es en 24 horas"
                )
                .setContentText(
                    "Recuerda tu vuelo CAPEBE el $fecha a las $hora."
                )
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(
                    NotificationCompat.PRIORITY_DEFAULT
                )
                .build()
        if (
            Build.VERSION.SDK_INT <
            Build.VERSION_CODES.TIRAMISU ||
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat
                .from(context)
                .notify(
                    inscripcionId,
                    notificacion
                )
        }
    }
}