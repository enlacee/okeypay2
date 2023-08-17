package com.anibalcopitan.okeypay2

import android.app.Notification
import android.content.Intent
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log

class TheNotificationListener : NotificationListenerService() {
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        super.onNotificationPosted(sbn)

        if (sbn.packageName.equals("com.whatsapp")) {
        //if (sbn.packageName.equals("com.bcp.innovacxion.yapeapp")) {
            Log.i("XXXX", "NOTIFICACION DE YAPE FUE = POSTED.......");
            val message = sbn.notification?.extras?.getString(Notification.EXTRA_TEXT)
            if (!message.isNullOrEmpty() && message.contains("Yape!")) {
                // Iniciar MainActivity y pasar la URL como extra
                val intent = Intent("com.anibalcopitan.okeypay2")
                intent.putExtra("message", message)
                sendBroadcast(intent)
            }
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        // This method will be called whenever a notification is removed
        // Process the removed notification here
    }
}