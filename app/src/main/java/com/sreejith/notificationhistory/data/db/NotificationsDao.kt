package com.sreejith.notificationhistory.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface NotificationsDao {
    @Insert
    suspend fun insertNotification(notification: Notification): Long

    @Query("select * from notifications order by postTime desc")
    fun getAllNotifications(): LiveData<List<Notification>>
}