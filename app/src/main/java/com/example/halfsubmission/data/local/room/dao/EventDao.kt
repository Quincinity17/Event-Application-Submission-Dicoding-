package com.example.halfsubmission.data.local.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.halfsubmission.data.local.entity.EventEntity


@Dao
interface EventDao {

    @Query("SELECT * FROM DatabaseEventsLocal")
    fun getAllEventsLocal(): LiveData<List<EventEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEvent(event: List<EventEntity>)

    @Update
    fun updateEvent(event: EventEntity)

    @Query("SELECT * FROM DatabaseEventsLocal WHERE isBookmarked = 1")
    fun getEventBookmarked(): LiveData<List<EventEntity>>

    @Query("DELETE FROM DatabaseEventsLocal WHERE isActive =:active")
    fun deleteAll(active: Int)

    @Query("DELETE FROM DatabaseEventsLocal WHERE id = :id")
    fun deleteEventById(id: Int)

    @Query("SELECT EXISTS(SELECT * FROM DatabaseEventsLocal WHERE id = :id AND isBookmarked = 1)")
    fun isEventBookmarked(id: Int): Boolean

    @Query("SELECT * FROM DatabaseEventsLocal WHERE isActive = 1")
    fun getUpcomingEvents(): LiveData<List<EventEntity>>

    @Query("SELECT * FROM DatabaseEventsLocal WHERE isActive = 0")
    fun getPastEvents(): LiveData<List<EventEntity>>

    @Query("SELECT * FROM DatabaseEventsLocal WHERE isActive =:active")
    fun getEvents( active: Int  ): LiveData<List<EventEntity>>

    @Query("SELECT * FROM DatabaseEventsLocal WHERE id = :id LIMIT 1")
    fun getEventById(id: Int): LiveData<EventEntity>

    @Query("SELECT * FROM DatabaseEventsLocal WHERE id = :id LIMIT 1")
    suspend fun getEventBasedId(id: Int): EventEntity?

    @Query("SELECT * FROM DatabaseEventsLocal WHERE name LIKE '%' || :query || '%' OR summary LIKE '%' || :query || '%'")
    fun searchEvents(query: String): LiveData<List<EventEntity>>
}