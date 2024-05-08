package com.sreejith.notificationhistory.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sreejith.notificationhistory.R
import com.sreejith.notificationhistory.data.db.Notification

private const val TAG = "NotificationsAdapter"
class NotificationsAdapter(private val notificationList: List<Notification>) :
    RecyclerView.Adapter<NotificationsAdapter.NotificationViewHolder>() {

    class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.notification_title)
        private val text: TextView = itemView.findViewById(R.id.notification_text)
        private val appName: TextView = itemView.findViewById(R.id.notification_appName)
        fun setData(notification: Notification) {
            title.text = notification.title
            text.text = notification.text
            appName.text = notification.appName
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.notification_recycler_row, parent, false)
        return NotificationViewHolder(view)
    }
    override fun getItemCount(): Int {
        return notificationList.size.also { Log.i(TAG, "size = $it") }
    }
    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.setData(notificationList[position])
    }
}