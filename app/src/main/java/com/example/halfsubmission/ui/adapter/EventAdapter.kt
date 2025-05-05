package com.example.halfsubmission.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.halfsubmission.R
import com.example.halfsubmission.data.local.entity.EventEntity
import com.example.halfsubmission.databinding.ItemCardBinding
import com.example.halfsubmission.ui.eventDetail.EventDetailActivity
import com.example.halfsubmission.utils.DateFormatter

/**
 * Adapter untuk menampilkan daftar event.
 *
 * @param fragmentSource Sumber fragment.
 * @param onBookmarkClick Callback saat bookmark diklik.
 */
class EventAdapter(
    private val fragmentSource: String,
    private val onBookmarkClick: (EventEntity) -> Unit
) : ListAdapter<EventEntity, EventAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding, fragmentSource, onBookmarkClick)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MyViewHolder(
        private val binding: ItemCardBinding,
        private val sourceFragment: String,
        private val onBookmarkClick: (EventEntity) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(event: EventEntity) {
            with(binding) {
                "${event.quota - event.registrants} quota tersedia".also { tvQuota.text = it }
                tvTitle.text = event.name
                tvDate.text = event.beginTime?.let(DateFormatter::formatDate)
                Glide.with(itemView.context).load(event.imageLogo).into(imageView)

                // Atur warna bookmark berdasarkan status
                val bookmarkColor = if (event.isBookmarked == true) R.color.red else R.color.grey
                tvbookmark.setColorFilter(ContextCompat.getColor(itemView.context, bookmarkColor), android.graphics.PorterDuff.Mode.SRC_IN)

                // Handle klik bookmark
                tvbookmark.setOnClickListener {
                    val newColor = if (event.isBookmarked == true) R.color.grey else R.color.red
                    tvbookmark.setColorFilter(ContextCompat.getColor(itemView.context, newColor), android.graphics.PorterDuff.Mode.SRC_IN)
                    onBookmarkClick(event)
                }

                // Navigasi ke detail event saat item diklik
                itemView.setOnClickListener {
                    itemView.context.startActivity(Intent(itemView.context, EventDetailActivity::class.java).apply {
                        putExtra("EVENT_ID", event.id)
                        putExtra("SOURCE_FRAGMENT", sourceFragment)
                        putExtra("EVENT_UPCOMMING", event.isUpcomming)
                        putExtra("EVENT_BOOKMARKED", event.isBookmarked)
                    })
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<EventEntity>() {
            override fun areItemsTheSame(oldItem: EventEntity, newItem: EventEntity): Boolean = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: EventEntity, newItem: EventEntity): Boolean = oldItem == newItem
        }
    }
}
