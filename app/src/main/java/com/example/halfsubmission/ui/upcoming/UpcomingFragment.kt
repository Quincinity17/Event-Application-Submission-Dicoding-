package com.example.halfsubmission.ui.upcoming

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.halfsubmission.databinding.FragmentUpcomingBinding
import com.example.halfsubmission.ui.adapter.EventAdapter

/**
 * Fragment untuk menampilkan daftar event yang akan datang.
 */
class UpcomingFragment : Fragment() {
    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!

    private lateinit var upcomingEventAdapter: EventAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel: UpcomingViewModel by viewModels { UpcomingViewModelFactory.getInstance(requireContext()) }

        // Inisialisasi adapter
        upcomingEventAdapter = EventAdapter("Upcoming") { event ->
            if (event.isBookmarked == true) viewModel.deleteNews(event) else viewModel.saveNews(event)
        }

        // Atur RecyclerView
        binding.listEvent.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = upcomingEventAdapter
        }

        // Observasi data event
        viewModel.getEvent(1).observe(viewLifecycleOwner) { outcome ->
            with(binding) {
                when (outcome) {
                    is com.example.halfsubmission.data.Result.Loading -> setVisibility(shimmerVisible = true, ilustrateVisible = false, listVisible = false)
                    is com.example.halfsubmission.data.Result.Error -> {
                        setVisibility(shimmerVisible = false, ilustrateVisible = false, listVisible = false)
                        Toast.makeText(context, "Error: ${outcome.error}", Toast.LENGTH_SHORT).show()
                    }
                    is com.example.halfsubmission.data.Result.Success -> {
                        shimmerLayout.visibility = View.GONE
                        if (outcome.data.isEmpty()) setVisibility(shimmerVisible = false, ilustrateVisible = true, listVisible =  false)
                        else {
                            setVisibility(shimmerVisible = false, ilustrateVisible = false, listVisible = true)
                            upcomingEventAdapter.submitList(outcome.data)
                        }
                    }
                }
            }
        }
    }

    private fun FragmentUpcomingBinding.setVisibility(
        shimmerVisible: Boolean,
        ilustrateVisible: Boolean,
        listVisible: Boolean
    ) {
        shimmerLayout.visibility = if (shimmerVisible) View.VISIBLE else View.GONE
        ilustrate.visibility = if (ilustrateVisible) View.VISIBLE else View.GONE
        listEvent.visibility = if (listVisible) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
