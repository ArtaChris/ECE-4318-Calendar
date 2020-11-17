package edu.cpp4310.calclkrem

import android.app.Notification
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


class MyNotificationPublisher : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification: Notification = intent.getParcelableExtra(NOTIFICATION)
        val notificationId = intent.getIntExtra(NOTIFICATION_ID, 0)
        notificationManager.notify(notificationId, notification)
    }

    companion object {
        var NOTIFICATION_ID = "notification_id"
        var NOTIFICATION = "notification"
    }
}