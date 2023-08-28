package com.anibalcopitan.okeypay2

import android.app.Notification
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import com.anibalcopitan.okeypay2.data.phonenumberregistration.SharedPreferencesManager

class TheNotificationListener : NotificationListenerService() {
    private val broadcastReceiver = MyReceiverBroadcast()

    /**
     * Crea al `broadcastReceiver` que escuchara despues
     * y sera llamado con el INTENT
     */
    override fun onCreate() {
        super.onCreate()
        val intentFilter = IntentFilter(MyReceiverBroadcast.ID_ACTION)
        registerReceiver(broadcastReceiver, intentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadcastReceiver)
    }

    override fun onBind(intent: Intent): IBinder? {
        return super.onBind(intent)
    }

    override fun onListenerConnected() {
        super.onListenerConnected()
        Log.i("TheNotificationListener", "Connected");
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        super.onNotificationPosted(sbn)

        if (sbn.packageName.equals("com.whatsapp")) {
        //if (sbn.packageName.equals("com.bcp.innovacxion.yapeapp")) {
            Log.i("debug", "== com.whatsapp  Yape! [fuera] == ");
            val message = sbn.notification?.extras?.getString(Notification.EXTRA_TEXT)
            if (!message.isNullOrEmpty() && message.contains("Yape!")) {
                Log.i("debug", " == com.whatsapp  Yape! ==");
                // v1. Iniciar MainActivity y pasar la URL como extra
//                val intent = Intent("com.anibalcopitan.okeypay2")
//                intent.putExtra("message", message)
//                sendBroadcast(intent)

                // v2
//                val intentFilter = IntentFilter()
//                intentFilter.addAction("com.anibalcopitan.okeypay2")
//                registerReceiver(broadcastReceiver, intentFilter)

                // v3
                // Llamar al BroadcastReceiver para manejar la notificación
                val intent = Intent(MyReceiverBroadcast.ID_ACTION)
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