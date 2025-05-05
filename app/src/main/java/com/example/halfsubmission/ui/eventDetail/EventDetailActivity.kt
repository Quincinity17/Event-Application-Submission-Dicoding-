package com.example.halfsubmission.ui.eventDetail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.example.halfsubmission.data.Result
import com.example.halfsubmission.data.local.entity.EventEntity
import com.example.halfsubmission.databinding.ActivityEventDetailBinding
import com.example.halfsubmission.utils.DateFormatter
import com.google.android.material.snackbar.Snackbar
/**
 * Activity untuk menampilkan detail event.
 */
class EventDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEventDetailBinding
    private val viewModel: EventDetailViewModel by viewModels { EventDetailViewModelFactory.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        // Ambil data dari intent
        val (eventId, isBookmarked, isUpcoming) = intent.getEventData()

        // Observasi detail event
        viewModel.getEventDetail(eventId, isUpcoming, isBookmarked).observe(this) { outcome ->
            displayLoadingState(outcome is Result.Loading)
            when (outcome) {
                is Result.Success -> updateUserInterface(outcome.data)
                is Result.Error -> Snackbar.make(binding.root, "Error: ${outcome.error}", Snackbar.LENGTH_LONG).show()
                else -> Unit
            }
        }

        // Tombol kembali
        binding.back.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    /**
     * Memperbarui UI dengan data event.
     *
     * @param eventDetail Data event yang akan ditampilkan.
     */
    private fun updateUserInterface(eventDetail: EventEntity) {
        with(binding) {
            Glide.with(this@EventDetailActivity).load(eventDetail.imageLogo).into(imageViewLogo)
            tvTitle.text = eventDetail.name
            tvDescription.text = HtmlCompat.fromHtml(eventDetail.description.orEmpty(), HtmlCompat.FROM_HTML_MODE_LEGACY)
            tvDateEvent.text = eventDetail.beginTime?.let(DateFormatter::formatDate)
            tvCategory.text = eventDetail.category
            tvOwnerName.text = eventDetail.ownerName
            tvQuota.text = (eventDetail.quota - eventDetail.registrants).toString()
            "${intent.getStringExtra("SOURCE_FRAGMENT")} /".also { tvSource.text = it }

            // Tombol untuk membuka link event
            buttonRegister.setOnClickListener {
                eventDetail.link?.let { url ->
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                }
            }
        }
    }

    /**
     * Menampilkan atau menyembunyikan loading state.
     *
     * @param isLoading True untuk menampilkan loading, false untuk menyembunyikan.
     */
    private fun displayLoadingState(isLoading: Boolean) {
        with(binding) {
            shimmerLayout.run {
                visibility = if (isLoading) View.VISIBLE else View.GONE
                if (isLoading) startShimmer() else stopShimmer()
            }
            listOf(imageViewLogo, eventDetail, tvDescription, buttonRegister).forEach {
                it.visibility = if (isLoading) View.GONE else View.VISIBLE
            }
        }
    }

    /**
     * Mengambil data dari halaman sebelumnya
     */
    private fun Intent.getEventData(): Triple<Int, Boolean, Boolean> {
        return Triple(
            getIntExtra("EVENT_ID", -1),
            getBooleanExtra("EVENT_BOOKMARKED", false),
            getBooleanExtra("EVENT_UPCOMMING", false)
        )
    }
}
