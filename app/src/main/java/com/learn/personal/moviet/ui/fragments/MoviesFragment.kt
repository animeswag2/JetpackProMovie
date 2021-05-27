package com.learn.personal.moviet.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.learn.personal.moviet.R
import com.learn.personal.moviet.data.local.entity.MovieEntity
import com.learn.personal.moviet.data.remote.models.Movie
import com.learn.personal.moviet.databinding.FragmentMoviesBinding
import com.learn.personal.moviet.ui.activity.DetailActivity
import com.learn.personal.moviet.ui.adapters.MovieAdapter
import com.learn.personal.moviet.ui.adapters.MoviePaginationAdapter
import com.learn.personal.moviet.ui.adapters.OnMovieItemClickCallback
import com.learn.personal.moviet.ui.viewmodel.HomeViewModel
import com.learn.personal.moviet.viewmodel.ViewModelFactory
import com.learn.personal.moviet.vo.Status
import java.text.FieldPosition

class MoviesFragment : Fragment(R.layout.fragment_movies) {
    private lateinit var binding: FragmentMoviesBinding
    private lateinit var adapter: MovieAdapter

    private lateinit var moviePaginationAdapter: MoviePaginationAdapter

    private var movieList = ArrayList<Movie>()

    private lateinit var homeViewModel: HomeViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMoviesBinding.bind(view)
        adapter = MovieAdapter(requireActivity(), movieList)
        binding.movieRv.layoutManager = LinearLayoutManager(requireActivity())

      //  binding.movieRv.adapter = adapter

        val viewModelFactory = ViewModelFactory.getInstance(requireActivity())
        homeViewModel = ViewModelProvider(this, viewModelFactory)[HomeViewModel::class.java]

        moviePaginationAdapter = MoviePaginationAdapter(requireActivity())

        binding.movieRv.adapter = moviePaginationAdapter

        moviePaginationAdapter.setOnItemClickCallBack(object: OnMovieItemClickCallback{
            override fun onItemClicked(film: Movie) {

            }

            override fun onItemClickedEntity(film: MovieEntity, position: Int) {
                startActivity(
                    Intent(
                        context,
                        DetailActivity::class.java
                    )
                        .putExtra(DetailActivity.TYPE, "Movie")
                        .putExtra(DetailActivity.ID, film.movieId)
                        .putExtra(DetailActivity.MOVIE,
                            moviePaginationAdapter.currentList?.get(position)
                        )
                )
            }

        })

        initMovies()

        adapter.setOnItemClickCallBack(object: OnMovieItemClickCallback {
            override fun onItemClicked(film: Movie) {
                Log.d("FILM ID", film.id.toString())
                startActivity(
                    Intent(
                        context,
                        DetailActivity::class.java
                    )
                    .putExtra(DetailActivity.TYPE, "Movie")
                    .putExtra(DetailActivity.ID, film.id)
                )
            }

            override fun onItemClickedEntity(film: MovieEntity, position: Int) {

            }
        })
    }

    private fun initMovies() {
        showProgressBar()
//        homeViewModel.getListMovies().observe(viewLifecycleOwner, Observer<List<Movie>> { movies ->
//            Log.d("Search Result", movies.toString())
//            if (movies != null) {
//                movieList.clear()
//                movieList.addAll(movies)
//                adapter.notifyDataSetChanged()
//                hideProgressBar()
//            }
//        })

        homeViewModel.getListMoviesPagination().observe(viewLifecycleOwner, Observer { movies ->
            when (movies.status) {
                Status.SUCCESS -> {
                    hideProgressBar()
                    movies.data?.let {
                        moviePaginationAdapter.submitList(it)
                        Log.d("UKURAN", "initMovies: ${it.size}")
                    }

                }
                Status.LOADING -> {
                    Toast.makeText(requireActivity(), "LOADING", Toast.LENGTH_SHORT).show()
                    showProgressBar()
                }
                Status.ERROR -> {
                    Toast.makeText(requireActivity(), "ERROR", Toast.LENGTH_SHORT).show()
                    hideProgressBar()
                }
            }
        })
    }

    private fun hideProgressBar() {
        binding.progressBarMovie.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        binding.progressBarMovie.visibility = View.VISIBLE
    }


}