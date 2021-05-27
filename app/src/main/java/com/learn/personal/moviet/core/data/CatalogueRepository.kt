package com.learn.personal.moviet.core.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.learn.personal.moviet.core.data.source.source.LocalDataSource
import com.learn.personal.moviet.core.data.source.source.local.entity.MovieEntity
import com.learn.personal.moviet.core.data.source.source.local.entity.TvShowEntity
import com.learn.personal.moviet.core.data.source.source.local.entity.remote.ApiResponse
import com.learn.personal.moviet.core.data.source.source.local.entity.remote.models.Movie
import com.learn.personal.moviet.core.data.source.source.local.entity.remote.models.TvShow
import com.learn.personal.moviet.core.data.source.source.local.entity.remote.RemoteDataSource
import com.learn.personal.moviet.core.data.source.source.local.entity.remote.responses.MovieResponse
import com.learn.personal.moviet.core.data.source.source.local.entity.remote.responses.TvShowResponse
import com.learn.personal.moviet.utils.AppExecutors
import kotlinx.coroutines.*

class CatalogueRepository(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val appExecutors: AppExecutors
) : CatalogDataSource {

    companion object {
        @Volatile
        private var instance: CatalogueRepository? = null

        fun getInstance(
            localDataSource: LocalDataSource,
            remoteDataSource: RemoteDataSource,
            appExecutors: AppExecutors
        ): CatalogueRepository =
            instance ?: synchronized(this) {
                instance ?: CatalogueRepository(localDataSource, remoteDataSource, appExecutors)
            }
    }

    override fun getMovies(page: Int): LiveData<List<Movie>> {
        val listMovieResult = MutableLiveData<List<Movie>>()
        CoroutineScope(Dispatchers.IO).launch {
            remoteDataSource.getMovies(page, object : RemoteDataSource.LoadMoviesCallback {
                override fun onAllMoviesReceived(movieResponse: List<Movie>) {
                    val movieList = ArrayList<Movie>()
                    for (response in movieResponse) {
                        val movie = Movie(
                            id = response.id,
                            title = response.title,
                            overview = response.overview,
                            poster = response.poster,
                            vote = response.vote,
                            release = response.release
                        )
                        movieList.add(movie)
                    }
                    listMovieResult.postValue(movieList)
                }
            })
        }

        return listMovieResult
    }

    override fun getMovieDetail(movieId: Int): LiveData<Movie> {
        val movieResult = MutableLiveData<Movie>()
        CoroutineScope(Dispatchers.IO).launch {
            remoteDataSource.getMovieDetail(
                movieId,
                object : RemoteDataSource.LoadMovieDetailCallback {
                    override fun onMovieDetailReceived(movieResponse: Movie) {
                        val movie = Movie(
                            id = movieResponse.id,
                            title = movieResponse.title,
                            overview = movieResponse.overview,
                            poster = movieResponse.poster,
                            vote = movieResponse.vote,
                            release = movieResponse.release
                        )

                        movieResult.postValue(movie)
                    }
                })
        }

        return movieResult
    }

    override fun getTvShows(page: Int): LiveData<List<TvShow>> {
        val listTvShowResult = MutableLiveData<List<TvShow>>()
        CoroutineScope(Dispatchers.IO).launch {
            remoteDataSource.getTvShows(page, object : RemoteDataSource.LoadTvShowsCallback {
                override fun onAllTvShowsReceived(tvShowResponse: List<TvShow>) {
                    val tvShowList = ArrayList<TvShow>()
                    for (response in tvShowResponse) {
                        val tvShow = TvShow(
                            id = response.id,
                            title = response.title,
                            overview = response.overview,
                            poster = response.poster,
                            vote = response.vote,
                            release = response.release
                        )
                        tvShowList.add(tvShow)
                    }

                    listTvShowResult.postValue(tvShowList)
                }
            })
        }

        return listTvShowResult
    }

    override fun getTvShowDetail(tvShowId: Int): LiveData<TvShow> {
        val tvShowResult = MutableLiveData<TvShow>()
        CoroutineScope(Dispatchers.IO).launch {
            remoteDataSource.getTvShowDetail(
                tvShowId,
                object : RemoteDataSource.LoadTvShowDetailCallback {
                    override fun onTvShowDetailReceived(tvShowResponse: TvShow) {
                        val tvShow = TvShow(
                            id = tvShowResponse.id,
                            title = tvShowResponse.title,
                            overview = tvShowResponse.overview,
                            poster = tvShowResponse.poster,
                            vote = tvShowResponse.vote,
                            release = tvShowResponse.release
                        )

                        tvShowResult.postValue(tvShow)
                    }
                })
        }

        return tvShowResult
    }

    override fun getAllMovie(page: Int): LiveData<Resource<PagedList<MovieEntity>>> {
        return object : NetworkBoundResource<PagedList<MovieEntity>, MovieResponse>(appExecutors) {
            override fun loadFromDB(): LiveData<PagedList<MovieEntity>> {
                val config = PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setInitialLoadSizeHint(5)
                    .setPageSize(5)
                    .build()

                return LivePagedListBuilder(localDataSource.getAllMovie(), config).build()
            }

            override fun shouldFetch(data: PagedList<MovieEntity>?): Boolean {
                return data!!.isEmpty()

            }

            override fun openCall(): LiveData<ApiResponse<MovieResponse>> {
//                GlobalScope.launch(Dispatchers.IO) {
//                    remoteDataSource.getMovies(page, object: RemoteDataSource.LoadMoviesCallback {
//                        override fun onAllMoviesReceived(movieResponse: List<Movie>) {
//
//                        }
//
//                    })
//                }

                return remoteDataSource.getAllMovies(page)
            }

            override fun saveCallResult(data: MovieResponse) {
                val listMovie = ArrayList<MovieEntity>()

                for (i in data.results!!) {
                    val movieEntity = MovieEntity(
                        i.id,
                        i.title!!,
                        i.overview!!,
                        i.poster!!,
                        i.vote,
                        i.release!!,
                        false
                    )

                    listMovie.add(movieEntity)
                }

                localDataSource.insertMovie(listMovie)

            }

        }.asLiveData()
    }

    override fun getMovieDetail2(movieId: Int): LiveData<Resource<MovieEntity>> {
        return object : NetworkBoundResource<MovieEntity, MovieResponse>(appExecutors) {
            override fun loadFromDB(): LiveData<MovieEntity> {

                return localDataSource.getDetailMovie(movieId)
            }

            override fun shouldFetch(data: MovieEntity?): Boolean {
                return data!!.title.isEmpty()
            }

            override fun openCall(): LiveData<ApiResponse<MovieResponse>> {
                return remoteDataSource.getAllMovies(1)
            }

            override fun saveCallResult(data: MovieResponse) {
                val listMovie = ArrayList<MovieEntity>()

                for (i in data.results!!) {
                    val movieEntity = MovieEntity(
                        i.id,
                        i.title!!,
                        i.overview!!,
                        i.poster!!,
                        i.vote,
                        i.release!!,
                        false
                    )

                    listMovie.add(movieEntity)
                }

                localDataSource.insertMovie(listMovie)
            }

        }.asLiveData()
    }


    override fun getAllTVShow(page: Int): LiveData<Resource<PagedList<TvShowEntity>>> {
        return object :
            NetworkBoundResource<PagedList<TvShowEntity>, TvShowResponse>(appExecutors) {
            override fun loadFromDB(): LiveData<PagedList<TvShowEntity>> {
                val config = PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setInitialLoadSizeHint(5)
                    .setPageSize(5)
                    .build()

                return LivePagedListBuilder(localDataSource.getAllTVShow(), config).build()
            }

            override fun shouldFetch(data: PagedList<TvShowEntity>?): Boolean {
                return data!!.isEmpty()

            }

            override fun openCall(): LiveData<ApiResponse<TvShowResponse>> {


                return remoteDataSource.getAllTVShow(page)
            }

            override fun saveCallResult(data: TvShowResponse) {
                val listTVShow = ArrayList<TvShowEntity>()

                for (i in data.results!!) {
                    val tvShowEntity = TvShowEntity(
                        i.id,
                        i.title!!,
                        i.overview!!,
                        i.poster!!,
                        i.vote,
                        i.release!!,
                        false
                    )

                    listTVShow.add(tvShowEntity)
                }

                localDataSource.insertTVShow(listTVShow)

            }

        }.asLiveData()
    }

    override fun getTVDetail2(movieId: Int): LiveData<Resource<TvShowEntity>> {
        return object : NetworkBoundResource<TvShowEntity, TvShowResponse>(appExecutors) {
            override fun loadFromDB(): LiveData<TvShowEntity> {

                return localDataSource.getDetailTVShow(movieId)
            }

            override fun shouldFetch(data: TvShowEntity?): Boolean {
                return data!!.title.isEmpty()
            }

            override fun openCall(): LiveData<ApiResponse<TvShowResponse>> {
                return remoteDataSource.getAllTVShow(1)
            }

            override fun saveCallResult(data: TvShowResponse) {
                val listTVShow = ArrayList<TvShowEntity>()

                for (i in data.results!!) {
                    val tvShowEntity = TvShowEntity(
                        i.id,
                        i.title!!,
                        i.overview!!,
                        i.poster!!,
                        i.vote,
                        i.release!!,
                        false
                    )

                    listTVShow.add(tvShowEntity)
                }

                localDataSource.insertTVShow(listTVShow)
            }

        }.asLiveData()
    }

    override fun setBookmarkedMovie(movieEntity: MovieEntity, isBookmarked: Boolean) {
        GlobalScope.launch(Dispatchers.IO) {
            localDataSource.setBookmarkedMovie(movieEntity, isBookmarked)
        }

    }

    override fun setBookmarkedTVShow(tvShowEntity: TvShowEntity, isBookmarked: Boolean) {
        GlobalScope.launch(Dispatchers.IO) {
            localDataSource.setBookmarkedTVShow(tvShowEntity, isBookmarked)
        }

    }

    override fun getAllBookmarkedMovie(): LiveData<PagedList<MovieEntity>> {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(5)
            .setPageSize(5)
            .build()

        return LivePagedListBuilder(localDataSource.getAllMovieBookmarked(), config).build()
    }

    override fun getAllBookmarkedTVShow(): LiveData<PagedList<TvShowEntity>> {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(5)
            .setPageSize(5)
            .build()

        return LivePagedListBuilder(localDataSource.getAllTVShowBookmarked(), config).build()
    }
}