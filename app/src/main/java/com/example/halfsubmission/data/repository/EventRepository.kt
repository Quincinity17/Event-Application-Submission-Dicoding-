package com.example.halfsubmission.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.halfsubmission.data.Result
import com.example.halfsubmission.data.local.entity.EventEntity
import com.example.halfsubmission.data.local.room.dao.EventDao
import com.example.halfsubmission.data.remote.response.EventResponse
import com.example.halfsubmission.data.remote.retrofit.ApiService
import com.example.halfsubmission.utils.AppExecutors
import com.loopj.android.http.AsyncHttpClient.log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class EventRepository private constructor(
    private val apiService: ApiService,
    private val eventDao: EventDao,
    private val appExecutors: AppExecutors,
) {

    fun getEvent(active: Int): LiveData<Result<List<EventEntity>>> {
        val result = MediatorLiveData<Result<List<EventEntity>>>().apply { value = Result.Loading }
        apiService.getActiveEvents(active).enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                if (response.isSuccessful) {
                    response.body()?.event?.let { articles ->
                        appExecutors.diskIO.execute {
                            val eventList = articles.map { article ->
                                EventEntity(
                                    id = article.id,
                                    name = article.name,
                                    summary = article.summary,
                                    description = article.description,
                                    imageLogo = article.imageLogo,
                                    mediaCover = article.mediaCover,
                                    category = article.category,
                                    ownerName = article.ownerName,
                                    cityName = article.cityName,
                                    quota = article.quota,
                                    registrants = article.registrants,
                                    beginTime = article.beginTime,
                                    endTime = article.endTime,
                                    link = article.link,
                                    isBookmarked = eventDao.isEventBookmarked(article.id),
                                    isUpcomming = active == 1
                                )
                            }
                            eventDao.deleteAll(active)
                            eventDao.insertEvent(eventList)
                            appExecutors.mainThread.execute {
                                result.addSource(eventDao.getEvents(active)) { newData ->
                                    result.value = Result.Success(newData)
                                }
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                appExecutors.mainThread.execute {
                    result.addSource(eventDao.getEvents(active)) { newData ->
                        result.value = Result.Success(newData)
                    }
                }
            }
        })
        return result
    }

    fun setBookmarkedEvent(event: EventEntity, bookmarkState: Boolean) {
        appExecutors.diskIO.execute {
            eventDao.updateEvent(event.apply { isBookmarked = bookmarkState })
        }
    }

    fun getSearchEvent(query: String): LiveData<List<EventEntity>> = eventDao.searchEvents(query)

    suspend fun getEventLocalBasedID(id: Int): EventEntity? = eventDao.getEventBasedId(id)

    fun getEventDetail(id: Int, isActive: Boolean, eventBookmark: Boolean): LiveData<Result<EventEntity>> {
        val result = MediatorLiveData<Result<EventEntity>>().apply { value = Result.Loading }

        apiService.getEventDetail(id.toString()).enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                if (response.isSuccessful) {
                    response.body()?.singleEvent?.let { singleEventItem ->
                        appExecutors.diskIO.execute {
                            eventDao.insertEvent(listOf(EventEntity(
                                id = singleEventItem.id,
                                name = singleEventItem.name,
                                summary = singleEventItem.summary,
                                description = singleEventItem.description,
                                imageLogo = singleEventItem.imageLogo,
                                mediaCover = singleEventItem.mediaCover,
                                category = singleEventItem.category,
                                ownerName = singleEventItem.ownerName,
                                cityName = singleEventItem.cityName,
                                quota = singleEventItem.quota,
                                registrants = singleEventItem.registrants,
                                beginTime = singleEventItem.beginTime,
                                endTime = singleEventItem.endTime,
                                link = singleEventItem.link,
                                isBookmarked = eventBookmark,
                                isUpcomming = isActive
                            )))
                        }
                    } ?: result.postValue(Result.Error("No event detail found for id=$id"))
                } else {
                    result.postValue(Result.Error("Failed to fetch event detail from server"))
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                result.postValue(Result.Error(t.message.toString()))
            }
        })

        result.addSource(eventDao.getEventById(id)) { newData ->
            result.value = if (newData != null) Result.Success(newData) else Result.Error("Event with id $id not found in local DB")
        }

        return result
    }

    companion object {
        @Volatile
        private var instance: EventRepository? = null

        fun getInstance(apiService: ApiService, eventDao: EventDao, appExecutors: AppExecutors): EventRepository =
            instance ?: synchronized(this) {
                instance ?: EventRepository(apiService, eventDao, appExecutors).also { instance = it }
            }
    }
}