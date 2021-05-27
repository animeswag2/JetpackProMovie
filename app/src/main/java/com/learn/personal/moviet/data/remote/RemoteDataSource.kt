package com.learn.personal.moviet.data.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.learn.personal.moviet.data.api.ApiClient
import com.learn.personal.moviet.data.api.TmdbApi
import com.learn.personal.moviet.data.remote.models.Movie
import com.learn.personal.moviet.data.remote.models.TvShow
import com.learn.personal.moviet.data.remote.responses.MovieResponse
import com.learn.personal.moviet.data.remote.responses.TvShowResponse
import com.learn.personal.moviet.utils.EspressoIdlingResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.await

class RemoteDataSource {
    private val tmdbApiClient = ApiClient.initClient().create(TmdbApi::class.java)

    companion object {
        @Volatile
        private var instance: RemoteDataSource? = null

        fun getInstance(): RemoteDataSource =
            instance ?: synchronized(this) {
                instance ?: RemoteDataSource()
            }
    }

    //Jaga"
    fun getAllMovies(page: Int): LiveData<ApiResponse<MovieResponse>> {
        EspressoIdlingResource.increment()
        val resultMovie = MutableLiveData<ApiResponse<MovieResponse>>()

        GlobalScope.launch(Dispatchers.IO) {
            val movieService = tmdbApiClient.getMovies(page).await()
            EspressoIdlingResource.decrement()
            resultMovie.postValue(ApiResponse.success(movieService))
        }

        return resultMovie
    }


    fun getAllTVShow(page: Int): LiveData<ApiResponse<TvShowResponse>> {
        EspressoIdlingResource.increment()
        val resultMovie = MutableLiveData<ApiResponse<TvShowResponse>>()

        GlobalScope.launch(Dispatchers.IO) {
            val movieService = tmdbApiClient.getTvShows(page).await()
            EspressoIdlingResource.decrement()
            resultMovie.postValue(ApiResponse.success(movieService))
        }

        return resultMovie
    }

    suspend fun getMovies(
        page: Int,
        callback: LoadMoviesCallback
    ) {
        EspressoIdlingResource.increment()
        tmdbApiClient.getMovies(page).await().results.let { listMovie ->
            if (listMovie != null) {
                callback.onAllMoviesReceived(
                    listMovie
                )
            }
            EspressoIdlingResource.decrement()
        }
    }

    suspend fun getMovieDetail(
        movieId: Int,
        callback: LoadMovieDetailCallback
    ) {
        EspressoIdlingResource.increment()
        tmdbApiClient.getMovieDetail(movieId).await().let { movie ->
            callback.onMovieDetailReceived(
                movie
            )
            EspressoIdlingResource.decrement()
        }
    }

    suspend fun getTvShows(
        page: Int,
        callback: LoadTvShowsCallback
    ) {
        EspressoIdlingResource.increment()
        tmdbApiClient.getTvShows(page).await().results.let { listTvShow ->
            if (listTvShow != null) {
                callback.onAllTvShowsReceived(
                    listTvShow
                )
            }
            EspressoIdlingResource.decrement()
        }
    }

    suspend fun getTvShowDetail(
        tvShowId: Int,
        callback: LoadTvShowDetailCallback
    ) {
        EspressoIdlingResource.increment()
        tmdbApiClient.getTvShowDetail(tvShowId).await().let { tvShow ->
            callback.onTvShowDetailReceived(
                tvShow
            )
            EspressoIdlingResource.decrement()
        }
    }

    interface LoadMoviesCallback {
        fun onAllMoviesReceived(movieResponse: List<Movie>)
    }

    interface LoadMovieDetailCallback {
        fun onMovieDetailReceived(movieResponse: Movie)
    }

    interface LoadTvShowsCallback {
        fun onAllTvShowsReceived(tvShowResponse: List<TvShow>)
    }

    interface LoadTvShowDetailCallback {
        fun onTvShowDetailReceived(tvShowResponse: TvShow)
    }
}