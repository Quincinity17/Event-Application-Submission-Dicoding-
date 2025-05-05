package com.example.halfsubmission.data.local.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.halfsubmission.data.local.entity.BookmarkEntity
import com.example.halfsubmission.data.local.room.dao.BookmarkDao

@Database(
    entities = [BookmarkEntity::class],
    version = 1,
    exportSchema = false)
abstract class BookmarkDatabase : RoomDatabase() {
    abstract fun bookmarkDao(): BookmarkDao

    companion object {
        @Volatile
        private var instance: BookmarkDatabase? = null

        fun getInstance(context: Context): BookmarkDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    BookmarkDatabase::class.java, "EventsBookmarkLocal.db"
                )
                    .build()
            }
    }
}