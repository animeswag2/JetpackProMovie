package com.learn.personal.moviet.ui.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.learn.personal.moviet.data.CatalogueRepository
import com.learn.personal.moviet.data.local.entity.MovieEntity
import com.learn.personal.moviet.data.local.entity.TvShowEntity
import com.learn.personal.moviet.data.remote.models.Movie
import com.learn.personal.moviet.data.remote.models.TvShow
import com.learn.personal.moviet.di.Injector
import com.learn.personal.moviet.vo.Resource

class HomeViewModel(
    private val catalogueRepository: CatalogueRepository
) : ViewModel() {

    companion object {
        @Volatile
        private var instance: HomeViewModel? = null

        fun getInstance(context: Context): HomeViewModel =
            instance ?: synchronized(this) {
                instance ?: HomeViewModel(Injector.provideRepository(context))
            }
    }

    var page: Int = 1

    fun getListMovies(): LiveData<List<Movie>> = catalogueRepository.getMovies(page)

    fun getListTvShows(): LiveData<List<TvShow>> = catalogueRepository.getTvShows(page)

    fun getMovieById(id: Int): LiveData<Movie> {
        return catalogueRepository.getMovieDetail(id)
    }

    fun getTvShowById(id: Int): LiveData<TvShow> {
        return catalogueRepository.getTvShowDetail(id)
    }

    //Pagination
    fun getListMoviesPagination(): LiveData<Resource<PagedList<MovieEntity>>> = catalogueRepository.getAllMovie(page)

    fun getDetailMovie(id: Int): LiveData<Resource<MovieEntity>> = catalogueRepository.getMovieDetail2(id)

    fun getListTVShowPagination(): LiveData<Resource<PagedList<TvShowEntity>>> = catalogueRepository.getAllTVShow(page)

    fun getDetailTVShow(id: Int): LiveData<Resource<TvShowEntity>> = catalogueRepository.getTVDetail2(id)


}