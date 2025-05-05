package com.example.halfsubmission.ui.bookmark

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.halfsubmission.data.Result
import com.example.halfsubmission.data.local.entity.BookmarkEntity
import com.example.halfsubmission.data.repository.EventRepository
import com.example.halfsubmission.data.local.entity.EventEntity
import com.example.halfsubmission.data.repository.BookmarkRepository
import kotlinx.coroutines.launch

/**
 * ViewModel untuk mengelola data bookmark.
 *
 * @param eventRepository Repository untuk mengakses data event.
 * @param bookmarkRepository Repository untuk mengakses data bookmark.
 */
class BookmarkViewModel(
    private val eventRepository: EventRepository,
    private val bookmarkRepository: BookmarkRepository
) : ViewModel() {

    /**
     * Mengambil daftar event yang di-bookmark.
     *
     * @return LiveData berisi hasil operasi (Result<List<BookmarkEntity>>).
     */
    fun getBookmarkedEvents(): LiveData<Result<List<BookmarkEntity>>> {
        return bookmarkRepository.getAllBookmarks()
    }

    /**
     * Menyimpan event ke bookmark.
     *
     * @param eventLocal EventEntity yang akan di-bookmark.
     * @param eventBookmark BookmarkEntity yang akan disimpan.
     */
    fun saveBookmark(eventLocal: EventEntity, eventBookmark: BookmarkEntity) {
        viewModelScope.launch {
            eventRepository.setBookmarkedEvent(eventLocal, true)
            bookmarkRepository.insertBookmark(eventBookmark.id)
        }
    }

    /**
     * Menghapus event dari bookmark.
     *
     * @param eventLocal EventEntity yang akan dihapus dari bookmark.
     * @param eventBookmark BookmarkEntity yang akan dihapus.
     */
    fun deleteBookmark(eventLocal: EventEntity, eventBookmark: BookmarkEntity) {
        viewModelScope.launch {
            eventRepository.setBookmarkedEvent(eventLocal, false)
            bookmarkRepository.deleteBookmarkById(eventBookmark)
        }
    }
}
