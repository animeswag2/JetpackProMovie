package com.learn.personal.moviet.core.data

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.learn.personal.moviet.core.data.source.source.local.entity.MovieEntity
import com.learn.personal.moviet.core.data.source.source.local.entity.TvShowEntity
import com.learn.personal.moviet.core.data.source.source.local.entity.remote.models.Movie
import com.learn.personal.moviet.core.data.source.source.local.entity.remote.models.TvShow

interface CatalogDataSource {
    fun getMovies(page: Int): LiveData<List<Movie>>

    fun getMovieDetail(movieId: Int): LiveData<Movie>

    fun getTvShows(page: Int): LiveData<List<TvShow>>

    fun getTvShowDetail(tvShowId: Int): LiveData<TvShow>

    fun getAllMovie(page: Int): LiveData<Resource<PagedList<MovieEntity>>>

    fun getMovieDetail2(movieId: Int): LiveData<Resource<MovieEntity>>

    fun setBookmarkedMovie(movieEntity: MovieEntity, isBookmarked: Boolean)

    fun getAllTVShow(page: Int): LiveData<Resource<PagedList<TvShowEntity>>>

    fun getTVDetail2(movieId: Int): LiveData<Resource<TvShowEntity>>

    fun setBookmarkedTVShow(tvShowEntity: TvShowEntity, isBookmarked: Boolean)

    fun getAllBookmarkedMovie(): LiveData<PagedList<MovieEntity>>

    fun getAllBookmarkedTVShow(): LiveData<PagedList<TvShowEntity>>
}