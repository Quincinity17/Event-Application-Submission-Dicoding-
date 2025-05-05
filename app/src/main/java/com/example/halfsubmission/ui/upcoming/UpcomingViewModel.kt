package com.example.halfsubmission.ui.upcoming

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.halfsubmission.data.local.entity.BookmarkEntity
import com.example.halfsubmission.data.repository.EventRepository
import com.example.halfsubmission.data.local.entity.EventEntity
import com.example.halfsubmission.data.repository.BookmarkRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * ViewModel untuk mengelola data event yang akan datang.
 *
 * @param eventRepository Repository untuk mengakses data event.
 * @param bookmarkRepository Repository untuk mengakses data bookmark.
 */
class UpcomingViewModel(
    private val eventRepository: EventRepository,
    private val bookmarkRepository: BookmarkRepository
) : ViewModel() {

    /**
     * Mengambil daftar event berdasarkan status aktif.
     *
     * @param active Status aktif event (1 untuk akan datang).
     * @return LiveData berisi daftar event.
     */
    fun getEvent(active: Int) = eventRepository.getEvent(active)

    /**
     * Menyimpan event ke bookmark.
     *
     * @param event Event yang akan disimpan.
     */
    fun saveNews(event: EventEntity) {
        viewModelScope.launch {
            eventRepository.setBookmarkedEvent(event, true)
            bookmarkRepository.insertBookmark(event.id)
        }
    }

    /**
     * Menghapus event dari bookmark.
     *
     * @param event Event yang akan dihapus.
     */
    fun deleteNews(event: EventEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            eventRepository.setBookmarkedEvent(event, false)
            val bookmark = BookmarkEntity(
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
            )
            bookmarkRepository.deleteBookmarkById(bookmark)
        }
    }
}
