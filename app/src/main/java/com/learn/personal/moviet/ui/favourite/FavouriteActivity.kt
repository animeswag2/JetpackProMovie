package com.learn.personal.moviet.ui.favourite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.learn.personal.moviet.R
import com.learn.personal.moviet.data.local.entity.MovieEntity
import com.learn.personal.moviet.data.remote.models.Movie
import com.learn.personal.moviet.databinding.ActivityFavouriteBinding
import com.learn.personal.moviet.ui.activity.DetailActivity
import com.learn.personal.moviet.ui.adapters.MoviePaginationAdapter
import com.learn.personal.moviet.ui.adapters.OnMovieItemClickCallback
import com.learn.personal.moviet.viewmodel.ViewModelFactory

class FavouriteActivity : AppCompatActivity() {

    private var _binding: ActivityFavouriteBinding ?= null
    private val binding get() = _binding!!


    private lateinit var favouriteViewModel: FavouriteViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFavouriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModelFactory = ViewModelFactory.getInstance(this)
        favouriteViewModel =
            ViewModelProvider(this, viewModelFactory)[FavouriteViewModel::class.java]

        val viewPagerAdapter = ViewPagerAdapter(this, supportFragmentManager)

        binding.viewPager.adapter = viewPagerAdapter
        binding.tabNav.setupWithViewPager(binding.viewPager)

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}