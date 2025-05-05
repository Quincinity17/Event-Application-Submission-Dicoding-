package com.example.halfsubmission.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.halfsubmission.R
import com.example.halfsubmission.data.Result
import com.example.halfsubmission.data.local.entity.EventEntity
import com.example.halfsubmission.databinding.FragmentHomeBinding
import com.example.halfsubmission.ui.adapter.EventAdapter
import com.example.halfsubmission.ui.searchResult.SearchResultActivity
import com.loopj.android.http.AsyncHttpClient.log

/**
 * Fragment untuk menampilkan daftar event yang akan datang dan sudah selesai.
 */
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var upcomingEventAdapter: EventAdapter
    private lateinit var finishedEventAdapter: EventAdapter
    private var isSearching = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel: HomeViewModel by viewModels { HomeViewModelFactory.getInstance(requireActivity()) }

        // Inisialisasi adapter untuk event yang akan datang dan sudah selesai
        upcomingEventAdapter = EventAdapter("Home") { event ->
            if (event.isBookmarked == true) viewModel.deleteNews(event) else viewModel.saveNews(event)
        }
        finishedEventAdapter = EventAdapter("Home") { event ->
            if (event.isBookmarked == true) viewModel.deleteNews(event) else viewModel.saveNews(event)
        }

        with(binding) {
            listUpcomingEvents.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = upcomingEventAdapter
            }
            listFinishedEvents.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = finishedEventAdapter
            }

            // Navigasi ke fragment Upcoming dan Finished
            linkUpcomingEvent.setOnClickListener { findNavController().navigate(R.id.navigation_Upcoming) }
            linkFinishedEvent.setOnClickListener { findNavController().navigate(R.id.navigation_Finished) }

            // Observasi data event
            viewModel.getEvent(1).observe(viewLifecycleOwner) { handleEventResult(it, shimmerLayoutUpcoming, listUpcomingEvents) }
            viewModel.getEvent(0).observe(viewLifecycleOwner) { handleEventResult(it, shimmerLayoutFinished, listFinishedEvents) }

            // Atur listener untuk search view
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (isSearching) return false
                    isSearching = true

                    query?.let {
                        Toast.makeText(requireContext(), "Searching for: $it", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(requireContext(), SearchResultActivity::class.java).apply {
                            putExtra("SOURCE_FRAGMENT", "Home")
                            putExtra("query", it)
                        })
                    }
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean = false
            })
        }
    }

    private fun handleEventResult(result: Result<List<EventEntity>>, shimmerLayout: View, listView: View) {
        when (result) {
            is Result.Loading -> {
                shimmerLayout.visibility = View.VISIBLE
                listView.visibility = View.GONE
                if (shimmerLayout == binding.shimmerLayoutUpcoming) binding.ilustrate.visibility = View.GONE
            }
            is Result.Error -> {
                shimmerLayout.visibility = View.GONE
                listView.visibility = View.VISIBLE
                Toast.makeText(context, "Error: ${result.error}", Toast.LENGTH_SHORT).show()
            }
            is Result.Success -> {
                shimmerLayout.visibility = View.GONE
                if (result.data.isEmpty() && shimmerLayout == binding.shimmerLayoutUpcoming) {
                    binding.ilustrate.visibility = View.VISIBLE
                    listView.visibility = View.GONE
                } else {
                    if (shimmerLayout == binding.shimmerLayoutUpcoming) binding.ilustrate.visibility = View.GONE
                    listView.visibility = View.VISIBLE
                    (if (listView == binding.listUpcomingEvents) upcomingEventAdapter else finishedEventAdapter).submitList(result.data)
                }
            }
        }
    }


    override fun onResume() {
        super.onResume()
        isSearching = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
