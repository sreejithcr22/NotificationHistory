package com.sreejith.notificationhistory.data.db

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    fun provideNotificationsDao(notificationsDb: NotificationsDb): NotificationsDao =
        notificationsDb.notificationsDao()

    @Provides
    @Singleton
    fun provideNotificationsDb(@ApplicationContext context: Context): NotificationsDb =
        Room.databaseBuilder(context, NotificationsDb::class.java, "app_db").build()
}