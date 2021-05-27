package com.learn.personal.moviet.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.learn.personal.moviet.R
import com.learn.personal.moviet.data.api.ApiConfig
import com.learn.personal.moviet.data.local.entity.MovieEntity
import com.learn.personal.moviet.data.local.entity.TvShowEntity
import com.learn.personal.moviet.data.remote.models.Movie
import com.learn.personal.moviet.data.remote.models.TvShow
import com.learn.personal.moviet.databinding.ActivityDetailBinding
import com.learn.personal.moviet.ui.favourite.FavouriteViewModel
import com.learn.personal.moviet.ui.viewmodel.HomeViewModel
import com.learn.personal.moviet.viewmodel.ViewModelFactory
import com.learn.personal.moviet.vo.Status

class DetailActivity : AppCompatActivity() {
    companion object {
        const val TYPE = "type"
        const val ID = "id"
        const val MOVIE = "movie"
        const val TVSHOW = "tvshow"
    }

    private lateinit var binding: ActivityDetailBinding

    private lateinit var film: LiveData<Movie>

    private lateinit var favouriteViewModel: FavouriteViewModel

    private var isBookmarked: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModelFactory = ViewModelFactory.getInstance(this)

        favouriteViewModel = ViewModelProvider(this, viewModelFactory)[FavouriteViewModel::class.java]

        initDetail()

        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun initDetail() {
        val id: Int = intent.getIntExtra(ID, 0)

        showProgressBar()
        when (intent.getStringExtra(TYPE)) {
            "Movie" -> {

                val movie = intent.extras?.getParcelable<MovieEntity>(MOVIE)

                HomeViewModel.getInstance(this).getDetailMovie(id)
                    .observe(this, Observer { status ->
                        when (status.status) {
                            Status.SUCCESS -> {
                                val data = status.data
                                if (data != null) {
                                    displayDetailMovie(data)
                                }
                                hideProgressBar()
                            }
                        }
                    })

                if (movie != null) {
                    bookmarkedState(movie.isBookmarked)
                    binding.fab.setOnClickListener {
                        isBookmarked = !isBookmarked
                        favouriteViewModel.setBookmarkedMovie(movie, isBookmarked)
                        bookmarkedState(isBookmarked)

                        Toast.makeText(this, "$isBookmarked", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            "TvShow" -> {

                val tvShow = intent.extras?.getParcelable<TvShowEntity>(TVSHOW)

                HomeViewModel.getInstance(this).getDetailTVShow(id).observe(this, Observer { status ->
                    when(status.status) {
                        Status.SUCCESS -> {
                            val data = status.data
                            if (data != null) {
                                displayDetailTvShow(data)
                            }
                            hideProgressBar()
                        }
                    }
                })

                if (tvShow != null) {
                    bookmarkedState(tvShow.isBookmarked)
                    binding.fab.setOnClickListener {
                        isBookmarked = !isBookmarked
                        favouriteViewModel.setBookmarkedTVShow(tvShow, isBookmarked)
                        bookmarkedState(isBookmarked)

                        Toast.makeText(this, "$isBookmarked", Toast.LENGTH_SHORT).show()
                    }

                }
            }


        }
    }


    private fun bookmarkedState(isBookmarked: Boolean){
        if (isBookmarked) {
            binding.fab.setImageResource(R.drawable.ic_baseline_favorite_24)
        } else {
            binding.fab.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }
    }

    private fun displayDetailMovie(film: MovieEntity) {
        Glide.with(applicationContext)
            .load(ApiConfig.IMG_URL + film.poster)
            .into(binding.imageView)

        binding.detailTextTitle.text = film.title
        binding.detailTextInfo.text = film.release
        binding.detailTextDescription.text = film.overview
        binding.ratingBarDetail.rating = film.vote / 2

        supportActionBar!!.title = film.title
    }

    private fun displayDetailTvShow(film: TvShowEntity) {
        Glide.with(applicationContext)
            .load(ApiConfig.IMG_URL + film.poster)
            .into(binding.imageView)

        binding.detailTextTitle.text = film.title
        binding.detailTextInfo.text = film.release
        binding.detailTextDescription.text = film.overview
        binding.ratingBarDetail.rating = film.vote / 2

        supportActionBar!!.title = film.title
    }

    private fun hideProgressBar() {
        binding.progressBarDetail.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        binding.progressBarDetail.visibility = View.VISIBLE
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}
