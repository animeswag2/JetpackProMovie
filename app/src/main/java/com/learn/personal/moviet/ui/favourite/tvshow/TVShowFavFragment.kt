package com.learn.personal.moviet.ui.favourite.tvshow

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.learn.personal.moviet.R
import com.learn.personal.moviet.data.local.entity.MovieEntity
import com.learn.personal.moviet.data.local.entity.TvShowEntity
import com.learn.personal.moviet.data.remote.models.Movie
import com.learn.personal.moviet.data.remote.models.TvShow
import com.learn.personal.moviet.databinding.FragmentMovieFavBinding
import com.learn.personal.moviet.databinding.FragmentTVShowFavBinding
import com.learn.personal.moviet.ui.activity.DetailActivity
import com.learn.personal.moviet.ui.adapters.OnMovieItemClickCallback
import com.learn.personal.moviet.ui.adapters.OnTvShowItemClickCallback
import com.learn.personal.moviet.ui.favourite.FavouriteAdapterMovie
import com.learn.personal.moviet.ui.favourite.FavouriteViewModel
import com.learn.personal.moviet.viewmodel.ViewModelFactory

class TVShowFavFragment : Fragment() {
    private var _binding: FragmentTVShowFavBinding?= null
    private val binding get() = _binding!!

    private lateinit var favouriteViewModel: FavouriteViewModel

    private lateinit var adapterMovie: FavouriteAdapterTVShow

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTVShowFavBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModelFactory = ViewModelFactory.getInstance(requireActivity())
        favouriteViewModel =
            ViewModelProvider(this, viewModelFactory)[FavouriteViewModel::class.java]

        adapterMovie = FavouriteAdapterTVShow(requireActivity())

        binding.rvFavouriteTvShow.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvFavouriteTvShow.adapter = adapterMovie

        getData()

        adapterMovie.setOnItemClickCallBack(object: OnTvShowItemClickCallback {

            override fun onItemClicked(film: TvShow) {

            }

            override fun onItemClickedEntity(tvShowEntity: TvShowEntity, position: Int) {
                startActivity(
                    Intent(
                        context,
                        DetailActivity::class.java
                    )
                        .putExtra(DetailActivity.TYPE, "TvShow")
                        .putExtra(DetailActivity.ID, tvShowEntity.movieId)
                        .putExtra(
                            DetailActivity.TVSHOW,
                            adapterMovie.currentList?.get(position)
                        )
                )
            }

        })


    }

    private fun getData(){
        favouriteViewModel.getTVShowFav().observe(viewLifecycleOwner, Observer {
            adapterMovie.submitList(it)
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}