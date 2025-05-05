package com.example.halfsubmission.ui.eventDetail

import androidx.lifecycle.ViewModel
import com.example.halfsubmission.data.repository.EventRepository

/**
 * ViewModel untuk mengelola data detail event.
 *
 * @param eventRepository Repository untuk mengakses data event.
 */
class EventDetailViewModel(private val eventRepository: EventRepository) : ViewModel() {

    /**
     * Mengambil detail event berdasarkan ID.
     *
     * @param id ID event.
     * @param isActive Status apakah event aktif.
     * @param eventBookmark Status bookmark event.
     * @return LiveData berisi detail event.
     */
    fun getEventDetail(id: Int, isActive: Boolean, eventBookmark: Boolean) =
        eventRepository.getEventDetail(id, isActive, eventBookmark)
}
