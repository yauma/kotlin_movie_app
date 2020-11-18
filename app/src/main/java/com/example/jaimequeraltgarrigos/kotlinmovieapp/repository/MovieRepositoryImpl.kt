package com.example.jaimequeraltgarrigos.kotlinmovieapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.jaimequeraltgarrigos.kotlinmovieapp.data.DataSource
import com.example.jaimequeraltgarrigos.kotlinmovieapp.data.database.Movie
import com.example.jaimequeraltgarrigos.kotlinmovieapp.data.database.MovieDBDataSource
import com.example.jaimequeraltgarrigos.kotlinmovieapp.data.database.MovieDao
import com.example.jaimequeraltgarrigos.kotlinmovieapp.model.MovieEntity
import com.example.jaimequeraltgarrigos.kotlinmovieapp.data.network.MainNetwork
import com.example.jaimequeraltgarrigos.kotlinmovieapp.data.network.MovieNetworkDataSource
import com.example.jaimequeraltgarrigos.kotlinmovieapp.data.network.MovieNetworkEntity
import com.example.jaimequeraltgarrigos.kotlinmovieapp.utils.mapper.DBEntityMapperImpl
import com.example.jaimequeraltgarrigos.kotlinmovieapp.utils.mapper.NetworkEntityMapperImpl
import kotlinx.coroutines.withTimeout

open class MovieRepositoryImpl(
    private val networkDataSource: DataSource<MovieEntity>,
    private val movieDBDataSource: DataSource<MovieEntity>
) : MovieRepository {
    val movies: LiveData<List<MovieEntity>> = movieDBDataSource.observeMovies()

    override suspend fun refreshMovies() {
        try {
            val result = withTimeout(5000) {
                networkDataSource.getMovies()
            }
            movieDBDataSource.insertAll(result)
        } catch (error: Throwable) {
            throw MoviesRefreshError("Unable to refresh movies", error)
        }
    }
}

class MoviesRefreshError(message: String, cause: Throwable?) : Throwable(message, cause)
