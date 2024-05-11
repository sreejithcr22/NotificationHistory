package com.sreejith.notificationhistory.viewmodel

import android.app.Application
import android.app.NotificationManager
import android.content.ComponentName
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sreejith.notificationhistory.data.repo.NotificationsRepo
import com.sreejith.notificationhistory.service.NotificationService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotificationsListViewModel @Inject constructor(
    private val app: Application,
    notificationsRepo: NotificationsRepo
) : ViewModel() {

    private val _hasNotificationAccess = MutableLiveData<Boolean>()
    val hasNotificationAccess: LiveData<Boolean> = _hasNotificationAccess
    val notifications = notificationsRepo.getAllNotifications()

     fun checkNotificationAccess() {
        val componentName = ComponentName(app.packageName, NotificationService::class.java.name)
        val notificationManager =
            app.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
        _hasNotificationAccess.value = notificationManager.isNotificationListenerAccessGranted(componentName)
        }
}