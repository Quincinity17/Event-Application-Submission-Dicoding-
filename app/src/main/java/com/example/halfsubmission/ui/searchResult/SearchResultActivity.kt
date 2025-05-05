
package com.example.halfsubmission.ui.searchResult

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.halfsubmission.databinding.ActivitySearchResultBinding
import com.example.halfsubmission.ui.adapter.EventAdapter

/**
 * Activity untuk menampilkan hasil pencarian event.
 */
class SearchResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchResultBinding
    private lateinit var eventAdapter: EventAdapter

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val viewModel: SearchResultViewModel by viewModels { SearchViewModelFactory.getInstance(this) }

        // Inisialisasi adapter
        eventAdapter = EventAdapter("Home / Search") { event ->
            if (event.isBookmarked == true) viewModel.deleteNews(event) else viewModel.saveNews(event)
        }

        // Ambil query dan sumber fragment dari intent
        val query = intent.getStringExtra("query") ?: return
        val fragmentSrc = intent.getStringExtra("SOURCE_FRAGMENT") ?: return

        // Atur teks sumber dan query pencarian
        with(binding) {
            "$fragmentSrc /".also { tvSource.text = it }
            "Pencarian \"$query\"".also { vtSearchQuery.text = it }
        }

        // Observasi hasil pencarian
        viewModel.getSearchEvent(query).observe(this) { searchEvent ->
            with(binding) {
                shimmerLayout.visibility = View.GONE
                if (searchEvent.isNullOrEmpty()) {
                    recyclerViewSearchResults.visibility = View.GONE
                    vtHasilPencarian.visibility = View.GONE
                    ilustrate.visibility = View.VISIBLE
                } else {
                    recyclerViewSearchResults.visibility = View.VISIBLE
                    ilustrate.visibility = View.GONE
                    "${searchEvent.size} event ditemukan".also { vtHasilPencarian.text = it }
                    eventAdapter.submitList(searchEvent) { eventAdapter.notifyDataSetChanged() }
                }
            }
        }

        // Atur RecyclerView
        binding.recyclerViewSearchResults.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = eventAdapter
        }

        // Tombol kembali
        binding.back.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }
}
