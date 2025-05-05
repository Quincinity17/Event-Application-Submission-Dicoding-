package com.example.halfsubmission.ui.bookmark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.halfsubmission.data.Result
import com.example.halfsubmission.data.local.entity.BookmarkEntity
import com.example.halfsubmission.data.local.entity.EventEntity
import com.example.halfsubmission.databinding.FragmentBookmarkBinding
import com.example.halfsubmission.ui.adapter.EventAdapter


/**
 * Fragment untuk menampilkan daftar event yang di-bookmark.
 */
class BookmarkFragment : Fragment() {
    private var _binding: FragmentBookmarkBinding? = null
    private val binding get() = _binding!!

    private lateinit var bookmarkEventAdapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = BookmarkViewModelFactory.getInstance(requireContext())
        val viewModel: BookmarkViewModel by viewModels { factory }

        // Inisialisasi adapter untuk menampilkan event yang di-bookmark
        bookmarkEventAdapter = EventAdapter(
            fragmentSource = "Bookmark",
            onBookmarkClick = { event ->
                val bookmarkEntity = BookmarkEntity(
                    id = event.id,
                    name = event.name,
                    summary = event.summary,
                    description = event.description,
                    imageLogo = event.imageLogo,
                    mediaCover = event.mediaCover,
                    category = event.category,
                    ownerName = event.ownerName,
                    cityName = event.cityName,
                    quota = event.quota,
                    registrants = event.registrants,
                    beginTime = event.beginTime,
                    endTime = event.endTime,
                    link = event.link,
                    isUpcomming = event.isUpcomming
                )

                // Hapus atau simpan bookmark berdasarkan status saat ini
                if (event.isBookmarked == true) {
                    viewModel.deleteBookmark(event, bookmarkEntity)
                } else {
                    viewModel.saveBookmark(event, bookmarkEntity)
                }
            }
        )

        // Atur RecyclerView dengan adapter
        binding.listEvent.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = bookmarkEventAdapter
        }

        // Observasi data event yang di-bookmark
        viewModel.getBookmarkedEvents().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    with(binding){
                        shimmerLayoutFragmentFinished.visibility = View.VISIBLE
                        listEvent.visibility = View.GONE
                        ilustrate.visibility = View.GONE
                    }
                }

                is Result.Error -> {
                    with(binding){
                        shimmerLayoutFragmentFinished.visibility = View.GONE
                        listEvent.visibility = View.GONE
                        ilustrate.visibility = View.GONE
                    }
                    Toast.makeText(context, "Error: ${result.error}", Toast.LENGTH_SHORT).show()
                }

                is Result.Success -> {
                    if (result.data.isEmpty()) {
                        // Tampilkan ilustrasi jika tidak ada bookmark
                        with(binding){
                            shimmerLayoutFragmentFinished.visibility = View.GONE
                            listEvent.visibility = View.GONE
                            ilustrate.visibility = View.VISIBLE
                        }
                    } else {
                        // Konversi BookmarkEntity ke EventEntity dan tampilkan
                        val bookmarkedEvents = result.data.map { bookmark ->
                            EventEntity(
                                id = bookmark.id,
                                name = bookmark.name,
                                summary = bookmark.summary,
                                description = bookmark.description,
                                imageLogo = bookmark.imageLogo,
                                mediaCover = bookmark.mediaCover,
                                category = bookmark.category,
                                ownerName = bookmark.ownerName,
                                cityName = bookmark.cityName,
                                quota = bookmark.quota,
                                registrants = bookmark.registrants,
                                beginTime = bookmark.beginTime,
                                endTime = bookmark.endTime,
                                link = bookmark.link,
                                isBookmarked = true,
                                isUpcomming = bookmark.isUpcomming
                            )
                        }

                        with(binding){
                            shimmerLayoutFragmentFinished.visibility = View.GONE
                            listEvent.visibility = View.VISIBLE
                            ilustrate.visibility = View.GONE
                        }
                        bookmarkEventAdapter.submitList(bookmarkedEvents)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
