package com.learn.personal.moviet.ui.favourite.tvshow

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
import com.learn.personal.moviet.ui.adapters.OnMovieItemClickCallback
import com.learn.personal.moviet.ui.adapters.OnTvShowItemClickCallback

class FavouriteAdapterTVShow(
    private val context: Context,
) : PagedListAdapter<TvShowEntity, FavouriteAdapterTVShow.MovieHolder>(DIFF_CALLBACK) {

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


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.single_item_row, parent, false)
        return MovieHolder(view, context, onItemClickCallback)
    }

    override fun onBindViewHolder(holder: MovieHolder, position: Int) {
        val movie = getItem(position)
        if ( movie != null) {
            holder.bind(movie)
        }

    }

    class MovieHolder(
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