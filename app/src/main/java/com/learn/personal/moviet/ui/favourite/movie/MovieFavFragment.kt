package com.learn.personal.moviet.ui.favourite.movie

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.learn.personal.moviet.R
import com.learn.personal.moviet.data.local.entity.MovieEntity
import com.learn.personal.moviet.data.remote.models.Movie
import com.learn.personal.moviet.databinding.ActivityFavouriteBinding
import com.learn.personal.moviet.databinding.FragmentMovieFavBinding
import com.learn.personal.moviet.ui.activity.DetailActivity
import com.learn.personal.moviet.ui.adapters.OnMovieItemClickCallback
import com.learn.personal.moviet.ui.favourite.FavouriteAdapterMovie
import com.learn.personal.moviet.ui.favourite.FavouriteViewModel
import com.learn.personal.moviet.viewmodel.ViewModelFactory

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MovieFavFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MovieFavFragment : Fragment() {

    private var _binding: FragmentMovieFavBinding?= null
    private val binding get() = _binding!!

    private lateinit var favouriteViewModel: FavouriteViewModel

    private lateinit var adapterMovie: FavouriteAdapterMovie

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMovieFavBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModelFactory = ViewModelFactory.getInstance(requireActivity())
        favouriteViewModel =
            ViewModelProvider(this, viewModelFactory)[FavouriteViewModel::class.java]

        adapterMovie = FavouriteAdapterMovie(requireActivity())

        binding.rvFavouriteMovie.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvFavouriteMovie.adapter = adapterMovie

        getData()

        adapterMovie.setOnItemClickCallBack(object: OnMovieItemClickCallback {
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
                            adapterMovie.currentList?.get(position)
                        )
                )
            }

        })

    }

    private fun getData(){
        favouriteViewModel.getMovieFav().observe(viewLifecycleOwner, Observer {
            adapterMovie.submitList(it)
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}