package com.example.jaimequeraltgarrigos.kotlinmovieapp.repository

interface MovieRepository {
    suspend fun refreshMovies()
}