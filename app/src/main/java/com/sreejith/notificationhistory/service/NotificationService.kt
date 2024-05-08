package com.sreejith.notificationhistory.service

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
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
        scope.launch {
            NotificationsRepo(
                NotificationsDb.getInstance(applicationContext).notificationsDao(),
                applicationContext.packageManager
            ).saveNotification(sbn)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

}