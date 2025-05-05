package com.example.halfsubmission.ui.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.halfsubmission.data.repository.BookmarkRepository
import com.example.halfsubmission.data.repository.EventRepository
import com.example.halfsubmission.di.Injection

/**
 * Factory untuk membuat instance HomeViewModel.
 */
class HomeViewModelFactory private constructor(
    private val eventRepository: EventRepository,
    private val bookmarkRepository: BookmarkRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(eventRepository, bookmarkRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var instance: HomeViewModelFactory? = null

        /**
         * Mendapatkan instance dari HomeViewModelFactory.
         *
         * @param context Konteks aplikasi.
         * @return Instance HomeViewModelFactory.
         */
        fun getInstance(context: Context): HomeViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: HomeViewModelFactory(
                    Injection.provideRepository(context),
                    Injection.provideBookmarkRepository(context)
                ).also { instance = it }
            }
    }
}
