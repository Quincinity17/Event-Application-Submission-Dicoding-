package com.example.halfsubmission.ui.bookmark

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.halfsubmission.data.repository.BookmarkRepository
import com.example.halfsubmission.data.repository.EventRepository
import com.example.halfsubmission.di.Injection


/**
 * Factory untuk membuat instance BookmarkViewModel.
 */
class BookmarkViewModelFactory private constructor(
    private val eventRepository: EventRepository,
    private val bookmarkRepository: BookmarkRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookmarkViewModel::class.java)) {
            return BookmarkViewModel(eventRepository, bookmarkRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var instance: BookmarkViewModelFactory? = null

        /**
         * Mendapatkan instance dari BookmarkViewModelFactory.
         *
         * @param context Konteks aplikasi.
         * @return Instance BookmarkViewModelFactory.
         */
        fun getInstance(context: Context): BookmarkViewModelFactory {
            return instance ?: synchronized(this) {
                instance ?: BookmarkViewModelFactory(
                    Injection.provideRepository(context),
                    Injection.provideBookmarkRepository(context)
                ).also { instance = it }
            }
        }
    }
}
