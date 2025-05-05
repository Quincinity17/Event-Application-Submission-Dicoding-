package com.example.halfsubmission.ui.finished

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.halfsubmission.databinding.FragmentFinishedBinding
import com.example.halfsubmission.ui.adapter.EventAdapter

/**
 * Fragment untuk menampilkan daftar event yang sudah selesai.
 */
class FinishedFragment : Fragment() {
    private var _binding: FragmentFinishedBinding? = null
    private val binding get() = _binding!!

    private lateinit var finishedEventAdapter: EventAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel: FinishedViewModel by viewModels { FinishedViewModelFactory.getInstance(requireContext()) }

        // Inisialisasi adapter
        finishedEventAdapter = EventAdapter("Finished") { event ->
            if (event.isBookmarked == true) viewModel.deleteNews(event) else viewModel.saveNews(event)
        }

        // Atur RecyclerView
        binding.listEvent.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = finishedEventAdapter
        }

        // Observasi data event
        viewModel.getEvent(0).observe(viewLifecycleOwner) { outcome ->
            with(binding) {
                when (outcome) {
                    is com.example.halfsubmission.data.Result.Loading -> setVisibility(
                        shimmerVisible = true,
                        listVisible = false
                    )
                    is com.example.halfsubmission.data.Result.Error -> {
                        setVisibility(shimmerVisible = false, listVisible = false)
                        Toast.makeText(context, "Error: ${outcome.error}", Toast.LENGTH_SHORT).show()
                    }
                    is com.example.halfsubmission.data.Result.Success -> {
                        setVisibility(false, listVisible = true)
                        finishedEventAdapter.submitList(outcome.data)
                    }
                }
            }
        }
    }
    private fun FragmentFinishedBinding.setVisibility(
        shimmerVisible: Boolean,
        listVisible: Boolean) {
        shimmerLayoutFragmentFinished.visibility = if (shimmerVisible) View.VISIBLE else View.GONE
        listEvent.visibility = if (listVisible) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}