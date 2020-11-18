package com.example.jaimequeraltgarrigos.kotlinmovieapp.data.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.jaimequeraltgarrigos.kotlinmovieapp.data.DataSource
import com.example.jaimequeraltgarrigos.kotlinmovieapp.model.MovieEntity
import com.example.jaimequeraltgarrigos.kotlinmovieapp.utils.mapper.NetworkEntityMapperImpl

class MovieNetworkDataSource(
    private val mainNetwork: MainNetwork,
    private val networkEntityMapperImpl: NetworkEntityMapperImpl
) : DataSource<MovieEntity> {
    private val observableMovies = MutableLiveData<List<MovieEntity>>()
    override fun observeMovies(): LiveData<List<MovieEntity>> {
        return observableMovies
    }

    override suspend fun getMovies(): List<MovieEntity> {
        val movies = mutableListOf<MovieEntity>()
        mainNetwork.fetchMovies().forEach {
            movies.add(networkEntityMapperImpl.entityToModel(it))
        }
        return movies
    }

    override suspend fun insertAll(list: List<MovieEntity>) {
        TODO("Not yet implemented")
    }

    override suspend fun insertMovie(movie: MovieEntity) {
        TODO("Not yet implemented")
    }

}