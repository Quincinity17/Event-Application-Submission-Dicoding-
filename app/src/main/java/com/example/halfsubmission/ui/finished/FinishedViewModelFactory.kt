package com.example.halfsubmission.ui.finished

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.halfsubmission.data.repository.BookmarkRepository
import com.example.halfsubmission.data.repository.EventRepository
import com.example.halfsubmission.di.Injection

/**
 * Factory untuk membuat instance FinishedViewModel.
 */
class FinishedViewModelFactory private constructor(
    private val eventRepository: EventRepository,
    private val bookmarkRepository: BookmarkRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FinishedViewModel::class.java)) {
            return FinishedViewModel(eventRepository, bookmarkRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var instance: FinishedViewModelFactory? = null

        /**
         * Mendapatkan instance dari FinishedViewModelFactory.
         *
         * @param context Konteks aplikasi.
         * @return Instance FinishedViewModelFactory.
         */
        fun getInstance(context: Context): FinishedViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: FinishedViewModelFactory(
                    Injection.provideRepository(context),
                    Injection.provideBookmarkRepository(context)
                ).also { instance = it }
            }
    }
}
