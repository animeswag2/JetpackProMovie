package com.learn.personal.moviet.core.data.source.source

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.learn.personal.moviet.core.data.source.source.local.entity.MovieEntity
import com.learn.personal.moviet.core.data.source.source.local.entity.TvShowEntity
import com.learn.personal.moviet.core.data.source.source.room.CatalogueDao


interface LocalDataSourceProtocol {
    fun getAllMovie(): DataSource.Factory<Int, MovieEntity>

    fun insertMovie(listMovie: List<MovieEntity>)

    fun getDetailMovie(imbd: Int): LiveData<MovieEntity>

    fun getAllTVShow(): DataSource.Factory<Int, TvShowEntity>

    fun insertTVShow(listTVShow: List<TvShowEntity>)

    fun getDetailTVShow(imbd: Int): LiveData<TvShowEntity>

    fun setBookmarkedMovie(movieEntity: MovieEntity, newState: Boolean)

    fun setBookmarkedTVShow(tvShowEntity: TvShowEntity, newState: Boolean)

    fun getAllMovieBookmarked(): DataSource.Factory<Int, MovieEntity>

    fun getAllTVShowBookmarked(): DataSource.Factory<Int, TvShowEntity>
}


class LocalDataSource private constructor(private val catalogueDao: CatalogueDao): LocalDataSourceProtocol{
    companion object {
        private var INSTANCE: LocalDataSource ?= null

        fun getInstance(catalogueDao: CatalogueDao): LocalDataSource = INSTANCE ?: LocalDataSource(catalogueDao)
    }

    override fun getAllMovie(): DataSource.Factory<Int, MovieEntity> = catalogueDao.getMovies()

    override fun insertMovie(listMovie: List<MovieEntity>) = catalogueDao.insertMovies(listMovie)

    override fun getDetailMovie(imbd: Int): LiveData<MovieEntity> = catalogueDao.getMoviesById(imbd)

    override fun getAllTVShow(): DataSource.Factory<Int, TvShowEntity> = catalogueDao.getTVShow()

    override fun insertTVShow(listTVShow: List<TvShowEntity>) = catalogueDao.insertTVShow(listTVShow)

    override fun getDetailTVShow(imbd: Int): LiveData<TvShowEntity> = catalogueDao.getTVShowById(imbd)

    override fun setBookmarkedMovie(movieEntity: MovieEntity, newState: Boolean) {
        movieEntity.isBookmarked = newState
        catalogueDao.setBookmarkedStateMovie(movieEntity)
    }

    override fun setBookmarkedTVShow(tvShowEntity: TvShowEntity, newState: Boolean) {
        tvShowEntity.isBookmarked = newState
        catalogueDao.setBookmarkedStateTVShow(tvShowEntity)
    }

    override fun getAllMovieBookmarked(): DataSource.Factory<Int, MovieEntity> = catalogueDao.getBookmarkedMovie()

    override fun getAllTVShowBookmarked(): DataSource.Factory<Int, TvShowEntity> = catalogueDao.getBookmarkedTVShow()


}