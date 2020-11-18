package com.example.jaimequeraltgarrigos.kotlinmovieapp.data

import androidx.lifecycle.LiveData
import com.example.jaimequeraltgarrigos.kotlinmovieapp.model.MovieEntity

interface DataSource<T> {
    fun observeMovies(): LiveData<List<MovieEntity>>
    suspend fun getMovies(): List<MovieEntity>
    suspend fun insertAll(list: List<MovieEntity>)
    suspend fun insertMovie(movie: MovieEntity)
}