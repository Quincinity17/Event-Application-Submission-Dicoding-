package com.example.halfsubmission.data.local.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.halfsubmission.data.local.entity.BookmarkEntity

@Dao
interface BookmarkDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEventBookmark(event: BookmarkEntity)

    @Delete
    suspend fun deleteEventBookmark(event: BookmarkEntity)

    @Query("SELECT * FROM EventsBookmarkLocal")
    fun getAllEventsDatabase(): LiveData<List<BookmarkEntity>>

    @Query("SELECT id FROM EventsBookmarkLocal")
    fun getAllEventIds(): LiveData<List<Int>>

}