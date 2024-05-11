package com.sreejith.notificationhistory.ui

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.sreejith.notificationhistory.R
import com.sreejith.notificationhistory.viewmodel.NotificationsListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var notificationsListViewModel: NotificationsListViewModel
    private lateinit var grantAccess: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        grantAccess = findViewById(R.id.btn_grant_access)
        notificationsListViewModel = ViewModelProvider(this)[NotificationsListViewModel::class.java]
        grantAccess.setOnClickListener { requestNotificationPermission() }

        notificationsListViewModel.hasNotificationAccess.observe(this) {
            if (it) {
                startActivity(Intent(this, NotificationsListActivity::class.java))
                finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        notificationsListViewModel.checkNotificationAccess()
    }

    private fun requestNotificationPermission() {
        val notificationAccessSettings =
            Intent(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))

        notificationAccessSettings.putExtra(
            Settings.EXTRA_NOTIFICATION_LISTENER_COMPONENT_NAME,
            componentName.flattenToString()
        )

        startActivity(notificationAccessSettings)
    }
}