package com.anibalcopitan.okeypay2

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification

class TheNotificationListenerService : NotificationListenerService() {
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        // This method will be called whenever a new notification is posted
        // Process the notification here
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        // This method will be called whenever a notification is removed
        // Process the removed notification here
    }
}