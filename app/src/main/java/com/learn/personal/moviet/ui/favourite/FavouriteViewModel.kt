package com.learn.personal.moviet.ui.favourite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.learn.personal.moviet.data.CatalogueRepository
import com.learn.personal.moviet.data.local.entity.MovieEntity
import com.learn.personal.moviet.data.local.entity.TvShowEntity
import com.learn.personal.moviet.vo.Resource

class FavouriteViewModel(private val catalogueRepository: CatalogueRepository) : ViewModel() {
    
    fun getMovieFav(): LiveData<PagedList<MovieEntity>> = catalogueRepository.getAllBookmarkedMovie()

    fun getTVShowFav(): LiveData<PagedList<TvShowEntity>> = catalogueRepository.getAllBookmarkedTVShow()

    fun setBookmarkedMovie(movieEntity: MovieEntity, newState:Boolean) = catalogueRepository.setBookmarkedMovie(movieEntity, newState)

    fun setBookmarkedTVShow(tvShowEntity: TvShowEntity, newState: Boolean) = catalogueRepository.setBookmarkedTVShow(tvShowEntity, newState)

}

