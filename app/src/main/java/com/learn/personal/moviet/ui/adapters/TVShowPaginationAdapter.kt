package com.learn.personal.moviet.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.learn.personal.moviet.R
import com.learn.personal.moviet.data.api.ApiConfig
import com.learn.personal.moviet.data.local.entity.MovieEntity
import com.learn.personal.moviet.data.local.entity.TvShowEntity
import com.learn.personal.moviet.databinding.SingleItemRowBinding

class TVShowPaginationAdapter(
    private val context: Context,
) : PagedListAdapter<TvShowEntity, TVShowPaginationAdapter.TvShowHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<TvShowEntity>() {
            override fun areItemsTheSame(oldItem: TvShowEntity, newItem: TvShowEntity): Boolean {
                return oldItem.movieId == newItem.movieId
            }

            override fun areContentsTheSame(oldItem: TvShowEntity, newItem: TvShowEntity): Boolean {
                return oldItem == newItem
            }

        }
    }

    private lateinit var onItemClickCallback: OnTvShowItemClickCallback

    fun setOnItemClickCallBack(onItemClickCallBack: OnTvShowItemClickCallback){
        this.onItemClickCallback = onItemClickCallBack
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TvShowHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.single_item_row, parent, false)
        return TvShowHolder(view, context, onItemClickCallback)
    }

    override fun onBindViewHolder(holder: TvShowHolder, position: Int) {
        val tvShow = getItem(position)
        if ( tvShow != null) {
            holder.bind(tvShow)
        }

    }

    class TvShowHolder(
        private val view: View,
        private val context: Context,
        private val callback: OnTvShowItemClickCallback
    ) : RecyclerView.ViewHolder(view) {
        @SuppressLint("SetTextI18n")
        fun bind(film: TvShowEntity) {
            val binding = SingleItemRowBinding.bind(view)
            binding.textTitle.text = film.title
            binding.textInfo.text = "Release: ${film.release}"
            Glide.with(context)
                .load(ApiConfig.IMG_URL + film.poster)
                .into(binding.imagePoster)
            binding.container.setOnClickListener {
                callback.onItemClickedEntity(film, adapterPosition)
            }
        }
    }
}