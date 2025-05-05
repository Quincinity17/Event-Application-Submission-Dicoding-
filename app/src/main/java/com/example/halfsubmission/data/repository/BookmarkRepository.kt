package com.example.halfsubmission.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.halfsubmission.data.Result
import com.example.halfsubmission.data.local.entity.BookmarkEntity
import com.example.halfsubmission.data.local.room.dao.BookmarkDao

class BookmarkRepository private constructor(
    private val bookmarkDao: BookmarkDao,
    private val eventRepository: EventRepository
) {

    suspend fun insertBookmark(eventId: Int) {
        eventRepository.getEventLocalBasedID(eventId)?.let { event ->
            bookmarkDao.insertEventBookmark(BookmarkEntity(
                id = event.id,
                name = event.name,
                summary = event.summary,
                description = event.description,
                imageLogo = event.imageLogo,
                mediaCover = event.mediaCover,
                category = event.category,
                ownerName = event.ownerName,
                cityName = event.cityName,
                quota = event.quota,
                registrants = event.registrants,
                beginTime = event.beginTime,
                endTime = event.endTime,
                link = event.link,
                isUpcomming = event.isUpcomming
            ))
        }
    }

    fun getAllBookmarks(): LiveData<Result<List<BookmarkEntity>>> =
        MediatorLiveData<Result<List<BookmarkEntity>>>().apply {
            value = Result.Loading
            addSource(bookmarkDao.getAllEventsDatabase()) { data ->
                value = Result.Success(data)
            }
        }

    suspend fun deleteBookmarkById(event: BookmarkEntity) {
        bookmarkDao.deleteEventBookmark(event)
    }

    companion object {
        @Volatile
        private var instance: BookmarkRepository? = null

        fun getInstance(bookmarkDao: BookmarkDao, eventRepository: EventRepository): BookmarkRepository =
            instance ?: synchronized(this) {
                instance ?: BookmarkRepository(bookmarkDao, eventRepository)
            }.also { instance = it }
    }
}