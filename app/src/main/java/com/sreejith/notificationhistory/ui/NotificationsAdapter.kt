package com.sreejith.notificationhistory.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sreejith.notificationhistory.R
import com.sreejith.notificationhistory.data.db.Notification

private const val TAG = "NotificationsAdapter"

class NotificationsAdapter(private val notificationList: List<Notification>) :
    RecyclerView.Adapter<NotificationsAdapter.NotificationViewHolder>(), Filterable {
    private var notificationListFiltered = notificationList

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
        return notificationListFiltered.size.also { Log.i(TAG, "size = $it") }
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.setData(notificationListFiltered[position])
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(input: CharSequence?): FilterResults {
                return FilterResults().apply {
                    values = input?.toString()?.let {
                        if (input.isEmpty()) notificationList else {
                            notificationList.filter { notification ->
                                notification.appName.contains(input, true) || notification.text.contains(
                                    input, true
                                ) || notification.title.contains(input, true)
                            }
                        }

                    } ?: notificationList
                }
            }

            override fun publishResults(p0: CharSequence?, filtered: FilterResults?) {
                notificationListFiltered =
                    (filtered?.values as List<Notification>?) ?: notificationList
                notifyDataSetChanged()

            }

        }
    }
}