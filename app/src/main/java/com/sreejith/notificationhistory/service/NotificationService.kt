package com.sreejith.notificationhistory.service

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.sreejith.notificationhistory.data.db.Notification
import com.sreejith.notificationhistory.data.db.NotificationsDb
import com.sreejith.notificationhistory.data.repo.NotificationsRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

const val TAG = "payload"
class NotificationService : NotificationListenerService() {

    private val scope = CoroutineScope(Job() + Dispatchers.IO)

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        processNotification(sbn)?.let { saveNotificationToDb(it) }
    }


    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
        //processNotification(sbn)
    }

    private fun processNotification(sbn: StatusBarNotification?): Notification? {
        val extras = sbn?.notification?.extras
        return extras?.let {
            val title = extras.getString("android.title", "")
            val text = extras.getString("android.text", "")
            val packageName = sbn.packageName
            val postTime = sbn.postTime
            Log.i(TAG, "time = $postTime, title = $title, text = $text, app = $packageName")
            Notification(postTime, title, text, packageName)
        }?: run {
            Log.e(TAG, "notification or bundle null")
            null
        }
    }

    private fun saveNotificationToDb(notification: Notification) {
        scope.launch {
            NotificationsRepo(NotificationsDb.getInstance(applicationContext).notificationsDao()).insertNotification(notification)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

}