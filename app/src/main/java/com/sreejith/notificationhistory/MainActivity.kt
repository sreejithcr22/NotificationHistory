package com.sreejith.notificationhistory

import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import com.sreejith.notificationhistory.data.db.NotificationsDb
import com.sreejith.notificationhistory.data.repo.NotificationsRepo
import com.sreejith.notificationhistory.service.NotificationService

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val componentName = ComponentName(packageName, NotificationService::class.java.name)
        val notificationManager =  getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (!notificationManager.isNotificationListenerAccessGranted(componentName))
        {
            Log.d("MainActivity", "App has no notification access")
            val notificationAccessSettings: Intent =
                Intent(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))

            notificationAccessSettings.putExtra(
                Settings.EXTRA_NOTIFICATION_LISTENER_COMPONENT_NAME,
                componentName.flattenToString()
            )

            startActivity(notificationAccessSettings)
        }
        else
        {
            Log.d("MainActivity", "App has notification access")
        }

        NotificationsRepo(NotificationsDb.getInstance(applicationContext).notificationsDao()).getAllNotifications()?.observe(this) {
            Log.i("notificationsdata", it.toString())
        }
    }
}