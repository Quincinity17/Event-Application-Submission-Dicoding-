package com.example.halfsubmission.ui.upcoming

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.halfsubmission.data.repository.BookmarkRepository
import com.example.halfsubmission.data.repository.EventRepository
import com.example.halfsubmission.di.Injection

/**
 * Factory untuk membuat instance UpcomingViewModel.
 */
class UpcomingViewModelFactory private constructor(
    private val eventRepository: EventRepository,
    private val bookmarkRepository: BookmarkRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UpcomingViewModel::class.java)) {
            return UpcomingViewModel(eventRepository, bookmarkRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var instance: UpcomingViewModelFactory? = null

        /**
         * Mendapatkan instance dari UpcomingViewModelFactory.
         *
         * @param context Konteks aplikasi.
         * @return Instance UpcomingViewModelFactory.
         */
        fun getInstance(context: Context): UpcomingViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: UpcomingViewModelFactory(
                    Injection.provideRepository(context),
                    Injection.provideBookmarkRepository(context)
                ).also { instance = it }
            }
    }
}
