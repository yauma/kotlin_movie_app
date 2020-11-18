package com.example.jaimequeraltgarrigos.kotlinmovieapp.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.jaimequeraltgarrigos.kotlinmovieapp.data.DataSource
import com.example.jaimequeraltgarrigos.kotlinmovieapp.data.FakeDataCompletableSource
import com.example.jaimequeraltgarrigos.kotlinmovieapp.data.FakeDataSource
import com.example.jaimequeraltgarrigos.kotlinmovieapp.data.network.MovieNetworkEntity
import com.example.jaimequeraltgarrigos.kotlinmovieapp.getOrAwaitValue
import com.example.jaimequeraltgarrigos.kotlinmovieapp.model.MovieEntity
import com.example.jaimequeraltgarrigos.kotlinmovieapp.utils.FAKE_RESULTS
import com.example.jaimequeraltgarrigos.kotlinmovieapp.utils.mapper.NetworkEntityMapperImpl
import junit.framework.TestCase
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest

import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MovieRepositoryImplTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var networkDataSource: DataSource<MovieEntity>
    private lateinit var movieDBDataSource: DataSource<MovieEntity>
    private lateinit var networkEntityMapperImpl: NetworkEntityMapperImpl

    private lateinit var moviesNetwork: MutableList<MovieEntity>

    //class under test
    private lateinit var movieRepositoryImpl: MovieRepositoryImpl

    @Before
    fun setup() {
        networkEntityMapperImpl = NetworkEntityMapperImpl()
        moviesNetwork = getMoviesNetwork(networkEntityMapperImpl)
        networkDataSource = FakeDataSource(moviesNetwork)
        movieDBDataSource = FakeDataSource(moviesNetwork)
        movieRepositoryImpl = MovieRepositoryImpl(networkDataSource, movieDBDataSource)
    }

    @Test
    fun getMovies_requestsAllMoviesFromDataSource() = runBlockingTest {
        //When
        movieRepositoryImpl.refreshMovies()
        //Then
        val value = movieRepositoryImpl.movies.getOrAwaitValue()
        TestCase.assertEquals(value, moviesNetwork)
    }

    @Test(expected = MoviesRefreshError::class)
    fun whenRefreshTitleTimeout_throws() = runBlockingTest {
        movieRepositoryImpl = MovieRepositoryImpl(
            FakeDataCompletableSource(),
            movieDBDataSource
        )
        launch {
            movieRepositoryImpl.refreshMovies()
        }

        advanceTimeBy(5_000)
    }

}

fun getMoviesNetwork(networkEntityMapperImpl: NetworkEntityMapperImpl): MutableList<MovieEntity> {
    return FAKE_RESULTS.map {
        networkEntityMapperImpl.entityToModel(it)
    }.toMutableList()
}