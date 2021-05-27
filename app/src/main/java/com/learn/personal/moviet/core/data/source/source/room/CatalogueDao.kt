package com.learn.personal.moviet.core.data.source.source.room

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.learn.personal.moviet.core.data.source.source.local.entity.MovieEntity
import com.learn.personal.moviet.core.data.source.source.local.entity.TvShowEntity

@Dao
interface CatalogueDao {

    @Insert
    fun insertMovies(listMovie: List<MovieEntity>)

    @Insert
    fun insertTVShow(listTVShow: List<TvShowEntity>)

    @Query("SELECT * FROM movieentities")
    fun getMovies(): DataSource.Factory<Int, MovieEntity>

    @Query("SELECT * FROM movieentities WHERE :imbdId = movieId")
    fun getMoviesById (imbdId: Int): LiveData<MovieEntity>

    @Query("SELECT * FROM tvshowentities")
    fun getTVShow(): DataSource.Factory<Int, TvShowEntity>

    @Query("SELECT * FROM tvshowentities WHERE movieId = :imbdId")
    fun getTVShowById (imbdId: Int): LiveData<TvShowEntity>

    @Update
    fun setBookmarkedStateMovie(movieEntity: MovieEntity)

    @Update
    fun setBookmarkedStateTVShow(tvShowEntity: TvShowEntity)

    @Query("SELECT * FROM movieentities WHERE isBookmarked = 1")
    fun getBookmarkedMovie(): DataSource.Factory<Int, MovieEntity>

    @Query("SELECT * FROM tvshowentities WHERE isBookmarked = 1")
    fun getBookmarkedTVShow(): DataSource.Factory<Int, TvShowEntity>


}