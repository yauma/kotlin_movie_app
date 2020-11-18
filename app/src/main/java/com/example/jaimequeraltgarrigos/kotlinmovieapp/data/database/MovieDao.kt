package com.example.jaimequeraltgarrigos.kotlinmovieapp.data.database

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    suspend fun insertAll(list: List<Movie>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: Movie)

    @get:Query("select * from Movie")
    val movieLiveData: LiveData<List<Movie>>
}

@Entity
data class Movie constructor(
    val movieTitle: String,
    val moviePosterUrl: String,
    @PrimaryKey val id: Int = 0
)