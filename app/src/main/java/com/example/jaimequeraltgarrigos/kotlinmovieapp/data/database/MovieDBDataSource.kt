package com.example.jaimequeraltgarrigos.kotlinmovieapp.data.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.jaimequeraltgarrigos.kotlinmovieapp.data.DataSource
import com.example.jaimequeraltgarrigos.kotlinmovieapp.model.MovieEntity
import com.example.jaimequeraltgarrigos.kotlinmovieapp.utils.mapper.DBEntityMapperImpl
import com.example.jaimequeraltgarrigos.kotlinmovieapp.utils.mapper.DBEntityMapperImpl_Factory

class MovieDBDataSource(
    private val movieDao: MovieDao,
    private val dbeEntityMapperImpl: DBEntityMapperImpl
) : DataSource<MovieEntity> {
    override fun observeMovies(): LiveData<List<MovieEntity>> {
        return movieDao.movieLiveData.map {
            dbeEntityMapperImpl.mapFromEntityList(it)
        }
    }

    override suspend fun getMovies(): List<MovieEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun insertAll(list: List<MovieEntity>) {
        movieDao.insertAll(dbeEntityMapperImpl.mapFromMovieList(list))
    }

    override suspend fun insertMovie(movie: MovieEntity) {
        movieDao.insertMovie(dbeEntityMapperImpl.modelToEntity(movie))
    }

}