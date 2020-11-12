package com.example.jaimequeraltgarrigos.kotlinmovieapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.jaimequeraltgarrigos.kotlinmovieapp.database.MovieDao
import com.example.jaimequeraltgarrigos.kotlinmovieapp.model.MovieEntity
import com.example.jaimequeraltgarrigos.kotlinmovieapp.network.MainNetwork
import com.example.jaimequeraltgarrigos.kotlinmovieapp.utils.mapper.DBEntityMapperImpl
import com.example.jaimequeraltgarrigos.kotlinmovieapp.utils.mapper.NetworkEntityMapperImpl
import kotlinx.coroutines.withTimeout

class MovieRepository(
    private val network: MainNetwork,
    private val db: MovieDao,
    private val networkEntityMapper: NetworkEntityMapperImpl,
    private val dbEntityMapper: DBEntityMapperImpl
) {
    val movies: LiveData<List<MovieEntity>> = db.movieLiveData.map {
        dbEntityMapper.mapFromEntityList(it)
    }

    suspend fun refreshMovies() {
        try {
            val result = withTimeout(5000) {
                network.fetchMovies()
            }.map {
                networkEntityMapper.entityToDBModel(it)
            }
            db.insertAll(result)
        } catch (error: Throwable) {
            throw MoviesRefreshError("Unable to refresh movies", error)
        }
    }
}

class MoviesRefreshError(message: String, cause: Throwable?) : Throwable(message, cause)
