package com.sreejith.notificationhistory.ui

import android.app.NotificationManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.sreejith.notificationhistory.R
import com.sreejith.notificationhistory.data.db.Notification
import com.sreejith.notificationhistory.data.db.NotificationsDb
import com.sreejith.notificationhistory.data.repo.NotificationsRepo
import com.sreejith.notificationhistory.service.NotificationService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject lateinit var notificationsRepo: NotificationsRepo
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkNotificationAccessPermission()
        loadNotificationRecyclerView()
    }

    private fun checkNotificationAccessPermission() {
        val componentName = ComponentName(packageName, NotificationService::class.java.name)
        val notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (!notificationManager.isNotificationListenerAccessGranted(componentName)) {
            Log.d("MainActivity", "App has no notification access")
            val notificationAccessSettings: Intent =
                Intent(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))

            notificationAccessSettings.putExtra(
                Settings.EXTRA_NOTIFICATION_LISTENER_COMPONENT_NAME,
                componentName.flattenToString()
            )

            startActivity(notificationAccessSettings)
        } else {
            Log.d("MainActivity", "App has notification access")
        }
    }

    private fun loadNotificationRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.notification_recycler_view)
        val notificationsList = ArrayList<Notification>()
        val notificationsAdapter = NotificationsAdapter(notificationsList)
        recyclerView.adapter = notificationsAdapter

        notificationsRepo.getAllNotifications()?.observe(this) {
            if (it.size > notificationsList.size) {
                val newItemsCount = it.size - notificationsList.size
                Log.i("MainActivity", "$newItemsCount items added")
                var i = newItemsCount
                while (--i >= 0) {
                    notificationsList.add(0, it[i])
                    notificationsAdapter.notifyItemInserted(0)
                }
            }

        }
    }
}