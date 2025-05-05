package com.example.halfsubmission.data.local.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.halfsubmission.data.local.entity.EventEntity
import com.example.halfsubmission.data.local.room.dao.EventDao

@Database(
    entities = [EventEntity::class],
    version = 1,
    exportSchema = false)
abstract class EventDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao

    companion object {
        @Volatile
        private var instance: EventDatabase? = null

        fun getInstance(context: Context): EventDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    EventDatabase::class.java, "DatabaseEventsLocal.db"
                ).build()
            }
    }
}