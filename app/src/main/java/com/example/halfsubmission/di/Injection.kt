package com.example.halfsubmission.di

import android.content.Context
import com.example.halfsubmission.data.local.room.database.BookmarkDatabase
import com.example.halfsubmission.data.repository.EventRepository
import com.example.halfsubmission.data.local.room.database.EventDatabase
import com.example.halfsubmission.data.remote.retrofit.ApiConfig
import com.example.halfsubmission.data.repository.BookmarkRepository
import com.example.halfsubmission.utils.AppExecutors

object Injection {

    fun provideRepository(context: Context): EventRepository {
        val apiService = ApiConfig.getApiService()
        val database = EventDatabase.getInstance(context)
        val dao = database.eventDao()
        val appExecutors = AppExecutors()
        return EventRepository.getInstance(apiService, dao, appExecutors)
    }

    fun provideBookmarkRepository(context: Context): BookmarkRepository {
        val database = BookmarkDatabase.getInstance(context)
        val dao = database.bookmarkDao()
        val appExecutors = AppExecutors()
        return BookmarkRepository.getInstance(dao, provideRepository(context))
    }
}