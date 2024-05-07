package com.sreejith.notificationhistory.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Notification::class], version = 1)
abstract class NotificationsDb : RoomDatabase() {
    abstract fun notificationsDao(): NotificationsDao

    companion object {
        private var INSTANCE: NotificationsDb? = null
        fun getInstance(context: Context): NotificationsDb {
            return INSTANCE ?: run {
                INSTANCE =
                    Room.databaseBuilder(context, NotificationsDb::class.java, "app_db").build()
                INSTANCE!!

            }
        }
    }
}