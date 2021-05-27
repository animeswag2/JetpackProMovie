package com.learn.personal.moviet.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.learn.personal.moviet.R
import com.learn.personal.moviet.data.local.entity.TvShowEntity
import com.learn.personal.moviet.data.remote.models.Movie
import com.learn.personal.moviet.data.remote.models.TvShow
import com.learn.personal.moviet.databinding.FragmentTvShowsBinding
import com.learn.personal.moviet.ui.activity.DetailActivity
import com.learn.personal.moviet.ui.adapters.OnTvShowItemClickCallback
import com.learn.personal.moviet.ui.adapters.TVShowPaginationAdapter
import com.learn.personal.moviet.ui.adapters.TvShowAdapter
import com.learn.personal.moviet.ui.viewmodel.HomeViewModel
import com.learn.personal.moviet.viewmodel.ViewModelFactory
import com.learn.personal.moviet.vo.Status
import java.text.FieldPosition

class TvShowsFragment : Fragment(R.layout.fragment_tv_shows) {
    private lateinit var binding: FragmentTvShowsBinding
    private lateinit var adapter: TvShowAdapter

    private lateinit var homeViewModel: HomeViewModel

    private lateinit var tvShowPaginationAdapter: TVShowPaginationAdapter

    private var tvShowList = ArrayList<TvShow>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTvShowsBinding.bind(view)
        adapter = TvShowAdapter(requireActivity(), tvShowList)
        binding.tvShowRv.layoutManager = LinearLayoutManager(requireActivity())


        val viewModelFactory = ViewModelFactory.getInstance(requireActivity())
        homeViewModel = ViewModelProvider(this, viewModelFactory)[HomeViewModel::class.java]

        tvShowPaginationAdapter = TVShowPaginationAdapter(requireActivity())

        binding.tvShowRv.adapter = tvShowPaginationAdapter

        initTvShows()

        tvShowPaginationAdapter.setOnItemClickCallBack(object : OnTvShowItemClickCallback {
            override fun onItemClicked(film: TvShow) {
                TODO("Not yet implemented")
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
                            tvShowPaginationAdapter.currentList?.get(position)
                        )
                )
            }

        })


        adapter.setOnItemClickCallBack(object : OnTvShowItemClickCallback {
            override fun onItemClicked(film: TvShow) {
                startActivity(
                    Intent(
                        context,
                        DetailActivity::class.java
                    )
                        .putExtra(DetailActivity.TYPE, "TvShow")
                        .putExtra(DetailActivity.ID, film.id)
                )
            }

            override fun onItemClickedEntity(tvShowEntity: TvShowEntity, position: Int) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun initTvShows() {
        showProgressBar()


        homeViewModel.getListTVShowPagination().observe(viewLifecycleOwner, Observer { status ->
            when (status.status) {
                Status.SUCCESS -> {
                    val data = status.data
                    data.let {
                        tvShowPaginationAdapter.submitList(it)
                    }
                }
            }
        })
    }

    private fun hideProgressBar() {
        binding.progressBarTvShow.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        binding.progressBarTvShow.visibility = View.VISIBLE
    }
}