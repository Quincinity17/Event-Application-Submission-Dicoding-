package com.example.halfsubmission.ui.searchResult

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.halfsubmission.data.repository.EventRepository
import com.example.halfsubmission.di.Injection

/**
 * Factory untuk membuat instance SearchResultViewModel.
 */
class SearchViewModelFactory private constructor(
    private val eventRepository: EventRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchResultViewModel::class.java)) {
            return SearchResultViewModel(eventRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var instance: SearchViewModelFactory? = null

        /**
         * Mendapatkan instance dari SearchViewModelFactory.
         *
         * @param context Konteks aplikasi.
         * @return Instance SearchViewModelFactory.
         */
        fun getInstance(context: Context): SearchViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: SearchViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}
