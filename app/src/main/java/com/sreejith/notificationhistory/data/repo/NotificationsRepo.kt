package com.sreejith.notificationhistory.data.repo

import android.util.Log
import androidx.lifecycle.LiveData
import com.sreejith.notificationhistory.data.db.Notification
import com.sreejith.notificationhistory.data.db.NotificationsDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val TAG = "NotificationsRepo"
class NotificationsRepo(private val notificationsDao: NotificationsDao) {

    suspend fun insertNotification(notification: Notification) {
        withContext(Dispatchers.IO) {
            try {
                val count = notificationsDao.insertNotification(notification)
                Log.i(TAG, "insertNotification count = $count")
            } catch (e: Exception) {
                Log.e(TAG, "insertNotification exception: $e")
            }
        }
    }

    fun getAllNotifications(): LiveData<List<Notification>>? {
        return try {
            notificationsDao.getAllNotifications()
        } catch (e: Exception) {
            Log.e(TAG, "getAllNotifications exception: $e")
            null
        }
    }
}