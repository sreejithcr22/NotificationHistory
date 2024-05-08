package com.sreejith.notificationhistory.data.db

import androidx.room.Entity

@Entity(primaryKeys = ["title", "text", "packageName"], tableName = "notifications")
data class Notification(
    val postTime: Long,
    val title: String,
    val text: String,
    val packageName: String,
    val appName: String
)
