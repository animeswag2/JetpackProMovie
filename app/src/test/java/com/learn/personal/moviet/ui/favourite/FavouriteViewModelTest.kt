package com.learn.personal.moviet.ui.favourite

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import com.learn.personal.moviet.data.CatalogueRepository
import com.learn.personal.moviet.data.local.entity.MovieEntity
import com.learn.personal.moviet.data.local.entity.TvShowEntity
import com.learn.personal.moviet.data.remote.models.Movie
import com.learn.personal.moviet.data.remote.models.TvShow
import com.learn.personal.moviet.utils.DataDummy
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class FavouriteViewModelTest {
    private lateinit var favouriteViewModel: FavouriteViewModel

    private val dummyMovie = DataDummy.generateMoviesList()[0]
    private val movieId = dummyMovie.id
    private val dummyTvShow = DataDummy.generateTvShowsList()[1]
    private val tvShowId = dummyTvShow.id

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var catalogueRepository: CatalogueRepository

    @Mock
    private lateinit var observerMovie: Observer<PagedList<MovieEntity>>

    @Mock
    private lateinit var observerTVShow: Observer<PagedList<TvShowEntity>>

    @Mock
    private lateinit var moviepagedList: PagedList<MovieEntity>

    @Mock
    private lateinit var tvshowpagedList: PagedList<TvShowEntity>



    @Before
    fun setUp() {
        favouriteViewModel = FavouriteViewModel(catalogueRepository)
    }


    @Test
    fun getMovieFav() {
        val dummyMovie = moviepagedList
        `when`(dummyMovie.size).thenReturn(3)
        val movies = MutableLiveData<PagedList<MovieEntity>>()
        movies.value = dummyMovie

        `when`(catalogueRepository.getAllBookmarkedMovie()).thenReturn(movies)
        val movie = favouriteViewModel.getMovieFav().value
        verify(catalogueRepository).getAllBookmarkedMovie()
        assertNotNull(movie)
        assertEquals(3, movie?.size)

        favouriteViewModel.getMovieFav().observeForever(observerMovie)
        verify(observerMovie).onChanged(dummyMovie)
    }

    @Test
    fun setBookmarkedMovie() {
        favouriteViewModel.setBookmarkedMovie(DataDummy.generateMoviesListEntity()[0], true)
        verify(catalogueRepository).setBookmarkedMovie(DataDummy.generateMoviesListEntity()[0], true)
        verifyNoMoreInteractions(catalogueRepository)
    }

    @Test
    fun getTVShowFav() {
        val dummytvshow = tvshowpagedList
        `when`(dummytvshow.size).thenReturn(3)
        val tvshows = MutableLiveData<PagedList<TvShowEntity>>()
        tvshows.value = dummytvshow

        `when`(catalogueRepository.getAllBookmarkedTVShow()).thenReturn(tvshows)
        val tvshow = favouriteViewModel.getTVShowFav().value
        verify(catalogueRepository).getAllBookmarkedTVShow()
        assertNotNull(tvshow)
        assertEquals(3, tvshow?.size)

        favouriteViewModel.getTVShowFav().observeForever(observerTVShow)
        verify(observerTVShow).onChanged(dummytvshow)
    }


    @Test
    fun setBookmarkedTVShow() {
        favouriteViewModel.setBookmarkedTVShow(DataDummy.generateTVShowsListEntity()[0], true)
        verify(catalogueRepository).setBookmarkedTVShow(DataDummy.generateTVShowsListEntity()[0], true)
        verifyNoMoreInteractions(catalogueRepository)
    }
}