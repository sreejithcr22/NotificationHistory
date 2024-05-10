package com.sreejith.notificationhistory.service

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.sreejith.notificationhistory.data.db.NotificationsDb
import com.sreejith.notificationhistory.data.repo.NotificationsRepo
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

const val TAG = "payload"

@AndroidEntryPoint
class NotificationService : NotificationListenerService() {

    private val scope = CoroutineScope(Job() + Dispatchers.IO)
    @Inject lateinit var  notificationsRepo: NotificationsRepo

    override fun onCreate() {
        super.onCreate()
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        scope.launch {
            notificationsRepo.saveNotification(sbn)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

}