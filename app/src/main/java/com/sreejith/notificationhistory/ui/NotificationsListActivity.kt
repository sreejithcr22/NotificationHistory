package com.sreejith.notificationhistory.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.sreejith.notificationhistory.R.id
import com.sreejith.notificationhistory.R.layout
import com.sreejith.notificationhistory.data.db.Notification
import com.sreejith.notificationhistory.data.repo.NotificationsRepo
import com.sreejith.notificationhistory.viewmodel.NotificationsListViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NotificationsListActivity : AppCompatActivity() {
    @Inject
    lateinit var notificationsRepo: NotificationsRepo
    private lateinit var notificationsListViewModel: NotificationsListViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_notification_list)

        notificationsListViewModel = ViewModelProvider(this)[NotificationsListViewModel::class.java]
        loadNotificationRecyclerView()


    }

    private fun loadNotificationRecyclerView() {
        val notificationsList = ArrayList<Notification>()
        val notificationsAdapter = NotificationsAdapter(notificationsList)
        val recyclerView: RecyclerView = findViewById(id.notification_recycler_view)
        val searchView = findViewById<EditText>(id.search)
        val emptyNotificationsText: TextView = findViewById(id.no_notifications)

        recyclerView.adapter = notificationsAdapter
        searchView.doOnTextChanged { text, _, _, _ ->
            notificationsAdapter.filter.filter(text)
        }

        notificationsListViewModel.notifications?.observe(this) {
            if (it.size > notificationsList.size) {
                val newItemsCount = it.size - notificationsList.size
                Log.i("NotificationsListActivity", "$newItemsCount items added")
                var i = newItemsCount
                while (--i >= 0) {
                    notificationsList.add(0, it[i])
                    notificationsAdapter.notifyItemInserted(0)
                }
            }

            recyclerView.visibility = if (notificationsList.isEmpty()) View.GONE else View.VISIBLE
            emptyNotificationsText.visibility =
                if (notificationsList.isEmpty()) View.VISIBLE else View.GONE

        }
    }
}