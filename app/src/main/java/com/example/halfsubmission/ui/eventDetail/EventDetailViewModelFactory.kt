package com.example.halfsubmission.ui.eventDetail

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.halfsubmission.data.repository.EventRepository
import com.example.halfsubmission.di.Injection

/**
 * Factory untuk membuat instance EventDetailViewModel.
 */
class EventDetailViewModelFactory private constructor(
    private val eventRepository: EventRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EventDetailViewModel::class.java)) {
            return EventDetailViewModel(eventRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var instance: EventDetailViewModelFactory? = null

        /**
         * Mendapatkan instance dari EventDetailViewModelFactory.
         *
         * @param context Konteks aplikasi.
         * @return Instance EventDetailViewModelFactory.
         */
        fun getInstance(context: Context): EventDetailViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: EventDetailViewModelFactory(Injection.provideRepository(context)).also { instance = it }
            }
    }
}
