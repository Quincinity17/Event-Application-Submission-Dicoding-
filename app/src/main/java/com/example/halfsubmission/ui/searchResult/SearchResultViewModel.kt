package com.example.halfsubmission.ui.searchResult

import androidx.lifecycle.ViewModel
import com.example.halfsubmission.data.repository.EventRepository
import com.example.halfsubmission.data.local.entity.EventEntity

/**
 * ViewModel untuk mengelola data hasil pencarian event.
 *
 * @param eventRepository Repository untuk mengakses data event.
 */
class SearchResultViewModel(private val eventRepository: EventRepository) : ViewModel() {

    /**
     * Mengambil hasil pencarian event berdasarkan query.
     *
     * @param query Kata kunci pencarian.
     * @return LiveData berisi daftar event yang sesuai.
     */
    fun getSearchEvent(query: String) = eventRepository.getSearchEvent(query)

    /**
     * Menyimpan event ke bookmark.
     *
     * @param event Event yang akan disimpan.
     */
    fun saveNews(event: EventEntity) {
        eventRepository.setBookmarkedEvent(event, true)
    }

    /**
     * Menghapus event dari bookmark.
     *
     * @param event Event yang akan dihapus.
     */
    fun deleteNews(event: EventEntity) {
        eventRepository.setBookmarkedEvent(event, false)
    }
}
