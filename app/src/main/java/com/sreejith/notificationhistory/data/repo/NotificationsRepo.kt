package com.sreejith.notificationhistory.data.repo

import android.content.pm.PackageManager
import android.service.notification.StatusBarNotification
import android.util.Log
import androidx.lifecycle.LiveData
import com.sreejith.notificationhistory.data.db.Notification
import com.sreejith.notificationhistory.data.db.NotificationsDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


private const val TAG = "NotificationsRepo"
class NotificationsRepo(
    private val notificationsDao: NotificationsDao,
    private val packageManager: PackageManager
) {

    private suspend fun insertNotificationToDb(notification: Notification) {
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

    suspend fun saveNotification(sbn: StatusBarNotification?) {
       processNotification(sbn)?.let { insertNotificationToDb(it) }
    }

    private fun processNotification(sbn: StatusBarNotification?): Notification? {
        if (sbn?.isOngoing == true) return null
        val extras = sbn?.notification?.extras
        return extras?.let {
            val title = extras.getString(
                "android.title",
                extras.getCharSequence("android.title", "").toString()
            )
            val text = extras.getString(
                "android.text",
                extras.getCharSequence("android.text", "").toString()
            )
            val packageName = sbn.packageName
            val postTime = sbn.postTime
            val appName = getAppNameFromPackage(packageName)
            if(title == "") return null
            Log.i(TAG, "time = $postTime, title = $title, text = $text, package = $packageName, app = $appName")
            Notification(postTime, title, text, packageName, appName)
        } ?: run {
            Log.e(TAG, "notification or bundle null")
            null
        }
    }

    private fun getAppNameFromPackage(packageName: String) =
        packageManager.getApplicationLabel(
            packageManager.getApplicationInfo(
                packageName,
                PackageManager.GET_META_DATA
            )
        ).toString()


}