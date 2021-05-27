package com.learn.personal.moviet.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.learn.personal.moviet.LiveDataTestUtil
import com.learn.personal.moviet.PagedListUtil
import com.learn.personal.moviet.data.local.LocalDataSource
import com.learn.personal.moviet.data.local.entity.MovieEntity
import com.learn.personal.moviet.data.local.entity.TvShowEntity
import com.learn.personal.moviet.data.remote.RemoteDataSource
import com.learn.personal.moviet.utils.AppExecutors
import com.learn.personal.moviet.utils.DataDummy
import com.learn.personal.moviet.vo.Resource
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.verify
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

class CatalogueRepositoryTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val dataSource = Mockito.mock(RemoteDataSource::class.java)
    private val local = Mockito.mock(LocalDataSource::class.java)
    private val appExecutor = Mockito.mock(AppExecutors::class.java)

    private val catalogRepository = FakeCatalogueRepository(local, dataSource, appExecutor)

    private val movies = DataDummy.generateMoviesList()
    private val movieId = movies[0].id

    private val tvShows = DataDummy.generateTvShowsList()
    private val tvShowId = tvShows[0].id

    private val movieResponse = DataDummy.generateMoviesList()[0]
    private val tvShowResponse = DataDummy.generateTvShowsList()[0]

    private val tvShowSelected = DataDummy.generateTVShowsListEntity()[0]
    private val movieSelected = DataDummy.generateMoviesListEntity()[0]

    private val page = 1

    @Test
    fun getMovies() {

        val dataSourceFactory = Mockito.mock(DataSource.Factory::class.java) as DataSource.Factory<Int, MovieEntity>
        Mockito.`when`(local.getAllMovie()).thenReturn(dataSourceFactory)
        catalogRepository.getAllMovie(page)

        val movieEntity = Resource.success(PagedListUtil.mockPagedList(movies))
        verify(local).getAllMovie()
        Assert.assertNotNull(movieEntity.data)
        assertEquals(movies.size.toLong(), movieEntity.data?.size?.toLong())

//        runBlocking {
//            doAnswer {invocation ->
//                (invocation.arguments[1] as RemoteDataSource.LoadMoviesCallback).onAllMoviesReceived(movies)
//                null
//            }.`when`(dataSource).getMovies(eq(page), any())
//        }
//
//        val data = LiveDataTestUtil.getValue(catalogRepository.getMovies(page))
//
//        runBlocking {
//            verify(dataSource).getMovies(eq(page), any())
//        }
//
//        assertNotNull(data)
//        assertEquals(movies.size.toLong(), data.size.toLong())
    }

    @Test
    fun getTvShows() {

        val dataSourceFactory = Mockito.mock(DataSource.Factory::class.java) as DataSource.Factory<Int, TvShowEntity>
        Mockito.`when`(local.getAllTVShow()).thenReturn(dataSourceFactory)
        catalogRepository.getAllTVShow(page)

        val tvShowEntity = Resource.success(tvShows)
        verify(local).getAllTVShow()
        Assert.assertNotNull(tvShowEntity)
        assertEquals(tvShows.size.toLong(), tvShowEntity.data?.size?.toLong())


//        runBlocking {
//            doAnswer {invocation ->
//                (invocation.arguments[1] as RemoteDataSource.LoadTvShowsCallback).onAllTvShowsReceived(tvShows)
//                null
//            }.`when`(dataSource).getTvShows(eq(page), any())
//        }
//
//        val data = LiveDataTestUtil.getValue(catalogRepository.getTvShows(page))
//
//        runBlocking {
//            verify(dataSource).getTvShows(eq(page), any())
//        }
//
//        assertNotNull(data)
//        assertEquals(tvShows.size.toLong(), data.size.toLong())
    }

    @Test
    fun getMovieDetail() {

        val dummyMovie = MutableLiveData<MovieEntity>()
        dummyMovie.value = movieSelected
        Mockito.`when`(local.getDetailMovie(movieSelected.movieId)).thenReturn(dummyMovie)

        val movieEntities = LiveDataTestUtil.getValue(catalogRepository.getMovieDetail2(movieSelected.movieId))
        verify(local).getDetailMovie(movieSelected.movieId)
        Assert.assertNotNull(movieEntities)
        Assert.assertNotNull(movieEntities.data?.title)
        assertEquals(movieEntities.data?.title, tvShows[0].title)
        assertEquals(movieEntities.data?.release, tvShows[0].release)
        assertEquals(movieEntities.data?.vote, tvShows[0].vote)


//        runBlocking {
//            doAnswer {invocation ->
//                (invocation.arguments[1] as RemoteDataSource.LoadMovieDetailCallback).onMovieDetailReceived(movieResponse)
//                null
//            }.`when`(dataSource).getMovieDetail(eq(movieId), any())
//        }
//
//        val data = LiveDataTestUtil.getValue(catalogRepository.getMovieDetail(movieId))
//
//        runBlocking {
//            verify(dataSource).getMovieDetail(eq(movieId), any())
//        }
//
//        assertNotNull(data)
//        assertEquals(movieId, data.id)
    }

    @Test
    fun getTvShowDetail() {

        val dummyTVShow = MutableLiveData<TvShowEntity>()
        dummyTVShow.value = tvShowSelected
        Mockito.`when`(local.getDetailTVShow(tvShowSelected.movieId)).thenReturn(dummyTVShow)

        val tvShowEntities = LiveDataTestUtil.getValue(catalogRepository.getTVDetail2(tvShowSelected.movieId))
        verify(local).getDetailTVShow(tvShowSelected.movieId)
        Assert.assertNotNull(tvShowEntities)
        Assert.assertNotNull(tvShowEntities.data?.title)
        assertEquals(tvShowEntities.data?.title, movies[0].title)
        assertEquals(tvShowEntities.data?.release, movies[0].release)
        assertEquals(tvShowEntities.data?.vote, movies[0].vote)

//
//        runBlocking {
//            doAnswer {invocation ->
//                (invocation.arguments[1] as RemoteDataSource.LoadTvShowDetailCallback).onTvShowDetailReceived(tvShowResponse)
//                null
//            }.`when`(dataSource).getTvShowDetail(eq(tvShowId), any())
//        }
//
//        val data = LiveDataTestUtil.getValue(catalogRepository.getTvShowDetail(tvShowId))
//
//        runBlocking {
//            verify(dataSource).getTvShowDetail(eq(tvShowId), any())
//        }
//
//        assertNotNull(data)
//        assertEquals(tvShowId, data.id)
    }

    @Test
    fun getBookmarkedMovie() {
        val dataSourceFactory = Mockito.mock(DataSource.Factory::class.java) as DataSource.Factory<Int, MovieEntity>
        Mockito.`when`(local.getAllMovieBookmarked()).thenReturn(dataSourceFactory)
        catalogRepository.getAllBookmarkedMovie()

        val movieEntities = Resource.success(PagedListUtil.mockPagedList(movies))

        verify(local).getAllMovieBookmarked()
        Assert.assertNotNull(movieEntities)
        assertEquals(movies.size.toLong(), movieEntities.data?.size?.toLong())
    }

    @Test
    fun getBookmarkedTVShow(){
        val dataSourceFactory = Mockito.mock(DataSource.Factory::class.java) as DataSource.Factory<Int, TvShowEntity>
        Mockito.`when`(local.getAllTVShowBookmarked()).thenReturn(dataSourceFactory)
        catalogRepository.getAllBookmarkedTVShow()

        val tvShowEntities = Resource.success(PagedListUtil.mockPagedList(tvShows))
        verify(local).getAllTVShowBookmarked()
        Assert.assertNotNull(tvShowEntities)
        assertEquals(tvShows.size.toLong(), tvShowEntities.data?.size?.toLong())
    }



}